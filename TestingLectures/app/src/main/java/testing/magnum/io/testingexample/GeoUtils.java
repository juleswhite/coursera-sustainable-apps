package testing.magnum.io.testingexample;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;


// Do we really need this class? Think about whether callers should be using
// Geocoder directly.
public class GeoUtils {

    private Geocoder geocoder;

    public GeoUtils(Geocoder geocoder){
        this.geocoder = geocoder;
    }

    public String getCurrentZipCode(double lat, double lng) throws IOException {
        List<Address> addressesAtLocation = geocoder.getFromLocation(lat, lng, 1);
        String zipCode = (addressesAtLocation.size() > 0) ?
                            addressesAtLocation.get(0).getPostalCode() :
                            null;
        return zipCode;
    }

}
