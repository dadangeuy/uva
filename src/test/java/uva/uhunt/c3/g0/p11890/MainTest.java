package uva.uhunt.c3.g0.p11890;

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
    public void morass_1() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "morass_1");
    }

    @Test
    public void morass_2() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "morass_2");
    }

    @Test
    public void morass_3() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "morass_3");
    }
}
