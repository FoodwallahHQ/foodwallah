package ai.kalico.api.service.youtubej;

import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoInfo;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.Extension;
import ai.kalico.api.service.youtubej.model.videos.VideoDetails;
import ai.kalico.api.service.youtubej.model.videos.VideoInfo;
import ai.kalico.api.service.youtubej.model.videos.formats.*;
import ai.kalico.api.service.youtubej.model.videos.formats.Format;
import ai.kalico.api.service.youtubej.model.videos.formats.VideoWithAudioFormat;
import ai.kalico.api.service.youtubej.model.videos.quality.AudioQuality;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class YoutubeVideoExtractorTest extends AbstractTestNGSpringContextTests  {

    @Test
    void getVideo_WithoutSignature_Success() {
        YoutubeDownloader downloader = new YoutubeDownloader();

        assertDoesNotThrow(() -> {
            Response<VideoInfo> response = downloader.getVideoInfo(new RequestVideoInfo(
                TestUtils.ME_AT_THE_ZOO_ID));
            assertTrue(response.ok());
            VideoInfo video = response.data();

            VideoDetails details = video.details();
            Assertions.assertEquals(TestUtils.ME_AT_THE_ZOO_ID, details.videoId(), "videoId should be " + TestUtils.ME_AT_THE_ZOO_ID);

            String title = "Me at the zoo";
            assertEquals(title, details.title(), "title should be " + title);
            assertFalse(details.thumbnails().isEmpty(), "thumbnails should be not empty");
            assertNotEquals(0, details.lengthSeconds(), "length should not be 0");
            assertNotEquals(0, details.viewCount(), "viewCount should not be 0");

            List<Format> formats = video.formats();
            assertFalse(formats.isEmpty(), "formats should not be empty");

            int itag = 18;
            Format format = video.findFormatByItag(itag);
            assertNotNull(format, "findFormatByItag should return not null format");
            assertTrue(format instanceof VideoWithAudioFormat, "format with itag " + itag + " should be instance of AudioVideoFormat");
            assertEquals(itag, format.itag().id(), "itag should be " + itag);

            int expectedWidth = 320;
            Integer width = ((VideoWithAudioFormat) format).width();
            assertNotNull(width, "width should not be null");
            assertEquals(expectedWidth, width.intValue(), "format with itag " + itag + " should have width " + expectedWidth);

            int expectedHeight = 240;
            Integer height = ((VideoWithAudioFormat) format).height();
            assertNotNull(height, "height should not be null");
            assertEquals(expectedHeight, height.intValue(), "format with itag " + itag + " should have height " + expectedHeight);

            AudioQuality expectedAudioQuality = AudioQuality.low;
            assertEquals(expectedAudioQuality, ((VideoWithAudioFormat) format).audioQuality(), "audioQuality should be " + expectedAudioQuality.name());

            String expectedMimeType = "video/mp4";
            assertTrue(format.mimeType().contains(expectedMimeType), "mimetype should be " + expectedMimeType);

            Extension expectedExtension = Extension.MPEG4;
            assertEquals(expectedExtension, format.extension(), "extension should be " + expectedExtension.value());

            String expectedLabel = "240p";
            assertEquals(expectedLabel, ((VideoWithAudioFormat) format).qualityLabel(), "qualityLable should be " + expectedLabel);

            assertNotNull(format.url(), "url should not be null");

            Assertions.assertTrue(TestUtils.isReachable(format.url()), "url should be reachable");
        });
    }

    @Test
    void getVideo_Unavailable_ThrowsException() {
        YoutubeDownloader downloader = new YoutubeDownloader();
        String unavailableVideoId = "12345678901";
        Response<VideoInfo> response = downloader.getVideoInfo(new RequestVideoInfo(unavailableVideoId));
        assertFalse(response.ok());
        assertTrue(response.error() instanceof YoutubeException.BadPageException);
    }

}
