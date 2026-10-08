package tw.edu.niu.hardwarehelper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.Executors;

public class AddActivity extends AppCompatActivity {

    EditText editName;

    EditText editTitle;

    EditText editDesc;

    EditText editApp;

    EditText editKeyword;

    ImageView imageView;

    Spinner spinnerType;

    Button btnSave;

    private String saveImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add);

        editName = findViewById(R.id.editTextText);

        editTitle = findViewById(R.id.editTextText2);

        editDesc = findViewById(R.id.editTextText3);

        editApp = findViewById(R.id.editTextText4);

        editKeyword = findViewById(R.id.editTextText5);

        imageView = findViewById(R.id.imageView3);

        spinnerType = findViewById(R.id.spinnerType);

        btnSave = findViewById(R.id.button4);

        // Spinner 類型
        String[] types = {
                "IC",
                "sensor"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        types
                );

        spinnerType.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveItem());
    }

    // 儲存圖片
    private String saveToInternalStorage(Uri uri) {

        try {

            InputStream inputStream =
                    getContentResolver().openInputStream(uri);

            String fileName =
                    "atlas_img_"
                            + System.currentTimeMillis()
                            + ".jpg";

            File file = new File(getFilesDir(), fileName);

            FileOutputStream outputStream =
                    new FileOutputStream(file);

            byte[] buffer = new byte[1024];

            int bytesRead;

            while ((bytesRead =
                    inputStream.read(buffer)) != -1) {

                outputStream.write(
                        buffer,
                        0,
                        bytesRead
                );
            }

            outputStream.close();

            inputStream.close();

            return file.getAbsolutePath();

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }

    // Bitmap 儲存
    private String saveBitmapToInternal(Bitmap bitmap) {

        try {

            String fileName =
                    "atlas_img_"
                            + System.currentTimeMillis()
                            + ".jpg";

            File file = new File(getFilesDir(), fileName);

            FileOutputStream fos =
                    new FileOutputStream(file);

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    fos
            );

            fos.close();

            return file.getAbsolutePath();

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }

    // 相機
    private final ActivityResultLauncher<Void>
            cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.TakePicturePreview(),
                    result -> {

                        if (result != null) {

                            imageView.setImageBitmap(result);

                            saveImagePath =
                                    saveBitmapToInternal(result);
                        }
                    }
            );

    // 相簿
    private final ActivityResultLauncher<Intent>
            galleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            Uri selectedUri =
                                    result.getData().getData();

                            saveImagePath =
                                    saveToInternalStorage(selectedUri);

                            imageView.setImageURI(selectedUri);
                        }
                    }
            );

    // 開啟相機
    public void oncamera(android.view.View view) {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    100
            );

        } else {

            cameraLauncher.launch(null);
        }
    }

    // 開啟相簿
    public void onphoto(android.view.View view) {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );

        galleryLauncher.launch(intent);
    }

    // 儲存
    private void saveItem() {

        String name =
                editName.getText().toString().trim();

        String title =
                editTitle.getText().toString().trim();

        String desc =
                editDesc.getText().toString().trim();

        String app =
                editApp.getText().toString().trim();

        String keyword =
                editKeyword.getText().toString().trim();

        if (name.isEmpty()) {

            Toast.makeText(
                    this,
                    "請輸入名稱",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        userItem item = new userItem();

        item.name = name;

        item.title = title;

        item.description = desc;

        item.category =
                spinnerType.getSelectedItem().toString();

        item.app = app;

        item.keyword = keyword;

        item.imagePath = saveImagePath;

        item.isUserAdded = true;

        Executors.newSingleThreadExecutor().execute(() -> {

            AppDatabase db =
                    AppDatabase.getInstance(this);

            db.atlasDao().insert(item);

            runOnUiThread(() -> {

                Toast.makeText(
                        this,
                        "新增成功",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            });
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