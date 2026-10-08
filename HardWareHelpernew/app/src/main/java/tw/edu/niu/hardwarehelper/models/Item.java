package tw.edu.niu.hardwarehelper.models;

public class Item {

    // 名稱
    public String name;

    // 類型（IC / sensor）
    public String type;

    // 標題
    public String title;

    // 工作電壓
    public String voltage;

    // 封裝類型
    public String packageName;

    // 腳位數
    public String pins;

    // 功能說明
    public String desc;

    // 應用範圍
    public String application;

    // drawable 圖片名稱（JSON 用）
    public String image;

    // 使用者新增圖片路徑（Room 用）
    public String imagePath;

    // IC 腳位圖
    public String pinImage;

    // 感測器腳位配置
    public String pinConfig;

    // 感測器輸出方式
    public String output;

    // 偵測範圍
    public String detectionRange;

    // 延遲時間
    public String delayTime;

    // 觸發模式
    public String triggerMode;

    // 是否為使用者新增
    // true  = 使用者新增
    // false = 系統內建
    public boolean isUserAdded = false;
}