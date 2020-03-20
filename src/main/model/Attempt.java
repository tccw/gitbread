package model;

import com.fasterxml.jackson.annotation.*;

import java.io.File;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/*
The attempt class represents an attempt at making a recipe. It records the date and time, as well as the weather, since
bread preparation can be sensitive to humidity and temperature. It also sets a pointer to the bread recipe used
and allows the user to enter in some notes about the results of the attempt.
 */

public class Attempt {
    /*
    @JsonBackReference tells Jackson to not serialize this field but to reconstruct using the @JasonManagedReference
    annotated field on the other side of the circular reference/bidirectional relationship. In this case that reference
    is attemptHistory which is ArrayList<Attempt> within Recipe (of which recipeVersion is type Recipe).
    More here: https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
     */
    @JsonBackReference
    private Recipe recipeVersion;
    private LocalDateTime dateTime;
    private String resultNotes;
    private String weatherNow;
    private File photoPath;
//    private String weatherForDay;

    //EFFECTS: Construct an attempt with the date, current weather, weather for the day,
    // given recipe, and empty resultNotes.

    public Attempt(Recipe recipeVersion, Clock clock) {
        this.recipeVersion = recipeVersion;
        this.dateTime = LocalDateTime.now(clock);
        this.resultNotes = "";
        this.weatherNow = "It's sunny and beautiful!"; //placeholder for API call
        this.photoPath = null;
//        this.weatherForDay = "A beautiful sunny day!"; //placeholder for API call
    }

    @JsonCreator
    public Attempt(@JsonProperty("recipeVersion") Recipe recipeVersion) {
        this.recipeVersion = recipeVersion;
//        this.dateTime = LocalDateTime.now(clock);
        this.resultNotes = "";
        this.weatherNow = "It's sunny and beautiful!"; //placeholder for API call
//        this.weatherForDay = "A beautiful sunny day!"; //placeholder for API call
    }

    //EFFECTS: add notes to the result notes string
    public void setResultNotes(String crumbNote, String crustNote, String flavorNote, String otherNotes) {
        this.resultNotes = "Crumb: "
                + crumbNote + "\n"
                + "Crust: "
                + crustNote + "\n"
                + "Flavor: "
                + flavorNote + "\n"
                + "Other notes: "
                + otherNotes + '\n';
    }

    // EFFECTS: returns the LocalDateTime field to an easy to read string
    private String datePretty() {
        return getDateTime()
                .format(DateTimeFormatter
                        .ofLocalizedDate(FormatStyle.FULL)
                        .withLocale(Locale.CANADA));
    }

    //EFFECTS:
    public boolean hasPhoto() {
        return (this.photoPath != null);
    }

    // setters
    public void setPhotoPath(File photoPath) {
        this.photoPath = photoPath;
    }

    // getters
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public Recipe getRecipeVersion() {
        return this.recipeVersion;
    }

    public String getResultNotes() {
        return this.resultNotes;
    }

    public File getPhotoPath() {
        return photoPath;
    }

    //    public String getWeather() {
//        return weatherNow;
//    }
    public String print() {
        return "\n----------- " + datePretty() + "-----------\n"
                + photoPath + "\n"
                + "ATTEMPT NOTES :\n"
                + resultNotes
                + "\n"
                + this.recipeVersion.toString();
    }

}