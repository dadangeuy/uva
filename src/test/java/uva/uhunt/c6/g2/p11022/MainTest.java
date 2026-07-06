package uva.uhunt.c6.g2.p11022;

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
    public void rizaldi() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "rizaldi");
    }

    @Test
    public void marcoa_1() throws Exception {
        TestHelper.run(Main::main, "marcoa_1");
    }

    @Test
    public void marcoa_2() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "marcoa_2");
    }

    @Test
    public void manetsus() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "manetsus");
    }
}
