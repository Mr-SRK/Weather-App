import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.*;

public class WeatherAPIData {
        
    public static JSONObject getWeatherData(String city) {

        JSONObject cityLocationData = (JSONObject) getLocationData(city);
        double latitude = (double) cityLocationData.get("latitude");
        double longitude = (double) cityLocationData.get("longitude");

        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude
                    + "&hourly=uv_index&current=temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code,is_day,precipitation&timezone=auto&forecast_hours=1";

            HttpURLConnection apiConnection = fetchApiResponse(url);

            if (apiConnection.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }
            String jsonResponse = readApiResponse(apiConnection);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);

            return jsonObject;

        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    private static JSONObject getLocationData(String city) {
        try {
            city = city.replaceAll(" ", "+");
            String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=1&language=en&format=json";

            HttpURLConnection apiConnection = fetchApiResponse(urlString);

            if (apiConnection.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }
            
            String jsonResponse = readApiResponse(apiConnection);

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(jsonResponse);

            JSONArray locationData = (JSONArray) obj.get("results");
            return (JSONObject) locationData.get(0);

        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
        
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            return conn;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String readApiResponse(HttpURLConnection apiConnection) {
        try {
            StringBuilder s = new StringBuilder();
            Scanner input = new Scanner(apiConnection.getInputStream());

            while (input.hasNext()) {
                s.append(input.nextLine());
            }
            input.close();
            return s.toString();

        } catch (IOException e) {
            e.getStackTrace();

        }
        return null;

    }
}
