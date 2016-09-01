package scpp.globaleye.com.scppclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import scpp.globaleye.com.senzc.enums.pojos.Senz;
import scpp.globaleye.com.senzc.enums.pojos.User;

/**
 * Created by umayanga on 6/23/16.
 */
public class SenzorsDbSource {

    private static final String TAG = SenzorsDbSource.class.getName();
    private static Context context;

    /**
     * Init db helper
     *
     * @param context application context
     */
    public SenzorsDbSource(Context context) {
        //Log.d(TAG, "Init: db source");
        this.context = context;
    }



    /**
     * Check Coin if exists in the database, other wise stor a coin in db
     *
     * @param
     * @return Senz
     */
    public String addCoin(String coin,String s_id) {

        // get matching user if exists
        SQLiteDatabase db = SenzorsDbHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(SenzorsDbContract.WalletCoins.TABLE_NAME, // table
                null, SenzorsDbContract.WalletCoins.COLUMN_NAME_COIN + "=?", // constraint
                new String[]{coin}, // prams
                null, // order by
                null, // group by
                null); // join

        if (cursor.moveToFirst()) {
            // have matching user
            // so get user data
            // we return id as password since we no storing users password in database
            String _id = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.WalletCoins._ID));
            String _coin = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.WalletCoins.COLUMN_NAME_COIN));
            String _time = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.WalletCoins.COLUMN_NAME_TIME));

            // clear
            cursor.close();
            db.close();

            return "Coin Already Exist in Wallet";
        } else {
            // no matching user
            // so create user
            ContentValues values = new ContentValues();
            values.put(SenzorsDbContract.WalletCoins.COLUMN_NAME_COIN, coin);
            values.put(SenzorsDbContract.WalletCoins.COLUMN_NAME_S_ID, s_id);
            values.put(SenzorsDbContract.WalletCoins.COLUMN_NAME_TIME, (Long) (System.currentTimeMillis() / 1000));

            // inset data
            long id = db.insert(SenzorsDbContract.WalletCoins.TABLE_NAME, SenzorsDbContract.WalletCoins.COLUMN_NAME_COIN, values);
            db.close();


            return "Save Coin To wallet Successfully";
        }
    }


}
