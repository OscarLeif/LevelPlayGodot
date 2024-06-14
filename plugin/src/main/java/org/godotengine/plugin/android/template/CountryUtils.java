package org.godotengine.plugin.android.template;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Locale;

public class CountryUtils {

    // List of European countries that require GDPR
    private static final String[] EUROPEAN_COUNTRIES = {
            "AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "GR", "HU", "IS", "IE", "IT",
            "LV", "LT", "LU", "MT", "NL", "NO", "PL", "PT", "RO", "SK", "SI", "ES", "SE", "GB"
    };

    // Method to get the SIM country ISO code
    public static String getSimCountryIso(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
            return tm.getSimCountryIso().toUpperCase();
        }
        return null;
    }

    // Method to get the device's default country.
    // It returns the Device Origin Country
    public static String getDeviceCountry() {
        return Locale.getDefault().getCountry().toUpperCase();
    }

    // Method to determine the user's country
    public static String getUserCountry(Context context) {
        String simCountry = getSimCountryIso(context);
        if (simCountry != null) {
            return simCountry;
        }

        String deviceCountry = getDeviceCountry();
        if (deviceCountry != null) {
            return deviceCountry;
        }

        // Fallback to IP-based geolocation
        return IpGeolocationUtils.getCountryFromIP();
    }

    // Method to check if the country is a European country (GDPR applies)
    public static boolean isEuropeanCountry(String country) {
        for (String europeanCountry : EUROPEAN_COUNTRIES) {
            if (europeanCountry.equals(country)) {
                return true;
            }
        }
        return false;
    }

    // Method to check if the country is the United States (COPPA applies)
    public static boolean isCoppaCountry(String country) {
        return "US".equals(country);
    }
}