import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeatherApp {

    private static final String API_KEY = "2780687c7063c86719034e642b369936";
    private static final Logger LOGGER = Logger.getLogger(WeatherApp.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String city = scanner.nextLine().trim();
        scanner.close();

        OkHttpClient client = new OkHttpClient();

        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + city + "&appid=" + API_KEY + "&units=metric";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                JSONObject obj = new JSONObject(jsonData);

                String cityName = obj.getString("name");
                double temp = obj.getJSONObject("main").getDouble("temp");
                int humidity = obj.getJSONObject("main").getInt("humidity");
                String weather = obj.getJSONArray("weather")
                        .getJSONObject(0)
                        .getString("description");

                System.out.println("\n🌍 Weather Report");
                System.out.println("--------------------------");
                System.out.println("City: " + cityName);
                System.out.println("Temperature: " + temp + "°C");
                System.out.println("Humidity: " + humidity + "%");
                System.out.println("Condition: " + weather);
            } else {
                LOGGER.warning("Failed to fetch weather data. Response code: " + response.code());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching weather data", e);
        }
    }
}
