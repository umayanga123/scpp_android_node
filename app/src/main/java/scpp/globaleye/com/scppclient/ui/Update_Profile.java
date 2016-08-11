package scpp.globaleye.com.scppclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import scpp.globaleye.com.scppclient.R;

/**
 * Created by umayanga on 8/11/16.
 */
public class Update_Profile extends Activity implements View.OnClickListener  {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private Button updateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update_view);


        initUi();

    }


    private void initUi() {
        updateProfile = (Button) findViewById(R.id.buttonUpdateProfile);
        updateProfile.setOnClickListener(Update_Profile.this);


    }

    @Override
    public void onClick(View v) {

    }

}
