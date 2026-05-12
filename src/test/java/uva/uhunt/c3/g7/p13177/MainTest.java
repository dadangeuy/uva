package uva.uhunt.c3.g7.p13177;

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
    public void feodorv_1() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "feodorv_1");
    }

    @Test
    public void feodorv_2() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "feodorv_2");
    }
}
