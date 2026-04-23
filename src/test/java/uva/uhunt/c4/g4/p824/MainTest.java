package uva.uhunt.c4.g4.p824;

import org.junit.jupiter.api.Test;
import uva.common.helper.TestHelper;

import java.time.Duration;

public class MainTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    @Test
    public void sample() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "sample");
    }

    @Test
    public void sourav() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "sourav");
    }
}
