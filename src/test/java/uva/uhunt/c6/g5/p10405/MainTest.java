package uva.uhunt.c6.g5.p10405;

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
    public void brianfry713() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "brianfry713");
    }
}
