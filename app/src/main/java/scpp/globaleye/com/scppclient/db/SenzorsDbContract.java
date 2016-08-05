package scpp.globaleye.com.scppclient.db;

import android.provider.BaseColumns;

/**
 * Created by umayanga on 6/23/16.
 */
public class SenzorsDbContract {

    public SenzorsDbContract() {}

    /* Inner class that defines sensor table contents */
    public static abstract class Senz implements BaseColumns {
        public static final String TABLE_NAME = "senz";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_USER = "user";
    }

    /* Inner class that defines the user table contents */
    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLOMN_NAME_IMAGE = "image";
    }

    /* Inner class that defines the shared_user table contents */
    public static abstract class SharedUser implements BaseColumns {
        public static final String TABLE_NAME = "shared_user";
        public static final String COLUMN_NAME_USER = "user";
        public static final String COLUMN_NAME_SENSOR = "sensor";
    }

}
