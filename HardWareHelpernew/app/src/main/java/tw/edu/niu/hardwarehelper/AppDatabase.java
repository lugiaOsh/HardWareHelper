package tw.edu.niu.hardwarehelper;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {userItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AtlasDao atlasDao();

    private static AppDatabase instance;

    // 使用單例模式 (Singleton) 確保全 App 只有一個資料庫連線
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "atlas_database")
                    .fallbackToDestructiveMigration() // 版本更新時自動清空舊資料庫防止閃退
                    .build();
        }
        return instance;
    }
}
