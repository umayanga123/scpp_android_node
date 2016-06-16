package scpp.globaleye.com.scppclient.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import scpp.globaleye.com.scppclient.R;
import scpp.globaleye.com.scppclient.exceptions.NoUserException;
import scpp.globaleye.com.scppclient.services.RemoteSenzService;
import scpp.globaleye.com.scppclient.utils.PreferenceUtils;

public class Login extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private static final String TAG = Login.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
        initNavigation();
    }

    /**
     * Initialize UI components
     */
    private void initUi() {
        //change label font ...etc
    }

    /**
     * Determine where to go from here
     */
    private void initNavigation() {
        // decide where to go
        // 1. goto registration
        // 2. goto home
        try {
            PreferenceUtils.getUser(this);
            initSenzService();
            navigateToHome();

        } catch (NoUserException e) {
            e.printStackTrace();

            // no user means navigate to login
            navigateToRegistration();
        }
    }

    /**
     * Initialize senz service
     */
    private void initSenzService() {
        // start service from here
        Intent serviceIntent = new Intent(Login.this, RemoteSenzService.class);
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
                Intent intent = new Intent(Login.this, UserSelect.class);
                Login.this.startActivity(intent);
                Login.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

}
