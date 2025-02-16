package persistance.api;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

public class ApiConnectionChecker {
    private static final String API_URL = "https://balandrau.salle.url.edu/dpoo/shared/characters";

    public static boolean isApiAvailable() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);  // 3 seconds timeout
            connection.setReadTimeout(3000);
            connection.connect();

            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            return false; // API is unavailable
        }
    }
}
