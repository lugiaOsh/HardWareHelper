package tw.edu.niu.hardwarehelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import tw.edu.niu.hardwarehelper.adapters.ItemAdapter;
import tw.edu.niu.hardwarehelper.models.Item;
import tw.edu.niu.hardwarehelper.utils.JsonUtils;

public class EncyclopediaActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    EditText searchBox;

    Button btnAll, btnIC, btnSensor, buttonAdd;

    TextView emptyView;

    List<Item> fullList;

    List<Item> displayList;

    ItemAdapter adapter;

    String currentFilter = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_encyclopedia);

        recyclerView = findViewById(R.id.recyclerView);

        searchBox = findViewById(R.id.searchBox);

        btnAll = findViewById(R.id.btnAll);

        btnIC = findViewById(R.id.btnIC);

        btnSensor = findViewById(R.id.btnSensor);

        buttonAdd = findViewById(R.id.button);

        emptyView = findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(
                new GridLayoutManager(this, 2)
        );

        fullList = new ArrayList<>();

        displayList = new ArrayList<>();

        adapter = new ItemAdapter(displayList);

        recyclerView.setAdapter(adapter);

        // 初次載入
        reloadData();

        // 搜尋
        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {}

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {

                updateList(
                        s.toString().toLowerCase()
                );
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 全部
        btnAll.setOnClickListener(v -> {

            currentFilter = "ALL";

            updateList(
                    searchBox.getText()
                            .toString()
                            .toLowerCase()
            );
        });

        // IC
        btnIC.setOnClickListener(v -> {

            currentFilter = "IC";

            updateList(
                    searchBox.getText()
                            .toString()
                            .toLowerCase()
            );
        });

        // 感測器
        btnSensor.setOnClickListener(v -> {

            currentFilter = "sensor";

            updateList(
                    searchBox.getText()
                            .toString()
                            .toLowerCase()
            );
        });

        // 新增
        buttonAdd.setOnClickListener(v -> {

            Intent intent = new Intent(
                    EncyclopediaActivity.this,
                    AddActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        reloadData();
    }

    // 重新載入資料
    // 重新載入資料
    private void reloadData() {

        Executors.newSingleThreadExecutor().execute(() -> {

            // 先清空
            fullList.clear();

            // JSON 圖鑑資料
            List<Item> jsonItems =
                    JsonUtils.loadItems(this);

            if (jsonItems != null) {

                fullList.addAll(jsonItems);
            }

            // Room 使用者新增資料
            AppDatabase db =
                    AppDatabase.getInstance(this);

            List<userItem> userItems =
                    db.atlasDao().getAll();

            for (userItem u : userItems) {

                Item item = new Item();

                item.name = u.name;

                item.title = u.title;

                item.desc = u.description;

                item.type = u.category;

                item.application = u.app;

                // drawable 預設圖
                item.image = "ic_launcher_foreground";

                // 使用者新增圖片
                item.imagePath = u.imagePath;

                fullList.add(item);
            }

            runOnUiThread(() -> {

                updateList(
                        searchBox.getText()
                                .toString()
                                .toLowerCase()
                );
            });
        });
    }

    // 更新列表
    private void updateList(String keyword) {

        displayList.clear();

        for (Item item : fullList) {

            if (item.type == null) continue;

            // 分類篩選
            if (!currentFilter.equals("ALL")
                    && !item.type.equalsIgnoreCase(
                    currentFilter
            )) {

                continue;
            }

            String name =
                    item.name == null
                            ? ""
                            : item.name.toLowerCase();

            String title =
                    item.title == null
                            ? ""
                            : item.title.toLowerCase();

            String desc =
                    item.desc == null
                            ? ""
                            : item.desc.toLowerCase();

            // 搜尋
            if (name.contains(keyword)
                    || title.contains(keyword)
                    || desc.contains(keyword)) {

                displayList.add(item);
            }
        }

        adapter.notifyDataSetChanged();

        // 空資料提示
        if (displayList.size() == 0) {

            emptyView.setVisibility(View.VISIBLE);

            recyclerView.setVisibility(View.GONE);

        } else {

            emptyView.setVisibility(View.GONE);

            recyclerView.setVisibility(View.VISIBLE);
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