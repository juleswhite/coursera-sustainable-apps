package extensibleapps.vandy.mooc.locationtracker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * UI interface for the Location logging application.
 */
public class UIActivity extends AppCompatActivity
        implements View.OnClickListener {

    /**
     * Constant used to identify the class during logging
     */
    protected final static String TAG =
            UIActivity.class.getSimpleName();

    /**
     * Constant used to differentiate a request to access the
     * ACCESS_FINE_LOCATION permission from other permissions
     */
    private final static int FINE_LOCATION_REQUEST_RESULT = 0;

    /**
     * Text entry field used by the user to input a discription of the location
     * log entry.
     */
    private EditText mDescEditText;

    /**
     * Instantiate views when activity is created.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);

        findViewById(R.id.log_button).setOnClickListener(this);
        mDescEditText = (EditText) (findViewById(R.id.desc_entry));
    }

    /**
     * Called when the user clicks the button to log their current location
     * data.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        // Check if the app has permission to access the user's location.
        // If so, log the data. If not, request it.
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {

            Log.v(TAG, "Need location permission");

            // Request the user to enable the location permission.
            // The result it returned to the onRequestPermissionsResult method
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            android.Manifest.permission
                                    .ACCESS_FINE_LOCATION},
                    FINE_LOCATION_REQUEST_RESULT);
        } else {
            Log.v(TAG, "No permission needed");
            startLocationLoggingService();
        }
    }

    /**
     * Callback method called when the user responds to the permission request.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {

        // Switch based on which permission is being requested.
        switch (requestCode) {
            case FINE_LOCATION_REQUEST_RESULT: {
                // Check if the user granted the permission.
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {

                    startLocationLoggingService();

                } else {
                    // The location is necessary for our app; make this clear
                    // to the
                    // user and don't attempt to log
                    Toast.makeText(this,
                                   "Location information is required for this app",
                                   Toast.LENGTH_LONG).show();
                }
                break;
            }
            default: {
                // Since our app only has one permission it is requesting,
                // This shouldn't be entered.
                throw new RuntimeException("Unexpected permission request");
            }
        }
    }

    /**
     * Start the Location Logging service
     */
    private void startLocationLoggingService() {

        Intent serviceIntent = new Intent();
        serviceIntent.setAction("vandy.mooc.intent.action.LogLocation");
        serviceIntent.setPackage(this.getPackageName());

        serviceIntent.putExtra(
                LocationLogService.DESCRIPTION_KEY,
                mDescEditText.getText().toString());

        // Reset the description text entry
        mDescEditText.getText().clear();

        try {
            this.startService(serviceIntent);
        } catch (Exception e) {
            Log.e(TAG, "startLocationLoggingService: ", e);
            Toast.makeText(this, "Unable to start service: " + e,
                           Toast.LENGTH_LONG).show();
        }
    }
}
