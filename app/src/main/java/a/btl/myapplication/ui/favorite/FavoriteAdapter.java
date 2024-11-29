package a.btl.myapplication.ui.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import a.btl.myapplication.R;
import a.btl.myapplication.entity.Exercises;
import a.btl.myapplication.ui.exercise.ExerciseAdapter;
import a.btl.myapplication.utils.AssetUtil;

public class FavoriteAdapter  extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private final Context context;
    private final List<Exercises> exercisesList;
    private final FavoriteAdapter.OnItemClickListener listener;

    public FavoriteAdapter(Context context, List<Exercises> exercisesList, FavoriteAdapter.OnItemClickListener listener) {
        this.context = context;
        this.exercisesList = exercisesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteAdapter.FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        Exercises exercises = exercisesList.get(position);
        holder.typeName.setText(exercises.getName());
        AssetUtil.loadImagesFromAssets(context, exercises.getAvatar(), holder.typeAvatar);
        holder.timeOrNum.setText(exercises.getTimeOrNum());
        holder.heartCheckbox.setChecked(true);
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

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView typeAvatar;
        TextView typeName, timeOrNum;
        CheckBox heartCheckbox;
        FavoriteViewHolder(View itemView) {
            super(itemView);
            typeAvatar = itemView.findViewById(R.id.imageView);
            typeName = itemView.findViewById(R.id.tv_name);
            timeOrNum = itemView.findViewById(R.id.tv_timeOrNum);
            heartCheckbox = itemView.findViewById(R.id.heart_checkbox);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
