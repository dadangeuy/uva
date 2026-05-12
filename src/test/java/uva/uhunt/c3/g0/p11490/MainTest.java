package uva.uhunt.c3.g0.p11490;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import uva.common.helper.TestHelper;

import java.time.Duration;

public class MainTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(4);

    @Test
    public void sample() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "sample");
    }

    @Test
    @Disabled("invalid output from uDebug (contains negative missing soldiers).")
    public void udebug() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "udebug");
    }
}
