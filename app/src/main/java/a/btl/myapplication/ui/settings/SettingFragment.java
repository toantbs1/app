package a.btl.myapplication.ui.settings;

import static a.btl.myapplication.utils.EmailCheck.isValidEmail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import a.btl.myapplication.R;
import a.btl.myapplication.databinding.FragmentNotificationsBinding;
import a.btl.myapplication.entity.User;
import a.btl.myapplication.entity.dto.UserSession;
import a.btl.myapplication.ui.login.LoginActivity;
import a.btl.myapplication.utils.AppDatabase;
import a.btl.myapplication.utils.MusicService;
import a.btl.myapplication.utils.PasswordUtil;

public class SettingFragment extends Fragment {

    private EditText email, username, loginName, password;
    private Switch musicSwitch, themeSwitch;
    private SeekBar volumeSeekBar;
    private Button logoutButton, updateButton;
    private AppDatabase appDatabase;
    private SharedPreferences prefs; // Khai báo biến SharedPreferences
    private MusicService musicService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo ViewModel
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);

        // Inflate layout
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Khởi tạo các thành phần
        email = root.findViewById(R.id.email);
        username = root.findViewById(R.id.username);
        loginName = root.findViewById(R.id.login_name);
        password = root.findViewById(R.id.password);
        musicSwitch = root.findViewById(R.id.music_switch);
        volumeSeekBar = root.findViewById(R.id.volume_seekbar);
        logoutButton = root.findViewById(R.id.logout_button);
        updateButton = root.findViewById(R.id.update_button);
        themeSwitch = root.findViewById(R.id.themeSwitch);

        appDatabase = AppDatabase.getInstance(getActivity());

        // Khởi tạo SharedPreferences
        prefs = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);

        // Load saved preferences
        loadPreferences();

        // Thiết lập sự kiện click cho nút cập nhật
        updateButton.setOnClickListener(view -> {
            updateUserData();
            UserSession.getInstance(getContext()).clearSession();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        Intent serviceIntent = new Intent(getActivity(), MusicService.class);
        getActivity().startService(serviceIntent);
        themeSwitch.setChecked(false);
        // Thiết lập listener cho music switch
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Kiểm tra xem service đã chạy chưa
                //if (!musicService.isServiceRunning(MusicService.class)) {
                getActivity().startService(serviceIntent);
                //}
            } else {
                //if (musicService.isServiceRunning(MusicService.class)) {
                getActivity().stopService(serviceIntent);
                //}
            }
        });
        // Thiết lập sự kiện cho nút đăng xuất
        logoutButton.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            UserSession.getInstance(getContext()).clearSession();
            prefs.edit().clear();
            prefs.edit().apply();
            getActivity().stopService(serviceIntent);
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        // Thiết lập listener cho volume seek bar
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                Intent volumeIntent = new Intent(MusicService.ACTION_SET_VOLUME);
                volumeIntent.putExtra("volume", volume);
                getActivity().sendBroadcast(volumeIntent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Thiết lập listener cho theme switch
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_theme", isChecked);
            editor.apply();
            updateTheme(isChecked);
        });

        // Đặt trạng thái cho themeSwitch từ SharedPreferences
        themeSwitch.setChecked(prefs.getBoolean("dark_theme", false));
        updateTheme(themeSwitch.isChecked());

        return root;
    }

    private void updateTheme(boolean isDarkTheme) {
        BottomNavigationView navView = getActivity().getWindow().getDecorView().findViewById(R.id.nav_view);
        if (isDarkTheme) {
            getActivity().getWindow().getDecorView().setBackgroundColor(Color.CYAN);
            navView.setBackgroundColor(Color.CYAN);
            //getActivity().getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.backgroundDark));
        } else {
            getActivity().getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            navView.setBackgroundColor(Color.WHITE);
            //getActivity().getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.backgroundLight));
        }
    }

    private void updateUserData() {
        String emailText = email.getText().toString();
        String usernameText = username.getText().toString();
        String loginNameText = loginName.getText().toString();
        String passwordText = password.getText().toString();

        // Lấy userId từ UserSession
        int userId = UserSession.getInstance(getContext()).getUserId();
        if (!isValidEmail(emailText)) {
            Toast.makeText(getActivity(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return; // Dừng lại nếu email không hợp lệ
        } else {
            // Tạo đối tượng User mới
            User user = new User(emailText, usernameText, loginNameText, PasswordUtil.hashPassword(passwordText), UserSession.getInstance(getContext()).getTypeId());
            user.setUserId(userId);

            new UpdateUserTask().execute(user);
        }
    }

    private class UpdateUserTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            appDatabase.userDao().update(users[0]);
            return null;
        }
    }

    private void loadPreferences() {
        volumeSeekBar.setProgress(prefs.getInt("volume", 200));
        musicSwitch.setChecked(prefs.getBoolean("music_enabled", true));
    }

    @Override
    public void onPause() {
        super.onPause();
        // Lưu thông tin người dùng
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("music_enabled", musicSwitch.isChecked());
        editor.putInt("volume", volumeSeekBar.getProgress());
        editor.apply();
    }
}