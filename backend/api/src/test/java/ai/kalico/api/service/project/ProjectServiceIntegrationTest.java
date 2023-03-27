package ai.kalico.api.service.project;

import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.utils.SeedData;
import ai.kalico.api.service.user.UserService;
import ai.kalico.api.utils.ServiceTestHelper;
import ai.kalico.api.utils.migration.FlywayMigration;
import com.kalico.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Bizuwork Melesse
 * created on 8/22/21
 */
@Ignore
@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class ProjectServiceIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private SeedData seedData;

    @Autowired
    private FlywayMigration flywayMigration;

    @Autowired
    private ServiceTestHelper testHelper;

    @Autowired
    private UserService userService;
    private final String userId = UUID.randomUUID().toString();

    @BeforeClass
    public void setup() {
      testHelper.prepareSecurity(userId);
    }

    @AfterTest
    public void teardown() {
    }

    @BeforeMethod
    public void beforeEachTest(Method method) {
        log.info("  Testcase: " + method.getName() + " has started");
        flywayMigration.migrate(true);

        UserProfileResponse response = userService.getOrCreateUserprofile();
        assertThat(response, is(notNullValue()));
        assertThat(response.getProfile().getFirebaseId(), startsWith(userId));
        seedData.seed(userId);
    }

    @AfterMethod
    public void afterEachTest(Method method) {
        log.info("  Testcase: " + method.getName() + " has ended");
    }

    @Test
    public void createProjectFromUrlTest() {
      CreateProjectRequest request = new CreateProjectRequest()
          .projectName("Test Project")
          .contentLink("https://www.youtube.com/watch?v=U-0JCdjkREU")
          .contentType(KalicoContentType.INTERVIEW)
          .embedImages(false)
          .paraphrase(true);
      CreateProjectResponse response = projectService.createProject(request);
      assertThat(response.getStatus().toLowerCase(), startsWith("ok"));
      assertThat(response.getProjectId(), is(notNullValue()));

      ProjectDetail detail = projectService.getProjectById(response.getProjectId());
      assertThat(detail, is(notNullValue()));
      assertThat(detail.getId().equals(response.getProjectId()), is(true));
      assertThat(detail.getName().get(), is(notNullValue()));
      assertThat(detail.getDateCreated(), is(notNullValue()));
      assertThat(detail.getContent(), is(nullValue()));
    }

    @Test
    public void updateProjectTest() {
      String projectId = getProjectId();
      ProjectDetail detail = projectService.getProjectById(projectId);
      UpdateProjectContentRequest request = new UpdateProjectContentRequest();
      detail.getContent().add(new ContentItem()
          .type("image")
          .url("https://picsum.photos/200/300")
          .children(List.of(new ContentItemChildren()
              .text(""))));
      request.setContent(detail.getContent());
      request.setProjectUid(projectId);
      projectService.updateProjectContent(request);

      ProjectDetail updatedDetail = projectService.getProjectById(projectId);
      assertThat(updatedDetail, is(notNullValue()));
      assertThat(updatedDetail.getContent().size(), equalTo(detail.getContent().size()));
      boolean imageFound = false;
      for (ContentItem contentItem : updatedDetail.getContent()) {
        if (contentItem.getType().equals("image")) {
          imageFound = true;
        }
      }
      assertThat(imageFound, is(true));
    }

    @Test
    public void deleteProjectTest() {
      String projectId = getProjectId();
      ProjectDetail detail = projectService.getProjectById(projectId);
      assertThat(detail, is(notNullValue()));
      assertThat(detail.getId().equals(projectId), is(true));

      // Delete it
      projectService.deleteProject(projectId);
      detail = projectService.getProjectById(projectId);
      assertThat(detail, is(nullValue()));
    }

    @Test
    public void getAllProjectsTest() {
      PageableResponse response = projectService.getAllProjects();
      assertThat(response, is(notNullValue()));
      assertThat(response.getNumPages(), is(equalTo(1)));
      assertThat(response.getTotalRecords(), is(equalTo(6)));
      assertThat(response.getRecords().size(), is(equalTo(6)));
      for (Project record : response.getRecords()) {
        assertThat(record.getProjectUid(), is(notNullValue()));
        assertThat(record.getProjectName(), is(notNullValue()));
      }
    }

    @Test
    public void getProjectByIdTest() {
      String projectId = getProjectId();
      ProjectDetail detail = projectService.getProjectById(projectId);
      assertThat(detail, is(notNullValue()));
      assertThat(detail.getId().equals(projectId), is(true));
      assertThat(detail.getName().get(), is(notNullValue()));
      assertThat(detail.getDateCreated(), is(notNullValue()));
      assertThat(detail.getContent().size(), is(greaterThan(0)));
      for (ContentItem contentItem : detail.getContent()) {
        assertThat(contentItem.getType(), is(notNullValue()));
        assertThat(contentItem.getChildren().size(), is(greaterThan(0)));
        for (ContentItemChildren child : contentItem.getChildren()) {
          assertThat(child.getText(), is(notNullValue()));
        }
      }
    }

  @Test
  public void getMediaContentByIdTest() {
    String projectId = getProjectId();
    MediaContent content = projectService.getMediaContent(projectId);
    assertThat(content, is(notNullValue()));
    assertThat(content.getProjectId().equals(projectId), is(true));
    assertThat(content.getMediaId(), is(notNullValue()));
    assertThat(content.getPermalink(), is(notNullValue()));
    assertThat(content.getTitle(), is(notNullValue()));
    assertThat(content.getDescription(), is(notNullValue()));
    assertThat(content.getTranscript(), is(notNullValue()));
  }

  @Test
  public void getSampledImagesTest() {
      String projectId = getProjectId();
      List<String> sampledImages = projectService.getSampledImages(projectId);
      assertThat(sampledImages, is(notNullValue()));
      assertThat(sampledImages.size(), is(equalTo(261)));
      for (String sampledImage : sampledImages) {
        assertThat(sampledImage, containsString("https"));
      }
  }

  private String getProjectId() {
    PageableResponse response = projectService.getAllProjects();
    return response.getRecords().get(0).getProjectUid();
  }
}
