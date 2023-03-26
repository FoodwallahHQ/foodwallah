package ai.kalico.api.service.lead;

import static com.amazonaws.util.StringUtils.UTF8;

import ai.kalico.api.RootConfiguration;
import ai.kalico.api.data.postgres.entity.EmailCampaignEntity;
import ai.kalico.api.data.postgres.entity.EmailTrackingEntity;
import ai.kalico.api.data.postgres.entity.LeadsEntity;
import ai.kalico.api.data.postgres.repo.EmailCampaignRepo;
import ai.kalico.api.data.postgres.repo.EmailTrackingRepo;
import ai.kalico.api.data.postgres.repo.LeadsRepo;
import ai.kalico.api.props.IpAddressProps;
import ai.kalico.api.props.LeadsProps;
import ai.kalico.api.props.UserProps;
import ai.kalico.api.props.YouTubeProps;
import ai.kalico.api.props.ZenRowsProps;
import ai.kalico.api.service.utils.KALUtils;
import ai.kalico.api.service.utils.LeadServiceHelper;
import ai.kalico.api.service.utils.ScraperUtils;
import ai.kalico.api.service.youtubej.YoutubeDownloader;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchContinuation;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchResult;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.search.SearchResult;
import ai.kalico.api.service.youtubej.model.search.SearchResultItem;
import ai.kalico.api.utils.security.firebase.SecurityFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.ChannelPageableResponse;
import com.kalico.model.CreateEmailCampaignRequest;
import com.kalico.model.EmailCampaign;
import com.kalico.model.EmailCampaignMetrics;
import com.kalico.model.EmailMetric;
import com.kalico.model.GenericResponse;
import com.kalico.model.YouTubeChannelDetail;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author Biz Melesse created on 2/5/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

  private final YoutubeDownloader youtubeDownloader = new YoutubeDownloader();
  private final ScraperUtils scraperUtils;
  private final ObjectMapper objectMapper;
  private final ZenRowsProps zenRowsProps;
  private final YouTubeProps youTubeProps;
  private final LeadsRepo leadsRepo;
  private final LeadServiceHelper leadServiceHelper;
  private final IpAddressProps ipAddressProps;
  private final EmailTrackingRepo emailTrackingRepo;
  private final EmailCampaignRepo emailCampaignRepo;
  private final SecurityFilter securityFilter;
  private final UserProps userProps;
  private final LeadsProps leadsProps;

  private byte[] trackingImage;

  @SneakyThrows
  @PostConstruct
  public void loadEmailTrackingImage() {
    int width = 1;
    int height = 1;
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    //create random image pixel by pixel
    for(int y = 0; y < height; y++){
      for(int x = 0; x < width; x++){
        int a = 0; //alpha
        int r = 256; //red
        int g = 256; //green
        int b = 256; //blue

        int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel
        img.setRGB(x, y, p);
      }
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ImageIO.write(img, "png", baos);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    trackingImage = baos.toByteArray();
  }

  @Override
  public ChannelPageableResponse getChannelInfo(String query) {
    Response<SearchResult> response = youtubeDownloader.search(
        new RequestSearchResult(query));
    if (response.ok()) {
      SearchResult result = response.data();
      Set<String> channels = new HashSet<>(getChannels(result));
      while (result != null && result.hasContinuation() && channels.size() < youTubeProps.getMaxChannelResults()) {
        result = youtubeDownloader.searchContinuation(new RequestSearchContinuation(result)).data();
        channels.addAll(getChannels(result));
      }

      List<YouTubeChannelDetail>  channelDetails = submitBatchedRequests(new ArrayList<>(channels), 
          this::getChannelDetail);
      channelDetails = fetchEmailAddress(channelDetails, query);
      channelDetails.sort(Comparator.comparing(YouTubeChannelDetail::getSubscribersValue));

      ChannelPageableResponse fullResponse = new ChannelPageableResponse()
          .count(channelDetails.size())
          .records(channelDetails);
      saveResponse(fullResponse);
      log.info(this.getClass().getSimpleName()+ ".getChannelInfo: Fetched {} channel details for query: '{}' ",
          channelDetails.size(), query);
      return fullResponse;
    }
    log.info(this.getClass().getSimpleName()+ ".getChannelInfo: Fetched {} channel details for query: '{}' ",
        0, query);
    return new ChannelPageableResponse()
        .count(0)
        .records(new ArrayList<>());
  }

  @Override
  public byte[] getUserEmailImage(String imageHash, HttpServletRequest httpServletRequest) {
    leadServiceHelper.logImageRequest(imageHash, getIp(httpServletRequest));
    return trackingImage;
  }

  @Override
  public EmailCampaignMetrics getEmailCampaignMetrics() {
    String email = securityFilter.getUser().getEmail();
    EmailCampaignMetrics emailCampaignMetrics = new EmailCampaignMetrics();

    // Only admins are allowed to view campaign metrics
    if (userProps.getAdminEmails().contains(email)) {
      List<EmailTrackingEntity> entities = emailTrackingRepo
          .findAllOrderByUpdatedAtDesc(leadsProps.getMinOpenCount());
      if (entities.size() > 0) {
        List<EmailCampaignEntity> campaignEntities = emailCampaignRepo.findAllOrderByCreatedAtDesc();
        // Group by campaign
        Map<String, List<EmailTrackingEntity>> entityMap = new HashMap<>();
        for (EmailTrackingEntity entity : entities) {
          List<EmailTrackingEntity> values = entityMap.getOrDefault(entity.getCampaignId(),
              new ArrayList<>());
          values.add(entity);
          entityMap.put(entity.getCampaignId(), values);
        }
        for (EmailCampaignEntity campaignEntity : campaignEntities) {
          // Iterate through the sorted list of campaigns and add metrics for all emails within that campaign
          String campaignId = campaignEntity.getCampaignId();
          if (entityMap.containsKey(campaignId)) {
            // Get all the emails tracked for this campaign
            List<EmailTrackingEntity> trackedEmails = entityMap.get(campaignId);
            List<EmailMetric> emailMetrics = trackedEmails.stream().map(it -> new EmailMetric()
                    .email(it.getEmail())
                    .lastOpenedAt(it.getUpdatedAt()
                        .toEpochSecond(ZoneOffset.UTC)).numOpened(it.getNumOpened()))
                .collect(Collectors.toList());
            emailCampaignMetrics.addCampaignsItem(new EmailCampaign()
                .campaignId(campaignId)
                .dateCreated(campaignEntity.getCreatedAt().toEpochSecond(ZoneOffset.UTC))
                .numEmailsSent(campaignEntity.getNumEmailsSent())
                .openRate(getOpenRate(emailMetrics.size(), campaignEntity.getNumEmailsSent()))
                .emailMetric(emailMetrics)
                .subject(campaignEntity.getSubject())
                .template(campaignEntity.getTemplate())
                .personalizedByName(campaignEntity.getPersonalizedByName())
                .personalizedByOther(campaignEntity.getPersonalizedByOther()));
          }
        }
      }
    }
    return emailCampaignMetrics;
  }

  private BigDecimal getOpenRate(int numOpened, Long numEmailsSent) {
    if (numEmailsSent > 0) {
      double percent = numOpened / (numEmailsSent * 1.0) * 100;
      return BigDecimal.valueOf(Math.round(percent * 100) / 100.0);
    }
    return BigDecimal.valueOf(0);
  }


  @Override
  public GenericResponse createEmailCampaign(
      CreateEmailCampaignRequest createEmailCampaignRequest) {
    if (!ObjectUtils.isEmpty(createEmailCampaignRequest.getSubject()) &&
        !ObjectUtils.isEmpty(createEmailCampaignRequest.getTemplate())) {
      EmailCampaignEntity entity = new EmailCampaignEntity();
      entity.setCampaignId(KALUtils.generateUid());
      entity.setSubject(createEmailCampaignRequest.getSubject());
      entity.setTemplate(createEmailCampaignRequest.getTemplate());
      entity.setPersonalizedByOther(createEmailCampaignRequest.getPersonalizedByOther());
      entity.setNumEmailsSent(createEmailCampaignRequest.getNumEmailsSent());
      entity.setPersonalizedByName(createEmailCampaignRequest.getPersonalizedByName());
      emailCampaignRepo.save(entity);
      log.info(this.getClass().getSimpleName()+ ".createEmailCampaign: Create a new email campaign with id={}",
          entity.getCampaignId());
    }
    return new GenericResponse().status("OK");
  }

  @SneakyThrows
  private void saveResponse(ChannelPageableResponse fullResponse) {
    // Save the response locally
    String outputStr = objectMapper.writeValueAsString(fullResponse);
    try (InputStream in = new ByteArrayInputStream(outputStr.getBytes(UTF8))) {
      // Save the file to disk for processing
      String path = "/tmp/youtube-channels-request-" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + ".json";
      if (!new File(path).exists()) {
        new File(path).mkdirs();
      }
      File output = new File(path);
      Files.copy(in, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
  }

  private List<YouTubeChannelDetail> fetchEmailAddress(List<YouTubeChannelDetail> channelDetails, String query) {
    // Fetch the email address from the channel's Facebook page, if available
    List<YouTubeChannelDetail> channelsWithoutFacebookPage = channelDetails
        .stream()
        .filter(it -> it.getFacebook() == null)
        .collect(Collectors.toList());
    List<YouTubeChannelDetail> channelsWithFacebookPage = channelDetails
        .stream()
        .filter(it -> it.getFacebook() != null)
        .collect(Collectors.toList());
    if (youTubeProps.isEmailRequired()) {
      log.info(this.getClass().getSimpleName()+ ".fetchEmailAddress: Fetching email addresses for {} channels",
          channelsWithFacebookPage.size());
      for (YouTubeChannelDetail detail : channelsWithFacebookPage) {
       detail.setQuery(query);
      }
      return submitBatchedRequests(channelsWithFacebookPage, this::getFacebookPage);
    }
    List<YouTubeChannelDetail> allChannels = new ArrayList<>();
    allChannels.addAll(channelsWithFacebookPage);
    allChannels.addAll(channelsWithoutFacebookPage);
    return allChannels;
  }

  private List<YouTubeChannelDetail>  submitBatchedRequests(List<?> itemsToBatch, 
      Function<Object, YouTubeChannelDetail> callback) {
    log.info(this.getClass().getSimpleName()+ ".submitBatchedRequests: Making batched requests for {} items",
        itemsToBatch.size());
    List<YouTubeChannelDetail> response = new ArrayList<>();
    if (!ObjectUtils.isEmpty(itemsToBatch)) {
      List<List<?>> batches = batchObjects(itemsToBatch);
      for (List<?> batch : batches) {
        List<CompletableFuture<YouTubeChannelDetail>> tasks = new ArrayList<>();
        for (final Object o : batch) {
          tasks.add(
              CompletableFuture.supplyAsync(() -> callback.apply(o),
                  RootConfiguration.executor));
        }
        List<YouTubeChannelDetail> batchedResult = tasks
            .stream()
            .map(CompletableFuture::join)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        response.addAll(batchedResult);
      }
    }
    return response;
  }

  private List<List<?>> batchObjects(List<?> objects) {
    // Batch up the task count up to the concurrency limit
    List<List<?>> batches = new ArrayList<>();
    List<Object> batch = new ArrayList<>();
    int concurrency = zenRowsProps.getConcurrency();
    for (Object o : objects) {
      if (batch.size() == concurrency) {
        batches.add(batch);
        batch = new ArrayList<>();
      }
      batch.add(o);
    }
    batches.add(batch);
    return batches;
  }

  private YouTubeChannelDetail getChannelDetail(Object channel) {
    String _channel = (String) channel;
    String url = String.format("https://www.youtube.com/%s/about", _channel);
    URI uri = scraperUtils.getZenRowsUri(url, false, false, false);
    final CloseableHttpClient httpClient = HttpClients.createDefault();
    if (uri != null) {
      HttpGet httpGet = new HttpGet(uri);
      CloseableHttpResponse httpResponse = null;
      try {
        httpResponse = httpClient.execute(httpGet);
        Header[] requestId = httpResponse.getHeaders("X-Request-Id");
        log.info("X-Request-Id: {} \turl: {}", requestId[0].getElements()[0].getName(), url);
        YouTubeChannelDetail detail = parseChannelSoup(EntityUtils.toString(httpResponse.getEntity()));
        if (detail != null) {
          detail.setChannelHandle(_channel);
        }
        return detail;
      } catch (IOException e) {
        log.error(this.getClass().getSimpleName()+ ".getChannelDetail: {}", e.getLocalizedMessage());
      }
    }
    return null;
  }

  private YouTubeChannelDetail getFacebookPage(Object detail) {
    YouTubeChannelDetail _detail = (YouTubeChannelDetail) detail;
    String url = _detail.getFacebook();
    if (url != null) {
      if (leadsRepo.findByFacebook(url).isEmpty()) {
        URI uri = scraperUtils.getZenRowsUri(url, true, true, false);
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        if (uri != null) {
          HttpGet httpGet = new HttpGet(uri);
          CloseableHttpResponse httpResponse = null;
          try {
            httpResponse = httpClient.execute(httpGet);
            Header[] requestId = httpResponse.getHeaders("X-Request-Id");
            log.info("X-Request-Id: {} \turl: {}", requestId[0].getElements()[0].getName(), url);
            String email = parseFacebookSoup(EntityUtils.toString(httpResponse.getEntity()));
            if (email == null) {
              return null;
            }
            _detail.setEmail(email);
            try {
              // Save to the database
              LeadsEntity entity = mapDetailToEntity(_detail);
              leadsRepo.save(entity);
            } catch (Exception e) {
              log.warn(this.getClass().getSimpleName() + ".getFacebookPage: {}",
                  e.getLocalizedMessage());
            }
            return _detail;
          } catch (IOException e) {
            log.error(this.getClass().getSimpleName() + ".getFacebookPage: {}",
                e.getLocalizedMessage());
          }
        }
      } else {
        log.warn(this.getClass().getSimpleName() + ".getFacebookPage: Url already exists in the database: {}",
            url);
      }
    }
    return null;
  }


  private YouTubeChannelDetail parseChannelSoup(String soup) {
    Document doc = Jsoup.parse(soup);
    String json = null;
    try {
      List<DataNode> dataNodes = doc.select("script").dataNodes();
      for (DataNode dataNode : dataNodes) {
        String wholeData = dataNode.getWholeData();
        if (wholeData.startsWith("var ytInitialData")) {
          json = wholeData
              .replace("var ytInitialData = ", "")
              .replace("\n", "")
              .strip();
          break;
        }
      }
      if (json != null) {
        JsonNode jsonNode = objectMapper.readValue(json, JsonNode.class);
        if (jsonNode.get("metadata") != null) {
          if (jsonNode.get("metadata").get("channelMetadataRenderer") != null) {
            JsonNode channelMeta = jsonNode.get("metadata").get("channelMetadataRenderer");
            String channelName = "", description = "", keywords = "", channelUrl = "", subscribers = "";
            Long subscribersValue = -1L;
            if (channelMeta.get("title") != null) {
              channelName = channelMeta.get("title").asText();
            }
            if (channelMeta.get("description") != null) {
              description = channelMeta.get("description").asText();
              description = description.replace("\n", " ").strip();
            }
            if (channelMeta.get("keywords") != null) {
              keywords = channelMeta.get("keywords").asText();
            }
            if (channelMeta.get("vanityChannelUrl") != null) {
              channelUrl = channelMeta.get("vanityChannelUrl").asText();
            }
            JsonNode channelAboutMetadata = findNode(jsonNode.get("contents"),
                "channelAboutFullMetadataRenderer");
            if (ObjectUtils.isEmpty(channelUrl) &&
                channelAboutMetadata != null &&
                channelAboutMetadata.get("canonicalChannelUrl") != null) {
              channelUrl = channelAboutMetadata.get("canonicalChannelUrl").asText();
            }
            Map<String, String> links = new HashMap<>();
            if (channelAboutMetadata.get("primaryLinks") != null) {
              links = parseLinks(channelAboutMetadata.get("primaryLinks"));
            }
            JsonNode subscribeNode = findNode(jsonNode.get("header"), "subscriberCountText");
            if (subscribeNode != null) {
              subscribers = subscribeNode
                  .get("simpleText")
                  .asText()
                  .replace("subscribers", "")
                  .strip();
              subscribersValue = getSubscriberNumericalValue(subscribers);
            }
            return new YouTubeChannelDetail()
                .channelName(channelName)
                .description(description)
                .keywords(keywords)
                .channelUrl(channelUrl)
                .subscribers(subscribers)
                .subscribersValue(subscribersValue)
                .twitter(links.get(SocialPlatforms.TWITTER))
                .website(links.get(SocialPlatforms.WEBSITE))
                .blog(links.get(SocialPlatforms.BLOG))
                .pinterest(links.get(SocialPlatforms.PINTEREST))
                .snapChat(links.get(SocialPlatforms.SNAPCHAT))
                .discord(links.get(SocialPlatforms.DISCORD))
                .tiktok(links.get(SocialPlatforms.TIKTOK))
                .facebook(links.get(SocialPlatforms.FACEBOOK))
                .instagram(links.get(SocialPlatforms.INSTAGRAM));
          }
        }
      }
    } catch (JsonProcessingException | NullPointerException e) {
      log.error(this.getClass().getSimpleName()+ ".parseChannelSoup: {}", e.getLocalizedMessage());
    }
    return null;
  }

  private String parseFacebookSoup(String soup) {
    soup = soup.replace("\\u0040", "@");
    // Limit the regex search space by taking 256 characters to the left and right of the @ symbol
    int maxChars = 256;
    int soupLength = soup.length();
    StringJoiner joiner = new StringJoiner("");
    int index = soup.indexOf('@');
    while (index > 0) {
      int start = index - maxChars;
      int end = index + maxChars;
      if (start < 0) {
        start = 0;
      }
      if (end >= soup.length()) {
        end = soupLength;
      }
      String substring = soup.substring(start, end);
      joiner.add(substring);
      index = soup.indexOf('@', index + 1);
    }
    String minifiedSoup = joiner.toString();
    String pattern = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
    Pattern emailPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    Matcher matcher = emailPattern.matcher(minifiedSoup);
    String email = null;
    // Find the first instance of an email address
    if (matcher.find()){
      email = matcher.group();
    }
    return email;
  }
  private Long getSubscriberNumericalValue(String subscribers) {
    long value = -1L;
    if (subscribers.toLowerCase().endsWith("m")) {
      value = Math.round(Double.parseDouble(subscribers.replace("M", "")) * 1000000);
    } else if (subscribers.toLowerCase().endsWith("k")) {
      value = Math.round(Double.parseDouble(subscribers.replace("K", "")) * 1000);
    } else {
      try {
        value = Long.parseLong(subscribers);
      } catch (Exception e) {
        log.error(this.getClass().getSimpleName()+ ".getSubscriberNumericalValue: {}", e.getLocalizedMessage());
      }
    }
    return value;
  }

  private Map<String, String>  parseLinks(JsonNode linkNode) {
    Map<String, String>  links = new HashMap<>();
    if (linkNode != null && linkNode.isArray()) {
      for (int i = 0; i < linkNode.size(); i++) {
        JsonNode link = linkNode.get(i);
        String name = null;
        if (link.get("title") != null) {
          if (link.get("title").get("simpleText") != null) {
            name = link.get("title").get("simpleText").asText();
          }
        }
        String url = null;
        if (link.get("navigationEndpoint") != null) {
          if (link.get("navigationEndpoint").get("urlEndpoint") != null) {
            if (link.get("navigationEndpoint").get("urlEndpoint").get("url") != null) {
              url = link.get("navigationEndpoint").get("urlEndpoint").get("url").asText();
            }
          }
        }
        name = getStandardFieldName(name);
       if (url != null && name != null)
       {
         try {
           URI uri = new URI(url);
           String query = uri.getQuery();
           if (query != null) {
             String wwwUrl = null;
             String httpUrl = null;
             // Remove the redirect tokens and extract the target url
             String[] wwwTokens = query.split("www");
             String[] httpTokens = query.split("http");

             if (wwwTokens.length > 1) {
               wwwUrl = "www" + wwwTokens[wwwTokens.length - 1];
             }
             if (httpTokens.length > 1) {
               httpUrl = "http" + httpTokens[httpTokens.length - 1];
             }
             String cleanUrl = wwwUrl;
             if ((cleanUrl != null &&
                 cleanUrl.toLowerCase().contains("youtube")) || httpUrl != null) {
               cleanUrl = httpUrl;
             }
             links.put(name, cleanUrl);
           }
         } catch (URISyntaxException | NullPointerException e) {
           log.error(this.getClass().getSimpleName()+ ".parseLinks: {}", e.getLocalizedMessage());
         }
       }
      }
    }
    return links;
  }

  private String getStandardFieldName(String name) {
    // Standardize
    if (name != null) {
      if (name.toLowerCase().contains("facebook")) {
        name = SocialPlatforms.FACEBOOK;
      } else if (name.toLowerCase().contains("instagram")) {
        name = SocialPlatforms.INSTAGRAM;
      } else if (name.toLowerCase().contains("twitter")) {
        name = SocialPlatforms.TWITTER;
      } else if (name.toLowerCase().contains("website")) {
        name = SocialPlatforms.WEBSITE;
      } else if (name.toLowerCase().contains("blog")) {
        name = SocialPlatforms.BLOG;
      } else if (name.toLowerCase().contains("pinterest")) {
        name = SocialPlatforms.PINTEREST;
      } else if (name.toLowerCase().contains("snapchat")) {
        name = SocialPlatforms.SNAPCHAT;
      } else if (name.toLowerCase().contains("discord")) {
        name = SocialPlatforms.DISCORD;
      } else if (name.equalsIgnoreCase("fb")) {
        name = SocialPlatforms.FACEBOOK;
      } else if (name.equalsIgnoreCase("ig")) {
        name = SocialPlatforms.INSTAGRAM;
      } else if (name.toLowerCase().contains("tik tok")) {
        name = SocialPlatforms.TIKTOK;
      } else {
        name = null;
      }
    }
    return name;
  }

  private JsonNode findNode(JsonNode root, String target) {
    Iterator<String> fieldNames = root.fieldNames();
    JsonNode result = null;
    while (fieldNames.hasNext()) {
      String key = fieldNames.next();
      if (key.equals(target)) {
        return root.get(key);
      }
      JsonNode child = root.get(key);
      if (child.isArray()) {
        for (int i = 0; i < child.size(); i++) {
          result= findNode(child.get(i), target);
          if (result != null) {
            return result;
          }
        }
      }
      if (child.fieldNames().hasNext()) {
         result = findNode(child, target);
         if (result != null) {
           return result;
         }
      }
    }
    return null;
  }

  private List<String> getChannels(SearchResult result) {
    List<String> channels = new ArrayList<>();
    if (result != null && !ObjectUtils.isEmpty(result.items())) {
      for (SearchResultItem item : result.items()) {
        try {
          channels.add(item.asVideo().channelName());
        } catch (UnsupportedOperationException e) {
          log.warn(this.getClass().getSimpleName()+ ".getChannels: {} -- Ignore", e.getLocalizedMessage());
        }
      }
    }
    return channels;
  }

  private Map<String, String> getRequestHeaders(HttpServletRequest httpServletRequest) {
    // Normalize the headers by lower-casing all the keys
    Map<String, String> normalized = new HashMap<>();
    Enumeration<String> headers = httpServletRequest.getHeaderNames();
    while (headers.hasMoreElements()) {
      String header = headers.nextElement();
      normalized.put(header.toLowerCase(), httpServletRequest.getHeader(header));
    }
    return normalized;
  }

  @Nullable
  private String getIp(HttpServletRequest httpServletRequest) {
    Map<String, String> normalizedHeaders = getRequestHeaders(httpServletRequest);
    Set<String> possibleIpAddressKeys = ipAddressProps.getHeaders();
    for (String header : possibleIpAddressKeys) {
      String ipList = normalizedHeaders.get(header.toLowerCase());
      if (!ObjectUtils.isEmpty(ipList)) {
        try {
          return ipList.split(",")[0];
        } catch (Exception ex) {
          log.error("RootConfiguration.getIp for ipList: {}, ex: {}"
              , ipList
              , ex.getLocalizedMessage());
        }
      }
    }
    // If the IP address is not set in any of the headers, then use the remote address
    String remoteAddress = httpServletRequest.getRemoteAddr();

    // Our own IP addresses are whitelisted so those must not be considered
    if (!whitelisted(remoteAddress)) {
      return remoteAddress;
    }
    return null;
  }

  private boolean whitelisted(String ipAddress) {
    // Check if an IP address is whitelisted. Because of subnetting, we only want
    // to check that the prefix
    for (String whitelistIpPrefix : ipAddressProps.getWhitelist()) {
      if (ipAddress.indexOf(whitelistIpPrefix) == 0) {
        return true;
      }
    }
    return false;
  }

  private LeadsEntity mapDetailToEntity(YouTubeChannelDetail detail) {
    LeadsEntity entity = new LeadsEntity();
    entity.setEmail(detail.getEmail());
    entity.setDescription(detail.getDescription());
    entity.setKeywords(detail.getKeywords());
    entity.setChannelUrl(detail.getChannelUrl());
    entity.setChannelName(detail.getChannelName());
    entity.setChannelHandle(detail.getChannelHandle());
    entity.setSubscribers(detail.getSubscribers());
    entity.setSubscribersValue(detail.getSubscribersValue());
    entity.setFacebook(detail.getFacebook());
    entity.setInstagram(detail.getInstagram());
    entity.setTwitter(detail.getTwitter());
    entity.setWebsite(detail.getWebsite());
    entity.setBlog(detail.getBlog());
    entity.setSnapChat(detail.getSnapChat());
    entity.setDiscord(detail.getDiscord());
    entity.setTiktok(detail.getTiktok());
    entity.setPinterest(detail.getPinterest());
    entity.setQuery(detail.getQuery());
    return entity;
  }

  private static class SocialPlatforms {
    public static final String FACEBOOK = "Facebook";
    public static final String TWITTER = "Twitter";
    public static final String INSTAGRAM = "Instagram";
    public static final String WEBSITE = "Website";
    public static final String BLOG = "Blog";
    public static final String PINTEREST = "Pinterest";
    public static final String SNAPCHAT = "SnapChat";
    public static final String TIKTOK = "TikTok";
    public static final String DISCORD = "Discord";
  }
}
