package scpp.globaleye.com.scppclient.db;

import android.provider.BaseColumns;

/**
 * Created by umayanga on 6/23/16.
 */
public class SenzorsDbContract {

    public SenzorsDbContract() {}


    /* Inner class that defines the WalletCoins table contents */
    public static abstract class WalletCoins implements BaseColumns {
        public static final String TABLE_NAME = "mining_details";
        public static final String COLUMN_NAME_COIN = "coin";
        public static final String COLUMN_NAME_S_ID = "s_id";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_USER_NAME = "user_name";
    }

}
