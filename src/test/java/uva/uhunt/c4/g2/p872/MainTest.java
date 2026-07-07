package uva.uhunt.c4.g2.p872;

import uva.common.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;

public class MainTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    @Test
    @Timeout(3)
    public void sample() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "sample");
    }

    @Test
    @Timeout(3)
    public void fabikw() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "fabikw");
    }

    @Test
    @Timeout(3)
    public void anonymous() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "anonymous");
    }

    @Test
    @Timeout(3)
    public void twyu() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "twyu");
    }
}
