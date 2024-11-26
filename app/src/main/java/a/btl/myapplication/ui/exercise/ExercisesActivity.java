package a.btl.myapplication.ui.exercise;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import a.btl.myapplication.ui.practise.PracticeActivity;
import a.btl.myapplication.R;
import a.btl.myapplication.entity.Exercises;
import a.btl.myapplication.entity.ListExercises;
import a.btl.myapplication.utils.AppDatabase;
import a.btl.myapplication.utils.AssetUtil;

public class ExercisesActivity extends AppCompatActivity implements ExerciseAdapter.OnItemClickListener {

    private AppDatabase db;
    private TextView tvListName, tvName, tvGuide, tvVideo, tvImage;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private Button btnStart, btnExit;
    private ExerciseAdapter exerciseAdapter;
    private FrameLayout container, container1;
    private boolean isOpen = false;
    private int p = 0;
    private List<Exercises> exercisesList;
    private VideoView videoView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        getWidget();
        setAdapter();
        setVideoAndImage(exercisesList.get(p).getVideo(), exercisesList.get(p).getAvatar());
        btnStart.setOnClickListener(new doSomething());
        btnExit.setOnClickListener(new doSomething());
        tvVideo.setOnClickListener(new doSomething());
        tvImage.setOnClickListener(new doSomething());
    }

    private void getWidget() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnStart = findViewById(R.id.btn_start);
        btnExit = findViewById(R.id.btn_exit);
        tvListName = findViewById(R.id.tv_list_name);
        tvName = findViewById(R.id.tv_exercise_name);
        tvGuide = findViewById(R.id.tv_guide);
        container = findViewById(R.id.container);
        imageView = findViewById(R.id.imageView);
        tvVideo = findViewById(R.id.tvVideo);
        tvImage = findViewById(R.id.tvImage);
        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.imageView);
        container1 = findViewById(R.id.container1);
        db = AppDatabase.getInstance(this);
    }

    private void setAdapter() {
        Intent intent = getIntent();
        ListExercises listExercises = (ListExercises) intent.getSerializableExtra("list_exercises");
        exercisesList = db.exercisesDao().getAllExercisesByListExerciseId(listExercises != null ? listExercises.getListExerciseId() : null);
        exerciseAdapter = new ExerciseAdapter(this, exercisesList, this);
        recyclerView.setAdapter(exerciseAdapter);
        tvListName.setText(listExercises != null ? listExercises.getListExerciseName() : "Bài tập");
    }

    private void setVideoAndImage(String videoPath, String imagePath) {
        AssetUtil.loadVideoFromAssets(this, videoPath, videoView);
        AssetUtil.loadImagesFromAssets(this, imagePath, imageView);
    }

    @Override
    public void onItemClick(int position) {
        if (!isOpen) {
            // Mở rộng
            container.setVisibility(View.VISIBLE);
            int cx = container.getWidth();
            int cy = container.getHeight();
            float finalRadius = (float) Math.sqrt(cx * cx + cy * cy);

            // Tạo hiệu ứng mở rộng
            Animator anim = ViewAnimationUtils.createCircularReveal(container, cx, cy, 0, finalRadius);
            anim.start();

            isOpen = true;
            p = position;
            AssetUtil.loadImagesFromAssets(this, exercisesList.get(p).getAvatar(), imageView);
            tvName.setText(exercisesList.get(p).getName());
            tvGuide.setText(exercisesList.get(p).getDescription());
        }
    }

    private void setDisplay(VideoView videoView, ImageView imageView, boolean checkVideo, int vView, int iView, TextView tvImage, TextView tvVideo) {
        tvVideo.setSelected(checkVideo);
        tvImage.setSelected(!checkVideo);
        videoView.setVisibility(vView);
        imageView.setVisibility(iView);
        // Hiển thị video trong container
        container1.removeAllViews();
        if (checkVideo) {
            container1.addView(videoView);
        } else {
            container1.addView(imageView);
        }
    }

    private class doSomething implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_start) {
                Intent intent1 = new Intent(ExercisesActivity.this, PracticeActivity.class);
                intent1.putExtra("exerciseList", (Serializable) exercisesList);
                startActivity(intent1);
            } else if (view.getId() == R.id.btn_exit) {
                // Đóng lại
                container.setVisibility(View.GONE);
                isOpen = false;
            } else if (view.getId() == R.id.tvVideo) {
                setDisplay(videoView, imageView, true, View.VISIBLE, View.GONE, tvImage, tvVideo);
            } else if (view.getId() == R.id.tvImage) {
                setDisplay(videoView, imageView, false, View.GONE, View.VISIBLE, tvImage, tvVideo);
            }
        }
    }
}