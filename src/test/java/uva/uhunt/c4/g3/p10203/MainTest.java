package uva.uhunt.c4.g3.p10203;

import org.junit.jupiter.api.Test;
import uva.common.helper.TestHelper;

import java.time.Duration;

public class MainTest {
    private static Duration TIMEOUT = Duration.ofSeconds(3);

    @Test
    public void sample() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "sample");
    }

    @Test
    public void anjupiter() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "anjupiter");
    }
}
