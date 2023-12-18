package com.example.photos.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.photos.adapter.AlbumViewAdapter;
import com.example.photos.R;
import com.example.photos.databse.PreferenceDB;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;
import com.example.photos.model.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Button createButton, searchButton;
    private RecyclerView recyclerView;
    private AutoCompleteTextView autoCompleteTxtView;
    private AlbumViewAdapter adapter;

    private PreferenceDB db;
    private List<Album> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // manage permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                111);

        // load albums from db
        db = new PreferenceDB(getApplicationContext());
        albums = db.loadAlbums();

        // init variables
        createButton = findViewById(R.id.create_album_button);
        searchButton = findViewById(R.id.search_button_b);
        recyclerView = findViewById(R.id.recyclerViewId);
        adapter = new AlbumViewAdapter(this, albums);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        autoCompleteTxtView = findViewById(R.id.searchEditTextViewId);

        /**
         * Init Auto complete text view
         */
        updateTagsSearchData();

        /*
         * Handling creating and saving new albums
         */
        createButton.setOnClickListener(view -> {
            handleCrateButton();
        });


        /**
         * Handle Search Button
         */
        searchButton.setOnClickListener(view -> {
            handleSearchButton();
        });

    }

    // update tags suggestion in search
    private void updateTagsSearchData() {
        List<String> allTags = getAllTags();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, allTags);
        autoCompleteTxtView.setAdapter(adapter);
        autoCompleteTxtView.setThreshold(1); // Start showing suggestions after 1 character is typed
    }

    // fetch all tags as a list from all albums
    private List<String> getAllTags() {
        Set<String> tags = new HashSet<>();
        for (Album album : albums) {
            for (Photo photo : album.getPhotos()) {
                for (Tag tag : photo.getTags()) {
                    tags.add(tag.getValue());
                }
            }
        }
        return new ArrayList<>(tags);
    }

    /**
     * Handle Search Logic
     * Conjunction and Disjunction in Searches: The system should support both conjunction (AND)
     * and disjunction (OR) in search queries. This allows users to refine their searches by
     * combining multiple conditions. For example:
     *
     * Conjunction: Finding photos tagged with both "John" (person) and "Paris" (location).
     * Disjunction: Finding photos tagged with either "Mary" (person) or "London" (location).
     */
    private void handleSearchButton() {
        String searchTerm = autoCompleteTxtView.getText().toString().trim().toLowerCase();
        if (!searchTerm.isEmpty()) {
            List<Photo> searchResults = new ArrayList<>();

            // Split the searchTerm based on 'and' or 'or'
            String[] keywords = searchTerm.split("(and|or)");
            boolean isConjunction = searchTerm.contains("and");

            for (Album album : albums) {
                for (Photo photo : album.getPhotos()) {
                    boolean firstMatch = false;
                    boolean secondMatch = false;

                    for (Tag tag : photo.getTags()) {
                        if (tag.getValue().equalsIgnoreCase(keywords[0].trim())) {
                            firstMatch = true;
                        }
                        if (keywords.length > 1 && tag.getValue().equalsIgnoreCase(keywords[1].trim())) {
                            secondMatch = true;
                        }
                    }

                    // Add photo to results based on conjunction or disjunction
                    if ((isConjunction && firstMatch && secondMatch) ||
                            (!isConjunction && (firstMatch || secondMatch))) {
                        searchResults.add(photo);
                    }
                }
            }

            if (searchResults.isEmpty()) {
                Toast.makeText(MainActivity.this, "No photo found with tag " + searchTerm, Toast.LENGTH_SHORT).show();
            } else {
                // Create an Intent to start the SearchResultsActivity
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("searchResults", (Serializable) searchResults);
                startActivity(intent);

                autoCompleteTxtView.setText("");
            }
        } else {
            Toast.makeText(MainActivity.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 111: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted
                } else {
                    // Permission denied
                }
            }
        }
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

    @Override
    protected void onResume() {
        albums = db.loadAlbums();
        adapter = new AlbumViewAdapter(this, albums);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateTagsSearchData();
        super.onResume();
    }
}


