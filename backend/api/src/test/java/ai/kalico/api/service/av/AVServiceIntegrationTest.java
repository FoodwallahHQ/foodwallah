//package ai.kalico.api.service.av;
//
//
//import ai.kalico.api.service.ServiceTestConfiguration;
//import ai.kalico.api.service.user.UserService;
//import ai.kalico.api.service.utils.AVAsyncHelper;
//import ai.kalico.api.service.utils.CookieHelper;
//import ai.kalico.api.utils.ServiceTestHelper;
//import ai.kalico.api.utils.migration.FlywayMigration;
//import java.io.File;
//import java.lang.reflect.Method;
//import java.util.UUID;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
///**
// * @author Bizuwork Melesse
// * created on 8/22/21
// */
//@Slf4j
//@SpringBootTest(classes = ServiceTestConfiguration.class)
//public class AVServiceIntegrationTest extends AbstractTestNGSpringContextTests {
//
//    @Autowired
//    private AVService avService;
//
//    @Autowired
//    private ServiceTestHelper testHelper;
//
//    @Autowired
//    private FlywayMigration flywayMigration;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private CookieHelper cookieHelper;
//
//    @Autowired
//    private AVAsyncHelper asyncHelper;
//
//    private final String userId = UUID.randomUUID().toString();
//
//    @BeforeClass
//    public void setup() {
//        testHelper.prepareSecurity(userId);
//    }
//
//    @AfterTest
//    public void teardown() {
//    }
//
//    @SneakyThrows
//    @BeforeMethod
//    public void beforeEachTest(Method method) {
//        log.info("  Testcase: " + method.getName() + " has started");
////        flywayMigration.migrate(true);
////        userService.getOrCreateUserprofile();
////        cookieHelper.loadFromFile();
//    }
//
//    @AfterMethod
//    public void afterEachTest(Method method) {
//        log.info("  Testcase: " + method.getName() + " has ended");
//    }
//
//    @SneakyThrows
//    @Test(enabled = false) // Enable for local testing. FFmpeg not available in GitHub runner so the test will fail
//    public void youTubeContentTest() {
//      String url = "https://www.youtube.com/watch?v=U-0JCdjkREU";
//        avService.processMedia(url, null);
////      Thread.sleep(60000);
//      String videoId = avService.extractYouTubeVideoId(url);
//      asserFileExistsAndDelete(asyncHelper.getVideoPath(videoId));
//      asserFileExistsAndDelete(asyncHelper.getAudioPath(videoId));
//    }
//
//    @Test(enabled = false)
//    public void instagramContentTest() {
//        String url = "https://www.instagram.com/p/ClFbVsNDLJN/";
//        avService.processMedia(url, null);
////        VideoInfoDto dto = new VideoInfoDto();
////        dto.setVideoIdOverride("AjAeGLI");
////        avService.processInstagramVideo(dto);
//    }
//
//    @SneakyThrows
//    private void asserFileExistsAndDelete(String path) {
//        File file = new File(new File(path).getCanonicalPath());
//        assert file.exists();
//        boolean isDeleted = file.delete();
//        assert isDeleted;
//    }
//
//
//}
