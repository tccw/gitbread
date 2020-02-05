package ui;
//import org.apache.commons.cli.*;

import com.google.gson.*;
import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;


public class Main {
    public static void main(String[] args) throws ForecastException {
        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey("DarkSkyAPIKey-here"))
                .language(ForecastRequestBuilder.Language.en)
                .location(new GeoCoordinates(new Longitude(-123.190460), new Latitude(49.264585))).build();

        DarkSkyClient client = new DarkSkyClient();
        String forecast = client.forecastJsonString(request);

        JsonObject jsonObject = JsonParser.parseString(forecast).getAsJsonObject();

        System.out.println(jsonObject.keySet());
        System.out.println(jsonObject.get("currently"));


    }
}
