package com.example.photos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photos.R;
import com.example.photos.activity.PhotoDetailsActivity;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Context context;
    private List<Photo> photos;
    private Album album;

    public PhotoAdapter(Context context, List<Photo> photos ,Album album) {
        this.context = context;
        this.photos = photos;
        this.album = album;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);

        if (photo.getUri() != null) {
            // Load image from URI
            Glide.with(context)
                    .load(photo.getUri())
                    .override(600, 800)
                    .into(holder.photoImageView);
        } else if (photo.getImageResourceId() != 0) {
            // Load image from drawable resource
            holder.photoImageView.setImageResource(photo.getImageResourceId());
        }

        holder.photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass the corresponding album to another activity
                Intent intent = new Intent(context, PhotoDetailsActivity.class);
                // Pass the selected photo
                intent.putExtra("PHOTO_KEY", photo);
                // Pass the corresponding album
                intent.putExtra("ALBUM_KEY", album);
                // Pass the index of the selected photo in the album
                intent.putExtra("PHOTO_INDEX", photos.indexOf(photo));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
        }
    }
}

