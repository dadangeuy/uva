package uva.uhunt.c3.g1.p13141;

import org.junit.jupiter.api.Test;
import uva.common.helper.TestHelper;

import java.time.Duration;

public class MainTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(1);

    @Test
    public void sample() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "sample");
    }

    @Test
    public void udebug() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "udebug");
    }

    @Test
    public void morass() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "morass");
    }
}
