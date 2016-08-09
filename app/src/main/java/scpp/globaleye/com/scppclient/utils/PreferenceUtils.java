package scpp.globaleye.com.scppclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

import scpp.globaleye.com.scppclient.R;


import scpp.globaleye.com.scppclient.exceptions.NoUserException;
import scpp.globaleye.com.senzc.enums.pojos.User;

/**
 * Created by umayanga on 6/15/16.
 */
public class PreferenceUtils {


    /**
     * Save user credentials in shared preference
     *
     * @param context application context
     * @param user    logged-in user
     */
    public static void saveUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();

        //keys should be constants as well, or derived from a constant prefix in a loop.
        editor.putString("id", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("password" , user.getPassword());
        editor.commit();
    }


    /**
     * Get user details from shared preference
     *
     * @param context application context
     * @return user object
     */
    public static User getUser(Context context) throws NoUserException {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_APPEND);
        String id = preferences.getString("id", "0");
        String username = preferences.getString("username", "");
        String password= preferences.getString("password","");

        if (username.isEmpty())
            throw new NoUserException();

        User user = new User(id, username);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }


    /**
     * Save public/private keys in shared preference,
     *
     * @param context application context
     * @param key     public/private keys(encoded key string)
     * @param keyType public_key, private_key, server_key
     *
     *                Context.MODE_MULTI_PROCESS ->change
     */
    public static void saveRsaKey(Context context, String key, String keyType) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(keyType, key);
        editor.commit();
    }

    /**
     * Get saved RSA key string from shared preference
     *
     * @param context application context
     * @param keyType public_key, private_key, server_key
     * @return key string
     */
    public static String getRsaKey(Context context, String keyType) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_APPEND);
        return preferences.getString(keyType, "");
    }
}
