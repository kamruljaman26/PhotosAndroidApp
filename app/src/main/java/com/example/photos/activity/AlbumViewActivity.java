package com.example.photos.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photos.adapter.PhotoAdapter;
import com.example.photos.R;
import com.example.photos.databse.PreferenceDB;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class AlbumViewActivity extends AppCompatActivity {

    private Album selectedAlbum;
    private PhotoAdapter adapter;
    private Button viewSlideShow, addPhoto;
    private static final int REQUEST_CODE_GALLERY = 1;

    private PreferenceDB db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        // database
        db = new PreferenceDB(getApplicationContext());

        // Set up the back button
        ImageView backButton = findViewById(R.id.backImageViewId);
        TextView backButtonText = findViewById(R.id.backTextViewId);
        backButton.setOnClickListener(view -> onBackPressed());
        backButtonText.setOnClickListener(view -> onBackPressed());

        // all button initializes
        viewSlideShow = findViewById(R.id.viewSlide_album_button);
        addPhoto = findViewById(R.id.add_photo_button);

        Intent intent = getIntent();
        if (intent != null) {
            selectedAlbum = (Album) intent.getSerializableExtra("album");
            // Check if selectedAlbum is not null and has photos
            if (selectedAlbum != null) {
                // Check if the selected album has photos
                if (selectedAlbum.getPhotos() != null && !selectedAlbum.getPhotos().isEmpty()) {
                    List<Photo> albumPhotos = selectedAlbum.getPhotos();
                    PhotoAdapter photoAdapter = new PhotoAdapter(this, albumPhotos, selectedAlbum);
                    RecyclerView photoRecyclerView = findViewById(R.id.photoRecyclerView);
                    photoRecyclerView.setAdapter(photoAdapter);
                    photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                } else {
                    // If the selected album doesn't have photos, create an empty adapter
                    PhotoAdapter photoAdapter = new PhotoAdapter(this, new ArrayList<>(), selectedAlbum);
                    RecyclerView photoRecyclerView = findViewById(R.id.photoRecyclerView);
                    photoRecyclerView.setAdapter(photoAdapter);
                    photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                }
            }
        }
        viewSlideShow.setOnClickListener(view -> {
            Intent intent1 = new Intent(AlbumViewActivity.this, ViewSlideShow.class);
            intent1.putExtra("ALBUM_KEY", selectedAlbum);
            startActivity(intent1);
        });
        addPhoto.setOnClickListener(view -> {
            // here I want to add photo from device gallery photo to selected album
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            // User has selected a photo from the gallery
            Uri selectedImageUri = data.getData();
            // Check if selectedAlbum is not null
            if (selectedAlbum != null) {
                // Create a new photo from the URI
                Photo newPhoto = new Photo(selectedImageUri);
                if (newPhoto != null) {
                    // Add the photo to the selected album
                    selectedAlbum.addPhoto(newPhoto);
                    // Update the adapter with the new photo
                    int position = selectedAlbum.getPhotos().indexOf(newPhoto);
                    // Check if the adapter is already initialized
                    if (adapter == null) {
                        // Initialize the adapter with the dataset
                        adapter = new PhotoAdapter(this, selectedAlbum.getPhotos(), selectedAlbum);
                        // Set the adapter to your RecyclerView
                        RecyclerView photoRecyclerView = findViewById(R.id.photoRecyclerView);
                        photoRecyclerView.setAdapter(adapter);
                    } else {
                        // Adapter is already initialized, just notify the dataset changed
                        adapter.notifyItemInserted(position);
                    }

                    db.addPhoto(selectedAlbum, newPhoto);

                    // todo:fix
                    // Update the database with the new photo
//                    PhotoManager.addPhoto(selectedAlbum,newPhoto);
                    // Update selectedAlbum to reflect the changes
//                    selectedAlbum = findAlbumByName(selectedAlbum.getName());


                    setResult(RESULT_OK);
                } else {
                    // Handle the case when creating a Photo object fails
                    Toast.makeText(this, "Error: Unable to create a Photo object", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
