package sh.surge.hammad.bankmobile.Databases;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String isLogin = "isLoggedIn";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_BALANCE = "balance";
    public static final String KEY_CARD= "cardNo";
    public static final String KEY_CVC = "cvc";

    public SessionManager(Context _context)
    {
        context = _context;
        userSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String username, String name, String password, String phone, String balance, String cardNo, String cvc)
    {
        editor.putBoolean(isLogin, true);

        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_BALANCE, balance);
        editor.putString(KEY_CARD, cardNo);
        editor.putString(KEY_CVC, cvc);

        editor.commit();
    }

    public HashMap <String, String> getUserDetailsFromSession()
    {
        HashMap<String,String> userData = new HashMap<String,String>();

        userData.put(KEY_USERNAME, userSession.getString(KEY_USERNAME, null));
        userData.put(KEY_NAME, userSession.getString(KEY_NAME, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_PHONE, userSession.getString(KEY_PHONE, null));
        userData.put(KEY_BALANCE, userSession.getString(KEY_BALANCE, null));
        userData.put(KEY_CARD, userSession.getString(KEY_CARD, null));
        userData.put(KEY_CVC, userSession.getString(KEY_CVC, null));

        return userData;
    }

    public boolean checkLogin()
    {
        if (userSession.getBoolean(isLogin, false))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void logoutFromSession()
    {
        editor.clear();
        editor.commit();
    }
}
