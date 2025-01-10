package a.btl.myapplication.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import a.btl.myapplication.ui.notification.TimeChooseActivity;

public class NotificationReceiver extends BroadcastReceiver {

    // Phương thức này được gọi khi BroadcastReceiver nhận một broadcast
    @Override
    public void onReceive(Context context, Intent intent) {
        // Gọi phương thức để gửi thông báo
        sendNotification(context);
    }

    // Phương thức để tạo và gửi thông báo
    private void sendNotification(Context context) {
        // Lấy dịch vụ NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "default_channel"; // ID cho kênh thông báo

        // Tạo kênh thông báo cho Android Oreo và các phiên bản cao hơn
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel); // Tạo kênh
        }

        // Tạo một intent sẽ mở TimeChooseActivity khi người dùng nhấn vào thông báo
        Intent notificationIntent = new Intent(context, TimeChooseActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Xây dựng thông báo bằng NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Đặt biểu tượng nhỏ cho thông báo
                .setContentTitle("Thông báo") // Đặt tiêu đề cho thông báo
                .setContentText("Bạn đã sẵn sàng cho buổi luyện tập hôm nay chưa!") // Đặt nội dung cho thông báo
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Đặt mức độ ưu tiên
                .setContentIntent(pendingIntent) // Đặt intent sẽ được gọi khi nhấn vào thông báo
                .setAutoCancel(true); // Tự động xóa thông báo khi được nhấn

        // Gửi thông báo với ID duy nhất (ở đây là 0)
        notificationManager.notify(0, builder.build());
    }
}