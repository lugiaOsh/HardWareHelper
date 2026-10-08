package tw.edu.niu.hardwarehelper;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_atlas") // 定義資料表名稱
public class userItem {
    @PrimaryKey(autoGenerate = true) // 設定 id 為自動產生的主鍵
    public int id;

    public String name;
    public String title;
    public String description;
    public String category;
    public String imagePath;
    public String keyword;
    public String app;
    public boolean isUserAdded;
}
