package a.btl.myapplication.utils;

import android.util.Patterns;

public class EmailCheck {
    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
