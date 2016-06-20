package scpp.globaleye.com.scppclient.handlers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import scpp.globaleye.com.scppclient.utils.SenzParser;
import scpp.globaleye.com.senzc.enums.pojos.Senz;

/**
 * Created by umayanga on 6/15/16.
 */
public class SenzHandler {

    private static final String TAG = SenzHandler.class.getName();

    private static Context context;

    private static SenzHandler instance;

    private SenzHandler() {
    }

    public static SenzHandler getInstance(Context context) {
        if (instance == null) {
            instance = new SenzHandler();
            SenzHandler.context = context;
        }

        return instance;
    }

    public void handleSenz(String senzMessage) {
        // parse and verify senz
        Senz senz = SenzParser.parse(senzMessage);
        Log.d(TAG,"Masage"+senzMessage);
        switch (senz.getSenzType()) {
            case PING:
                Log.d(TAG, "PING received");
                break;
            case SHARE:
                Log.d(TAG, "SHARE received");
                broadcastSenz(senz, new Intent("scpp.globaleye.com.scppclient.NEW_SENZ"));
                break;
            case GET:
                Log.d(TAG, "GET received");
                broadcastSenz(senz, new Intent("scpp.globaleye.com.scppclient.NEW_SENZ"));
                break;
            case DATA:
                Log.d(TAG, "DATA received");
                broadcastSenz(senz, new Intent("scpp.globaleye.com.scppclient.DATA_SENZ"));
                break;
            case PUT:
                Log.d(TAG, "PUT received");
                broadcastSenz(senz, new Intent("scpp.globaleye.com.scppclient.NEW_SENZ"));
                break;
        }
    }

    private void broadcastSenz(Senz senz, Intent intent) {
        intent.putExtra("SENZ", senz);
        Log.d(TAG,"brodcast");
        context.sendBroadcast(intent);
    }

}
