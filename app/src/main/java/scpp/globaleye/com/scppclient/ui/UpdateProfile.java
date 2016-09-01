package scpp.globaleye.com.scppclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import scpp.globaleye.com.scppclient.R;

/**
 * Created by umayanga on 8/11/16.
 */
public class UpdateProfile extends AppCompatActivity implements View.OnClickListener  {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private Button updateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        initUi();

    }


    private void initUi() {
        updateProfile = (Button) findViewById(R.id.buttonUpdateProfile);
        updateProfile.setOnClickListener(UpdateProfile.this);


    }

    @Override
    public void onClick(View v) {

    }

    public void goHome(View v) {
        Intent intent = new Intent(UpdateProfile.this, Home.class);
        startActivity(intent);
        UpdateProfile.this.finish();
    }
    public void goBack(View v) {
        Intent intent = new Intent(UpdateProfile.this, Home.class);
        UpdateProfile.this.startActivity(intent);
        UpdateProfile.this.finish();
    }

    public void logout(View v) {
        Intent intent = new Intent(UpdateProfile.this, Login.class);
        UpdateProfile.this.startActivity(intent);
        UpdateProfile.this.finish();
    }
}
