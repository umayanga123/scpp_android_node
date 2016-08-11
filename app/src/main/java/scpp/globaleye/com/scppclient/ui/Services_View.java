package scpp.globaleye.com.scppclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import scpp.globaleye.com.scppclient.R;

/**
 * Created by umayanga on 8/11/16.
 */
public class Services_View extends Activity implements View.OnClickListener {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private Button carPoolong;
    private Button buyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_view);


        initUi();

    }


    private void initUi() {
        carPoolong = (Button) findViewById(R.id.btCarpooling);
        buyItem= (Button) findViewById(R.id.btBuyItem);

        carPoolong.setOnClickListener(Services_View.this);
        buyItem.setOnClickListener(Services_View.this);
    }

    @Override
    public void onClick(View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Services_View.this, MapsActivity.class);
                Services_View.this.startActivity(intent);
                Services_View.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


}
