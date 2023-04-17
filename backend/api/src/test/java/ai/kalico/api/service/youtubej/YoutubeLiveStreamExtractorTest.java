package ai.kalico.api.service.youtubej;

import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoInfo;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.videos.VideoDetails;
import ai.kalico.api.service.youtubej.model.videos.VideoInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;


import static ai.kalico.api.service.youtubej.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@Ignore
@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
class YoutubeLiveStreamExtractorTest extends AbstractTestNGSpringContextTests {

    @Test(enabled = false)
    void getLiveStreamHLS_Success() {
        YoutubeDownloader downloader = new YoutubeDownloader();

        assertDoesNotThrow(() -> {
            Response<VideoInfo> response = downloader.getVideoInfo(new RequestVideoInfo(LIVE_ID));
            assertTrue(response.ok());
            VideoInfo video = response.data();
            VideoDetails details = video.details();

            assertEquals(LIVE_ID, details.videoId(), "videoId should be " + LIVE_ID);

            assertTrue(details.isLive(), "this should be a live video");
            assertNotNull(details.liveUrl(), "there should be a live video url");
            assertTrue(isReachable(details.liveUrl()), "url should be reachable");
        });
    }

    @Test
    void getWasLiveFormats_Success() {
        YoutubeDownloader downloader = new YoutubeDownloader();

        assertDoesNotThrow(() -> {
            Response<VideoInfo> response = downloader.getVideoInfo(new RequestVideoInfo(WAS_LIVE_ID));
            assertTrue(response.ok());
            VideoInfo video = response.data();
            VideoDetails details = video.details();

            assertEquals(WAS_LIVE_ID, details.videoId(), "videoId should be " + WAS_LIVE_ID);
            assertTrue(details.isLiveContent(), "videoId was live ");
            assertFalse(details.thumbnails().isEmpty(), "thumbnails should be not empty");
            assertNotEquals(0, details.lengthSeconds(), "length should not be 0");
            assertNotEquals(0, details.viewCount(), "viewCount should not be 0");
        });
    }

}
