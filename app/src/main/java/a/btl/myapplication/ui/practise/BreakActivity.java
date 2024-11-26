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
import java.util.List;

import a.btl.myapplication.MainActivity;
import a.btl.myapplication.R;
import a.btl.myapplication.entity.Exercises;
import a.btl.myapplication.utils.AssetUtil;

public class BreakActivity extends AppCompatActivity {
    private VideoView videoView;
    private TextView tvPos, tvName, tvTime;
    private Button btnPre, btnNext;
    private List<Exercises> exercisesList;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30000; // 30 giây
    private boolean isTimerRunning = false;
    private int currentPosition;
    private FloatingActionButton fabExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break);
        setWidget();
        setData();
        btnNext.setOnClickListener(new doSomething());
        btnPre.setOnClickListener(new doSomething());
        fabExit.setOnClickListener(new doSomething());
        startCountdown();
        updateTimerText();
    }

    private void setData() {
        Intent intent = getIntent();
        exercisesList = (List<Exercises>) intent.getSerializableExtra("exerciseList");
        currentPosition = getIntent().getIntExtra("current_position", 0);
        tvPos.setText(String.format("Tiếp theo %d/%d", currentPosition + 1, exercisesList.size()));
        tvName.setText(exercisesList.get(currentPosition).getName());
        AssetUtil.loadVideoFromAssets(this, exercisesList.get(0).getVideo(), videoView);
    }

    private void setWidget() {
        tvPos = findViewById(R.id.tv_pos);
        tvName = findViewById(R.id.tv_name);
        tvTime = findViewById(R.id.timerTextView);
        btnPre = findViewById(R.id.btn_pre);
        btnNext = findViewById(R.id.btn_skip);
        videoView = findViewById(R.id.videoView);
        fabExit = findViewById(R.id.fab_exit);
    }

    private void handleControlButton() {
        timeLeftInMillis += 20000;
        updateTimerText();
        countDownTimer.cancel();
        startCountdown();
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
                isTimerRunning = false;
                if (currentPosition < exercisesList.size() - 1) {
                    Intent intent = new Intent(BreakActivity.this, PracticeActivity.class);
                    intent.putExtra("current_position", currentPosition);
                    intent.putExtra("exerciseList", (Serializable) exercisesList);
                    startActivity(intent);
                } else {
                    Toast.makeText(BreakActivity.this, "Đã đến item cuối cùng", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
        isTimerRunning = true;
    }

    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeLeftFormatted = String.format("%02d:%02d", seconds / 60, seconds % 60);
        tvTime.setText(timeLeftFormatted);
    }

    private class doSomething implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.fab_exit) {
                startActivity(new Intent(BreakActivity.this, MainActivity.class));
            } else if (view.getId() == R.id.btn_pre) {
                handleControlButton();
            } else if (view.getId() == R.id.btn_skip) {
                if (currentPosition < exercisesList.size()) {
                    Intent intent = new Intent(BreakActivity.this, PracticeActivity.class);
                    intent.putExtra("current_position", currentPosition);
                    intent.putExtra("exerciseList", (Serializable) exercisesList);
                    startActivity(intent);
                } else {
                    Toast.makeText(BreakActivity.this, "Đã đến item cuối cùng", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
