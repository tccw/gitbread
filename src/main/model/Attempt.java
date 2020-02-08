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

}
