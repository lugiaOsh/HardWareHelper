package tw.edu.niu.hardwarehelper;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.File;

import tw.edu.niu.hardwarehelper.models.Item;

public class EncyclopediaDetailActivity
        extends AppCompatActivity {

    ImageView img;

    ImageView pinImg;

    TextView name;

    TextView title;

    TextView voltage;

    TextView pkg;

    TextView pins;

    TextView desc;

    TextView app;

    TextView pinConfig;

    CardView basicCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_encyclopedia_detail
        );

        img = findViewById(R.id.img);

        pinImg = findViewById(R.id.pinImg);

        name = findViewById(R.id.name);

        title = findViewById(R.id.title);

        voltage = findViewById(R.id.voltage);

        pkg = findViewById(R.id.pkg);

        pins = findViewById(R.id.pins);

        desc = findViewById(R.id.desc);

        app = findViewById(R.id.app);

        pinConfig = findViewById(R.id.pinConfig);

        // 基本資訊卡片
        basicCard =
                (CardView) voltage.getParent().getParent();

        // 接收資料
        Item item = new Item();

        item.name =
                getIntent().getStringExtra("name");

        item.title =
                getIntent().getStringExtra("title");

        item.desc =
                getIntent().getStringExtra("desc");

        item.application =
                getIntent().getStringExtra("application");

        item.voltage =
                getIntent().getStringExtra("voltage");

        item.packageName =
                getIntent().getStringExtra("packageName");

        item.pins =
                getIntent().getStringExtra("pins");

        item.image =
                getIntent().getStringExtra("image");

        item.imagePath =
                getIntent().getStringExtra("imagePath");

        item.pinImage =
                getIntent().getStringExtra("pinImage");

        item.pinConfig =
                getIntent().getStringExtra("pinConfig");

        // 主圖片
        if (item.imagePath != null
                && !item.imagePath.isEmpty()) {

            File imgFile =
                    new File(item.imagePath);

            if (imgFile.exists()) {

                img.setImageURI(
                        Uri.fromFile(imgFile)
                );

            } else {

                img.setImageResource(
                        R.drawable.ic_launcher_foreground
                );
            }

        } else {

            int imageId =
                    getResources().getIdentifier(
                            item.image,
                            "drawable",
                            getPackageName()
                    );

            if (imageId == 0) {

                imageId =
                        R.drawable.ic_launcher_foreground;
            }

            img.setImageResource(imageId);
        }

        // 名稱
        name.setText(safeText(item.name));

        // 標題
        title.setText(safeText(item.title));

        // 功能說明
        desc.setText(safeText(item.desc));

        // 應用範圍
        app.setText(safeText(item.application));

        // 是否為使用者新增資料
        boolean isUserItem =
                item.voltage == null
                        && item.packageName == null
                        && item.pins == null;

        if (isUserItem) {

            // 隱藏基本資訊
            basicCard.setVisibility(View.GONE);

            // 隱藏 pin 圖
            pinImg.setVisibility(View.GONE);

            // 隱藏 pinConfig
            pinConfig.setVisibility(View.GONE);

        } else {

            // 基本資訊
            voltage.setText(
                    "電壓：" + safeText(item.voltage)
            );

            pkg.setText(
                    "封裝：" + safeText(item.packageName)
            );

            pins.setText(
                    "腳位：" + safeText(item.pins)
            );

            // pin image
            if (item.pinImage != null) {

                int pinId =
                        getResources().getIdentifier(
                                item.pinImage,
                                "drawable",
                                getPackageName()
                        );

                if (pinId != 0) {

                    pinImg.setImageResource(pinId);

                } else {

                    pinImg.setVisibility(View.GONE);
                }

            } else {

                pinImg.setVisibility(View.GONE);
            }

            // pin config
            if (item.pinConfig != null
                    && !item.pinConfig.trim().isEmpty()) {

                pinConfig.setVisibility(View.VISIBLE);

                pinConfig.setText(item.pinConfig);

            } else {

                pinConfig.setVisibility(View.GONE);
            }
        }
    }

    private String safeText(String text) {

        if (text == null
                || text.trim().isEmpty()) {

            return "無資料";
        }

        return text;
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