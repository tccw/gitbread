package commandline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestCommandParser {

    CommandParser parser;
    Options options;
    String command;

    @BeforeEach
    void setUp() {
        parser = new CommandParser();
        options = new Options();
        options.addOption(new Option("-n", "--name", false, "The name of the recipe"));
        options.addOption(new Option("-dw", "--doughweight", false, "Mixed dough weight in grams"));
        options.addOption(new Option("-fw", "--flourweight", false, "Flour weight in grams"));
        options.addOption(new Option("-h", "--hydration", false, "Hydration fraction for fluid calculation"));
        options.addOption(new Option("-si", "--setinstructions", false, "Recipe instructions"));
        options.addOption(new Option("-v", "--verbose", true, "Print out what is happening"));
        options.addOption(new Option("-m", "--master", true, "master recipe selector"));
        options.addOption(new Option("-t", "--testing", true, "testing recipe selector"));

    }

    @Test
    void TestConstructor() {
        assertEquals(0, parser.size());
    }

    @Test
    void TestParseShortFlags() {
        command = "bread new -n French loaf -dw 1000";
        parser.parse(options, command);
        assertEquals("French loaf", parser.get("-n"));
        assertEquals("1000", parser.get("-dw"));
    }

    @Test
    void TestParseLongFlags() {
        command = "bread new --name French loaf --doughweight 1500";
        parser.parse(options, command);
        assertEquals("French loaf", parser.get("-n"));
        assertEquals("1500", parser.get("-dw"));
    }

    @Test
    void containsFlag() {
        command = "bread attempt -n Pizza dough -m";
        parser.parse(options, command);
        assertTrue(parser.containsFlag("-n"));
        assertTrue((parser.containsFlag("-m")));
    }

    @Test
    void get() {
    }
}