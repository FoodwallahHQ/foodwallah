package ai.kalico.api.service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ai.kalico.api.data.postgres.entity.ProjectEntity;
import ai.kalico.api.data.postgres.entity.SampledImageEntity;
import ai.kalico.api.data.postgres.entity.UserEntity;
import ai.kalico.api.data.postgres.entity.MediaContentEntity;
import ai.kalico.api.data.postgres.repo.ProjectRepo;
import ai.kalico.api.data.postgres.repo.SampledImageRepo;
import ai.kalico.api.data.postgres.repo.UserRepo;
import ai.kalico.api.data.postgres.repo.MediaContentRepo;
import ai.kalico.api.props.AWSProps;
import ai.kalico.api.props.ProjectProps;
import com.kalico.model.KalicoContentType;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

/**
 * @author Biz Melesse created on 1/29/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeedData {
  private final ProjectRepo projectRepo;
  private final MediaContentRepo mediaContentRepo;
  private final SampledImageRepo sampledImageRepo;
  private final UserRepo userRepo;
  private final ProjectProps projectProps;
  private final ObjectMapper objectMapper;
  private final AWSProps awsProps;

  public void seed(String userId) {
    if (projectProps.getSeedDb() != null && projectProps.getSeedDb()) {
      int numRecords = 5;
      if (projectRepo.count() < numRecords) {
        try {
          if (userId == null) {
            userId = createUserAccounts();
          }
          List<ProjectEntity> projectEntities = createProject(numRecords, userId);
          
          List<Long> projectIds = projectEntities.stream()
              .map(ProjectEntity::getId)
              .collect(Collectors.toList());
          createSampledImages(projectIds);
          createMediaContent(projectIds);
        } catch (Exception e) {
          log.error(e.getLocalizedMessage());
        }
      }
    }
  }


  private String createUserAccounts() {
    TypeReference<List<UserEntity>> typeRef = new TypeReference<>() {};
    List<UserEntity> userEntities = null;
    try {
      userEntities = objectMapper.readValue(loadFromFile("mr_public_user.json"), typeRef);
      userRepo.saveAll(userEntities);
    } catch (Exception e) {
      log.error(e.getLocalizedMessage());
    }
   if (userEntities != null) {
     return userEntities.get(0).getFirebaseId();
   }
   return "";
  }
  

  public List<ProjectEntity> createProject(int count, String userId) {
    List<ProjectEntity> projectEntities = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      ProjectEntity entity = new ProjectEntity();
      entity.setContentLink("https://www.youtube.com/watch?v=U-0JCdjkREU");
      entity.setContentType(KalicoContentType.FOOD_RECIPE.getValue());
      entity.setContent(loadFromFile("generated_content.json"));
      entity.setParaphrase(true);
      entity.setEmbedImages(false);
      entity.setUserId(userId);
      entity.setProjectName("Demo Project: Korean Tofu");
      entity.setProcessed(true);
      entity.setProjectUid(KALUtils.generateUid());
      projectEntities.add(entity);
    }
    projectRepo.saveAll(projectEntities);
    return projectEntities;
  }


  private void createMediaContent(List<Long> projectIds) {
    String onScreenTest = "<br>0002.jpg<br>----------<br> <br>   <br><br>WVIAMS OeInt Mnlinestel<br><br>*<br>\f<br>0003.jpg<br>----------<br>Eien part thinly sliced<br><br> <br>\f<br>0005.jpg<br>----------<br>oS<br>©<br>&<br><br>S<br>q<br>@<br>><br>©<br>©)<br>Bo)<br>TS<br>©<br>fo)<br>Ke)<br><br> <br>\f<br>0006.jpg<br>----------<br>2 eggs and set aside<br><br> <br>\f<br>0008.jpg<br>----------<br>1-2 tosp red chilli flakes<br><br> <br>\f<br>0009.jpg<br>----------<br>Stir fry on high flame<br><br> <br>\f<br>0010.jpg<br>----------<br>a)<br>@<br>&<br>©<br>®<br>—<br>iS<br>i)<br>=<br>=<br>Ne<br>S<br>o)<br>eS<br>RS<br>(ep)<br>)<br>©<br>=y<br>oy<br>co<br>ie<br>ie<br><¢<br><br> <br>\f<br>0011.jpg<br>----------<br>Add sauces (check written recipe)<br><br> <br>\f<br>0014.jpg<br>----------<br>Switch off the flame and mix<br><br> <br>\f<br>";
    String description = "Spicy egg fried rice \uD83D\uDD25 Recipe \uD83D\uDC47<br><br>INGREDIENTS:<br>5-6 green onions<br>6 cloves of garlic<br>1-2 tbsp crushed chilli flakes (add based on spice tolerance)<br>2 servings cold rice<br>2 eggs<br>Cooking oil as needed<br>—Sauce—<br>1 tbsp light/regular soy sauce<br>1 tbsp dark soy sauce<br>1 tbsp water<br>1 tsp sugar<br>Note: Add salt if needed<br><br>PROCESS:<br>1. Clean and trim 5-6 green onions. Mince the white parts and thinly slice the green parts for garnish<br>2. Mince 6 cloves of garlic. Add more or less depending on size of the cloves or taste<br>3. Take 2 servings of cold, day old rice in a bowl and separate the clumps. This helps in even stir frying. Cold, slightly dried out rice is essential for the perfect fried rice texture<br>4. Mix the sauce ingredients in a bowl and set aside<br>5. To a heated wok/pan add some oil and scramble 2 eggs with a pinch of salt. Set aside<br>6. Set flame to medium and add 2 tbsp neutral cooking oil. When the oil is hot, sauté the garlic and green onion for 30 seconds<br>7. Reduce flame to low and sauté 1-2 tbsp crushed red chilli flakes for 30 seconds or until fragrant. Chilli flakes will burn if the flame is too high<br>8. Increase flame to medium high, add rice and stir fry until mixed well with the aromatics<br>9. Add the prepared sauce and stir fry for 2-3 minutes. The rice needs to fry well so keep tossing and turning<br>10. When the rice is fried well, add the previously scrambled eggs. Mix everything well and adjust salt if needed<br>11. Add the green part of green onions and switch off the flame. You don’t want to overcook them<br>12. Serve the spicy egg fried rice with a crispy fried egg, a side of your choice or on its own<br><br>#easyrecipes #asmrcooking #recipes #friedrice";
    String transcript = "WEBVTT<br><br>00:00.000 --> 00:16.480<br>Wuhan<br><br>00:16.480 --> 00:16.980<br>Okay, now add the<br><br>00:16.980 --> 00:23.920<br>finely chopped onion<br><br>";
    List<MediaContentEntity> contentEntities = new ArrayList<>();
    for (Long projectId : projectIds) {
      MediaContentEntity contentEntity = new MediaContentEntity();
      contentEntity.setMediaId(KALUtils.generateUid());
      contentEntity.setScrapedDescription(description);
      contentEntity.setScrapedTitle("Spicy egg fried rice");
      contentEntity.setRawTranscript(transcript);
      contentEntity.setOnScreenText(onScreenTest);
      contentEntity.setPermalink("https://www.instagram.com/reel/CmywGx6MYso/?igshid=YmMyMTA2M2Y=");
      contentEntity.setProjectId(projectId);
      contentEntities.add(contentEntity);
    }
    mediaContentRepo.saveAll(contentEntities);
  }


  private void createSampledImages(List<Long> projectIds) {
    TypeReference<List<SampledImageEntity>> typeRef = new TypeReference<>() {};
    List<SampledImageEntity> sampledImageEntities = null;
    try {
      sampledImageEntities = objectMapper.readValue(loadFromFile("mr_public_sampled_image.json"), typeRef);
    } catch (JsonProcessingException e) {
      log.error(e.getLocalizedMessage());
    }
    sampledImageEntities.sort(Comparator.comparing(SampledImageEntity::getImageKey));
    for (Long projectId : projectIds) {
      for (SampledImageEntity entity : sampledImageEntities) {
        entity.setId(null);
        entity.setProjectId(projectId);
    }
      sampledImageRepo.saveAll(sampledImageEntities);
    }
  }

  @SneakyThrows
  private String loadFromFile(String fileName) {
    InputStream resource = new ClassPathResource("seed/" + fileName).getInputStream();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource)) ) {
      String json = FileCopyUtils.copyToString(reader);
      return json;
    }
  }

  private String getPrimaryThumbnail(String key) {
    return awsProps.getCdn() + "/" + key;
  }
}
