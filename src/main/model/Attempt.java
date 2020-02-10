package model;

import java.time.Clock;
import java.time.LocalDateTime;

public class Attempt {

    private Recipe recipeVersion;
    private LocalDateTime dateTime;
    private String resultNotes;
    private String weatherNow;
    private String weatherForDay;
    private final Clock clock;

    //EFFECTS: Construct an attempt with the date, current weather, weather for the day,
    // given recipe, and empty resultNotes.
    public Attempt(Recipe recipeVersion, Clock clock) {
        this.clock = clock;
        this.recipeVersion = recipeVersion;
        this.dateTime = LocalDateTime.now(clock);
        this.resultNotes = "";
        this.weatherNow = "It's sunny and beautiful!"; //placeholder for API call
        this.weatherForDay = "A beautiful sunny day!"; //placeholder for API call
    }

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

    public String getWeather() {
        return weatherNow;
    }

    // setters

    //EFFECTS: add notes to the result notes string
    public void setResultNotes(String crumbNote, String crustNote, String flavorNote, String otherNotes) {
        //stub
    }

    //MODIFIES: this
    //EFFECTS: changes the date time in case there is a mistaken entry
    public void changeDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}



