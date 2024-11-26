package a.btl.myapplication.ui.exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import a.btl.myapplication.R;
import a.btl.myapplication.entity.Exercises;
import a.btl.myapplication.utils.AssetUtil;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private final Context context;
    private final List<Exercises> exercisesList;
    private final ExerciseAdapter.OnItemClickListener listener;

    public ExerciseAdapter(Context context, List<Exercises> exercisesList, ExerciseAdapter.OnItemClickListener listener) {
        this.context = context;
        this.exercisesList = exercisesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseAdapter.ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseViewHolder holder, int position) {
        Exercises exercises = exercisesList.get(position);
        holder.typeName.setText(exercises.getName());
        AssetUtil.loadImagesFromAssets(context, exercises.getAvatar(), holder.typeAvatar);
        holder.timeOrNum.setText(exercises.getTimeOrNum());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position); // Gọi hàm callback với vị trí item
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercisesList.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        ImageView typeAvatar;
        TextView typeName, timeOrNum;

        ExerciseViewHolder(View itemView) {
            super(itemView);
            typeAvatar = itemView.findViewById(R.id.imageView);
            typeName = itemView.findViewById(R.id.tv_name);
            timeOrNum = itemView.findViewById(R.id.tv_timeOrNum);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
