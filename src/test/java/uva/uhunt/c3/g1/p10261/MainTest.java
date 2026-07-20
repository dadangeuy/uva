package uva.uhunt.c3.g1.p10261;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import uva.common.helper.TestHelper;

import java.time.Duration;

public class MainTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    @Test
    @Disabled("non-deterministic output")
    public void sample() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "sample");
    }
}
