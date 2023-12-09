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

    // save full album
    public void saveAlbums(List<Album> albums) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(albums);
        editor.putString(ALBUMS_KEY, json);
        editor.apply();
    }

    // load album
    public List<Album> loadAlbums() {
        String json = sharedPreferences.getString(ALBUMS_KEY, null);
        if (json == null) {
            System.out.println("Album is null");
            // Add photos from the drawable folder
            Photo photo1 = new Photo(R.drawable.acura);
            Photo photo2 = new Photo(R.drawable.bugatti_mistral);
            Photo photo3 = new Photo(R.drawable.jaguar);
            Photo photo4 = new Photo(R.drawable.lamborghini);
            Photo photo5 = new Photo(R.drawable.lamborghini_huracan);
            Photo photo6 = new Photo(R.drawable.porsche);

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

    // save new albums
    public void addAlbum(Album album) {
        List<Album> albums = loadAlbums();
        albums.add(album);
        saveAlbums(albums);
    }

    // save new albums
    public void addPhoto(Album album, Photo photo) {
        List<Album> albums = loadAlbums();
        albums.get(albums.indexOf(album)).addPhoto(photo);
        saveAlbums(albums);
    }

    // delete album
    public void deleteAlbum(Album album) {
        List<Album> albums = loadAlbums();
        albums.remove(album);
        saveAlbums(albums);
    }

    public void renameAlbum(Album album, String name) {
        List<Album> albums = loadAlbums();
        albums.get(albums.indexOf(album)).setName(name);
        saveAlbums(albums);
    }

    public void removePhoto(Album album, Photo photo) {
        List<Album> albums = loadAlbums();
        albums.get(albums.indexOf(album)).getPhotos().remove(album.getPhotos().indexOf(photo));
        saveAlbums(albums);
    }
}
