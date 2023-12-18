package com.example.photos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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
import com.example.photos.model.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PhotoDetailsActivity extends AppCompatActivity {

    private EditText tagValueTxtFld;
    private Button addTagBtn, removeTag;
    private Album parentAlbum, destiantionAlbum;
    private int selectedPhotoIndex;
    private Photo selectedPhoto;
    private List<Album> albums;
    private ArrayAdapter<Tag> tags;
    private Spinner tagSpinner;
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
        tagValueTxtFld = findViewById(R.id.edittext_enter_tag_value);
        addTagBtn = findViewById(R.id.add_tag_button);
        removeTag = findViewById(R.id.remove_tag_button);

        // init move button
        Button moveButton = findViewById(R.id.move_button);
        moveButton.setOnClickListener(view -> {
            if (destiantionAlbum != null) {
                Photo remove = albums.get(albums.indexOf(parentAlbum)).remove(selectedPhoto);
                if (remove != null)
                    albums.get(albums.indexOf(destiantionAlbum)).addPhoto(selectedPhoto);
                db.saveAlbums(albums);

                Toast.makeText(getApplicationContext(),
                        "Moved Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Problem with moved!", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * show received images
         */
        Intent intent = getIntent();
        if (intent != null) {

            // revise data form intent
            parentAlbum = (Album) intent.getSerializableExtra("ALBUM_KEY");
            selectedPhoto = (Photo) intent.getSerializableExtra("PHOTO_KEY");
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

        /**
         * handle move section
         */
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        /**
         * Handle Add Tags
         */
        final String[] tagType = {""};
        Spinner tagTypeSpinner = (Spinner) findViewById(R.id.tag_category_spinner);
        ArrayAdapter<CharSequence> tagTypes = ArrayAdapter.createFromResource(
                this,
                R.array.tag_category_array,
                R.layout.custom_spinner_item
        );
        tagTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagTypeSpinner.setAdapter(tagTypes);
        tagTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                tagType[0] = tagTypes.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        Log.e("Photo Tags: ", selectedPhoto.getTags().toString());

        /**
         * Handle add tag button
         */
        addTagBtn.setOnClickListener(view -> {
            if(!tagValueTxtFld.getText().toString().isEmpty()) {
                if (!tagType[0].isEmpty()) {
                    Tag tag = new Tag(tagType[0], tagValueTxtFld.getText().toString());

                    Set<Tag> tags1 = albums.get(albums.indexOf(parentAlbum))
                            .getPhotos()
                            .get(selectedPhotoIndex).getTags();
                    for (Tag t:tags1){
                        if (tag.getKey().equalsIgnoreCase("Location")
                                && t.getKey().equalsIgnoreCase("Location")){
                            showToast("You can only add 1 location tag for a image.");
                            return;
                        }
                    }

                    albums.get(albums.indexOf(parentAlbum))
                            .getPhotos()
                            .get(selectedPhotoIndex)
                            .addTag(tag);

                    boolean b = selectedPhoto.addTag(new Tag(tagType[0], tagValueTxtFld.getText().toString()));
                    db.saveAlbums(albums);
                    tagValueTxtFld.setText("");
                    updateTagsChnageOnUI(); // update change
                    if (b)
                        showToast("Tag Added Successfully ");
                    else
                        showToast("Duplicate Tag");
                }
            }else {
                showToast("Enter tag value to add tag.");
            }
        });


        /**
         * Handle Add Tags
         */
        final String[] tag = {""};
        tagSpinner = (Spinner) findViewById(R.id.tag_spinner);
        tags = new ArrayAdapter<>(getApplicationContext(),
                R.layout.custom_spinner_item,
                new ArrayList<>(selectedPhoto.getTags())
        );
        tags.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tags);
        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                tag[0] = tags.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        /**
         * Handle add tag button
         */
        removeTag.setOnClickListener(view -> {
            Log.e("Selected Tags: ", tag[0]);

            if (!tag[0].isEmpty()) {
                albums.get(albums.indexOf(parentAlbum))
                        .getPhotos()
                        .get(selectedPhotoIndex)
                        .removeTag(tag[0]);

                db.saveAlbums(albums);
                selectedPhoto.removeTag(tag[0]);
                updateTagsChnageOnUI();
                tag[0] = "";
                showToast("Tag Removed Successfully ");
            }else {
                showToast("No tag available for remove.");
            }
        });
    }

    private void updateTagsChnageOnUI(){
        tags = new ArrayAdapter<>(getApplicationContext(),
                R.layout.custom_spinner_item,
                new ArrayList<>(selectedPhoto.getTags())
        );
        tags.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tags);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }
}
