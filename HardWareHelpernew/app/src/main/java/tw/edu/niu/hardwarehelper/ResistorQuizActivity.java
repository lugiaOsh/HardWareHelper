package tw.edu.niu.hardwarehelper;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ResistorQuizActivity extends AppCompatActivity {

    TextView textLevel, textRightAnswer;

    View band1, band2, band3;

    Button button1, button2, button3;

    Dialog dialoghint;

    int level = 0;

    int great = 0;

    int rightAns = 0;

    int[] colors = {
            0xFF000000, // 黑
            0xFF8B4513, // 棕
            0xFFFF0000, // 紅
            0xFFFFA500, // 橙
            0xFFFFFF00, // 黃
            0xFF008000, // 綠
            0xFF0000FF, // 藍
            0xFF800080, // 紫
            0xFF808080, // 灰
            0xFFFFFFFF  // 白
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_resistor_quiz);

        textLevel = findViewById(R.id.textQuestionNumber);

        textRightAnswer = findViewById(R.id.textRightAns);

        button1 = findViewById(R.id.button);

        button2 = findViewById(R.id.button2);

        button3 = findViewById(R.id.button3);

        band1 = findViewById(R.id.view);

        band2 = findViewById(R.id.view2);

        band3 = findViewById(R.id.view3);

        textLevel.setText("Q: " + level + " / 10");

        textRightAnswer.setText("RA: " + great + " / 10");

        if (level < 10) {

            getARandomQuestion();
        }
    }

    private void getARandomQuestion() {

        button1.setBackgroundResource(R.drawable.buttons_option_bg);

        button2.setBackgroundResource(R.drawable.buttons_option_bg);

        button3.setBackgroundResource(R.drawable.buttons_option_bg);

        Random r = new Random();

        int a, b, c;

        do {

            a = r.nextInt(10);

            b = r.nextInt(10);

            c = r.nextInt(5);

            band1.setBackgroundColor(colors[a]);

            band2.setBackgroundColor(colors[b]);

            band3.setBackgroundColor(colors[c]);

            rightAns = (a * 10 + b) * (int) Math.pow(10, c);

        } while (rightAns == 0);

        int[] option = {
                rightAns * 10,
                rightAns / 10,
                rightAns * 100,
                rightAns / 100
        };

        int position = r.nextInt(3) + 1;

        int op1, op2;

        do {

            op1 = r.nextInt(4);

            op2 = r.nextInt(4);

        } while (op1 == op2
                || option[op1] <= 0
                || option[op2] <= 0);

        if (position == 1) {

            button1.setText("" + rightAns);

            button2.setText("" + option[op1]);

            button3.setText("" + option[op2]);

        } else if (position == 2) {

            button1.setText("" + option[op1]);

            button2.setText("" + rightAns);

            button3.setText("" + option[op2]);

        } else {

            button1.setText("" + option[op2]);

            button2.setText("" + option[op1]);

            button3.setText("" + rightAns);
        }
    }

    public void onbutton1(View view) {

        checkAnswer(button1);
    }

    public void onbutton2(View view) {

        checkAnswer(button2);
    }

    public void onbutton3(View view) {

        checkAnswer(button3);
    }

    private void checkAnswer(Button button) {

        if (button.getText().toString().equals("" + rightAns)) {

            button.setBackgroundResource(R.drawable.right_answer_bg);

            great++;

            textRightAnswer.setText("RA: " + great + " / 10");

        } else {

            button.setBackgroundResource(R.drawable.wrong_answer_bg);
        }

        level++;

        textLevel.setText("Q: " + level + " / 10");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (level < 10) {

                getARandomQuestion();

            } else {

                Intent intent = new Intent(
                        ResistorQuizActivity.this,
                        QuizResultActivity.class
                );

                intent.putExtra("RA", great);

                startActivity(intent);

                finish();
            }

        }, 1000);
    }

    public void onhint(View view) {

        dialoghint = new Dialog(ResistorQuizActivity.this);

        dialoghint.setContentView(R.layout.activity_hint);

        dialoghint.setCancelable(false);

        Button back = dialoghint.findViewById(R.id.button5);

        back.setOnClickListener(v -> dialoghint.dismiss());

        dialoghint.show();

        // 放大 Hint 視窗
        if (dialoghint.getWindow() != null) {

            dialoghint.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
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