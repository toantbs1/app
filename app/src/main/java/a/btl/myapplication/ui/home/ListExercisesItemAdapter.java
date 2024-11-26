package a.btl.myapplication.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import a.btl.myapplication.R;
import a.btl.myapplication.entity.ListExercises;

public class ListExercisesItemAdapter extends RecyclerView.Adapter<ListExercisesItemAdapter.ViewHolder> {

    private List<ListExercises> list;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ListExercises listExercise);
    }

    public ListExercisesItemAdapter(List<ListExercises> itemList, OnItemClickListener listener) {
        this.list = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_exercises, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListExercises item = list.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemText;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.tv_list_exercise);
            cardView = itemView.findViewById(R.id.card);
        }

        public void bind(final ListExercises item, final OnItemClickListener listener) {
            itemText.setText(item.getListExerciseName());
            cardView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
