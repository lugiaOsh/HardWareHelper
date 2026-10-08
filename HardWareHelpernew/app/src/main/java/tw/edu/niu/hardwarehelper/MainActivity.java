package tw.edu.niu.hardwarehelper;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageButton btnEncyclopedia;
    ImageButton btnQuiz;
    ImageButton btnMemoryGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEncyclopedia = findViewById(R.id.btnEncyclopedia);
        btnQuiz = findViewById(R.id.btnQuiz);
        btnMemoryGame = findViewById(R.id.btnMemoryGame);
        btnEncyclopedia.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EncyclopediaActivity.class);
            startActivity(intent);
        });

        btnQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResistorQuizActivity.class);
            startActivity(intent);
        });

        btnMemoryGame.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MemoryGameActivity.class);
            startActivity(intent);
        });
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