package ai.kalico.api.service.youtubej;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.kalico.api.service.youtubej.base64.Base64EncoderImpl;
import java.util.Base64;
import java.util.Random;

import ai.kalico.api.service.ServiceTestConfiguration;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class Base64Test extends AbstractTestNGSpringContextTests {

    @Test
    public void encodeRandomAndCheck_Success() {
        Random r = new Random();
        Base64.Encoder jdkEncoder = Base64.getUrlEncoder();
        Base64EncoderImpl ownEncoder = new Base64EncoderImpl();
        for (int i = 1; i < 1000; i++) {
            byte[] bytes = new byte[i];
            r.nextBytes(bytes);
            String expected = jdkEncoder.encodeToString(bytes);
            String actual = ownEncoder.encodeToString(bytes);
            assertEquals(expected, actual, "JDK and own encodings should be equal");
        }
    }

}
