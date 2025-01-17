package a.btl.myapplication.ui.practise;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import a.btl.myapplication.MainActivity;
import a.btl.myapplication.R;
import a.btl.myapplication.entity.Exercises;
import a.btl.myapplication.entity.History;
import a.btl.myapplication.entity.dto.UserSession;
import a.btl.myapplication.ui.favorite.FavoriteActivity;
import a.btl.myapplication.utils.AppDatabase;
import a.btl.myapplication.utils.AssetUtil;
import a.btl.myapplication.utils.MusicService;

public class PracticeActivity extends AppCompatActivity {
    private VideoView videoView;
    private TextView tvPos, tvName, tvTime;
    private Button btnControl, btnPre, btnNext;
    private List<Exercises> exercisesList;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30000; // 30 giây
    private boolean isTimerRunning = false;
    private int currentPosition;
    private AppDatabase db;
    private FloatingActionButton fabExit;
    private MusicService musicService;
    private CheckBox heartCheckbox;
    private Dialog dialog;
    private Switch musicSwitch;
    private SeekBar volumeSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        getWidget();
        setData();
        Intent serviceIntent = new Intent(this, MusicService.class);
        startService(serviceIntent);
        FloatingActionButton fabMusic = findViewById(R.id.fab_music);
        fabMusic.setOnClickListener(view -> showMusicControlDialog());
        fabExit.setOnClickListener(new doSomething());
        btnNext.setOnClickListener(new doSomething());
        btnPre.setOnClickListener(new doSomething());




        musicSwitch.setChecked(true);

        heartCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                new Thread(()->{
                    History history = new History();
                    history.setUserId(UserSession.getInstance(this).getUserId());
                    history.setExerciseId(exercisesList.get(currentPosition).getExerciseId());
                    history.setBMI(0);
                    history.setIsFav1(true);
                    history.setNgayTao(new Date());
                    db.historyDao().insert(history);
                }).start();
            } else {
                new Thread(()->{
                    History history = db.historyDao().getHistoryByIsFavAndExerciseIdAndUserId(1, exercisesList.get(currentPosition).getExerciseId(), UserSession.getInstance(this).getUserId());
                    history.setIsFav1(false);
                    db.historyDao().update(history);
                }).start();
            }
        });


    }

    private void getWidget() {
        tvName = findViewById(R.id.tv_name);
        tvPos = findViewById(R.id.tv_pos);
        tvTime = findViewById(R.id.timerTextView);
        btnControl = findViewById(R.id.controlButton);
        btnPre = findViewById(R.id.btn_pre);
        btnNext = findViewById(R.id.btn_next);
        heartCheckbox = findViewById(R.id.heart_checkbox);


        videoView = findViewById(R.id.videoView);
        db = AppDatabase.getInstance(this);
        fabExit = findViewById(R.id.fab_exit);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_music_control);

        musicSwitch = dialog.findViewById(R.id.music_switch);
        volumeSeekBar = dialog.findViewById(R.id.volume_seekbar);

    }

    private void setData() {
        Intent intent = getIntent();
        exercisesList = (List<Exercises>) intent.getSerializableExtra("exerciseList");
        currentPosition = getIntent().getIntExtra("current_position", 0);
        tvPos.setText(String.format("%d/%d", currentPosition + 1, exercisesList.size()));
        tvName.setText(exercisesList.get(currentPosition).getName());
        int count = db.historyDao().countByIsFavAndExerciseIdAndUserId(1, exercisesList.get(currentPosition).getExerciseId(), UserSession.getInstance(this).getUserId());
        heartCheckbox.setChecked(count == 0 ? false : true);

        new Thread(() -> {
            History history = new History();
            history.setUserId(UserSession.getInstance(this).getUserId());
            history.setExerciseId(exercisesList.get(currentPosition).getExerciseId());
            history.setBMI(Double.valueOf(0));
            history.setNgayTao(new Date());
            history.setIsFav1(false);
            db.historyDao().insert(history);
        }).start();

        // Sao chép video từ assets đến bộ nhớ tạm thời
        AssetUtil.loadVideoFromAssets(this, exercisesList.get(currentPosition).getVideo(), videoView);

        if (exercisesList.get(currentPosition).getTimeOrNum().contains("x")) {
            tvTime.setText(exercisesList.get(currentPosition).getTimeOrNum());
        } else if (exercisesList.get(currentPosition).getTimeOrNum().matches("\\d{2}:\\d{2}")) {
            String[] timeParts = exercisesList.get(currentPosition).getTimeOrNum().split(":");
            String hours = timeParts[0];
            String minutes = timeParts[1];
            timeLeftInMillis = (Long.parseLong(hours) * 60 + Long.parseLong(minutes)) * 1000;
            btnControl.setVisibility(View.VISIBLE);
            btnControl.setOnClickListener(v -> handleControlButton());
            updateTimerText();
        }
    }

    private class doSomething implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.fab_exit) {
                startActivity(new Intent(PracticeActivity.this, MainActivity.class));
            } else if (view.getId() == R.id.btn_pre) {
                if (currentPosition > 0) {
                    Intent intent = new Intent(PracticeActivity.this, PracticeActivity.class);
                    intent.putExtra("current_position", currentPosition - 1);
                    intent.putExtra("exerciseList", (Serializable) exercisesList);
                    startActivity(intent);
                } else {
                    Toast.makeText(PracticeActivity.this, "Đã đến bài tập đầu tiên", Toast.LENGTH_SHORT).show();
                }
            } else if (view.getId() == R.id.btn_next) {
                if (currentPosition < exercisesList.size() - 1) {
                    Intent intent = new Intent(PracticeActivity.this, BreakActivity.class);
                    intent.putExtra("current_position", currentPosition + 1);
                    intent.putExtra("exerciseList", (Serializable) exercisesList);
                    startActivity(intent);
                } else {
                    Toast.makeText(PracticeActivity.this, "Đã đến bài tập cuối cùng", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showMusicControlDialog() {
        // Tạo dialog

        // Lấy trạng thái nhạc từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        musicSwitch.setChecked(prefs.getBoolean("music_enabled", true));
        volumeSeekBar.setProgress(prefs.getInt("volume", 100)); // Giá trị âm lượng mặc định

        // Thiết lập sự kiện cho Switch
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Intent serviceIntent = new Intent(this, MusicService.class);
            if (isChecked) {
                //if (!musicService.isServiceRunning(MusicService.class)) {
                    startService(serviceIntent);
                //}
            } else {
                //if (musicService.isServiceRunning(MusicService.class)) {
                    stopService(serviceIntent);
                //}
            }
            // Lưu trạng thái nhạc vào SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("music_enabled", isChecked);
            editor.apply();
        });

        // Thiết lập sự kiện cho SeekBar
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                Intent volumeIntent = new Intent(MusicService.ACTION_SET_VOLUME);
                volumeIntent.putExtra("volume", volume);
                sendBroadcast(volumeIntent);

                // Lưu âm lượng vào SharedPreferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("volume", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        dialog.show();
    }
    private void handleControlButton() {
        if (isTimerRunning) {
            pauseCountdown();
        } else {
            startCountdown();
        }
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                btnControl.setText("Đã hoàn thành!");
                isTimerRunning = false;
                btnControl.setEnabled(false);
            }
        }.start();

        isTimerRunning = true;
        btnControl.setText("Tạm dừng"); // Đổi nút thành "Pause"
    }

    private void pauseCountdown() {
        countDownTimer.cancel(); // Hủy bộ đếm
        isTimerRunning = false; // Đánh dấu bộ đếm đã tạm dừng
        btnControl.setText("Tiếp tục"); // Đổi nút thành "Resume"
    }

    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeLeftFormatted = String.format("%02d:%02d", seconds / 60, seconds % 60);
        tvTime.setText(timeLeftFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(this, MusicService.class);
        stopService(serviceIntent); // Dừng service khi ứng dụng bị đóng
    }
}
