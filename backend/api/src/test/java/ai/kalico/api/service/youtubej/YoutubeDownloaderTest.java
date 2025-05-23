package ai.kalico.api.service.youtubej;

import com.alibaba.fastjson.JSONObject;
import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.youtubej.downloader.YoutubeCallback;
import ai.kalico.api.service.youtubej.downloader.YoutubeProgressCallback;
import ai.kalico.api.service.youtubej.downloader.request.*;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.downloader.response.ResponseStatus;
import ai.kalico.api.service.youtubej.model.Extension;
import ai.kalico.api.service.youtubej.model.videos.VideoInfo;
import ai.kalico.api.service.youtubej.model.videos.formats.Format;
import ai.kalico.api.service.youtubej.model.subtitles.SubtitlesInfo;
import ai.kalico.api.service.youtubej.downloader.request.RequestSubtitlesDownload;
import ai.kalico.api.service.youtubej.downloader.request.RequestSubtitlesInfo;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoFileDownload;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoInfo;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoStreamDownload;
import ai.kalico.api.service.youtubej.downloader.request.RequestWebpage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Ignore
@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
class YoutubeDownloaderTest extends AbstractTestNGSpringContextTests {

    private List<SubtitlesInfo> getSubtitles() {
        Response<List<SubtitlesInfo>> response = downloader.getSubtitlesInfo(new RequestSubtitlesInfo(
            TestUtils.N3WPORT_ID));
        assertTrue(response.ok());
        return response.data();
    }

    private Format getFormat() {
        Response<VideoInfo> response = downloader.getVideoInfo(new RequestVideoInfo(
            TestUtils.ME_AT_THE_ZOO_ID));
        assertTrue(response.ok());
        VideoInfo video = response.data();

        int itag = 18;
        return video.findFormatByItag(itag);
    }

    private void validateFileDownloadAndDelete(Format format, File file) {
        validateFileDownloadAndDelete(format, file, null, false);
    }

    private void validateFileDownloadAndDelete(Format format, File file, String fileName) {
        validateFileDownloadAndDelete(format, file, fileName, false);
    }

    private void validateFileDownloadAndDelete(Format format, File file, String fileName, boolean overwrite) {
        assertTrue(file.exists(), "file should be downloaded");

        Extension extension = format.extension();
        assertTrue(file.getName().endsWith(extension.value()), "file name should ends with: " + extension.value());

        assertTrue(file.length() > 0, "file should be not empty");
        if (fileName != null) {
            if (overwrite) {
                String actualFileName = fileName + "." + format.extension().value();
                assertEquals(file.getName(), actualFileName, "file name should be: " + actualFileName);
            } else {
                assertTrue(file.getName().contains(fileName), "file name should contains: " + fileName);
            }
        }
        file.delete();
    }

    private YoutubeDownloader downloader;

    @BeforeMethod
    public void initDownloader() {
        downloader = new YoutubeDownloader();
    }

    @Test
    public void downloadVideo_Success() {
        File outDir = new File("videos");
        String fileName = "myAwesomeName";

        Format format = getFormat();
        assertNotNull(format, "findFormatByItag should return not null format");
        Assertions.assertTrue(TestUtils.isReachable(format.url()), "url should be reachable");

        assertDoesNotThrow(() -> {
            Response<File> responseFile = downloader.downloadVideoFile(new RequestVideoFileDownload(format).saveTo(outDir));
            assertTrue(responseFile.ok());

            File file = responseFile.data();
            validateFileDownloadAndDelete(format, file);
        }, "download video sync should work");

        assertDoesNotThrow(() -> {
            Response<File> responseFile = downloader.downloadVideoFile(new RequestVideoFileDownload(format).saveTo(outDir).async());
            Assertions.assertEquals(ResponseStatus.downloading, responseFile.status());

            File file = responseFile.data(5, TimeUnit.SECONDS);
            validateFileDownloadAndDelete(format, file);
        }, "download video should work async future");

        assertDoesNotThrow(() -> {

            RequestVideoFileDownload request = new RequestVideoFileDownload(format).saveTo(outDir).renameTo(fileName);
            Response<File> responseFile = downloader.downloadVideoFile(request);
            assertTrue(responseFile.ok());

            File file = responseFile.data();
            validateFileDownloadAndDelete(format, file, fileName);
        }, "download video sync with specified output file name should work");

        assertDoesNotThrow(() -> {
            RequestVideoFileDownload request = new RequestVideoFileDownload(format).saveTo(outDir).renameTo(fileName).overwriteIfExists(true);
            Response<File> responseFile = downloader.downloadVideoFile(request);
            assertTrue(responseFile.ok());

            File file = responseFile.data();
            validateFileDownloadAndDelete(format, file, fileName, true);
        }, "download video sync with specified output file name and overwrite flag should work");


        assertDoesNotThrow(() -> {
            YoutubeProgressCallback<File> mockCallback = mock(YoutubeProgressCallback.class);
            RequestVideoFileDownload request = new RequestVideoFileDownload(format).callback(mockCallback).async();
            Response<File> responseFile = downloader.downloadVideoFile(request);

            assertTimeout(Duration.ofSeconds(5), () -> {
                assertTrue(responseFile.ok());
                File file = responseFile.data();

                validateFileDownloadAndDelete(format, file);
            });

            verify(mockCallback, atLeastOnce()).onFinished(any(File.class));
            verify(mockCallback, atLeastOnce()).onDownloading(anyInt());
        }, "download video async with callback should work");

        assertDoesNotThrow(() -> {
            RequestVideoFileDownload request = new RequestVideoFileDownload(format).renameTo(fileName).async();
            Response<File> responseFile = downloader.downloadVideoFile(request);

            assertTimeout(Duration.ofSeconds(5), () -> {
                assertTrue(responseFile.ok());
                File file = responseFile.data();

                validateFileDownloadAndDelete(format, file, fileName);
            });

        }, "download video async with specified output file name should work");

        assertDoesNotThrow(() -> {
            RequestVideoFileDownload request = new RequestVideoFileDownload(format).renameTo(fileName).overwriteIfExists(true).async();
            Response<File> responseFile = downloader.downloadVideoFile(request);

            assertTimeout(Duration.ofSeconds(5), () -> {
                assertTrue(responseFile.ok());
                File file = responseFile.data();

                validateFileDownloadAndDelete(format, file, fileName, true);
            });
        }, "download video async with specified output file name and overwrite flag should work");

        assertDoesNotThrow(() -> {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                Response<Void> responseFile = downloader.downloadVideoStream(new RequestVideoStreamDownload(format, os));
                assertTrue(responseFile.ok());
                byte[] outputBytes = os.toByteArray();
                int size = outputBytes.length;
                assertTrue(size > 0, "OutputStream should have some content");

                Long contentLength = format.contentLength();
                if (contentLength != null) {
                    assertEquals(contentLength.intValue(), size, "OutputStream bytes size be contentLength");
                }
            }
        });
    }

    @Test(enabled = false)
    public void downloadSubtitle_Success() {
        List<SubtitlesInfo> subtitlesInfo = getSubtitles();
        assertFalse(subtitlesInfo.isEmpty(), "subtitles should be not empty");

        assertDoesNotThrow(() -> {
            for (SubtitlesInfo subtitleInfo : subtitlesInfo) {
                RequestSubtitlesDownload request = new RequestSubtitlesDownload(subtitleInfo);
                Response<String> responseSubtitle = downloader.downloadSubtitle(request);
                assertTrue(responseSubtitle.ok());
                assertFalse(responseSubtitle.data().isEmpty(), "subtitles should not be empty");
            }
        }, "download subtitle should work");

        assertDoesNotThrow(() -> {
            for (SubtitlesInfo info : subtitlesInfo) {
                SubtitlesInfo subtitleInfo = new SubtitlesInfo(info.getUrl().replace("lang=" + info.getLanguage(), "lang=not_a_code"), "not_a_code", false);
                RequestSubtitlesDownload request = new RequestSubtitlesDownload(subtitleInfo);
                Response<String> responseSubtitle = downloader.downloadSubtitle(request);
                assertFalse(responseSubtitle.ok());
                assertNotNull(responseSubtitle.error(), "error should be not null");
            }
        }, "download subtitles with wrong lang code should throw exception");

        assertDoesNotThrow(() -> {
            for (SubtitlesInfo subtitleInfo : subtitlesInfo) {
                RequestWebpage request = new RequestSubtitlesDownload(subtitleInfo)
                        .async();
                Response<String> responseSubtitle = downloader.downloadSubtitle(request);

                assertTimeout(Duration.ofSeconds(5), () -> {
                    String subtitles = responseSubtitle.data();
                    assertNotNull(subtitles, "subtitles should be not null");
                    assertFalse(subtitles.isEmpty(), "subtitles should not be empty");
                });
            }
        }, "download subtitle async should work");

        assertDoesNotThrow(() -> {
            YoutubeCallback<String> callback = Mockito.mock(YoutubeCallback.class);

            for (SubtitlesInfo subtitleInfo : subtitlesInfo) {
                RequestWebpage request = new RequestSubtitlesDownload(subtitleInfo)
                        .callback(callback)
                        .async();
                Response<String> responseSubtitle = downloader.downloadSubtitle(request);

                assertTimeout(Duration.ofSeconds(5), () -> {
                    String subtitles = responseSubtitle.data();
                    assertNotNull(subtitles, "subtitles should be not null");
                    assertFalse(subtitles.isEmpty(), "subtitles should not be empty");
                });
            }

            verify(callback, times(subtitlesInfo.size())).onFinished(any());
            verify(callback, never()).onError(any());
        }, "download subtitles async with callback should call onFinished");

        assertDoesNotThrow(() -> {
            YoutubeCallback<String> callback = Mockito.mock(YoutubeCallback.class);

            for (SubtitlesInfo subtitleInfo : subtitlesInfo) {
                subtitleInfo = new SubtitlesInfo(subtitleInfo.getUrl().replace("lang=" + subtitleInfo.getLanguage(), "lang=not_a_code"), "not_a_code", false);

                RequestWebpage request = new RequestSubtitlesDownload(subtitleInfo)
                        .callback(callback)
                        .async();
                Response<String> responseSubtitle = downloader.downloadSubtitle(request);

                assertTimeout(Duration.ofSeconds(5), () -> {
                    assertNotNull(responseSubtitle.error(), "error should be not null");
                    assertNull(responseSubtitle.data(), "subtitles should be null");
                });

            }
            verify(callback, times(subtitlesInfo.size())).onError(any());
            verify(callback, never()).onFinished(any());
        }, "download subtitles async with callback and wrong lang code should call onError");

        assertDoesNotThrow(() -> {
            for (SubtitlesInfo subtitleInfo : subtitlesInfo) {
                RequestSubtitlesDownload request = new RequestSubtitlesDownload(subtitleInfo)
                        .formatTo(Extension.JSON3);
                Response<String> responseSubtitle = downloader.downloadSubtitle(request);
                assertTrue(responseSubtitle.ok());
                String subtitles = responseSubtitle.data();
                assertFalse(subtitles.isEmpty(), "subtitles should not be empty");
                assertDoesNotThrow(() -> {
                    JSONObject.parseObject(subtitles);
                }, "subtitles should be formatted to json");
            }
        }, "download formatted subtitles should work");

        assertDoesNotThrow(() -> {
            for (SubtitlesInfo subtitleInfo : subtitlesInfo) {
                RequestSubtitlesDownload request = new RequestSubtitlesDownload(subtitleInfo)
                        .formatTo(Extension.JSON3)
                        .translateTo("uk");
                Response<String> responseSubtitle = downloader.downloadSubtitle(request);
                assertTrue(responseSubtitle.ok());
                String subtitles = responseSubtitle.data();
                assertFalse(subtitles.isEmpty(), "subtitles should not be empty");
                assertDoesNotThrow(() -> {
                    JSONObject.parseObject(subtitles);
                }, "subtitles should be formatted to json");
            }
        }, "download formatted and translated subtitles should work");
    }

    //    @Test // currently disabled because even on youtube.com translate feature does not work
    @Test(enabled = false)
    public void downloadSubtitles_FormattedTranslatedFromCaptions_Success() {
        YoutubeDownloader downloader = new YoutubeDownloader();
        assertDoesNotThrow(() -> {
            Response<VideoInfo> response = downloader.getVideoInfo(new RequestVideoInfo(
                TestUtils.N3WPORT_ID));
            assertTrue(response.ok());
            VideoInfo video = response.data();
            List<SubtitlesInfo> subtitlesInfo = video.subtitlesInfo();

            for (SubtitlesInfo subtitleInfo : subtitlesInfo) {
                RequestSubtitlesDownload request = new RequestSubtitlesDownload(subtitleInfo)
                        .formatTo(Extension.JSON3)
                        .translateTo("uk");
                Response<String> responseSubtitle = downloader.downloadSubtitle(request);
                assertEquals(ResponseStatus.completed, responseSubtitle.status());
                assertTrue(responseSubtitle.ok());
                String subtitle = responseSubtitle.data();
                assertFalse(subtitle.isEmpty(), "subtitles should not be empty");
                assertDoesNotThrow(() -> {
                    JSONObject.parseObject(subtitle);
                }, "subtitles should be formatted to json");
            }
        });
    }


}
