package extensibleapps.vandy.mooc.locationtracker;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Service that logs the device's location in a database when a client requests
 * it.
 */
public class LocationLogService extends Service {

    private LocationManager mLocationManager;

    // Binder given to clients to reference service
    private final IBinder mBinder = new LocalBinder();

    // Binder class used to access the service
    public class LocalBinder extends Binder {
        LocationLogService getService() {
            return LocationLogService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Tag used for class identification during logging.
     */
    protected final static String TAG =
            LocationLogService.class.getSimpleName();

    /**
     * Constant key used to store and access the description provided by the
     * client in the Intent used to start this service.
     */
    public static final String DESCRIPTION_KEY = "descKey";

    /**
     * Manager that handles writing location info in a database.
     */
    private LocLogDBManager mDBManager = null;

    public LocationLogService() {
    }

    /**
     * Method called by the Android started service framework whenever a client
     * starts this service with an intent.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v(TAG, "Service Started");

        // TODO | Perform any start up actions, gather the required data, and store said data in the database using the LocLogDBManger.

        // Stop the service once the logging is complete
        stopSelf(startId);

        // Returning START_NOT_STICKY means that in the event that the
        // service is killed while started (after returning from this method)
        // it won't be started again unless it is started explicitly.
        return START_NOT_STICKY;
    }

    /**
     * Perform one-time setup procedures.
     */
    @Override
    public void onCreate() {
        // Create an instance of the location database manager
        mDBManager = new LocLogDBManager(getApplicationContext());
    }

    /**
     * Clean up resources
     */
    @Override
    public void onDestroy() {
        // Nothing to clean up.
    }
}
