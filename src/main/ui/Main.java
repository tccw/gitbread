package ui;
//import org.apache.commons.cli.*;

import com.google.gson.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.google.maps.*;

public class Main {


    public static void main(String[] args) throws ForecastException, IOException, InterruptedException, ApiException {
        // get API keys from file
        String path = "res\\value\\api_keys.txt";
        ArrayList<String> apiKey = new ArrayList<String>(Files.readAllLines(Paths.get(path)));

        // API calls
        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey(apiKey.get(0)))
                .language(ForecastRequestBuilder.Language.en)
                .location(new GeoCoordinates(new Longitude(-123.190460), new Latitude(49.264585))).build();

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey.get(1))
                .build();
        GeocodingResult[] results = GeocodingApi
                .geocode(context, "Vancouver, BC")
                .await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(results[0].geometry.location));

        DarkSkyClient client = new DarkSkyClient();
        String forecast = client.forecastJsonString(request);

        JsonObject jsonObject = JsonParser.parseString(forecast).getAsJsonObject();
        System.out.println(jsonObject.keySet());
        System.out.println(jsonObject.get("currently"));

    }


}
