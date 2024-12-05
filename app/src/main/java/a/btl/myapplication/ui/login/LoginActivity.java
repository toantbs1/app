package a.btl.myapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Optional;

import a.btl.myapplication.MainActivity;
import a.btl.myapplication.R;
import a.btl.myapplication.entity.User;
import a.btl.myapplication.entity.dto.UserSession;
import a.btl.myapplication.ui.forget_password.ForgotPasswordActivity;
import a.btl.myapplication.ui.signup.SignupActivity;
import a.btl.myapplication.utils.AppDatabase;
import a.btl.myapplication.utils.PasswordUtil;

public class LoginActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWidget();
        btnLogin.setOnClickListener(new doSomething());
        tvForgotPassword.setOnClickListener(new doSomething());
        tvRegister.setOnClickListener(new doSomething());
    }

    private void getWidget() {
        db = AppDatabase.getInstance(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        new Thread(() -> {
            Optional<User> user = db.userDao().getUserByUsername(username);
            runOnUiThread(() -> {
                if (user.isPresent()) {
                    if (user != null && user.get().getUsername() != null && !user.get().getUsername().isEmpty() && PasswordUtil.checkPassword(password, user.get().getPassword())) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        UserSession.getInstance(this).setData(user.get().getHoten(), user.get().getUserId(), user.get().getTypeId());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private class doSomething implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnLogin) {
                login();
            } else if (view.getId() == R.id.tvRegister) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            } else if (view.getId() == R.id.tvForgotPassword) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        }
    }
}