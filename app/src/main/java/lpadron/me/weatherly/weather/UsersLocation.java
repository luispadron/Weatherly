package lpadron.me.weatherly.weather;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UsersLocation {

    public UsersLocation(){}

    public static String getUsersLocation(double latitude, double longitude, Context context) {
        String area = "";
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0)
            if (addresses.get(0).getLocality() != null && addresses.get(0).getAdminArea() == null) {
                //In case we cant find the users state/country, this happens a bit in places outside
                //the US & Canada
                //We will only display the city if we have it.
                return addresses.get(0).getLocality();
            } else if (addresses.get(0).getLocality() != null && addresses.get(0).getAdminArea() != null) {
                //If we have both the city and state/country
                //display both to the user
                return addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
            } else if (addresses.get(0).getLocality() == null && addresses.get(0).getAdminArea() != null) {
                //If we don't have the city but we have the country/state
                return addresses.get(0).getAdminArea();
            }
            else {
                //In case we cant find the users city and country?
                //We leave the location label blank.
                return "";
            }

        return area;
    }
}
