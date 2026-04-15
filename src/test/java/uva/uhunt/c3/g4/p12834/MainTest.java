package uva.uhunt.c3.g4.p12834;

import org.junit.jupiter.api.Test;
import uva.common.helper.TestHelper;

import java.time.Duration;

public class MainTest {
    private static Duration TIMEOUT = Duration.ofSeconds(1);

    @Test
    public void sample() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "sample");
    }

    @Test
    public void brianfry() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "brianfry");
    }
}
