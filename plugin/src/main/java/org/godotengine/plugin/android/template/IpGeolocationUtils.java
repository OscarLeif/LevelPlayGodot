package org.godotengine.plugin.android.template;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IpGeolocationUtils {

    private static final String GEOLOCATION_API_URL = "https://ipapi.co/json/";

    public static String getCountryFromIP() {
        String country = null;
        try {
            URL url = new URL(GEOLOCATION_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            country = jsonResponse.getString("country_code");

        } catch (Exception e) {
            Log.e("IpGeolocationUtils", "Error fetching IP geolocation: " + e.getMessage(), e);
        }
        return country;
    }
}