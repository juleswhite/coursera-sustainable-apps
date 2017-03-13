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
 * This example demonstrates a violation of the "Economy of Mechanism" design principle.
 * The use of the LoginAction interface and loginActions Map is overly complicated for
 * this application and provides no apparent benefits.
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

        // A developer can easily end up registering the wrong action with
        // a given button. This map adds a level of indirection that provides
        // no apparent benefit.
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

        // It is hard to tell from looking at this code what is going to happen when
        // a given button is clicked
        loginActions.get(v).execute();
    }

    private LoginAction createResetPasswordAction(){
        return new LoginAction() {
            @Override
            public void execute() {
                // ... implementation of password reset
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
