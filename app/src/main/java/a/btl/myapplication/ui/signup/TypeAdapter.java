package a.btl.myapplication.ui.signup;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import a.btl.myapplication.R;
import a.btl.myapplication.entity.Type;
import a.btl.myapplication.utils.AssetUtil;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder> {
    private final Context context;
    private final List<Type> typeList;
    private final OnItemClickListener listener;
    private int selectedPosition = -1;

    public TypeAdapter(Context context, List<Type> typeList, OnItemClickListener listener) {
        this.context = context;
        this.typeList = typeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_type, parent, false);
        return new TypeViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        Type type = typeList.get(position);
        holder.typeName.setText(type.getTypeName());
        AssetUtil.loadImagesFromAssets(context, type.getTypeAvatar(), holder.typeAvatar);
        if (position == selectedPosition) {
            animateColorChange(holder.itemView, ContextCompat.getColor(context, R.color.white), ContextCompat.getColor(context, R.color.gray));
        } else {
            animateColorChange(holder.itemView, ContextCompat.getColor(context, R.color.white), ContextCompat.getColor(context, R.color.white));
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position); // Gọi hàm callback với vị trí item
                selectedPosition = holder.getBindingAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    static class TypeViewHolder extends RecyclerView.ViewHolder {
        ImageView typeAvatar;
        TextView typeName;

        TypeViewHolder(View itemView) {
            super(itemView);
            typeAvatar = itemView.findViewById(R.id.type_avatar);
            typeName = itemView.findViewById(R.id.type_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private void animateColorChange(View view, int startColor, int targetColor) {
        ValueAnimator colorAnimation = ValueAnimator.ofArgb(startColor, targetColor);
        colorAnimation.addUpdateListener(animator -> {
            view.setBackgroundColor((int) animator.getAnimatedValue());
        });
        colorAnimation.setDuration(550); // Thời gian chuyển màu
        colorAnimation.start();
    }
}