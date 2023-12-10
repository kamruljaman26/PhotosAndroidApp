package com.example.photos.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photos.R;
import com.example.photos.activity.PhotoDetailsActivity;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.io.File;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Photo> photos;
    private Context context;

    public SearchAdapter(List<Photo> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.for_add_photos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = photos.get(position);
        if (photo.getUri() != null) {
            // load URI image
            File imgFile = new File(photo.getUri());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.photoImageView.setImageBitmap(bitmap);
            }
        } else if (photo.getImageResourceId() != 0) {
            holder.photoImageView.setImageResource(photo.getImageResourceId());
        }

        holder.photoImageView.setOnClickListener(view -> {
            // create a search album
            Album album = new Album("Search Album");
            for (Photo p : photos) {
                album.addPhoto(p);
            }

            // Pass the corresponding album to another activity
            Intent intent = new Intent(context, PhotoDetailsActivity.class);
            intent.putExtra("PHOTO_KEY", photo);
            intent.putExtra("ALBUM_KEY", album);
            intent.putExtra("PHOTO_INDEX", album.getPhotos().indexOf(photo));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView photoImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
        }
    }

}

