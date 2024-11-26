package a.btl.myapplication.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import a.btl.myapplication.MainActivity;
import a.btl.myapplication.R;
import a.btl.myapplication.entity.Type;
import a.btl.myapplication.entity.User;
import a.btl.myapplication.entity.dto.UserSession;
import a.btl.myapplication.utils.AppDatabase;

public class TypeActivity extends AppCompatActivity implements TypeAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private TypeAdapter typeAdapter;
    private boolean isButtonActive = false;
    private Button btnRegister, btnExit;
    private User user;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        btnRegister = findViewById(R.id.btnRegister);
        btnExit = findViewById(R.id.btnExit);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy dữ liệu từ Room
        db = AppDatabase.getInstance(this);
        List<Type> typeList = db.typeDao().getAllTypes();

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        typeAdapter = new TypeAdapter(this, typeList, this);
        recyclerView.setAdapter(typeAdapter);

        btnRegister.setOnClickListener(new doSomething());
        btnExit.setOnClickListener(new doSomething());
    }

    private class doSomething implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnRegister) {
                signup(user);
            } else if (view.getId() == R.id.btnExit) {
                Intent intent = new Intent(TypeActivity.this, SignupActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        }
    }

    private void signup(User user) {
        AtomicReference<String> mes = new AtomicReference<>();
        new Thread(() -> {
            if (db.userDao().countUserByUsername(user.getUsername()) == 0 && db.userDao().countUserByEmail(user.getEmail()) == 0) {
                db.userDao().insert(user);
                Optional<User> user1 = db.userDao().getUserByUsername(user.getUsername());
                UserSession.getInstance(this).setData(user1.get().getHoten(), user1.get().getUserId(), user1.get().getTypeId());
                mes.set("Đăng ký thành công!");
                startActivity(new Intent(TypeActivity.this, MainActivity.class));
            } else {
                mes.set("Tài khoản đã tồn tại!");
            }
            runOnUiThread(() ->
                    Toast.makeText(TypeActivity.this, mes.toString(), Toast.LENGTH_SHORT).show());
        }).start();
    }

    private void updateButtonState() {
        if (isButtonActive) {
            btnRegister.setEnabled(true);
        }
    }

    @Override
    public void onItemClick(int position) {
        // Khi nhấp vào item, thay đổi trạng thái của button
        isButtonActive = true;
        updateButtonState();

        user.setTypeId(position + 1);
    }
}