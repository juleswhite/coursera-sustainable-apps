package testing.magnum.io.testingexample;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jules on 12/20/16.
 */
public class LocationHelper implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public interface AddressConsumer {
        public void addressFound(Address location);
    }

    public interface LocationConsumer {
        public void locationFound(Location location);
    }

    private boolean connected = false;
    private GoogleApiClient apiClient;
    private Geocoder geocoder;

    private List<LocationConsumer> locationConsumers = new ArrayList<>();

    public LocationHelper(Context ctx){

        if (apiClient == null) {
            apiClient = new GoogleApiClient.Builder(ctx)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        geocoder = new Geocoder(ctx, Locale.getDefault());
    }

    public synchronized  void getLocation(LocationConsumer locationConsumer){
        locationConsumers.add(locationConsumer);
        updateConsumers();
    }

    public synchronized  void getAddress(final AddressConsumer addressConsumer){
        locationConsumers.add(new LocationConsumer() {
            @Override
            public void locationFound(Location location) {
                try{
                    Address addr = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                    addressConsumer.addressFound(addr);
                }
                catch(Exception e){
                    addressConsumer.addressFound(null);
                }
            }
        });
        updateConsumers();
    }

    private synchronized void updateConsumers(){
        if(connected){
            try {
                Location location = LocationServices.FusedLocationApi.getLastLocation(
                        apiClient);

                for(LocationConsumer locationConsumer : locationConsumers){
                    locationConsumer.locationFound(location);
                }

                locationConsumers.clear();

            }catch(SecurityException e){
                throw new RuntimeException(e);
            }
        }
    }

    protected void onStart() {
        connected = false;
        apiClient.connect();
    }

    protected void onStop() {
        connected = false;
        apiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        connected = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        connected = false;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        connected = true;
        updateConsumers();
    }
}
