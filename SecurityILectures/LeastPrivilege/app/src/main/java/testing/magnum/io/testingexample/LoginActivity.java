package testing.magnum.io.testingexample;

import android.location.Address;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

/**
 * An example Activity that violates the principle of "Least Privilege" by taking the
 * fine location permission when coarse location would be sufficient. See the AndroidManifest.xml
 * file for the declaration of the uses-permission.
 *
 */
public class LoginActivity extends AppCompatActivity implements Button.OnClickListener {

    private static final String TAG = LoginActivity.class.getName();

    private Button loginButton;

    private EditText passwordEditText;
    private EditText emailEditText;

    private LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);

        loginButton.setOnClickListener(this);

        locationHelper = new LocationHelper(this);
    }

    @Override
    protected void onStart() {
        locationHelper.onStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        locationHelper.onStop();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Executing log in action for "+emailEditText.getText().toString());

        if(v == loginButton){
            // Make sure and see the AndroidManifest.xml file where the
            // app asks for more privileges than it needs

            // Get the current address, which is an async operation
            locationHelper.getAddress(new LocationHelper.AddressConsumer() {
                @Override
                public void addressFound(Address address) {
                    // When we get the current address back, extract the
                    // zip code and find the email for the user
                    String email = emailEditText.getText().toString();
                    String zipCode = address.getPostalCode();

                    // Record the login for the user at the zip code
                    UsageStatistics.recordLogin(email, zipCode);

                    //... Log the user in
                }
            });

        }
    }

}
