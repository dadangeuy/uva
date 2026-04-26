package uva.uhunt.c3.g9.p11259;

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
    public void bryton() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "bryton");
    }
}
