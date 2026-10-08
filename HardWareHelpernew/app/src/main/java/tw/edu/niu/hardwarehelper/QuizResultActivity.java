package tw.edu.niu.hardwarehelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResultActivity extends AppCompatActivity {

    TextView textView;

    int great;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz_result);
        great = getIntent().getIntExtra("RA", 0);

        textView = findViewById(R.id.textView);

        textView.setText(
                "You answered "
                        + great
                        + " / 10"
        );
    }

    // 再玩一次
    public void onagain(View view) {

        Intent intent = new Intent(
                QuizResultActivity.this,
                ResistorQuizActivity.class
        );

        startActivity(intent);

        finish();
    }

    // 返回主頁
    public void onback(View view) {

        Intent intent = new Intent(
                QuizResultActivity.this,
                MainActivity.class
        );

        startActivity(intent);

        finish();
    }
    protected void onStart() {
        super.onStart();
        // 👉 頁面亮起：計動計數器並確保音樂播放
        MusicManager.activityStarted(this, R.raw.bgmusic);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 👉 頁面隱藏：減少計數，若全退則會自動暫停
        MusicManager.activityStopped();
    }
}