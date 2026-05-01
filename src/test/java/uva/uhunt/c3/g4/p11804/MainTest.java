package uva.uhunt.c3.g4.p11804;

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
    public void morass() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "morass");
    }

    @Test
    public void anupam() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "anupam");
    }
}
