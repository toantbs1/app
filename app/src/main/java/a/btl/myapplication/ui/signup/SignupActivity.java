package a.btl.myapplication.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import a.btl.myapplication.R;
import a.btl.myapplication.entity.User;
import a.btl.myapplication.utils.AppDatabase;
import a.btl.myapplication.utils.EmailCheck;
import a.btl.myapplication.utils.PasswordUtil;

public class SignupActivity extends AppCompatActivity {
    private EditText etHoten, etEmail, etUsername, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etHoten = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        etHoten.setText(user != null ? user.getHoten() : "");
        etEmail.setText(user != null ? user.getEmail() : "");
        etUsername.setText(user != null ? user.getUsername() : "");
        etPassword.setText(user != null ? user.getPassword() : "");
    }

    private void signup() {
        String hoten = etHoten.getText().toString();
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!hoten.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
            User user = new User();
            user.setHoten(hoten);
            if (!EmailCheck.isValidEmail(email)) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return; // Dừng lại nếu email không hợp lệ
            } else {
                user.setEmail(email);
            }
            user.setUsername(username);
            user.setPassword(PasswordUtil.hashPassword(password));
            user.setTypeId(1);
            Intent intent = new Intent(SignupActivity.this, TypeActivity.class);
            intent.putExtra("user", user); // Đưa đối tượng vào Intent
            startActivity(intent);
        } else {
            Toast.makeText(SignupActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
    }
}