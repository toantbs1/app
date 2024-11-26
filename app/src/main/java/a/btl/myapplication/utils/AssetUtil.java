package a.btl.myapplication.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetUtil {
    public static void loadImagesFromAssets(Context context, String fileName, ImageView imageView) {
        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open(fileName)) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadVideoFromAssets(Context context, String fileName, VideoView videoView) {
        File tempFile = new File(context.getCacheDir(), "chay_tai_cho.mp4");
        try (InputStream inputStream = context.getAssets().open(fileName);
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            Uri videoUri = Uri.parse(tempFile.getAbsolutePath());
            videoView.setVideoURI(videoUri);
            // Bắt đầu phát video khi chuẩn bị xong
            videoView.setOnPreparedListener(mp -> videoView.start());
            videoView.setOnCompletionListener(mp -> videoView.start());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
