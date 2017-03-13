package testing.magnum.io.testingexample;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by jules on 8/8/16.
 */
public class LoginUtils {


    /**
     * This method checks if the provided string represents a
     * valid email address and returns true if it is.
     *
     * @param email
     * @return
     */
    public boolean isValidEmailAddress(String email){

        boolean hasAtSign = email.indexOf("@") > -1;

        return hasAtSign;
    }

    /**
     * This method returns the length of the local part of an email address,
     * which is the part that comes before the "@" in the address.
     *
     * If you test this method thoroughly, you will see that there are some
     * edge cases it doesn't handle well.
     *
     * @param email
     * @return
     */
    public int getLocalPartLength(String email){
        int start = email.indexOf("@");
        String localPart = email.substring(0, start);
        return localPart.length();
    }

    /**
     * This method returns the postal code at a given latitude / longitude.
     *
     * If you test this method thoroughly, you will see that there are some
     * edge cases it doesn't handle well.
     *
     * @param ctx
     * @param lat
     * @param lng
     * @return
     */
    public String getCurrentZipCode(Context ctx, double lat, double lng){
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            // lat,lng, your current location

            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            return addresses.get(0).getPostalCode();

        } catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }

}

