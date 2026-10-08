package tw.edu.niu.hardwarehelper;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AtlasDao {
    @Insert
    void insert(userItem item);

    @Query("SELECT * FROM user_atlas ORDER BY id DESC")
    List<userItem> getAll();

    // ⭐ 檢查這兩行：冒號 ":" 後面的變數名必須跟參數 name 一模一樣
    @Query("SELECT * FROM user_atlas WHERE name = :name LIMIT 1")
    userItem getByName(String name);
}