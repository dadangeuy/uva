package uva.uhunt.c6.g4.p10854;

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
    public void dinic() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "dinic");
    }
}
