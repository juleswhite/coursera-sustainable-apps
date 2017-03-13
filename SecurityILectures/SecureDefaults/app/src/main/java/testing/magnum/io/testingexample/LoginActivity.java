package testing.magnum.io.testingexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

/**
 * An example of an Activity that violates the design principle of "Secure Defaults."
 */
public class LoginActivity extends AppCompatActivity implements Button.OnClickListener {

    private static final String TAG = LoginActivity.class.getName();

    private Button loginButton;
    private Button registerButton;
    private Button forgotPasswordButton;

    private EditText passwordEditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        forgotPasswordButton = (Button) findViewById(R.id.forgotPassword);

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);


        // Does having a single click listener really simplify or
        // improve this code's modularity in any helpful way?
        forgotPasswordButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Executing log in action for "+emailEditText.getText().toString());

        // What is the default behavior of this conditional logic?
        if(v == loginButton){
            login();
        }
        else if(v == registerButton){
            register();
        }
        else {
            // Does this really make sense as a default?!!
            // No, it is not a secure default.
            resetPassword();
        }
    }

    private void resetPassword(){
        // Send a password reset to the email listed in the emailEditText with
        // no confirmation
    }

    private void login() {
        // Log the user in with the provided information
    }

    private void register(){
        // Start the flow to register a new account
    }

}
