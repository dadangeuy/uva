package uva.uhunt.c3.g5.p12955;

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
    public void feodorv() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "feodorv");
    }
}
