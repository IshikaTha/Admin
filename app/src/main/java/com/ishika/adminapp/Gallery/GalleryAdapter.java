package com.ishika.adminapp.Gallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ishika.adminapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.viewHolder> {
    private final Context context;
    private final ArrayList<GalleryData> list;

    public GalleryAdapter(Context context, ArrayList<GalleryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        GalleryData galleryData = list.get(position);
        try {
            Picasso.get().load(galleryData.getImage()).into(holder.galPre);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.galPre.setOnClickListener(v -> {
            Intent intent = new Intent(context, OpenPhotoActivity.class);
            intent.putExtra("photo", galleryData.getImage());
            intent.putExtra("event", galleryData.getTitle());
            intent.putExtra("key", galleryData.getKey());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        private final ImageView galPre;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            galPre = itemView.findViewById(R.id.galPre);
        }
    }
}
