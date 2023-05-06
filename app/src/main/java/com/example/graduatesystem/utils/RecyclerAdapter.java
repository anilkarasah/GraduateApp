package com.example.graduatesystem.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduatesystem.R;
import com.example.graduatesystem.entities.Model;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Model> dataSet;
    Context mContext;
    int totalTypes;

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView cardView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textView = itemView.findViewById(R.id.textTypeView);
            this.cardView = itemView.findViewById(R.id.textTypeCard);
        }
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView textView;
        ImageView imageView;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.announcementTypeLayout);
            textView = itemView.findViewById(R.id.announcementTypeText);
            imageView = itemView.findViewById(R.id.announcementTypeImage);
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ImageView imageView;
        TextView textView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.userTypeLayout);
            imageView = itemView.findViewById(R.id.userTypeImage);
            textView = itemView.findViewById(R.id.userTypeText);
        }
    }

    public RecyclerAdapter(ArrayList<Model> data, Context context) {
        this.dataSet = data;
        this.mContext = context;

        totalTypes = data.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case Model.TYPE_POST:
                view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.holder_post, parent, false);
                return new PostViewHolder(view);
            case Model.TYPE_ANNOUNCEMENT:
                view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.holder_announcement, parent, false);
                return new AnnouncementViewHolder(view);
            case Model.TYPE_USER:
                view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.holder_user, parent, false);
                return new UserViewHolder(view);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        switch (dataSet.get(position).type) {
            case 0:
                return Model.TYPE_POST;
            case 1:
                return Model.TYPE_ANNOUNCEMENT;
            case 2:
                return Model.TYPE_USER;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Model model = dataSet.get(position);

        if (model == null) return;

        switch (model.type) {
            case Model.TYPE_POST:
                PostViewHolder post = (PostViewHolder) holder;
                post.textView.setText(model.text);
                break;
            case Model.TYPE_ANNOUNCEMENT:
                AnnouncementViewHolder announcement = (AnnouncementViewHolder) holder;
                announcement.textView.setText(model.text);
                announcement.imageView.setImageResource(model.data);
                break;
            case Model.TYPE_USER:
                UserViewHolder user = (UserViewHolder) holder;
                user.textView.setText(model.text);
                user.imageView.setImageResource(model.data);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
