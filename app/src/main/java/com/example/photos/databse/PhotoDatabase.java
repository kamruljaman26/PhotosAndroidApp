package com.example.photos.databse;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.photos.R;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoDatabase {

    private static List<Album> albums = new ArrayList<>();
    private static List<Photo> allPhotos = new ArrayList<>();
    private static Map<String, List<Photo>> albumPhotosMap = new HashMap<>();

    // Initialize our photo database with some sample data
    static {
        // Add photos from the drawable folder
        Photo photo1 = new Photo( R.drawable.acura, null);
        Photo photo2 = new Photo(R.drawable.bugatti_mistral, null);
        Photo photo3 = new Photo(R.drawable.jaguar, null);
        Photo photo4 = new Photo(R.drawable.lamborghini, null);
        Photo photo5 = new Photo(R.drawable.lamborghini_huracan, null);
        Photo photo6 = new Photo(R.drawable.porsche, null);
        allPhotos.add(photo1);
        allPhotos.add(photo2);
        allPhotos.add(photo3);
        allPhotos.add(photo4);
        allPhotos.add(photo5);
        allPhotos.add(photo6);
        // Create an album with these photos
        Album albumWithDrawablePhotos = new Album("Stock", allPhotos);
        albums.add(albumWithDrawablePhotos);
        // Add more albums as needed
    }
    public static List<Album> getAlbums() {
        return albums;
    }
    public static List<Photo> getAllPhotos() {
        return allPhotos;
    }
    public static void addPhoto(Photo photo) {
        allPhotos.add(photo);
    }
    public static void addAlbum(Album album) {
        albums.add(album);
    }
    public static void removeAlbum(Album album) {
        albums.remove(album);
    }

    public static void updatePhoto(String albumName, int photoIndex, Photo updatedPhoto) {
        Album album = findAlbumByName(albumName);
        if (album != null) {
            List<Photo> photos = album.getPhotos();
            if (photoIndex >= 0 && photoIndex < photos.size()) {
                photos.set(photoIndex, updatedPhoto);
            }
        }
    }
    public static Album findAlbumByName(String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null;
    }


    private static final String PREF_NAME = "PhotoDatabasePrefs";

    public static void saveDataToSharedPreferences(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        // Save albums
        Gson gson = new Gson();
        String albumsJson = gson.toJson(albums);
        editor.putString("albums", albumsJson);
        // Save photos
        String photosJson = gson.toJson(allPhotos);
        editor.putString("photos", photosJson);

        editor.apply();
    }

    public static void loadDataFromSharedPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Retrieve albums
        String albumsJson = prefs.getString("albums", null);
        if (albumsJson != null) {
            Gson gson = new Gson();
            Type albumListType = new TypeToken<ArrayList<Album>>() {}.getType();
            albums = gson.fromJson(albumsJson, albumListType);
        }
        // Retrieve photos
        String photosJson = prefs.getString("photos", null);
        if (photosJson != null) {
            Gson gson = new Gson();
            Type photoListType = new TypeToken<ArrayList<Photo>>() {}.getType();
            allPhotos = gson.fromJson(photosJson, photoListType);
        }
    }

    private static Album selectedAlbum;

    // Method to set the selected album
    public static void setSelectedAlbum(Album album) {
        selectedAlbum = album;
    }

    // Method to get the selected album
    public static Album getSelectedAlbum() {
        return selectedAlbum;
    }


}

