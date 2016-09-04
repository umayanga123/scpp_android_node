package scpp.globaleye.com.scppclient.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import scpp.globaleye.com.scppclient.ISenzService;
import scpp.globaleye.com.scppclient.R;
import scpp.globaleye.com.scppclient.db.SenzorsDbContract;
import scpp.globaleye.com.scppclient.db.SenzorsDbSource;
import scpp.globaleye.com.scppclient.utils.ActivityUtils;
import scpp.globaleye.com.scppclient.utils.NetworkUtil;
import scpp.globaleye.com.senzc.enums.enums.SenzTypeEnum;
import scpp.globaleye.com.senzc.enums.pojos.Senz;
import scpp.globaleye.com.senzc.enums.pojos.User;

public class SendCoinPeer extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = WalletInfo.class.getName();

    private Button btSendCoin;
    private TextView coinhashTextView;
    private ListView coinList;
    private SenzorsDbSource dbSource;


    // use to track share timeout
    private SenzCountDownTimer senzCountDownTimer;
    private boolean isResponseReceived;



    // custom font
    private Typeface typeface;
    private  String userName;
    private String receiver;
    private Long recordId;




    // service interface
    private ISenzService senzService = null;
    private boolean isServiceBound = false;

    // service connection
    private ServiceConnection senzServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("TAG", "Connected with senz service");
            senzService = ISenzService.Stub.asInterface(service);

        }

        public void onServiceDisconnected(ComponentName className) {
            senzService = null;
            Log.d("TAG", "Disconnected from senz service");


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_coin_peer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        senzCountDownTimer = new SenzCountDownTimer(25000, 5000);
        isResponseReceived = false;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName= extras.getString("USER_NAME");
            receiver = extras.getString("RECIVER");

        }

        initUi();
        bindConService();
        populateListViewInDB();
        registerListClickCallback();
    }


    /**
     * {@inheritDoc}
     */
    public void bindConService() {

        // bind with senz service
        // bind to service from here as well
        Intent intent = new Intent();
        intent.setClassName("scpp.globaleye.com.scppclient", "scpp.globaleye.com.scppclient.services.RemoteSenzService");
        bindService(intent, senzServiceConnection, Context.BIND_AUTO_CREATE);
        isServiceBound=true;
        registerReceiver(senzMessageReceiver, new IntentFilter("scpp.globaleye.com.scppclient.SHARE_SENZ"));
        registerReceiver(senzMessageReceiver, new IntentFilter("scpp.globaleye.com.scppclient.DATA_SENZ"));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        //Log.d("unregister" , "call on destrot");
        super.onDestroy();
        if (isServiceBound) {
            unbindService(senzServiceConnection);
            Log.d("unbind" , "call on destrot");
        }
        unregisterReceiver(senzMessageReceiver);
    }


    private void initUi() {

        btSendCoin= (Button) findViewById(R.id.btCoinTransaction);
        coinhashTextView= (TextView) findViewById(R.id.tvCoinHash);
        coinList = (ListView)findViewById(R.id.CoinlistView);

        btSendCoin.setOnClickListener(SendCoinPeer.this);

        TextView textView = new TextView(this);
        textView.setText("Your Coins");
        coinList.addHeaderView(textView);


        coinhashTextView.setText("---------");



    }

    /*
        this some function is Deprecate but not do refatoring this
        stage
     */
    private void populateListViewInDB(){
        dbSource = new SenzorsDbSource(SendCoinPeer.this);
        Cursor cur= dbSource.getAllMiningDteail();

        startManagingCursor(cur);

        Log.d("load cur", cur.toString());

        String[] filedName = new String[]
                {SenzorsDbContract.WalletCoins.COLUMN_NAME_COIN ,
                        SenzorsDbContract.WalletCoins.COLUMN_NAME_TIME};
        int [] toViewIDs = new int[]
                {R.id.tvCoinHash , R.id.tvDate};


        SimpleCursorAdapter myCursorAdaptor=new SimpleCursorAdapter(
                this,
                R.layout.coin_view_layout,
                cur,
                filedName,
                toViewIDs
        );
        coinList.setAdapter(myCursorAdaptor);

    }


    private void registerListClickCallback() {
        coinList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long idInDB) {
                displayToastForId(idInDB);
            }
        });
    }

    private void displayToastForId(long idInDB) {
        Cursor cursor = dbSource.getMiningRow(idInDB);
        if (cursor.moveToFirst()) {
            String _id = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.WalletCoins._ID));
            String coin = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.WalletCoins.COLUMN_NAME_COIN));
            String s_id = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.WalletCoins.COLUMN_NAME_S_ID));
            String time = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.WalletCoins.COLUMN_NAME_TIME));


            String message = "ID: " + _id + "\n"
                    + "coin: " + coin + "\n"
                    + "Service ID: " + s_id + "\n"
                    + "Time: " + time;

            coinhashTextView.setText(coin);
            recordId  = idInDB;
            Toast.makeText(SendCoinPeer.this, message, Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }







    @Override
    public void onClick(View v) {
        if(v==btSendCoin){
            if (NetworkUtil.isAvailableNetwork(SendCoinPeer.this)) {
                ActivityUtils.showProgressDialog(SendCoinPeer.this, "Please wait...");
                senzCountDownTimer.start();
            } else {
                Toast.makeText(SendCoinPeer.this, "No network connection available", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Keep track with share response timeout  ,start share activity , sending quarry to switch
     */
    private class SenzCountDownTimer extends CountDownTimer {

        public SenzCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // if response not received yet, resend share
            if (!isResponseReceived) {
                sendCoin();
                Log.d(TAG, "Response not received yet");
            }
        }

        @Override
        public void onFinish() {
            ActivityUtils.hideSoftKeyboard(SendCoinPeer.this);
            ActivityUtils.cancelProgressDialog();
            // display message dialog that we couldn't reach the user
            if (!isResponseReceived) {
                String message = "<font color=#000000>Seems we couldn't reach the  </font> <font color=#eada00>" + "<b>" + receiver+ "</b>" + "</font> <font color=#000000> at this moment</font>";
                displayInformationMessageDialog("#Send Money Fail", message);
            }else{
                //removeCoinFromDB();
                String message = "<font color=#000000>Successfully Send Coin to </font> <font color=#eada00>" + "<b>" +receiver  + "</b>" + "</font> <font color=#000000> at this moment</font>";
                displayInformationMessageDialog("#Send Money ", message);

            }
        }
    }



    /**
     *
     * SHARE #COIN_VALUE @baseNode
     */
    private void sendCoin() {
        try {
            // create senz attributes
            HashMap<String, String> senzAttributes = new HashMap<>();
            senzAttributes.put("COIN",coinhashTextView.getText().toString().trim());
            senzAttributes.put("MSG","OK");
            senzAttributes.put("time", ((Long) (System.currentTimeMillis() / 1000)).toString());

            // new senz
            String id = "_ID";
            String signature = "_SIGNATURE";
            SenzTypeEnum senzType = SenzTypeEnum.DATA;
            User sender = new User("", userName);
            User coin_receiver = new User("",receiver);
            //send quarry
            Senz senz = new Senz(id, signature, senzType,sender , coin_receiver, senzAttributes);

            senzService.send(senz);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * update sl lite DB
     *
     */
    private void removeCoinFromDB() {
        SenzorsDbSource dbSource = new SenzorsDbSource(SendCoinPeer.this);
        boolean dbState= dbSource.deleteRow(recordId);
        if(dbState==true){
            Toast.makeText(SendCoinPeer.this, "Coin :" + coinhashTextView.getText().toString()+" is Removed", Toast.LENGTH_SHORT).show();
        }
        coinhashTextView.setText("---------");
        registerListClickCallback();
    }


    /**
     * Display message dialog when user request(click) to delete invoice
     *
     * @param message message to be display
     */
    public void displayInformationMessageDialog(String title, String message) {
        final Dialog dialog = new Dialog(SendCoinPeer.this);

        //set layout for dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.information_message_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        // set dialog texts
        TextView messageHeaderTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_header_text);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_text);
        messageHeaderTextView.setText(title);
        messageTextView.setText(Html.fromHtml(message));

        // set custom font
        messageHeaderTextView.setTypeface(typeface);
        messageTextView.setTypeface(typeface);

        //set ok button
        Button okButton = (Button) dialog.findViewById(R.id.information_message_dialog_layout_ok_button);
        okButton.setTypeface(typeface);
        okButton.setTypeface(null, Typeface.BOLD);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }





    //recived messege
    private BroadcastReceiver senzMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Got message from Senz service");
            handleMessage(intent);
        }
    };

    /**
     * Handle broadcast message receives
     * Need to handle share success failure here
     *
     * @param intent intent
     */
    private void handleMessage(Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "MY ACTION" + action);


        //Toast.makeText(WalletInfo.this, "Handle masage", Toast.LENGTH_LONG).show();
        Senz senz = intent.getExtras().getParcelable("SENZ");

        if(action.equalsIgnoreCase("scpp.globaleye.com.scppclient.SHARE_SENZ")) {
            boolean a =senz.getAttributes().containsKey("MSG");

            if (senz.getAttributes().containsKey("MSG")) {

                ActivityUtils.cancelProgressDialog();
                isResponseReceived = true;
                senzCountDownTimer.cancel();

                String cv = senz.getAttributes().get("COIN");

                if (cv != null) {
                    onPostShare(senz);
                } else {
                    //String user = usernameEditText.getText().toString().trim();
                    String message = "<font color=#000000>Seems we couldn't take coin value in this time </font> <font color=#eada00>" + "<b>" + "</font>";
                    displayInformationMessageDialog("Checking Coin Value  is Fail", message);
                }
            }
        }else if(action.equalsIgnoreCase("scpp.globaleye.com.scppclient.DATA_SENZ")){

                ActivityUtils.cancelProgressDialog();
                isResponseReceived = true;
                senzCountDownTimer.cancel();
                onPoseRecived(senz);




        }
    }



    /**
     * Clear input fields and reset activity components
     */
    private void onPostShare(Senz senz) {

        isResponseReceived = false;
        String cv = senz.getAttributes().get("MSG");
        Toast.makeText(SendCoinPeer.this, "Send Coin Successfully ", Toast.LENGTH_SHORT).show();

        cv = cv + "$";
        coinhashTextView.setText("---------------");

    }

    private void onPoseRecived(Senz senz) {
        isResponseReceived = false;
        String cv = senz.getAttributes().get("COIN");

        Toast.makeText(SendCoinPeer.this, "Thank YOU", Toast.LENGTH_LONG).show();


    }


    /**
     * {@inheritDoc}
     * Navigation Button Section
     */


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SendCoinPeer.this, UserSelect.class);
        intent.putExtra("USER_NAME", userName);
        SendCoinPeer.this.startActivity(intent);
        SendCoinPeer.this.finish();
    }

    public void goHome(View v) {
        Intent intent = new Intent(SendCoinPeer.this, Home.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
        SendCoinPeer.this.finish();
    }
    public void goBack(View v) {
        Intent intent = new Intent(SendCoinPeer.this, UserSelect.class);
        intent.putExtra("USER_NAME", userName);
        SendCoinPeer.this.startActivity(intent);
        SendCoinPeer.this.finish();
    }

    public void logout(View v) {
        Intent intent = new Intent(SendCoinPeer.this, Login.class);
        SendCoinPeer.this.startActivity(intent);
        SendCoinPeer.this.finish();
    }




}
