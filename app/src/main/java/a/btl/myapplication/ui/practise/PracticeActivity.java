package a.btl.myapplication.ui.practise;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
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
import a.btl.myapplication.utils.AppDatabase;
import a.btl.myapplication.utils.AssetUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        getWidget();
        setData();
        fabExit.setOnClickListener(new doSomething());
        btnNext.setOnClickListener(new doSomething());
        btnPre.setOnClickListener(new doSomething());
    }

    private void getWidget() {
        tvName = findViewById(R.id.tv_name);
        tvPos = findViewById(R.id.tv_pos);
        tvTime = findViewById(R.id.timerTextView);
        btnControl = findViewById(R.id.controlButton);
        btnPre = findViewById(R.id.btn_pre);
        btnNext = findViewById(R.id.btn_next);
        videoView = findViewById(R.id.videoView);
        db = AppDatabase.getInstance(this);
        fabExit = findViewById(R.id.fab_exit);
    }

    private void setData() {
        Intent intent = getIntent();
        exercisesList = (List<Exercises>) intent.getSerializableExtra("exerciseList");
        currentPosition = getIntent().getIntExtra("current_position", 0);
        tvPos.setText(String.format("%d/%d", currentPosition + 1, exercisesList.size()));
        tvName.setText(exercisesList.get(currentPosition).getName());

        new Thread(() -> {
            History history = new History();
            history.setUserId(UserSession.getInstance(this).getUserId());
            history.setExerciseId(currentPosition + 1);
            history.setBMI(Double.valueOf(0));
            history.setNgayTao(new Date());
            db.historyDao().insert(history);
        }).start();

        // Sao chép video từ assets đến bộ nhớ tạm thời
        AssetUtil.loadVideoFromAssets(this, exercisesList.get(0).getVideo(), videoView);

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
                    Toast.makeText(PracticeActivity.this, "Đã đến item đầu tiên", Toast.LENGTH_SHORT).show();
                }
            } else if (view.getId() == R.id.btn_next) {
                if (currentPosition < exercisesList.size() - 1) {
                    Intent intent = new Intent(PracticeActivity.this, BreakActivity.class);
                    intent.putExtra("current_position", currentPosition + 1);
                    intent.putExtra("exerciseList", (Serializable) exercisesList);
                    startActivity(intent);
                } else {
                    Toast.makeText(PracticeActivity.this, "Đã đến item cuối cùng", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
}
