package com.example.photos.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.photos.adapter.AlbumsViewAdapter;
import com.example.photos.R;
import com.example.photos.databse.PreferenceDB;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button createButton;
    private Button rename;
    private Button delete;
    private Button search;
    private RecyclerView recyclerView;
    private ImageView searchButton;
    private EditText getSearchValue;
    private AlbumsViewAdapter adapter;

    private PreferenceDB db;
    private List<Album> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide action bar

        // load albums from db
        db = new PreferenceDB(getApplicationContext());
        albums = db.loadAlbums();

        // init variables
        createButton = findViewById(R.id.create_album_button);
        rename = findViewById(R.id.rename_button);
        delete = findViewById(R.id.delete_button);
        searchButton = findViewById(R.id.search_image_button_id);
        getSearchValue = findViewById(R.id.searchEditTextViewId);
        search = findViewById(R.id.search_button_b);
        recyclerView = findViewById(R.id.recyclerViewId);
        adapter = new AlbumsViewAdapter(this, albums);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
         * Handling creating and saving new albums
         */
        createButton.setOnClickListener(view -> {
            handleCrateButton();
        });


        /**
         * todo: later
         */
        // rename
        rename.setOnClickListener(view -> {
            int selectedAlbumIndex = adapter.getSelectedAlbumIndex();
            if (selectedAlbumIndex != RecyclerView.NO_POSITION) {
                // Use an AlertDialog to get user input for the new album name
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Rename Album");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", (dialog, which) -> {
                    String newAlbumName = input.getText().toString().trim();
                    if (!newAlbumName.isEmpty()) {
                        // Rename the selected album
                        adapter.renameSelectedAlbum(newAlbumName);
                    }
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                builder.show();
            } else {
                Toast.makeText(this, "No album selected", Toast.LENGTH_SHORT).show();
            }
        });

        // delete
        delete.setOnClickListener(view -> {
            int selectedAlbumIndex = adapter.getSelectedAlbumIndex();
            if (selectedAlbumIndex != RecyclerView.NO_POSITION) {
                // Use an AlertDialog to confirm album deletion
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Album");
                builder.setMessage("Are you sure you want to delete this album?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // Delete the selected album
                    adapter.deleteSelectedAlbum();
                    Toast.makeText(this, "Album deleted", Toast.LENGTH_SHORT).show();
                });

                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                builder.show();
            } else {
                Toast.makeText(this, "No album selected", Toast.LENGTH_SHORT).show();
            }
        });

        //Searches apply to photos across all albums, not just to the album that may be open.
        search.setOnClickListener(view -> {
            String searchTerm = getSearchValue.getText().toString().trim().toLowerCase();
            if (!searchTerm.isEmpty()) {
                List<Photo> searchResults = new ArrayList<>();
                //todo: fix search
                // Iterate through all photos and check if the tags match the search term
//                List<Photo> allPhotos = PhotoManager.getAllPhotos();
//                for (Photo photo : allPhotos) {
//                    for (String tag : photo.getTags()) {
//                        if (tag.toLowerCase().contains(searchTerm)) {
//                            searchResults.add(photo);
//                            break;  // Break out of inner loop once a match is found for the current photo
//                        }
//                    }
//                }
                // TODO: Display or handle the search results as needed
                // i want to open a new activity  with the search results
                // Create an Intent to start the SearchResultsActivity
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass the search results to the new activity
                intent.putExtra("searchResults", (Serializable) searchResults);
                // Start the new activity
                startActivity(intent);
                // Start the new activity
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Search completed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });

        // search button
        searchButton.setOnClickListener(view -> {
            String searchTerm = getSearchValue.getText().toString().trim().toLowerCase();
            if (!searchTerm.isEmpty()) {
                List<Photo> searchResults = new ArrayList<>();
                // Iterate through all photos and check if the tags match the search term
                //todo: fix search
/*                List<Photo> allPhotos = PhotoManager.getAllPhotos();
                for (Photo photo : allPhotos) {
                    for (String tag : photo.getTags()) {
                        if (tag.toLowerCase().contains(searchTerm)) {
                            searchResults.add(photo);
                            break;  // Break out of inner loop once a match is found for the current photo
                        }
                    }
                }*/
                // TODO: Display or handle the search results as needed
                // i want to open a new activity  with the search results
                // Create an Intent to start the SearchResultsActivity
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass the search results to the new activity
                intent.putExtra("searchResults", (Serializable) searchResults);
                // Start the new activity
                startActivity(intent);
                // Start the new activity
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Search completed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // handle create albums
    private void handleCrateButton() {
        // Use an AlertDialog to get user input for the new album name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Album");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String albumName = input.getText().toString().trim();
            if (!albumName.isEmpty()) {
                // Create a new album with the entered name and an empty list of photos
                Album newAlbum = new Album(albumName);

                // save in db
                albums.add(newAlbum);
                db.addAlbum(newAlbum);

                // Notify the adapter about the specific insertion
                int position = albums.indexOf(newAlbum);
                adapter.notifyItemInserted(position);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}


