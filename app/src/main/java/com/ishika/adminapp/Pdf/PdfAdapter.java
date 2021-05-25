package com.ishika.adminapp.Pdf;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ishika.adminapp.R;

import java.util.ArrayList;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.viewHolder> {
    private final Context context;
    private List<PdfData> list;

    public PdfAdapter(Context context, List<PdfData> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_item_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PdfData pdfData = list.get(position);
        holder.pName.setText(pdfData.getTitle());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PdfViewerActivity.class);
            intent.putExtra("pdfUrl", pdfData.getFile());
            context.startActivity(intent);
        });
        holder.download.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(pdfData.getFile()));
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public void FilteredList(ArrayList<PdfData> filterList) {
        list = filterList;
        notifyDataSetChanged();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        private final TextView pName;
        private final ImageView download;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            pName = itemView.findViewById(R.id.pName);
            download = itemView.findViewById(R.id.download);
        }
    }
}
