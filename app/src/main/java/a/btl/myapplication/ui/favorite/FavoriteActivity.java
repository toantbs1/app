package a.btl.myapplication.ui.favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import a.btl.myapplication.R;
import a.btl.myapplication.entity.Exercises;
import a.btl.myapplication.entity.ListExercises;
import a.btl.myapplication.entity.dto.UserSession;
import a.btl.myapplication.ui.exercise.ExerciseAdapter;
import a.btl.myapplication.ui.practise.BreakActivity;
import a.btl.myapplication.ui.practise.PracticeActivity;
import a.btl.myapplication.utils.AppDatabase;

public class FavoriteActivity extends AppCompatActivity implements FavoriteAdapter.OnItemClickListener{
    private AppDatabase db;
    private RecyclerView recyclerView;
    private List<Exercises> exercisesList, exercisesList1;
    private FavoriteAdapter favoriteAdapter;
    private Integer userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        userId = UserSession.getInstance(this).getUserId();
        getWidget();
        setAdapter();
    }
    private void getWidget() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = AppDatabase.getInstance(this);
    }

    private void setAdapter() {
        exercisesList = db.exercisesDao().getAllExercisesByIsFavAndUserId(1, userId);
        favoriteAdapter = new FavoriteAdapter(this, exercisesList, this);
        recyclerView.setAdapter(favoriteAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Exercises exercises = exercisesList.get(position);
        Integer listExerciseId = db.exercisesDao().getListExerciseIdByExerciseId(exercises.getExerciseId());
        exercisesList1 = db.exercisesDao().getAllExercisesByListExerciseId(listExerciseId);
        int p = -1;
        for (int i = 0; i < exercisesList1.size(); i++) {
            if (exercisesList1.get(i).equals(exercises)) {
                p = i;
                break; // Dừng lại khi tìm thấy
            }
        }
        Intent intent = new Intent(FavoriteActivity.this, PracticeActivity.class);
        intent.putExtra("current_position", p);
        intent.putExtra("exerciseList", (Serializable) exercisesList1);
        startActivity(intent);
    }
}