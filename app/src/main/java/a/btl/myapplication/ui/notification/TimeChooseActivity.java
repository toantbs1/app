package a.btl.myapplication.ui.notification;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import a.btl.myapplication.R;
import a.btl.myapplication.entity.TimeNotification;
import a.btl.myapplication.utils.AppDatabase;

public class TimeChooseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TimeNotificationAdapter adapter;
    private List<TimeNotification> timeNotificationList;
    private AppDatabase database;
    private FloatingActionButton fabAddTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_choose);
        getWidget();
        loadNotifications(); // Tải thông báo từ cơ sở dữ liệu

        fabAddTime.setOnClickListener(v -> {
            showTimePickerDialog();
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Xóa item khi bị trượt sang trái
                int position = viewHolder.getAdapterPosition();
                adapter.removeItem(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void getWidget() {
        recyclerView = findViewById(R.id.recycler_view);
        fabAddTime = findViewById(R.id.fab_add_time);
        timeNotificationList = new ArrayList<>();
        database = AppDatabase.getInstance(this);

        adapter = new TimeNotificationAdapter(timeNotificationList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void loadNotifications() {
        new Thread(() -> {
            List<TimeNotification> notifications = database.timeNotificationDao().getAllNotifications();
            runOnUiThread(() -> {
                timeNotificationList.clear();
                timeNotificationList.addAll(notifications);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void showTimePickerDialog() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    // Khi người dùng chọn xong thời gian
                    addTimeNotification(selectedHour, selectedMinute);
                }, hour, minute, true); // true để hiển thị định dạng 24 giờ
        timePickerDialog.show();
    }

    private void addTimeNotification(int hour, int minute) {
        // Thay thế bằng logic để lấy giờ từ TimePicker
        String newTime = String.format("%02d:%02d", hour, minute);
        TimeNotification newNotification = new TimeNotification();
        newNotification.setTime(newTime);
        newNotification.setIsEnabled(false);
        newNotification.setDaysOfWeek("0,0,0,0,0,0,0");
        timeNotificationList.add(newNotification);
        adapter.notifyItemInserted(timeNotificationList.size() - 1);
        // Lưu vào cơ sở dữ liệu
        new Thread(() -> database.timeNotificationDao().insert(newNotification)).start();
    }
}