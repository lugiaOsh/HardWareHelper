package tw.edu.niu.hardwarehelper.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import tw.edu.niu.hardwarehelper.models.Item;

public class JsonUtils {

    // 載入 JSON 圖鑑資料
    public static List<Item> loadItems(Context context) {

        try {

            InputStream is = context.getAssets().open("data.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();

            Type listType =
                    new TypeToken<List<Item>>() {}.getType();

            return gson.fromJson(json, listType);

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // 依名稱查找 Item
    public static Item findItemByName(Context context,
                                      String targetName) {

        List<Item> items = loadItems(context);

        if (items == null) {

            return null;
        }

        for (Item item : items) {

            if (item.name != null
                    && item.name.equals(targetName)) {

                return item;
            }
        }

        return null;
    }
}