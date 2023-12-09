package com.example.photos.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Photos Pojo
public class Photo implements Serializable {
    private List<Tag> tags;
    private int imageResourceId;
    private String internalUri;

    public Photo(int imageResourceId) {
        this.imageResourceId = imageResourceId;
        tags = new ArrayList<>();
    }

    public Photo(String internalUri) {
        this.internalUri = internalUri;
        tags = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        if(getUri() != null && photo.getUri() != null && getUri().equals(photo.internalUri)) return true;
        if(getImageResourceId() != 0 && photo.getImageResourceId()
                != 0 && getImageResourceId() == photo.getImageResourceId()) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageResourceId, internalUri);
    }


    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getUri() {
        return internalUri;
    }

    public void setUri(Uri uri) {
        this.internalUri = uri.toString();
    }

    @Override
    public String toString() {
        return "Photo{" +
                ", tags=" + tags +
                ", uri=" + internalUri +
                ", imageResourceId=" + imageResourceId +
                '}';
    }
}
