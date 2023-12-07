package com.example.photos.databse;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.photos.R;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class PreferenceDB {

    private static final String PREFS_NAME = "AlbumPrefs";
    private static final String ALBUMS_KEY = "albums";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public PreferenceDB(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveAlbums(List<Album> albums) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(albums);
        editor.putString(ALBUMS_KEY, json);
        editor.apply();
    }

    public List<Album> loadAlbums() {
        String json = sharedPreferences.getString(ALBUMS_KEY, null);
        if (json == null) {
            System.out.println("Album is null");
            // Add photos from the drawable folder
            Photo photo1 = new Photo(R.drawable.acura, null);
            Photo photo2 = new Photo(R.drawable.bugatti_mistral, null);
            Photo photo3 = new Photo(R.drawable.jaguar, null);
            Photo photo4 = new Photo(R.drawable.lamborghini, null);
            Photo photo5 = new Photo(R.drawable.lamborghini_huracan, null);
            Photo photo6 = new Photo(R.drawable.porsche, null);

            // Create an album with these photos
            Album album = new Album("Stock");
            album.addPhoto(photo1);
            album.addPhoto(photo2);
            album.addPhoto(photo3);
            album.addPhoto(photo4);
            album.addPhoto(photo5);

            List<Album> albums = new ArrayList<>();
            albums.add(album);

            saveAlbums(albums);

            return albums;
        }
        return gson.fromJson(json, new TypeToken<List<Album>>() {}.getType());
    }

    public void addAlbum(Album album) {
        List<Album> albums = loadAlbums();
        albums.add(album);
        saveAlbums(albums);
    }

}
