package uva.uhunt.c3.g0.p11420;

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
    public void executioner() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "executioner");
    }

    @Test
    public void obrienm() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "obrienm");
    }

    @Test
    public void priojeetpriyom() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "priojeetpriyom");
    }

    @Test
    public void rizaldi() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "rizaldi");
    }
}
