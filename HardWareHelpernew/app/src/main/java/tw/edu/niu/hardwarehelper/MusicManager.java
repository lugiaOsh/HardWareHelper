package tw.edu.niu.hardwarehelper;
import android.content.Context;
import android.media.MediaPlayer;
public class MusicManager {
    private static MediaPlayer mediaPlayer;
    private static int activityCount = 0; // 👉 用來記錄目前有幾個 Activity 在前景

    // 某個頁面進入前景（Visible）時呼叫
    public static void activityStarted(Context context, int resourceId) {
        activityCount++;
        // 只要有頁面亮起來，且音樂沒在播，就開始播放
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context.getApplicationContext(), resourceId);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        } else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // 某個頁面離開前景（Invisible）時呼叫
    public static void activityStopped() {
        activityCount--;
        // 👉 核心邏輯：當計數器歸零，代表所有頁面都看不見了（使用者退出了 App）
        if (activityCount <= 0) {
            activityCount = 0; // 確保不為負數
            pause();           // 暫停音樂
        }
    }

    public static void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
