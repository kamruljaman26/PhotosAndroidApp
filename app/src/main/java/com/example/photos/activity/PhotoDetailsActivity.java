package com.example.photos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photos.R;
import com.example.photos.databse.PreferenceDB;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoDetailsActivity extends AppCompatActivity {

    private TextView displayTagValue;
    private EditText getTagValue;
    private Button addTag, removeTag;

    private Album parentAlbum, destiantionAlbum;
    private int selectedPhotoIndex;
    private Photo photo;
    private List<Album> albums;
    private PreferenceDB db;
    private static final int ADD_TAG_REQUEST_CODE = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        // init db
        db = new PreferenceDB(this);
        albums = db.loadAlbums();

        // Set up the back button
        ImageView backButton = findViewById(R.id.backImageViewId);
        ImageView imageView = findViewById(R.id.imageViewID);
        TextView backButtonText = findViewById(R.id.backTextViewId);
        backButton.setOnClickListener(view -> onBackPressed());
        backButtonText.setOnClickListener(view -> onBackPressed());

        // Initialize views for tags
        displayTagValue = findViewById(R.id.textview_display_tag_details);
        getTagValue = findViewById(R.id.edittext_enter_tag_value);
        addTag = findViewById(R.id.add_tag_button);
        removeTag = findViewById(R.id.remove_tag_button);

        // init move button
        Button moveButton = findViewById(R.id.move_button);
        moveButton.setOnClickListener(view -> {
            if (destiantionAlbum != null) {
                Photo remove = albums.get(albums.indexOf(parentAlbum)).remove(photo);
                if (remove != null)
                    albums.get(albums.indexOf(destiantionAlbum)).addPhoto(photo);
                db.saveAlbums(albums);

                Toast.makeText(getApplicationContext(),
                        "Moved Successfully!",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),
                        "Problem with moved!",Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * show received images
         */
        Intent intent = getIntent();
        if (intent != null) {

            // revise data form intent
            parentAlbum = (Album) intent.getSerializableExtra("ALBUM_KEY");
            photo = (Photo) intent.getSerializableExtra("PHOTO_KEY");
            selectedPhotoIndex = intent.getIntExtra("PHOTO_INDEX", -1);

            // load photo
            if (parentAlbum != null && selectedPhotoIndex != -1) {
                // set image
                Photo photo = parentAlbum.getPhotos().get(selectedPhotoIndex);
                if (photo.getUri() == null)
                    imageView.setImageResource(photo.getImageResourceId());
                else {
                    // load URI image
                    File imgFile = new File(photo.getUri());
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }

        // for select_album_spinner
        Spinner selectAlbumSpinner = findViewById(R.id.select_album_spinner);
        List<String> albumNames = new ArrayList<>();
        for (Album album : albums) {
            albumNames.add(album.getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item, albumNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectAlbumSpinner.setAdapter(spinnerAdapter);
        selectAlbumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                destiantionAlbum = albums.get(position);
//                Toast.makeText(getApplicationContext(), ""+destiantionAlbum,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });


        Spinner tagSpinner = (Spinner) findViewById(R.id.tag_category_spinner);
        ArrayAdapter<CharSequence> adapterTag = ArrayAdapter.createFromResource(
                this,
                R.array.tag_category_array,
                android.R.layout.simple_spinner_item
        );
        // Tag Section
//        parentAlbum = findAlbumByName(selectedAlbumName);
        if (parentAlbum != null && selectedPhotoIndex != -1) {
            // Get the selected photo
            List<Photo> albumPhotos = parentAlbum.getPhotos();
            if (selectedPhotoIndex >= 0 && selectedPhotoIndex < albumPhotos.size()) {
                Photo selectedPhoto = albumPhotos.get(selectedPhotoIndex);
                // Display all tags for the selected photo in the displayTagValue TextView
//                displayTagValue.setText(formatTags(selectedPhoto.getTags()));
            }
        }

        // Specify the layout to use when the list of choices appears.
        adapterTag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        tagSpinner.setAdapter(adapterTag);
        addTag.setOnClickListener(view -> {
            // Get the selected album
//            parentAlbum = findAlbumByName(selectedAlbumName);
            if (parentAlbum != null && selectedPhotoIndex != -1) {
                // Get the selected photo
                List<Photo> albumPhotos = parentAlbum.getPhotos();
                if (selectedPhotoIndex >= 0 && selectedPhotoIndex < albumPhotos.size()) {
                    Photo selectedPhoto = albumPhotos.get(selectedPhotoIndex);
                    // Get the selected tag category and tag value
                    String selectedTagCategory = tagSpinner.getSelectedItem().toString();
                    String tagValue = getTagValue.getText().toString();
                    // Add the tag to the selected photo
//                    selectedPhoto.getTags().add(selectedTagCategory + ": " + tagValue);
                    // Update the PhotoManager with the changes
                    // todo update operation
//                    PhotoManager.updatePhoto(selectedAlbumName, selectedPhotoIndex, selectedPhoto);
                    // Display all tags for the selected photo in the displayTagValue TextView
//                    displayTagValue.setText(formatTags(selectedPhoto.getTags()));
                    getTagValue.setText("");
                    showToast("Tag Added Successfully ");
                }
            }
        });

        // Remove Tag button
        removeTag.setOnClickListener(view -> {
            // Get the selected album
//            parentAlbum = findAlbumByName(selectedAlbumName);
            if (parentAlbum != null && selectedPhotoIndex != -1) {
                // Get the selected photo
                List<Photo> albumPhotos = parentAlbum.getPhotos();
                if (selectedPhotoIndex >= 0 && selectedPhotoIndex < albumPhotos.size()) {
                    Photo selectedPhoto = albumPhotos.get(selectedPhotoIndex);
                    // Get the tag value to be removed
                    String tagValueToRemove = getTagValue.getText().toString();
                    // Remove the tag from the selected photo
                    removeTagFromPhoto(selectedPhoto, tagValueToRemove);
                    // Update the PhotoManager with the changes
                    // todo update operation

//                    PhotoManager.updatePhoto(selectedAlbumName, selectedPhotoIndex, selectedPhoto);
                    // Display all tags for the selected photo in the displayTagValue TextView
//                    displayTagValue.setText(formatTags(selectedPhoto.getTags()));
                    showToast("Tag Removed Successfully ");
                }
            }
        });
    }

    private String formatTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "No tags";
        }
        StringBuilder formattedTags = new StringBuilder();
        for (String tag : tags) {
            formattedTags.append(tag).append("\n");
        }
        return formattedTags.toString().trim();
    }


    // Method to delete the photo
    private void deletePhoto(String albumName, int photoIndex) {
        // Remove the photo from its selected album
        Album selectedAlbum = findAlbumByName(albumName);
        if (selectedAlbum != null) {
            List<Photo> albumPhotos = selectedAlbum.getPhotos();
            if (photoIndex >= 0 && photoIndex < albumPhotos.size()) {
                // Remove the photo from the selected album
                albumPhotos.remove(photoIndex);
            }
        }
    }


    // Helper method to find an album by name
    private Album findAlbumByName(String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDisplayedTags();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TAG_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Update displayed tags when returning from another activity
                updateDisplayedTags();
                showToast("Tags added successfully");
            } else {
                showToast("Adding tags canceled or failed");
            }
        }
    }

    private void updateDisplayedTags() {
        if (parentAlbum != null && selectedPhotoIndex != -1) {
            List<Photo> albumPhotos = parentAlbum.getPhotos();
            if (selectedPhotoIndex >= 0 && selectedPhotoIndex < albumPhotos.size()) {
                Photo selectedPhoto = albumPhotos.get(selectedPhotoIndex);
//                displayTagValue.setText(formatTags(selectedPhoto.getTags()));
                showToast("Tags updated successfully");
            }
        }
    }

    private void removeTagFromPhoto(Photo selectedPhoto, String tagValueToRemove) {
//        List<String> photoTags = selectedPhoto.getTags();
//        for (String tag : photoTags) {
//            if (tag.contains(tagValueToRemove)) {
//                // Remove the tag from the list
//                photoTags.remove(tag);
//                break; // Break the loop after removing the first occurrence
//            }
//        }
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }
}
