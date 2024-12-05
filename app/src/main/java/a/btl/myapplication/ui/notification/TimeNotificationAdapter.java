package a.btl.myapplication.ui.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import a.btl.myapplication.R;
import a.btl.myapplication.entity.TimeNotification;
import a.btl.myapplication.utils.AppDatabase;
import a.btl.myapplication.utils.NotificationReceiver;

public class TimeNotificationAdapter extends RecyclerView.Adapter<TimeNotificationAdapter.ViewHolder> {
    private List<TimeNotification> timeNotificationList;
    private Context context;
    private AppDatabase database;

    public TimeNotificationAdapter(List<TimeNotification> timeNotificationList, Context context) {
        this.timeNotificationList = timeNotificationList;
        this.context = context;
        this.database = AppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_choose, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeNotification timeNotification = timeNotificationList.get(position);
        holder.tvTime.setText(timeNotification.getTime());
        holder.switchNotification.setChecked(timeNotification.getIsEnabled());

        holder.switchNotification.setOnCheckedChangeListener(null);

        // Xử lý sự kiện nhấp vào TextView để chọn lại thời gian
        holder.tvTime.setOnClickListener(v -> showTimePicker(timeNotification, holder, position));

        holder.switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            timeNotification.setIsEnabled(isChecked);
            Toast.makeText(context, timeNotification.getDaysOfWeek(), Toast.LENGTH_SHORT).show();

            if (isChecked) {
                scheduleNotification(timeNotification);
            }
            // Cập nhật vào cơ sở dữ liệu
            new Thread(() -> database.timeNotificationDao().update(timeNotification)).start();
        });

        // Thiết lập trạng thái cho các CheckBox
        String[] days = timeNotification.getDaysOfWeek().split(",");
        holder.cbMonday.setChecked(days.length > 6 && days[1].equals("1"));
        holder.cbTuesday.setChecked(days.length > 6 && days[2].equals("1"));
        holder.cbWednesday.setChecked(days.length > 6 && days[3].equals("1"));
        holder.cbThursday.setChecked(days.length > 6 && days[4].equals("1"));
        holder.cbFriday.setChecked(days.length > 6 && days[5].equals("1"));
        holder.cbSaturday.setChecked(days.length > 6 && days[6].equals("1"));
        holder.cbSunday.setChecked(days.length > 6 && days[0].equals("1"));

        // Thiết lập listener cho các CheckBox
        holder.cbMonday.setOnCheckedChangeListener((buttonView, isChecked) -> updateDaysOfWeek(timeNotification, 1, isChecked));
        holder.cbTuesday.setOnCheckedChangeListener((buttonView, isChecked) -> updateDaysOfWeek(timeNotification, 2, isChecked));
        holder.cbWednesday.setOnCheckedChangeListener((buttonView, isChecked) -> updateDaysOfWeek(timeNotification, 3, isChecked));
        holder.cbThursday.setOnCheckedChangeListener((buttonView, isChecked) -> updateDaysOfWeek(timeNotification, 4, isChecked));
        holder.cbFriday.setOnCheckedChangeListener((buttonView, isChecked) -> updateDaysOfWeek(timeNotification, 5, isChecked));
        holder.cbSaturday.setOnCheckedChangeListener((buttonView, isChecked) -> updateDaysOfWeek(timeNotification, 6, isChecked));
        holder.cbSunday.setOnCheckedChangeListener((buttonView, isChecked) -> updateDaysOfWeek(timeNotification, 0, isChecked));
    }

    @Override
    public int getItemCount() {
        return timeNotificationList.size();
    }

    public static CheckBox setCB(View itemView, int id, String text) {
        FrameLayout cbMon = itemView.findViewById(id);
        TextView tvM = cbMon.findViewById(R.id.text_view);
        tvM.setText(text);
        return cbMon.findViewById(R.id.checkbox);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTime;
        public Switch switchNotification;
        public CheckBox cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday, cbSaturday, cbSunday;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            switchNotification = itemView.findViewById(R.id.switch_notification);
            cbMonday = setCB(itemView, R.id.cb_monday, "T2");
            cbTuesday = setCB(itemView, R.id.cb_tuesday, "T3");
            cbWednesday = setCB(itemView, R.id.cb_wednesday, "T4");
            cbThursday = setCB(itemView, R.id.cb_thursday, "T5");
            cbFriday = setCB(itemView, R.id.cb_friday, "T6");
            cbSaturday = setCB(itemView, R.id.cb_saturday, "T7");
            cbSunday = setCB(itemView, R.id.cb_sunday, "CN");
        }
    }

    private void updateDaysOfWeek(TimeNotification notification, int pos, boolean isEnabled) {
        String[] days = notification.getDaysOfWeek().split(",");
        if (isEnabled) {
            days[pos] = "1";
        } else {
            days[pos] = "0";
        }
        String result = String.join(",", days);
        notification.setDaysOfWeek(result);
        // Cập nhật vào cơ sở dữ liệu
        new Thread(() -> database.timeNotificationDao().update(notification)).start();
        scheduleNotification(notification);
    }

    private void scheduleNotification(TimeNotification timeNotification) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        String[] days = timeNotification.getDaysOfWeek().split(",");
        for (int i = 0; i < days.length; i++) {
            if (days[i].equals("1")) {
                int actualDayOfWeek = i + 1;

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, actualDayOfWeek);
                int hour = Integer.parseInt(timeNotification.getTime().split(":")[0]);
                int minute = Integer.parseInt(timeNotification.getTime().split(":")[1]);

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                // Đảm bảo thông báo không được lên lịch trong quá khứ
                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                }

                // Tạo PendingIntent với mã duy nhất
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, actualDayOfWeek * 100 + hour * 60 + minute, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    public void removeItem(int position) {
        TimeNotification timeNotification = timeNotificationList.get(position);
        timeNotificationList.remove(position);
        notifyItemRemoved(position);
        new Thread(() -> database.timeNotificationDao().deleteById(timeNotification.getTimeId())).start();
    }

    private void showTimePicker(TimeNotification timeNotification, ViewHolder holder, int position) {
        String[] timeParts = timeNotificationList.get(position).getTime().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, selectedHour, selectedMinute) -> {
            String newTime = String.format("%02d:%02d", selectedHour, selectedMinute);
            timeNotification.setTime(newTime);
            holder.tvTime.setText(newTime);
            timeNotificationList.set(position, timeNotification);
            // Cập nhật thời gian trong danh sách
            // Nếu switchNotification đang bật, cập nhật lại thông báo
            if (holder.switchNotification.isChecked()) {
                scheduleNotification(timeNotification); // Cập nhật lịch thông báo mới
            }
            new Thread(() -> database.timeNotificationDao().update(timeNotification)).start();
        }, hour, minute, true);
        timePickerDialog.show();
    }
}
