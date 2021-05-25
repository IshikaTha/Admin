package com.ishika.adminapp.Notice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ishika.adminapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.viewHolder>{
    private final Context context;
    private final ArrayList<NoticeData> list;

    public NoticeAdapter(Context context, ArrayList<NoticeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_item_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        NoticeData noticeData = list.get(position);
        holder.noticeText.setText(noticeData.getTitle());
        holder.nDate.setText(noticeData.getDate());
        holder.nTime.setText(noticeData.getTime());
        try {
            if(noticeData.getImage() != null)
                Picasso.get().load(noticeData.getImage()).into(holder.notPre);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.updateNotice.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateNoticeActivity.class);
            intent.putExtra("image", noticeData.getImage());
            intent.putExtra("title", noticeData.getTitle());
            intent.putExtra("key", noticeData.getKey());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        private final Button updateNotice;
        private final TextView noticeText;
        private final TextView nTime;
        private final TextView nDate;
        private final ImageView notPre;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            updateNotice = itemView.findViewById(R.id.updateNotice);
            noticeText = itemView.findViewById(R.id.noticeText);
            notPre = itemView.findViewById(R.id.notPre);
            nDate = itemView.findViewById(R.id.nDate);
            nTime = itemView.findViewById(R.id.nTime);
        }
    }
}
