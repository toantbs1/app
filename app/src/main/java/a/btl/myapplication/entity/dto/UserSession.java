package a.btl.myapplication.entity.dto;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    private static UserSession instance;
    private String hoten;
    private Integer userId;
    private Integer typeId;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "user_session";
    private static final String KEY_HOTEN = "hoten";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_TYPE_ID = "type_id";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loadData();
    }

    public static UserSession getInstance(Context context) {
        if (instance == null) {
            instance = new UserSession(context);
        }
        return instance;
    }

    public void setData(String hoten, Integer userId, Integer typeId) {
        this.hoten = hoten;
        this.userId = userId;
        this.typeId = typeId;

        // Lưu dữ liệu vào SharedPreferences
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_HOTEN, hoten);
        editor.putInt(KEY_USER_ID, userId);
        editor.putInt(KEY_TYPE_ID, typeId);
        editor.apply();
    }

    private void loadData() {
        this.hoten = sharedPreferences.getString(KEY_HOTEN, null);
        this.userId = sharedPreferences.getInt(KEY_USER_ID, -1);
        this.typeId = sharedPreferences.getInt(KEY_TYPE_ID, -1);
    }

    public Integer getTypeId() {
        return typeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getHoten() {
        return hoten;
    }
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    public void clearSession() {
        editor.clear();
        editor.apply();
        hoten = null;
        userId = null;
        typeId = null;
    }
}
