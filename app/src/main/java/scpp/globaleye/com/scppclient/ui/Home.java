package scpp.globaleye.com.scppclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

import scpp.globaleye.com.scppclient.R;

/**
 * Created by umayanga on 8/11/16.
 */
public class Home extends Activity implements View.OnClickListener{

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    private ImageButton profileimgButton;
    private ImageButton transctionimgButton;
    private ImageButton walletimgButton;
    private ImageButton serviceimgButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        initUi();

    }


    private void initUi() {
        profileimgButton = (ImageButton) findViewById(R.id.imageButtonProfile);
        transctionimgButton=(ImageButton) findViewById(R.id.imageButtontransaction);
        walletimgButton=(ImageButton) findViewById(R.id.imageButtonwallet);
        serviceimgButton=(ImageButton) findViewById(R.id.imageButtonService);

        profileimgButton.setOnClickListener(Home.this);
        transctionimgButton.setOnClickListener(Home.this);
        walletimgButton.setOnClickListener(Home.this);
        serviceimgButton.setOnClickListener(Home.this);

    }


    @Override
    public void onClick(View v) {
        if (v == profileimgButton) {
            navigateToPrfileUpdateView();
        }else if(v ==transctionimgButton){
            navigateToTransaction();
        }else if(v==walletimgButton){
            navigateToWallte();
        }else if(v==serviceimgButton){
            navgateToServicesListView();
        }

    }




    private void navigateToTransaction() {
           new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Home.this, UserSelect.class);
                    Home.this.startActivity(intent);
                    Home.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
    }

    private void navigateToPrfileUpdateView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Home.this, Update_Profile.class);
                Home.this.startActivity(intent);
                Home.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }


    private void navgateToServicesListView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Home.this, Services_View.class);
                Home.this.startActivity(intent);
                Home.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void navigateToWallte() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // Intent intent = new Intent(Home.this, Wallet.class);
               // Home.this.startActivity(intent);
               // Home.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
