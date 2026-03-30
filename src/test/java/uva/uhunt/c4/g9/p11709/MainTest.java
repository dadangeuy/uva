package uva.uhunt.c4.g9.p11709;

import org.junit.jupiter.api.Test;
import uva.common.helper.TestHelper;

import java.time.Duration;

public class MainTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    @Test
    public void sample() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "sample");
    }

    @Test
    public void anjupiter() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "anjupiter");
    }

    @Test
    public void twyu() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "twyu");
    }
}
