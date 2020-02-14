package commandline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestOptions {
    Options options;

    @BeforeEach
    public void beforeEach() {
        options = new Options();
    }

    @Test
    public void TestConstructor() {
        assertEquals(0, options.size());
    }

    @Test
    public void TestOptionsAddSingle() {
        assertEquals(0, options.size());
        options.addOption(new Option("-n", "--name", false, "the name of the recipe"));
        assertEquals(1, options.size());
    }

    @Test
    public void TestOptionsAddMultiple() {
        assertEquals(0, options.size());
        options.addOption(new Option("-dw", "--dough weight", false, "desired final dough weight"));
        options.addOption(new Option("-h", "--hydration", false, "desired fractional dough hydration"));
        options.addOption(new Option("-n", true,"the name of the recipe"));
        assertEquals(3, options.size());
        assertEquals("-h", options.get(1).getArgShort());
        assertEquals("--hydration", options.get(1).getArgLong());
        assertEquals("-dw", options.get(0).getArgShort());
        assertEquals("--dough weight", options.get(0).getArgLong());
        assertFalse(options.get(0).isBoolean());
        assertTrue(options.get(2).isBoolean());
        assertEquals("desired final dough weight", options.get(0).getDescription());
    }
}
