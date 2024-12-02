package a.btl.myapplication.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private MediaPlayer mediaPlayer;

    // Hành động để nhận thông điệp điều chỉnh âm lượng
    public static final String ACTION_SET_VOLUME = "a.btl.myapplication.SET_VOLUME";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = getAssets().openFd("music/background_music.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.setLooping(true); // Đặt nhạc phát lặp
            mediaPlayer.prepare(); // Chuẩn bị nhạc

            // Đăng ký BroadcastReceiver
            registerReceiver(volumeReceiver, new IntentFilter(ACTION_SET_VOLUME));
        } catch (IOException e) {
            Log.e(TAG, "Error setting data source", e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // Bắt đầu phát nhạc
            Log.d(TAG, "Music started playing");
        } else {
            Log.d(TAG, "Music is already playing or MediaPlayer is null");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop(); // Dừng nhạc
            }
            mediaPlayer.release(); // Giải phóng tài nguyên
            mediaPlayer = null;
        }
        unregisterReceiver(volumeReceiver); // Hủy đăng ký BroadcastReceiver
    }

    // Receiver để nhận thông điệp điều chỉnh âm lượng
    private final BroadcastReceiver volumeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("volume")) {
                float volume = intent.getFloatExtra("volume", 1.0f);
                setVolume(volume); // Gọi phương thức setVolume
            }
        }
    };

    // Phương thức để điều chỉnh âm lượng
    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume); // Cập nhật âm lượng
        }
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}