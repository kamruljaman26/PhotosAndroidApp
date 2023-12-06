package com.example.photos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photos.R;
import com.example.photos.activity.AlbumViewActivity;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyRecyclerViewHolder> {

    private int selectedPosition = RecyclerView.NO_POSITION;
    private GestureDetector gestureDetector;
    private RecyclerView recyclerView;
    private Context context;
    private List<Album> albums;

    // View holder for
    public static class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView albumName;
        private ImageView albumFirstImages;
        private CardView albumCardView;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.album_name_textView);
            albumFirstImages = itemView.findViewById(R.id.album_first_images);
            albumCardView = itemView.findViewById(R.id.albumCardViewId);

        }
    }

    public MyRecyclerViewAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                int position = getAdapterPositionFromView(e.getX(), e.getY());
                if (position != RecyclerView.NO_POSITION) {
                    Album selectedAlbum = albums.get(position);
                    Intent intent = new Intent(context, AlbumViewActivity.class);
                    intent.putExtra("album", selectedAlbum);
                    context.startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private int getAdapterPositionFromView(float x, float y) {
        View childView = recyclerView.findChildViewUnder(x, y);
        if (childView != null) {
            return recyclerView.getChildAdapterPosition(childView);
        }
        return RecyclerView.NO_POSITION;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public int getSelectedAlbumIndex() {
        return selectedPosition;
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

        // Example: Assuming album.getFirstPhoto() returns the first photo in the album
        Photo firstPhoto = album.getPhotos().get(0);
        holder.albumFirstImages.setImageResource(firstPhoto.getImageResourceId());
//
        // open album card view
        holder.albumCardView.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                selectedPosition = adapterPosition;  // Update selected position here
                Album selectedAlbum = albums.get(adapterPosition);
                Intent intent = new Intent(context, AlbumViewActivity.class);
                Toast.makeText(view.getContext(), "Clicked " + adapterPosition, Toast.LENGTH_SHORT).show();
                // Pass the selected album to AlbumView activity
                intent.putExtra("album", selectedAlbum);
                view.getContext().startActivity(intent);
            }
        });

        // Set up touch listener on the card view
        holder.albumCardView.setOnTouchListener((view, event) -> {
            if (gestureDetector.onTouchEvent(event)) {
                // Double-tap action
                return true; // to consume the event
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                // Single tap action
                view.performClick();
                return true; // to consume the event
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void renameSelectedAlbum(String newAlbumName) {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            Album selectedAlbum = albums.get(selectedPosition);
            selectedAlbum.setName(newAlbumName);
            notifyItemChanged(selectedPosition);
        }
    }

    public void deleteSelectedAlbum() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            albums.remove(selectedPosition);
            notifyItemRemoved(selectedPosition);
            selectedPosition = RecyclerView.NO_POSITION;
        }
    }
}
