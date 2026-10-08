package tw.edu.niu.hardwarehelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tw.edu.niu.hardwarehelper.models.Card;
import tw.edu.niu.hardwarehelper.models.IC;

public class MemoryGameActivity extends AppCompatActivity {

    LinearLayout container;

    TextView timerText;

    List<Card> cards = new ArrayList<>();

    ImageButton firstBtn = null;

    ImageButton secondBtn = null;

    boolean isBusy = false;

    // 計時
    long startTime;

    Handler timerHandler = new Handler();

    Runnable timerRunnable;

    // 震動
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 鎖直向
        setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        );

        setContentView(R.layout.activity_memory_game);

        container = findViewById(R.id.container);

        timerText = findViewById(R.id.timerText);

        // 初始化震動
        vibrator = (Vibrator) getSystemService(
                Context.VIBRATOR_SERVICE
        );

        // IC 資料
        List<IC> allIC = new ArrayList<>();

        allIC.add(new IC("7408", "AND"));

        allIC.add(new IC("7432", "OR"));

        allIC.add(new IC("7486", "XOR"));

        allIC.add(new IC("7400", "NAND"));

        allIC.add(new IC("7402", "NOR"));

        // 隨機抽 3 組
        Collections.shuffle(allIC);

        List<IC> selected = allIC.subList(0, 3);

        // 建立卡片
        for (IC ic : selected) {

            cards.add(new Card(ic.id, true));

            cards.add(new Card(ic.id, false));
        }

        Collections.shuffle(cards);

        // 卡片尺寸
        int screenWidth =
                getResources()
                        .getDisplayMetrics()
                        .widthPixels;

        int margin = dpToPx(12);

        int totalMarginW = margin * 4;

        int cardWidth =
                (screenWidth - totalMarginW) / 2;

        int cardHeight = cardWidth * 4 / 3;

        // 建立 UI
        for (int i = 0; i < cards.size(); i += 2) {

            LinearLayout row = new LinearLayout(this);

            row.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            for (int j = 0; j < 2; j++) {

                Card c = cards.get(i + j);

                ImageButton btn = new ImageButton(this);

                btn.setImageResource(
                        R.drawable.card_back
                );

                btn.setScaleType(
                        ImageView.ScaleType.FIT_CENTER
                );

                btn.setAdjustViewBounds(true);

                btn.setTag(c);

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                cardWidth,
                                cardHeight
                        );

                params.setMargins(
                        margin,
                        margin,
                        margin,
                        margin
                );

                btn.setLayoutParams(params);

                btn.setOnClickListener(
                        v -> onCardClick(btn)
                );

                row.addView(btn);
            }

            container.addView(row);
        }

        // 開始計時
        startTime = System.currentTimeMillis();

        timerRunnable = new Runnable() {

            @Override
            public void run() {

                long currentTime =
                        System.currentTimeMillis();

                long elapsed =
                        (currentTime - startTime) / 1000;

                timerText.setText(
                        "時間：" + elapsed + " 秒"
                );

                timerHandler.postDelayed(
                        this,
                        500
                );
            }
        };

        timerHandler.post(timerRunnable);
    }

    // dp → px
    private int dpToPx(int dp) {

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    // 點擊邏輯
    private void onCardClick(ImageButton btn) {

        if (isBusy
                || btn.getVisibility() != View.VISIBLE) {

            return;
        }

        if (btn == firstBtn) return;

        Card card = (Card) btn.getTag();

        btn.setImageResource(getImage(card));

        if (firstBtn == null) {

            firstBtn = btn;

        } else {

            secondBtn = btn;

            isBusy = true;

            new Handler(getMainLooper())
                    .postDelayed(() -> {

                        if (isMatch(
                                firstBtn,
                                secondBtn
                        )) {

                            // 配對成功震動
                            vibratePhone();

                            firstBtn.setVisibility(
                                    View.INVISIBLE
                            );

                            secondBtn.setVisibility(
                                    View.INVISIBLE
                            );

                            if (checkGameEnd()) {

                                showWinDialog();
                            }

                        } else {

                            firstBtn.setImageResource(
                                    R.drawable.card_back
                            );

                            secondBtn.setImageResource(
                                    R.drawable.card_back
                            );
                        }

                        firstBtn = null;

                        secondBtn = null;

                        isBusy = false;

                    }, 800);
        }
    }

    // 震動
    private void vibratePhone() {

        if (vibrator == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            vibrator.vibrate(
                    VibrationEffect.createOneShot(
                            15,   // 震動時間（毫秒）
                            255    // 最大震動強度
                    )
            );

        } else {

            // 舊版 Android
            vibrator.vibrate(300);
        }
    }

    // 圖片對應
    private int getImage(Card card) {

        if (card.isFront) {

            switch (card.id) {

                case "7408":
                    return R.drawable.ic_7408;

                case "7432":
                    return R.drawable.ic_7432;

                case "7486":
                    return R.drawable.ic_7486;

                case "7400":
                    return R.drawable.ic_7400;

                case "7402":
                    return R.drawable.ic_7402;
            }

        } else {

            switch (card.id) {

                case "7408":
                    return R.drawable.ic_7408_pin;

                case "7432":
                    return R.drawable.ic_7432_pin;

                case "7486":
                    return R.drawable.ic_7486_pin;

                case "7400":
                    return R.drawable.ic_7400_pin;

                case "7402":
                    return R.drawable.ic_7402_pin;
            }
        }

        return R.drawable.card_back;
    }

    // 配對判斷
    private boolean isMatch(ImageButton a,
                            ImageButton b) {

        Card c1 = (Card) a.getTag();

        Card c2 = (Card) b.getTag();

        return c1.id.equals(c2.id);
    }

    // 是否完成
    private boolean checkGameEnd() {

        for (int i = 0;
             i < container.getChildCount();
             i++) {

            LinearLayout row =
                    (LinearLayout)
                            container.getChildAt(i);

            for (int j = 0;
                 j < row.getChildCount();
                 j++) {

                ImageButton btn =
                        (ImageButton)
                                row.getChildAt(j);

                if (btn.getVisibility()
                        == View.VISIBLE) {

                    return false;
                }
            }
        }

        return true;
    }

    // 完成視窗
    private void showWinDialog() {

        timerHandler.removeCallbacks(
                timerRunnable
        );

        long endTime =
                System.currentTimeMillis();

        long elapsedTime =
                (endTime - startTime) / 1000;

        new AlertDialog.Builder(this)
                .setTitle("完成！")
                .setMessage(
                        "你已完成所有配對 💪\n"
                                + "花費時間："
                                + elapsedTime
                                + " 秒"
                )
                .setCancelable(false)

                // 重新開始
                .setPositiveButton(
                        "重新開始",
                        (dialog, which) -> recreate()
                )

                // 返回
                .setNegativeButton(
                        "返回",
                        (dialog, which) -> {

                            Intent intent =
                                    new Intent(
                                            MemoryGameActivity.this,
                                            MainActivity.class
                                    );

                            startActivity(intent);

                            finish();
                        }
                )

                .show();
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