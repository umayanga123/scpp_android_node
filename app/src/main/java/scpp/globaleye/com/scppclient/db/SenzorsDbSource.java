package scpp.globaleye.com.scppclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by umayanga on 6/23/16.
 */
public class SenzorsDbSource {

    private static final String TAG = SenzorsDbSource.class.getName();
    private static Context context;
    private SQLiteDatabase db;
    public static final String[] ALL_KEYS = new String[] {SenzorsDbContract.WalletCoins._ID,
            SenzorsDbContract.WalletCoins.COLUMN_NAME_COIN,SenzorsDbContract.WalletCoins.COLUMN_NAME_S_ID,
            SenzorsDbContract.WalletCoins.COLUMN_NAME_TIME};


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
        db = SenzorsDbHelper.getInstance(context).getWritableDatabase();
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
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateFormat = df.format(c.getTime());
            //String dateFormat = DateFormat.getDateTimeInstance().format(new Date((Long) (System.currentTimeMillis() / 1000)));

            ContentValues values = new ContentValues();
            values.put(SenzorsDbContract.WalletCoins.COLUMN_NAME_COIN, coin);
            values.put(SenzorsDbContract.WalletCoins.COLUMN_NAME_S_ID, s_id);
            values.put(SenzorsDbContract.WalletCoins.COLUMN_NAME_TIME, dateFormat);

            // inset data
            long id = db.insert(SenzorsDbContract.WalletCoins.TABLE_NAME, SenzorsDbContract.WalletCoins.COLUMN_NAME_COIN, values);
            cursor.close();
            db.close();
            return "Save Coin To wallet Successfully";
        }
    }


    public Cursor getAllMiningDteail(){
        // get matching user if exists
        db = SenzorsDbHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(SenzorsDbContract.WalletCoins.TABLE_NAME, // table
                null,null, // constraint
                null, // prams
                null, // order by
                null, // group by
                null); // join

        return cursor;
    }

    public Cursor getMiningRow(String coin){
        String where = null;
        Cursor c = 	db.query(true, SenzorsDbContract.WalletCoins.TABLE_NAME, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getMiningRow(long rowId) {
        String where = SenzorsDbContract.WalletCoins._ID + "=" + rowId;
        Cursor c = 	db.query(true, SenzorsDbContract.WalletCoins.TABLE_NAME, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


}
