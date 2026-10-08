package tw.edu.niu.hardwarehelper.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tw.edu.niu.hardwarehelper.EncyclopediaDetailActivity;
import tw.edu.niu.hardwarehelper.R;
import tw.edu.niu.hardwarehelper.models.Item;

public class ItemAdapter
        extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {

        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        Item item = itemList.get(position);

        // 名稱
        holder.textView.setText(item.name);

        // drawable 圖片
        int imageId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(
                        item.image,
                        "drawable",
                        holder.itemView.getContext().getPackageName()
                );

        // 找不到 drawable 時給預設圖
        if (imageId == 0) {

            imageId = R.drawable.ic_launcher_foreground;
        }

        // 使用者新增圖片
        if (item.imagePath != null
                && !item.imagePath.isEmpty()) {

            holder.imageView.setImageURI(
                    Uri.parse(item.imagePath)
            );

        } else {

            holder.imageView.setImageResource(imageId);
        }

        // 點擊卡片
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    EncyclopediaDetailActivity.class
            );

            // 傳完整資料
            intent.putExtra("name", item.name);

            intent.putExtra("title", item.title);

            intent.putExtra("desc", item.desc);

            intent.putExtra("application", item.application);

            intent.putExtra("voltage", item.voltage);

            intent.putExtra("packageName", item.packageName);

            intent.putExtra("pins", item.pins);

            intent.putExtra("image", item.image);

            intent.putExtra("imagePath", item.imagePath);

            intent.putExtra("pinImage", item.pinImage);

            intent.putExtra("pinConfig", item.pinConfig);

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imageView;

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);

            textView = itemView.findViewById(R.id.textView);
        }
    }
}