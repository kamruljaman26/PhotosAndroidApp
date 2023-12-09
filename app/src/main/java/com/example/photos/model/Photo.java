package com.example.photos.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Photos Pojo
public class Photo implements Serializable {
    private List<String> tags;
    private int imageResourceId;
    private String uri;

    public Photo(int imageResourceId) {
        this.imageResourceId = imageResourceId;
        tags = new ArrayList<>();
    }

    public Photo(Uri imageUri) {
        this.uri = imageUri.toString();
        tags = new ArrayList<>();
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public Uri getUri() {
        if (uri == null) return null;
        return Uri.parse(uri);
    }

    public void setUri(Uri uri) {
        this.uri = uri.toString();
    }

    @Override
    public String toString() {
        return "Photo{" +
                ", tags=" + tags +
                ", uri=" + uri +
                ", imageResourceId=" + imageResourceId +
                '}';
    }
}
