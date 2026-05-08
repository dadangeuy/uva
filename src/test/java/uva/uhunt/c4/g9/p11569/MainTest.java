package uva.uhunt.c4.g9.p11569;

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
    public void rizaldi() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "rizaldi");
    }

    @Test
    public void marcoa() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "marcoa");
    }

    @Test
    public void udebug() throws Exception {
        TestHelper.run(TIMEOUT, Main::main, "udebug");
    }
}
