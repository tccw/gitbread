package model;

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

    private Recipe recipeVersion;
    private LocalDateTime dateTime;
    private String resultNotes;
    private String weatherNow;
//    private String weatherForDay;

    //EFFECTS: Construct an attempt with the date, current weather, weather for the day,
    // given recipe, and empty resultNotes.
    public Attempt(Recipe recipeVersion, Clock clock) {
        this.recipeVersion = recipeVersion;
        this.dateTime = LocalDateTime.now(clock);
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

    //EFFECTS: returns the LocalDateTime field to an easy to read string
//    public String datePretty() {
//        String result = getDateTime()
//                .format(DateTimeFormatter.RFC_1123_DATE_TIME
//                        .ofLocalizedDate(FormatStyle.FULL)
//                        .withLocale(Locale.CANADA));
//        return result;
//    }

    // getters
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Recipe getRecipeVersion() {
        return recipeVersion;
    }

    public String getResultNotes() {
        return resultNotes;
    }

//    public String getWeather() {
//        return weatherNow;
//    }

    // setters
}