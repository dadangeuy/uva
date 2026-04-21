package uva.uhunt.c4.g7.p10477;

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
    public void metaphysis() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "metaphysis");
    }
}
