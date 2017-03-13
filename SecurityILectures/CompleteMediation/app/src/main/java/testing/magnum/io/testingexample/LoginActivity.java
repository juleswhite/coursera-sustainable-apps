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
 * An example Activity that violates the design principle of "Complete Mediation."
 *
 * The app also is violating the principle of "Economy of Mechanism."
 *
 */
public class LoginActivity extends AppCompatActivity implements Button.OnClickListener {

    private static final String TAG = LoginActivity.class.getName();

    private interface LoginAction {
        public void execute();
    }

    private Button loginButton;
    private Button registerButton;
    private Button forgotPasswordButton;

    private EditText passwordEditText;
    private EditText emailEditText;

    private Map<View, LoginAction> loginActions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        forgotPasswordButton = (Button) findViewById(R.id.forgotPassword);

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);

        loginActions.put(forgotPasswordButton, createResetPasswordAction());
        loginActions.put(loginButton, createAuthenticateAction());
        loginActions.put(registerButton, createRegisterAction());

        forgotPasswordButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Executing log in action for "+emailEditText.getText().toString());

        loginActions.get(v).execute();
    }

    private LoginAction createResetPasswordAction(){
        return new LoginAction() {
            @Override
            public void execute() {
                // Figure out the default account registered on the device
                String registeredAccount = LoginUtils.getRegisteredAccountIdOnDevice();

                // Yikes! We are accepting any email address that has been typed into the
                // login form. This is a violation of the complete mediation design principle.
                String email = emailEditText.getText().toString();

                // Create a password reset link for the account registered on the device
                String passwordResetLink = LoginUtils.createPasswordResetLink(registeredAccount);
                String emailBody = "Please click this link to reset your password: "+passwordResetLink;

                // Send the reset to the email in the email box
                LoginUtils.sendEmailTo(email, passwordResetLink);
            }
        };
    }

    private LoginAction createAuthenticateAction(){
        return new LoginAction() {
            @Override
            public void execute() {
                // ... implementation of an authentication action
            }
        };
    }

    private LoginAction createRegisterAction(){
        return new LoginAction() {
            @Override
            public void execute() {
                // ... implementation of a register action
            }
        };
    }

}
