package uva.uhunt.c3.g1.p1281;

import org.junit.jupiter.api.Test;
import uva.common.helper.TestHelper;

import java.time.Duration;

public class MainTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(20);

    @Test
    public void sample() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "sample");
    }

    @Test
    public void rizaldi_1() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "rizaldi_1");
    }

    @Test
    public void rizaldi_2() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "rizaldi_2");
    }

    @Test
    public void rizaldi_3() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "rizaldi_3");
    }
}
