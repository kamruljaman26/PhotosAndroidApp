package com.example.photos.model;

import android.net.Uri;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

// Photos Pojo
public class Photo implements Serializable {
    private final Set<Tag> tags;
    private int imageResourceId;
    private String internalUri;

    public Photo(int imageResourceId) {
        this.imageResourceId = imageResourceId;
        tags = new HashSet<>();
    }

    public Photo(String internalUri) {
        this.internalUri = internalUri;
        tags = new HashSet<>();
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

    public boolean addTag(Tag tag){
        return tags.add(tag);
    }

    public void removeTag(String s) {
        for(Tag tag:tags) {
            if(tag.toString().contains(s)){
                tags.remove(tag);
                break;
            }
        }
    }

    public Set<Tag> getTags() {
        return tags;
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
