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

    /*
    public static List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
        List<Photo> album1Photos = new ArrayList<>();
        album1Photos.add(new Photo("path/to/photo1.jpg", new ArrayList<>(), R.drawable.art));
        album1Photos.add(new Photo("path/to/photo2.jpg", new ArrayList<>(), R.drawable.beautifu_women));
        Album album1 = new Album("Vacation 2022", album1Photos);
        albums.add(album1);
        // Add more albums as needed
        return albums;
    }
     */
    // Initialize our photo database with some sample data
    static {
        // Add photos from the drawable folder
        Photo photo1 = new Photo("acura.jpg", new ArrayList<>(), R.drawable.acura);
        Photo photo2 = new Photo("bugatti_mistral.jpg", new ArrayList<>(), R.drawable.bugatti_mistral);
        Photo photo3 = new Photo("jaguar.jpg", new ArrayList<>(), R.drawable.jaguar);
        Photo photo4 = new Photo("lamborghini.jpg", new ArrayList<>(), R.drawable.lamborghini);
        Photo photo5 = new Photo("lamborghini_huracan.jpg", new ArrayList<>(), R.drawable.lamborghini_huracan);
        Photo photo6 = new Photo("porsche.jpg", new ArrayList<>(), R.drawable.porsche);
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
    public static List<Photo> getAlbumPhotos(String albumName) {
        // Retrieve the list of photos for the specified album name
        return albumPhotosMap.get(albumName);
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

