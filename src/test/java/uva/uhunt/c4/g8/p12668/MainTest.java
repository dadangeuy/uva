package uva.uhunt.c4.g8.p12668;

import org.junit.jupiter.api.Test;
import uva.common.helper.TestHelper;

import java.time.Duration;

public class MainTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(20);

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
    public void feodorv() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "feodorv");
    }
}
