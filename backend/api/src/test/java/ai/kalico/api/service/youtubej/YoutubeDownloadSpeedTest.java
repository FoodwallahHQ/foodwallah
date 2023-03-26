package ai.kalico.api.service.youtubej;

import static org.junit.jupiter.api.Assertions.*;

import ai.kalico.api.service.youtubej.downloader.DownloaderImpl;
import ai.kalico.api.service.youtubej.downloader.YoutubeProgressCallback;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoInfo;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.videos.VideoInfo;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ai.kalico.api.service.ServiceTestConfiguration;
import lombok.extern.slf4j.Slf4j;

import ai.kalico.api.service.youtubej.model.videos.formats.Format;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class YoutubeDownloadSpeedTest extends AbstractTestNGSpringContextTests {
    private static final File outDir = new File("videos");

    // Zooming out from earth
    private static final String ZOOM_OUT_ID = "DgqAAE9Aagc";

    // Make download pauses obvious
    private static final YoutubeProgressCallback<File> listener = new YoutubeProgressCallback<File>() {

        @Override
        public void onFinished(File file) {}

        @Override
        public void onError(Throwable throwable) {}

        @Override
        public void onDownloading(int progress) {
            System.out.print(".");
        }
    };

    protected DownloaderImpl downloader;
    protected YoutubeDownloader youtubeDownloader;
    private static Method straightMethod;
    private static Method byPartMethod;

    @BeforeMethod
    public void initReflectMethods() throws NoSuchMethodException, SecurityException {
        for (Method declaredMethod : DownloaderImpl.class.getDeclaredMethods()) {
            if (declaredMethod.getName().equals("downloadStraight")) {
                straightMethod = declaredMethod;
                declaredMethod.setAccessible(true);
            } else if (declaredMethod.getName().equals("downloadByPart")){
                byPartMethod = declaredMethod;
                declaredMethod.setAccessible(true);
            }
        }

      initDownloader();
    }

    public void initDownloader() {
        final Config config = Config.buildDefault();
        this.downloader = new DownloaderImpl(config);
        this.youtubeDownloader = new YoutubeDownloader(config, downloader);
        if (!outDir.isDirectory()) {
            outDir.mkdirs();
        }
    }

    @AfterMethod
    void cleanOutDir() {
        if (outDir.isDirectory()) {
            TestUtils.clean(outDir);
        }
    }

    @Test(enabled = false)
    void downloadSpeed_Success() {

        assertDoesNotThrow(() -> {
            Response<VideoInfo> response = youtubeDownloader.getVideoInfo(new RequestVideoInfo(ZOOM_OUT_ID));
            VideoInfo video = response.data();
            testSpeed(downloader, video, 18);
            testSpeed(downloader, video, 135);
        });
    }

    private static void testSpeed(DownloaderImpl downloader, VideoInfo video, int itag) {
        final Format format = video.findFormatByItag(itag);
        assertNotNull(format, "findFormatByItag should return not null format");

        assertDoesNotThrow(() -> {
            String title = video.details().title();
            long straightTime = System.currentTimeMillis();
            File straightFile = download(downloader, title, format, false);
            straightTime = System.currentTimeMillis() - straightTime;
            System.out.println(" " + straightTime + "ms");

            long byPartTime = System.currentTimeMillis();
            File byPartFile = download(downloader, title, format, true);
            byPartTime = System.currentTimeMillis() - byPartTime;
            System.out.println(" " + byPartTime + "ms");

            assertSameContent(straightFile, byPartFile);

            if (format.isAdaptive()) {
                double ratio = (double) straightTime / (double) byPartTime;
                assertTrue(byPartTime < straightTime, "Download by part should be faster for format: " + format.itag());
                System.out.println("Download by part is " + String.format("%.2f", ratio) + " faster for format " + format.itag());
            } else {
                double ratio = (double) byPartTime / (double) straightTime;
                assertTrue(byPartTime > straightTime, "Straight download should be faster for format: " + format.itag());
                System.out.println("Straight download is " + String.format("%.2f", ratio) + " faster for format " + format.itag());
            }
        });
    }

    private static File download(DownloaderImpl downloader, String title, Format format, boolean isByPart) throws IOException, YoutubeException {
        System.out.print(isByPart ? "By part " : "Straight");
        File outputFile = new File(outDir, title + "_" + (isByPart ? "bypart" : "straight") + format.itag().id() + "." + format.extension().value());
        try (OutputStream os = new FileOutputStream(outputFile)) {
            Method method = isByPart ? byPartMethod : straightMethod;
            method.invoke(downloader, format, os, null, null, listener);
            listener.onFinished(outputFile);
            return outputFile;
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            fail(e);
            return null;
        } catch (InvocationTargetException e) {
            listener.onError(e.getTargetException());
            if (e.getTargetException() instanceof YoutubeException) {
                throw (YoutubeException) e.getTargetException();
            } else if (e.getTargetException() instanceof IOException) {
                throw (IOException) e.getTargetException();
            }
            fail(e);
            return null;
        }
    }

    private static void assertSameContent(File left, File right) throws IOException {
        assertEquals(left.length(), right.length());

        try (
                InputStream leftIs = new FileInputStream(left);
                InputStream rightIs = new FileInputStream(right);
        ) {
            int leftRead, rightRead;
            byte[] leftBuf = new byte[4096];
            byte[] rightBuf = new byte[4096];

            while ((leftRead = leftIs.read(leftBuf)) != -1) {
                rightRead = rightIs.read(rightBuf);
                // Local disk access, buffers should always be equal
                assertEquals(leftRead, rightRead);
                assertArrayEquals(leftBuf, rightBuf);
            }
        }
    }
}
