package ui;
//import org.apache.commons.cli.*;

import com.google.gson.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.Forecast;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

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
        context.shutdown();

        DarkSkyClient client = new DarkSkyClient();
        String forecast = client.forecastJsonString(request);
        JsonObject jsonForecast = JsonParser.parseString(forecast).getAsJsonObject();
//        String jsonOutput = gson.toJson(forecast);
//        System.out.println(jsonOut);

        System.out.println(jsonForecast);
        JsonObject currently = jsonForecast.getAsJsonObject("daily");
        System.out.println(currently);
        System.out.println(jsonForecast.keySet());
        System.out.println(currently.get("time"));
        LocalDateTime localDate = LocalDateTime.now();
        System.out.println(localDate
                .format(DateTimeFormatter
                        .ofLocalizedDate(FormatStyle.FULL)
                        .withLocale(Locale.CANADA)));



    }


}
