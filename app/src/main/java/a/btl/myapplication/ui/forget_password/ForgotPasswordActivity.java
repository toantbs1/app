package a.btl.myapplication.ui.forget_password;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import a.btl.myapplication.R;
import a.btl.myapplication.entity.User;
import a.btl.myapplication.ui.login.LoginActivity;
import a.btl.myapplication.utils.AppDatabase;
import a.btl.myapplication.utils.PasswordUtil;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button resetPasswordButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.emailEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        db = AppDatabase.getInstance(this);

        resetPasswordButton.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Kiểm tra xem mật khẩu mới có khớp không
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            User user = db.userDao().findByEmail(email);
            if (user != null) {
                user.setPassword(PasswordUtil.hashPassword(newPassword));
                db.userDao().update(user);

                runOnUiThread(() -> Toast.makeText(this, "Mật khẩu đã được đặt lại!", Toast.LENGTH_SHORT).show());
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Email không tồn tại!", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}