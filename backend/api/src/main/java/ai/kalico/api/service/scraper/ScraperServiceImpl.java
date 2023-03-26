package ai.kalico.api.service.scraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ai.kalico.api.dto.VideoInfoDto;
import ai.kalico.api.service.parser.InstagramParser;
import ai.kalico.api.service.utils.ScraperUtils;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Bizuwork Melesse
 * created on 6/17/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScraperServiceImpl implements ScraperService {
    private final InstagramParser instagramParser;
    private final ScraperUtils scraperUtils;

    @Override
    public VideoInfoDto getContent(String contentId, String url) {
        try { // TODO: check that the content can be processed before scraping
            Platform platform = getPlatform(url);
            WebClient client = scraperUtils.getWebClient();
            log.info("About to process URL for {} with content ID {}: {}", platform.name(), contentId, url);
            HtmlPage page = client.getPage(url);
            int statusCode = page.getWebResponse().getStatusCode();
            if (statusCode >= 200 && statusCode < 400 ) {
                String soup = page.getWebResponse().getContentAsString();
                if (platform == Platform.INSTAGRAM) {
                    return instagramParser.processSoup(soup, url, contentId, client);
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }

    private Platform getPlatform(String url) {
        URL parsedUrl = null;
        try {
            parsedUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (parsedUrl != null) {
            if (parsedUrl.getHost().toLowerCase().contains("reddit")) {
                return Platform.REDDIT;
            }
            else if (parsedUrl.getHost().toLowerCase().contains("instagram")) {
                return Platform.INSTAGRAM;
            }
        }
        return Platform.INSTAGRAM;
    }
}
