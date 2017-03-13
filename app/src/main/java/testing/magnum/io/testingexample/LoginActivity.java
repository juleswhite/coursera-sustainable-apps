package testing.magnum.io.testingexample;

import android.location.Address;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity  {

    private static final String TAG = LoginActivity.class.getName();

    private interface LoginAction {
        public void execute();
    }

    private Button loginButton;

    private EditText passwordEditText;
    private EditText emailEditText;
    private TextView errorMessageView;

    private boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // If you have lots of code that looks like this, consider
        // using a dependency injection framework like ButterKnife
        // to make life easier.
        loginButton = (Button) findViewById(R.id.loginButton);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        errorMessageView = (TextView) findViewById(R.id.errorMessage);

        // Real activities have much more complex behaviors, but we can test
        // this simple behavior in the LoginActivityTest
        loginButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();

                if(password.length() < 4){
                    errorMessageView.setText("Bad password");
                }
            }
        });

    }


}
