package uva.uhunt.c4.g7.p10187;

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
    public void batman() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "batman");
    }

    @Test
    public void brianfry() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "brianfry");
    }

    @Test
    public void nasher() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "nasher");
    }
}
