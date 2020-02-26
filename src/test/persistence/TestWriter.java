package persistence;

import model.BreadRecipe;
import model.RecipeCollection;
import model.RecipeHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class TestWriter {
    private static final String TEST_DIRECTORY = "./data/recipecollections/testWriter.json";
    private Clock clock = Clock.fixed(LocalDateTime.of(1989, 8, 5, 12, 0)
            .toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
    private Writer testWriter;
    private RecipeCollection collection;
    BreadRecipe frenchLoaf;
    BreadRecipe frenchLoafTesting;
    BreadRecipe hearthLoaf;
    BreadRecipe hearthLoafTesting;
    RecipeHistory frenchLoafHist;
    RecipeHistory hearthLoafHist;

    @BeforeEach
    void setUp() {
        try {
            testWriter = new Writer(new File(TEST_DIRECTORY));

            frenchLoaf = new BreadRecipe(1000);
            frenchLoafTesting = new BreadRecipe(650, 0.72);
            hearthLoaf = new BreadRecipe(375, 0.58);
            hearthLoafTesting = new BreadRecipe(450, 0.72);

            frenchLoafHist = new RecipeHistory(frenchLoaf);
            hearthLoafHist = new RecipeHistory(hearthLoaf);

            frenchLoafHist.setMasterRecipe(frenchLoaf);
            frenchLoafHist.addToHistory(frenchLoafTesting);
            frenchLoafHist.setTestingRecipe(frenchLoafHist.get(1));
            hearthLoafHist.setMasterRecipe(hearthLoaf);
            hearthLoafHist.addToHistory(hearthLoafTesting);
            hearthLoafHist.setTestingRecipe(hearthLoafHist.get(1));

            collection = new RecipeCollection();
            collection.add("French loaf", frenchLoafHist);
            collection.add("Hearth loaf", hearthLoafHist);
            collection.get("French loaf").attempt(collection.get("French loaf").getMasterRecipe(), clock);
            collection.get("French loaf").attempt(collection.get("French loaf").getTestingRecipe(), clock);
        } catch (FileNotFoundException e) {
            fail("Unexpected FileNotFoundException");
        } catch (IOException e) {
            fail("Unexpected IOException in setUp()");
        }
    }

    @Test
    void TestConstructor() {
        try {
            File file = new File(TEST_DIRECTORY);
            Writer writer = new Writer(file);
            assertEquals("UTF8", writer.getFileWriter().getEncoding());
        } catch (IOException e) {
            fail("Unexpected IOException in TestConstructor()");
        }
    }

    @Test
    void TestSetter() {
        try {
            File file = new File(TEST_DIRECTORY);
            Writer writer = new Writer(file);
            writer.setFileWriter(new FileWriter(new File(TEST_DIRECTORY)));
            assertEquals("UTF8", writer.getFileWriter().getEncoding());
        } catch (IOException e) {
            fail("Unexpected IOException in TestConstructor()");
        }
    }

    @Test
    void TestWriteExpectedIOException() {
        try {
            Writer writer = new Writer(new File("data/persistence/folderThatDoesntExist/test.json"));
            writer.write(new RecipeCollection());
            fail("Should have thrown an exception.");
        } catch (IOException e){
            // do nothing as this is expected
        }
    }

    @Test
    void TestWrite() {
        try {
            testWriter.write(collection);
            testWriter.close();
            //Try reading them back
            RecipeCollection loadedCollection = Reader.loadRecipeCollection(new File(TEST_DIRECTORY));
            assertEquals(frenchLoaf.toString(), loadedCollection.get("French loaf").getMasterRecipe().toString());
            assertEquals(frenchLoafTesting.toString(), collection.get("French loaf").getTestingRecipe().toString());
            assertEquals(hearthLoaf.toString(), loadedCollection.get("Hearth loaf").getMasterRecipe().toString());
            assertEquals(hearthLoafTesting.toString(), collection.get("Hearth loaf").getTestingRecipe().toString());
            assertEquals(2, loadedCollection.get("French loaf").countAttempts());
            assertEquals(1, loadedCollection.get("French loaf").countTimesModified());
            assertEquals(0, loadedCollection.get("Hearth loaf").countAttempts());
            assertEquals(1, loadedCollection.get("Hearth loaf").countTimesModified());
        } catch (IOException e) {
            System.out.println("Unexpected IOException");
        }
    }
}