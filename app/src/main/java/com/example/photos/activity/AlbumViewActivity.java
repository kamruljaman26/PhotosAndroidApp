package com.example.photos.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photos.adapter.PhotoViewAdapter;
import com.example.photos.R;
import com.example.photos.databse.PreferenceDB;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AlbumViewActivity extends AppCompatActivity {

    RecyclerView photoRecyclerView;
    PhotoViewAdapter photoViewAdapter;
    private Album selectedAlbum;
    private PhotoViewAdapter adapter;
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

        // initializes
        viewSlideShow = findViewById(R.id.viewSlide_album_button);
        photoRecyclerView = findViewById(R.id.photoRecyclerView);
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addPhoto = findViewById(R.id.add_photo_button);

        // receive data
        Intent intent = getIntent();
        if (intent != null) {
            selectedAlbum = (Album) intent.getSerializableExtra("album");
            if (selectedAlbum != null) {
                // Check if the selected album has photos
                if (selectedAlbum.getPhotos() != null && !selectedAlbum.getPhotos().isEmpty()) {
                    photoViewAdapter = new PhotoViewAdapter(this, selectedAlbum);
                    photoRecyclerView.setAdapter(photoViewAdapter);
                } else {
                    // If the selected album doesn't have photos, create an empty adapter
                    photoViewAdapter = new PhotoViewAdapter(this, selectedAlbum);
                    photoRecyclerView.setAdapter(photoViewAdapter);
                }
            }
        }

        viewSlideShow.setOnClickListener(view -> {
            Intent intent1 = new Intent(AlbumViewActivity.this, ImageSlideActivity.class);
            intent1.putExtra("ALBUM_KEY", selectedAlbum);
            startActivity(intent1);
        });

        addPhoto.setOnClickListener(view -> {
            // here I want to add photo from device gallery photo to selected album
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
        });
    }


    private String saveImageToInternalStorage(Uri uri, Context context) {
        String fileName = "myImage_" + System.currentTimeMillis() + ".jpg"; // or use appropriate format
        File file = new File(context.getFilesDir(), fileName);

        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onResume() {
        List<Album> albums = db.loadAlbums();
        Album album = albums.get(albums.indexOf(selectedAlbum));
        // If the selected album doesn't have photos, create an empty adapter
        photoViewAdapter = new PhotoViewAdapter(this, album);
        photoRecyclerView.setAdapter(photoViewAdapter);
        photoViewAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            // User has selected a photo from the gallery
            Uri selectedImageUri = data.getData();

            // copy and store to image to local storage
            String internalUri = saveImageToInternalStorage(selectedImageUri, this);

            // Create a new photo from the URI
            Photo newPhoto = new Photo(internalUri);

            // Add the photo to the selected album
            selectedAlbum.addPhoto(newPhoto);

            // Update the adapter with the new photo
            int position = selectedAlbum.getPhotos().indexOf(newPhoto);

            // Adapter is already initialized, just notify the dataset changed
            adapter.notifyItemInserted(position);

            db.addPhoto(selectedAlbum, newPhoto);
            setResult(RESULT_OK);
        }
    }
}
