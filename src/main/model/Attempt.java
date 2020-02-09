package model;

import java.time.LocalDateTime;

public class Attempt {

    private Recipe recipeVersion;
    private LocalDateTime dateTime;
    private String resultNotes;
    private String weather;

    public Attempt(Recipe recipeVersion, LocalDateTime dateTime, String resultNotes, String weather) {
        this.recipeVersion = recipeVersion;
        this.dateTime = dateTime;
        this.resultNotes = resultNotes;
        this.weather = weather;
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
        return weather;
    }

    // setters

    public void setRecipeVersion() {
        //stub
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    //EFFECTS: Construct an attempt with the date, current weather, weather for the day,
    //         given recipe, and empty resultNotes.
    public Attempt(Recipe recipeVersion) {
        //stub
    }

    //EFFECTS: add notes to the result notes string
    public void addResultNotes(String crumbDescription, String crustDescription, String flavorDescription) {
        // stub
    }

}
