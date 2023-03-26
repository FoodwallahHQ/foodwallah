package ai.kalico.api.service.youtubej;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.youtubej.model.Utils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class UtilsTest extends AbstractTestNGSpringContextTests  {

    @Test
    public void parseLengthSeconds_Success() {
        assertSeconds("1:32:54", 3600 + (32 * 60) + 54);
        assertSeconds("24", 24);
        assertSeconds("17:05", (17 * 60) + 5);
    }

    private static void assertSeconds(String text, int expected) {
        int actual = Utils.parseLengthSeconds(text);
        assertEquals(expected, actual, "Seconds: " + text);
    }

    @Test
    public void parseViewCount_Success() {
        assertViewCount("2 865 063 views", 2_865_063L);
        assertViewCount("1 view", 1L);
    }

    private static void assertViewCount(String text, long expected) {
        long actual = Utils.parseViewCount(text);
        assertEquals(expected, actual, "Views: " + text);
    }
}
