package scpp.globaleye.com.scppclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import scpp.globaleye.com.scppclient.R;

public class BuyItemActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String userName;

    private Button btbuyItem;
    private EditText etBillId;
    private EditText etBillAmount;
    private Spinner shopNameSpiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName= extras.getString("USER_NAME");
        }

        initUi();


    }

    private void initUi() {
        btbuyItem = (Button) findViewById(R.id.btBuyItem);
        etBillId = (EditText) findViewById(R.id.etBillNo);
        etBillAmount = (EditText) findViewById(R.id.etBillAount);
        shopNameSpiner = (Spinner) findViewById(R.id.shop_name_spinner);

        shopNameSpiner.setOnItemSelectedListener(BuyItemActivity.this);
        btbuyItem.setOnClickListener(BuyItemActivity.this);




    }

    @Override
    public void onClick(View v) {
        if (v == btbuyItem) {
            
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BuyItemActivity.this, Home.class);
        intent.putExtra("USER_NAME", userName);
        BuyItemActivity.this.startActivity(intent);
        BuyItemActivity.this.finish();
    }

    public void goHome(View v) {
        Intent intent = new Intent(BuyItemActivity.this, Home.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
        BuyItemActivity.this.finish();
    }
    public void goBack(View v) {
        Intent intent = new Intent(BuyItemActivity.this, Home.class);
        intent.putExtra("USER_NAME", userName);
        BuyItemActivity.this.startActivity(intent);
        BuyItemActivity.this.finish();
    }

    public void logout(View v) {
        Intent intent = new Intent(BuyItemActivity.this, Login.class);
        BuyItemActivity.this.startActivity(intent);
        BuyItemActivity.this.finish();
    }



}
