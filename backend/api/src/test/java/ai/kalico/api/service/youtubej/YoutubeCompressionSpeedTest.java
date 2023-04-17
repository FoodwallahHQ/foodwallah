package ai.kalico.api.service.youtubej;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.youtubej.downloader.request.RequestChannelUploads;
import ai.kalico.api.service.youtubej.downloader.request.RequestPlaylistInfo;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchContinuation;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchResult;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoInfo;
import ai.kalico.api.service.youtubej.model.search.SearchResult;
import lombok.extern.slf4j.Slf4j;

import ai.kalico.api.service.youtubej.downloader.request.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

@Ignore
@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class YoutubeCompressionSpeedTest extends AbstractTestNGSpringContextTests {

    private static interface Task {
        void process() throws Exception;
    }

    private void execute(String title, Task task) {
        long time;

        // Compression disabled
        downloader.getConfig().setCompressionEnabled(false);
        time = System.currentTimeMillis();
        assertDoesNotThrow(() -> task.process());
        long plainTime = System.currentTimeMillis() - time;

        // Compression enabled
        downloader.getConfig().setCompressionEnabled(true);
        time = System.currentTimeMillis();
        assertDoesNotThrow(() -> task.process());
        long gzipTime = System.currentTimeMillis() - time;

        double ratio = (double) gzipTime / (double) plainTime;
        System.out.println(title + " - " + gzipTime + "/" + plainTime + " > " + ratio);
//        assertTrue(ratio < 1, title + " - gzip execution should be faster than plain");
    }

    private YoutubeDownloader downloader;

    @BeforeMethod
    void initDownloader() {
        this.downloader = new YoutubeDownloader();
    }

    @BeforeClass
    static void warmUp() {
        new YoutubeDownloader().getVideoInfo(new RequestVideoInfo(TestUtils.LIVE_ID)).data();
    }

    @Test
    void getVideoSpeed_Success() {
        execute("get video", () -> {
            downloader.getVideoInfo(new RequestVideoInfo(TestUtils.ME_AT_THE_ZOO_ID)).data();
            downloader.getVideoInfo(new RequestVideoInfo(TestUtils.N3WPORT_ID)).data();
        });
    }

    @Test
    void getPlaylistSpeed_Success() {
        execute("get playlist", () -> {
            downloader.getPlaylistInfo(new RequestPlaylistInfo(YoutubePlaylistTest.ASK_NASA_PLAYLIST_ID)).data();
            downloader.getPlaylistInfo(new RequestPlaylistInfo(YoutubePlaylistTest.LOTR_PLAYLIST_ID)).data();
        });
    }

    @Test(enabled = false)
    void getChannelSpeed_Success() {
        execute("get channel", () -> {
            downloader.getChannelUploads(new RequestChannelUploads(YoutubeChannelUploadsTest.CHANNELID)).data();
            downloader.getChannelUploads(new RequestChannelUploads(YoutubeChannelUploadsTest.MUSICCHANNELID)).data();
        });
    }

    @Test
    void searchSpeed_Success() {
        execute("search", () -> {
            SearchResult result = downloader.search(new RequestSearchResult("nasa")).data();
            downloader.searchContinuation(new RequestSearchContinuation(result)).data();
            result = downloader.search(new RequestSearchResult("science")).data();
            downloader.searchContinuation(new RequestSearchContinuation(result)).data();
        });
    }
}
