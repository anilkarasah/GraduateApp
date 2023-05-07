package com.example.graduatesystem.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduatesystem.R;
import com.example.graduatesystem.entities.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Post> posts;
    Context mContext;

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        ImageView image_authorPicture;
        TextView text_authorFullName;
        TextView text_postedAt;
        TextView text_postMessage;
        TextView text_validUntil;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);

            this.image_authorPicture = itemView.findViewById(R.id.imagePostAuthor);
            this.text_authorFullName = itemView.findViewById(R.id.textAuthorFullName);
            this.text_postedAt = itemView.findViewById(R.id.textPostedAt);
            this.text_postMessage = itemView.findViewById(R.id.textPostMessage);
            this.text_validUntil = itemView.findViewById(R.id.textValidUntil);
        }
    }

    public PostAdapter(ArrayList<Post> data, Context context) {
        this.posts = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.holder_post_text, parent, false);

        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = posts.get(position);

        if (post == null) return;

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.ENGLISH);

        TextViewHolder viewHolder = (TextViewHolder) holder;
        viewHolder.text_postMessage.setText(post.getText());
        viewHolder.text_postedAt.setText(formatter.format(post.getPostedAt()));
        viewHolder.text_authorFullName.setText(post.getAuthorFullName());
        viewHolder.text_validUntil.setText(formatter.format(post.getValidUntil()));
        viewHolder.image_authorPicture.setImageBitmap(post.getAuthorProfilePicture());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
