package uva.uhunt.c3.g6.p1176;

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
    public void taher() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "taher");
    }

    @Test
    public void pedro() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "pedro");
    }
}
