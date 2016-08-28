package scpp.globaleye.com.scppclient.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import scpp.globaleye.com.scppclient.ISenzService;
import scpp.globaleye.com.scppclient.R;
import scpp.globaleye.com.scppclient.services.RemoteSenzService;
import scpp.globaleye.com.scppclient.utils.ActivityUtils;
import scpp.globaleye.com.scppclient.utils.NetworkUtil;
import scpp.globaleye.com.scppclient.utils.NotificationUtils;
import scpp.globaleye.com.scppclient.utils.PreferenceUtils;
import scpp.globaleye.com.senzc.enums.enums.SenzTypeEnum;
import scpp.globaleye.com.senzc.enums.pojos.Senz;
import scpp.globaleye.com.senzc.enums.pojos.User;

public class Wallet extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = UserSelect.class.getName();

    private TextView myWalletLabel,recivedLabel,amountLabel;
    private EditText amountEditText;
    private Button sendButton ,wlogoutButton;

    // use to track share timeout
    private SenzCountDownTimer senzCountDownTimer;
    private boolean isResponseReceived;

    // custom font
    private Typeface typeface;

    // service interface
    private ISenzService senzService = null;
    private boolean isServiceBound = false;


    private User receiver;

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
        setContentView(R.layout.activity_wallet);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        senzCountDownTimer = new SenzCountDownTimer(16000, 5000);
        isResponseReceived = false;
        initUi();
        bindConService();

        receiver = new User("", getIntent().getStringExtra("USER"));



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
        registerReceiver(senzMessageReceiver, new IntentFilter("scpp.globaleye.com.scppclient.DATA_SENZ"));
        registerReceiver(senzMessageReceiver, new IntentFilter("scpp.globaleye.com.scppclient.NEW_SENZ"));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceBound){
            unbindService(senzServiceConnection);
        }
        unregisterReceiver(senzMessageReceiver);
    }

    /**
     * Initialize UI components,
     * Set country code text
     * set custom font for UI fields
     */

    private void initUi() {


        myWalletLabel = (TextView)findViewById(R.id.myWallet_lb);
        recivedLabel = (TextView)findViewById(R.id.recivelb);
        amountLabel = (TextView)findViewById(R.id.amountlb);
        amountEditText = (EditText) findViewById(R.id.amount_txt);
        sendButton = (Button) findViewById(R.id.send_bt);
        sendButton.setOnClickListener(Wallet.this);


    }

    @Override
    public void onClick(View v) {
        if (v == sendButton) {
            if (amountEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(Wallet.this, "Empty Amount", Toast.LENGTH_LONG).show();
            } else {
                if (NetworkUtil.isAvailableNetwork(Wallet.this)) {
                    //ActivityUtils.showProgressDialog(Wallet.this, "Please wait...");
                    String confirmationMessage = "<font color=#000000>Are you sure you want to Send Money to </font> <font color=#306d97>" + "<b>" + receiver.getUsername() + "</b>" + "</font>";
                    displayConfirmationMessageDialog(confirmationMessage);

                } else {
                    Toast.makeText(Wallet.this, "No network connection available", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    /**
     * send money
     */
    private void sendMoney() {
        try {
            // create senz attributes
            HashMap<String, String> senzAttributes = new HashMap<>();
            senzAttributes.put("time", ((Long) (System.currentTimeMillis() / 1000)).toString());
            senzAttributes.put("msg", "msg");
            senzAttributes.put("balance",amountEditText.getText().toString());
            String id = "_ID";
            String signature = "_SIGNATURE";
            SenzTypeEnum senzType = SenzTypeEnum.DATA;
            Senz senz = new Senz(id, signature, senzType, null, receiver, senzAttributes);
            senzService.send(senz);


        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


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

        //Toast.makeText(UserSelect.this, "Handle masage", Toast.LENGTH_LONG).show();
        Senz senz = intent.getExtras().getParcelable("SENZ");

        if (senz != null && senz.getSenzType() == SenzTypeEnum.DATA) {
            if (senz.getAttributes().containsKey("msg")) {
                // msg response received
                ActivityUtils.cancelProgressDialog();
                isResponseReceived = true;
                senzCountDownTimer.cancel();

                String msg = senz.getAttributes().get("msg");
                if (msg != null && msg.equalsIgnoreCase("SendMoneyDone")) {
                    Toast.makeText(this, "Successfully Send Money", Toast.LENGTH_LONG).show();
                    onPostSendMoney(senz);


                } else {
                   // NotificationUtils.showNotification(this, this.getString(R.string.new_senz), "You Recived Money " + senz.getSender().getUsername());
                    String sender = senz.getSender().getUsername();
                    String balance = senz.getAttributes().get("balance");

                    sendButton.setEnabled(false);

                    amountLabel.setText(balance);
                    User sen = new User("", sender);
                    sendResponse(senzService, sen, true);
                }
            }
        }
    }


    private void sendResponse(ISenzService senzService, User receiver, boolean isDone) {
        Log.d(TAG, "send response");
        try {
            // create senz attributes
            HashMap<String, String> senzAttributes = new HashMap<>();
            senzAttributes.put("time", ((Long) (System.currentTimeMillis() / 1000)).toString());
            if (isDone){
                senzAttributes.put("msg", "SendMoneyDone");
            }else{
                senzAttributes.put("msg", "SendMoneyFail");
            }

            String id = "_ID";
            String signature = "";
            SenzTypeEnum senzType = SenzTypeEnum.DATA;
            Senz senz = new Senz(id, signature, senzType, null, receiver, senzAttributes);
            senzService.send(senz);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * Keep track with share response timeout
     */
    private class SenzCountDownTimer extends CountDownTimer {

        public SenzCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // if response not received yet, resend share
            if (!isResponseReceived) {

                sendMoney();
               // String confirmationMessage = "<font color=#000000>Are you sure you want to Send Money to </font> <font color=#306d97>" + "<b>" + receiver.getUsername() + "</b>" + "</font>";
               // displayConfirmationMessageDialog(confirmationMessage);

                Log.d(TAG, "Response not received yet");
            }
        }

        @Override
        public void onFinish() {
            ActivityUtils.hideSoftKeyboard(Wallet.this);
            ActivityUtils.cancelProgressDialog();

            // display message dialog that we couldn't reach the user
            if (!isResponseReceived) {
                String message = "<font color=#000000>Seems we couldn't reach the senz service at this moment</font>";
                displayInformationMessageDialog("#Send Fail", message);
            }
        }

    }

    /**
     * Display message dialog when user request(click) to register
     *
     * @param message message to be display
     */
    public void displayConfirmationMessageDialog(String message) {
        final Dialog dialog = new Dialog(Wallet.this);

        //set layout for dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.share_confirm_message_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        // set dialog texts
        TextView messageHeaderTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_header_text);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_text);
        messageHeaderTextView.setText("Confirm username");
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
                ActivityUtils.showProgressDialog(Wallet.this, "Please wait...");
                senzCountDownTimer.start();
            }
        });

        // cancel button
        Button cancelButton = (Button) dialog.findViewById(R.id.information_message_dialog_layout_cancel_button);
        cancelButton.setTypeface(typeface);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    /**
     * Clear input fields and reset activity components
     */
    private void onPostSendMoney(Senz senz) {
        // Create user with senz sender(he is a friend)
        //SenzorsDbSource dbSource = new SenzorsDbSource(getActivity());
        //dbSource.getOrCreateUser(senz.getSender().getUsername());

        amountEditText.setText("");
        Toast.makeText(Wallet.this, "Successfully send Money", Toast.LENGTH_LONG).show();

        //navigate
        Intent intent = new Intent(Wallet.this, UserSelect.class);
        Wallet.this.startActivity(intent);
        Wallet.this.finish();

    }

    /**
     * Display message dialog with registration status
     *
     * @param message message to be display
     */
    public void displayInformationMessageDialog(String title, String message) {
        final Dialog dialog = new Dialog(Wallet.this);

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
        //messageHeaderTextView.setTypeface(typeface);
        // messageTextView.setTypeface(typeface);

        //set ok buttons
        Button okButton = (Button) dialog.findViewById(R.id.information_message_dialog_layout_ok_button);
        // okButton.setTypeface(typeface);
        okButton.setTypeface(null, Typeface.BOLD);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }





    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Wallet.this, UserSelect.class);
        Wallet.this.startActivity(intent);
        Wallet.this.finish();
    }

    public void goHome(View v) {
        Intent intent = new Intent(Wallet.this, Home.class);
        startActivity(intent);
        Wallet.this.finish();
    }
    public void goBack(View v) {
        Intent intent = new Intent(Wallet.this, UserSelect.class);
        Wallet.this.startActivity(intent);
        Wallet.this.finish();
    }

    public void logout(View v) {
        Intent intent = new Intent(Wallet.this, Login.class);
        Wallet.this.startActivity(intent);
        Wallet.this.finish();
    }


}
