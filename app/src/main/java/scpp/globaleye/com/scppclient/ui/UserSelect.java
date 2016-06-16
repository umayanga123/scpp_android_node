package scpp.globaleye.com.scppclient.ui;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import scpp.globaleye.com.scppclient.R;
import scpp.globaleye.com.scppclient.utils.NetworkUtil;

public class UserSelect extends Activity implements View.OnClickListener {


    // UI fields
    private Button logOutButton;
    private Typeface typeface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select);

        initUi();

    }

    /**
     * Initialize UI components,
     * Set country code text
     * set custom font for UI fields
     */

    private void initUi() {
        logOutButton = (Button) findViewById(R.id.logOutBtn);
        logOutButton.setOnClickListener(UserSelect.this);

    }

    @Override
    public void onClick(View v) {
        if (v == logOutButton) {
            Toast.makeText(this, "Thank You", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }
}
