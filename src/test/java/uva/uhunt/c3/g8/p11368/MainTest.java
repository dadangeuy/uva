package uva.uhunt.c3.g8.p11368;

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
    public void batman() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "batman");
    }

    @Test
    public void uva() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "uva");
    }

    @Test
    public void john() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "john");
    }

    @Test
    public void vqt() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "vqt");
    }
}
