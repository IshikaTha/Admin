package com.ishika.adminapp.Faculty;

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

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.viewHolder> {
    private final List<TeacherData> list;
    private final Context context;
    private final String category;

    public TeacherAdapter(List<TeacherData> list, Context context, String category) {
        this.list = list;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.faculty_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        TeacherData teacherData = list.get(position);
        holder.name.setText(teacherData.getName());
        holder.email.setText(teacherData.getEmail());
        holder.phone.setText(teacherData.getPhone());
        holder.post.setText(teacherData.getPost());

        try {
            Picasso.get().load(teacherData.getImage()).into(holder.image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.update.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateFacultyInfoActivity.class);
            intent.putExtra("image", teacherData.getImage());
            intent.putExtra("name", teacherData.getName());
            intent.putExtra("email", teacherData.getEmail());
            intent.putExtra("phone", teacherData.getPhone());
            intent.putExtra("post", teacherData.getPost());
            intent.putExtra("key", teacherData.getUniqueKey());
            intent.putExtra("category", category);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView email;
        private final TextView post;
        private final TextView phone;
        private final ImageView image;
        private final Button update;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.facultyName);
            email = itemView.findViewById(R.id.facultyMail);
            phone = itemView.findViewById(R.id.facultyPhone);
            post = itemView.findViewById(R.id.facultyPost);
            update = itemView.findViewById(R.id.facultyUpdate);
            image = itemView.findViewById(R.id.facultyImage);

            name.setSelected(true);
            email.setSelected(true);
            post.setSelected(true);
        }
    }
}
