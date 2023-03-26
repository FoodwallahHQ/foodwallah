package ai.kalico.api.service.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ai.kalico.api.data.postgres.entity.CookieJarEntity;
import ai.kalico.api.data.postgres.repo.CookieJarRepo;
import ai.kalico.api.props.InstagramProps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;

/**
 * @author Biz Melesse created on 12/24/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CookieHelper {
  private final InstagramProps instagramProps;
  private final CookieJarRepo cookieJarRepo;
  private final ObjectMapper mapper;

  public final List<String> urls = List.of(
      "b.i.instagram.com",
      "i.instagram.com",
      "www.instagram.com",
      "instagram.com"
  );

  @Deprecated
  public List<CookieJarEntity> loadFromFile() throws IOException {
    List<CookieJarEntity> allCookies = new ArrayList<>();
    InputStream resource = new ClassPathResource("cookies/instagram.json").getInputStream();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource)) ) {
      String json = FileCopyUtils.copyToString(reader);
      TypeReference<Map<String, List<JsonNode>>> typeRef = new TypeReference<>() {
      };
      Map<String, List<JsonNode>> cookieFile = mapper.readValue(json, typeRef);
      for (Map.Entry<String, List<JsonNode>> entry : cookieFile.entrySet()) {
        String host = entry.getKey();
        Map<String, CookieJarEntity> uniqueCookies = new HashMap<>();
        for (JsonNode cookie : entry.getValue()) {
          CookieJarEntity entity = new CookieJarEntity();
          entity.setKey(host);
          entity.setName(cookie.get("name").asText());
          entity.setPath(cookie.get("path").asText());
          entity.setDomain(cookie.get("domain").asText());
          entity.setValue(cookie.get("value").asText());
          entity.setUsername(instagramProps.getUsername());
          entity.setHostOnly(cookie.get("hostOnly").asBoolean());
          entity.setHttpOnly(cookie.get("httpOnly").asBoolean());
          entity.setPersistent(cookie.get("persistent").asBoolean());
          entity.setSecure(cookie.get("secure").asBoolean());
          entity.setExpiresAt(cookie.get("expiresAt").asLong());
          uniqueCookies.put(entity.getName(), entity);
        }
        allCookies.addAll(uniqueCookies.values());
      }

      // Fetch any existing cookies from the database and update duplicates
      List<CookieJarEntity> cookieJarEntities = cookieJarRepo
          .findCookiesByUrls(instagramProps.getUsername(), urls);
      Map<String, CookieJarEntity> existingEntityMap = new HashMap<>();
      if (!ObjectUtils.isEmpty(cookieJarEntities)) {
        for (CookieJarEntity cookieJarEntity : cookieJarEntities) {
          existingEntityMap.put(cookieJarEntity.getName(), cookieJarEntity);
        }
      }
      List<CookieJarEntity> newCookies = new ArrayList<>();
      for (CookieJarEntity newCookie : allCookies) {
        if (!existingEntityMap.containsKey(newCookie.getName())) {
          newCookies.add(newCookie);
        }
      }
      // Only add cookies that are not already in the database
      cookieJarRepo.saveAll(newCookies);
      allCookies.addAll(newCookies);
    }
    return allCookies;
  }

}
