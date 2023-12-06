package com.example.photos.model;

import android.net.Uri;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Photo implements Serializable {
    private String filePath;
    private List<String> tags;

    private  Album  associatedAlbum;

    private transient Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Photo(Uri uri) {
        this.uri = uri;
    }

    public Album getAssociatedAlbum() {
        return associatedAlbum;
    }

    public void setAssociatedAlbum(Album associatedAlbum) {
        this.associatedAlbum = associatedAlbum;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    private int imageResourceId; // Add this line
    public Photo(String filePath, List<String> tags, int imageResourceId) {
        this.filePath = filePath;
        this.tags = tags;
        this.imageResourceId = imageResourceId; // Add this line
    }
    // Other existing methods...
    public int getImageResourceId() {
        return imageResourceId;
    }
    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
    public String getFilePath() {
        return filePath;
    }

    public boolean matchesSearch(String tagType, String tagValue) {
        for (String tag : tags) {
            // Check if the tag matches the search criteria
            if (tag.toLowerCase().startsWith(tagType.toLowerCase() + ":") &&
                    tag.toLowerCase().contains(tagValue.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // Serialize Uri as a String
        out.writeObject(uri != null ? uri.toString() : null);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Deserialize String back to Uri
        String uriString = (String) in.readObject();
        uri = (uriString != null) ? Uri.parse(uriString) : null;
    }

}
