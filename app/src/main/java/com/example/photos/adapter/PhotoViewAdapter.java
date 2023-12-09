package com.example.photos.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.photos.R;
import com.example.photos.activity.PhotoDetailsActivity;
import com.example.photos.databse.PreferenceDB;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;
import java.io.File;

public class PhotoViewAdapter extends RecyclerView.Adapter<PhotoViewAdapter.PhotoViewHolder> {

    private Context context;
    private Album album;
    private PreferenceDB db;

    public PhotoViewAdapter(Context context, Album album) {
        this.context = context;
        this.album = album;
        db = new PreferenceDB(context);
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = album.getPhotos().get(position);

        if (photo.getUri() != null) {
            // load URI image
            File imgFile = new  File(photo.getUri());
            if(imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.photoImageView.setImageBitmap(bitmap);
            }
        } else if (photo.getImageResourceId() != 0) {
            // Load image from drawable resource
            holder.photoImageView.setImageResource(photo.getImageResourceId());
        }

        // handle delete and remove
        holder.dropdownButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.dropdownButton);
            popupMenu.getMenuInflater().inflate(R.menu.popup_remove_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menu_delete: {
                        // Use an AlertDialog to confirm album deletion
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete Photo");
                        builder.setMessage("Are you sure you want to delete the photo?");
                        builder.setPositiveButton("Yes", (dialog, which) -> {
                            // Delete the selected album
                            db.removePhoto(album, album.getPhotos().get(position));
                            album.getPhotos().remove(position);
                            notifyItemRemoved(position);
                        });

                        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                        builder.show();
                        Toast.makeText(context, "Photo deleted", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                    default:
                        return false;
                }
            });
            popupMenu.show();
        });

        // move to details activity
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
                intent.putExtra("PHOTO_INDEX", album.getPhotos().indexOf(photo));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return album.getPhotos().size();
    }
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        ImageButton dropdownButton;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
            dropdownButton = itemView.findViewById(R.id.button_dropdown);
        }
    }
}

