package uva.uhunt.c3.g2.p12862;

import org.junit.jupiter.api.Test;
import uva.common.helper.TestHelper;

import java.time.Duration;

public class MainTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(2);

    @Test
    public void sample() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "sample");
    }

    @Test
    public void anjupiter() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "anjupiter");
    }
}
