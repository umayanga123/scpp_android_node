package scpp.globaleye.com.scppclient.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import scpp.globaleye.com.scppclient.R;
import scpp.globaleye.com.scppclient.exceptions.NoUserException;
import scpp.globaleye.com.scppclient.services.RemoteSenzService;
import scpp.globaleye.com.scppclient.utils.NetworkUtil;
import scpp.globaleye.com.scppclient.utils.PreferenceUtils;
import scpp.globaleye.com.senzc.enums.pojos.User;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private static final String TAG = Login.class.getName();
    private long backPressedTime = 0;
    private EditText loginTextUsername;
    private EditText passwordTextPasword;
    private Button loginButton;
    private Button registraionButton;

    private String username;
    private String password;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
        //initNavigation();
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View v) {
        if (v == loginButton) {
            initNavigation();
            //Intent intent = new Intent(Login.this, Home.class);
            //Login.this.startActivity(intent);
            //Login.this.finish();
        }else if(v ==registraionButton){
            navigateToRegistration();
        }
    }


    /**
     * Initialize UI components
     */
    private void initUi() {
        //change label font ...etc
        loginTextUsername = (EditText) findViewById(R.id.luserName_txt);
        passwordTextPasword = (EditText)findViewById(R.id.lpasswordTxt);
        loginButton = (Button)findViewById(R.id.lbutton);
        registraionButton=(Button)findViewById(R.id.lrButton);

        loginButton.setOnClickListener(Login.this);
        registraionButton.setOnClickListener(Login.this);

        initSenzService();

    }

    /**
     * Determine where to go from here
     */
    private void initNavigation() {
        // decide where to go
        // 1. goto registration
        // 2. goto home
        try {

            username = loginTextUsername.getText().toString().trim();
            password =passwordTextPasword.getText().toString().trim();


            user = PreferenceUtils.getUser(this, username, password);

            if(user.getPassword().equals(password)){
                navigateToHome();
            }else{
                Toast.makeText(this, "Invalid Password", Toast.LENGTH_LONG).show();
            }



        } catch (NoUserException e) {
            //e.printStackTrace();
            // no user means navigate to login
            Toast.makeText(this, "Please  registered", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Initialize senz service
     */
    private void initSenzService() {
        // start service from here
        Intent serviceIntent = new Intent(Login.this, RemoteSenzService.class);
        //serviceIntent.putExtra("userName" ,username);
        //serviceIntent.putExtra("passwrod",password);
        startService(serviceIntent);

    }


    /**
     * Navigate to Register activity
     */
    private void navigateToRegistration() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Login.this, Registration.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Login.this.startActivity(intent);
                Login.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    /**
     * Switch to home activity
     * This method will be call after successful login
     */
    private void navigateToHome() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Login.this, Home.class);
                intent.putExtra("USER_NAME", username);
                Login.this.startActivity(intent);
                Login.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    @Override
    public void onBackPressed() {        // to prevent irritating accidental exits
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
            Toast.makeText(this, "Press back again to exit",
                    Toast.LENGTH_SHORT).show();
        } else {
            // clean up
            super.onBackPressed();
            Login.this.finish();

        }
    }
}
