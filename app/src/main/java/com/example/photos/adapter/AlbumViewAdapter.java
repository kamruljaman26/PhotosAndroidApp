package com.example.photos.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.photos.R;
import com.example.photos.activity.AlbumViewActivity;
import com.example.photos.databse.PreferenceDB;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.io.File;
import java.util.List;

public class AlbumViewAdapter extends RecyclerView.Adapter<AlbumViewAdapter.MyRecyclerViewHolder> {

    private Context context;
    private List<Album> albums;
    private PreferenceDB db;

    // View holder for
    public static class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView albumName;
        private ImageView firstImage;
        private CardView albumCardView;
        ImageButton dropdownButton;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.album_name_textView);
            firstImage = itemView.findViewById(R.id.album_first_images);
            albumCardView = itemView.findViewById(R.id.albumCardViewId);
            dropdownButton = itemView.findViewById(R.id.button_dropdown);
        }
    }

    public AlbumViewAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
        db = new PreferenceDB(context);
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.album_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        Album album = albums.get(position);
        holder.albumName.setText(album.getName());

        // handle delete and remove
        holder.dropdownButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.dropdownButton);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menu_delete: {
                        // Handle action_item1 click
                        Toast.makeText(context, "Item 1 clicked", Toast.LENGTH_SHORT).show();

                        // Use an AlertDialog to confirm album deletion
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete Album");
                        builder.setMessage("Are you sure you want to delete this album?");
                        builder.setPositiveButton("Yes", (dialog, which) -> {
                            // Delete the selected album
                            db.deleteAlbum(album);
                            albums.remove(position);

                            notifyItemRemoved(position);
                        });

                        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                        builder.show();
                        Toast.makeText(context, "Album deleted", Toast.LENGTH_SHORT).show();

                        return true;
                    }

                    case R.id.menu_rename: {
                        // Use an AlertDialog to get user input for the new album name
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Rename Album");
                        final EditText input = new EditText(context);
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            String newAlbumName = input.getText().toString().trim();
                            if (!newAlbumName.isEmpty()) {
                                // Delete the selected album
                                db.renameAlbum(album, input.getText().toString());
                                album.setName(input.getText().toString());
                                notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                        builder.show();
                        return true;
                    }
                    default:
                        return false;
                }
            });
            popupMenu.show();
        });


        // Example: Assuming album.getFirstPhoto() returns the first photo in the album
        if (!album.getPhotos().isEmpty()) {
            Photo photo = album.getPhotos().get(0);
            if (photo.getUri() == null)
                holder.firstImage.setImageResource(photo.getImageResourceId());
            else {
                // load URI image
                File imgFile = new File(photo.getUri());
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    holder.firstImage.setImageBitmap(bitmap);
                }
            }
        }

        // open album in another activity
        holder.albumCardView.setOnClickListener(view -> {
            Album selectedAlbum = albums.get(position);
            Intent intent = new Intent(context, AlbumViewActivity.class);
            intent.putExtra("album", selectedAlbum);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

}
