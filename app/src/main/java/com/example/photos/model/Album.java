package com.example.photos.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Album implements Serializable {

    private String name;  // Name is used as the identifier
    private List<Photo> photos;

    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<>();
    }

    public Album(String name, List<Photo> photos) {
        this.name = name;
        this.photos = photos;
    }

    public Photo remove(Photo photo){
        int i = photos.indexOf(photo);
        if (i > -1){
            photos.remove(i);
            return photo;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(name, album.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, photos);
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", photos=" + photos +
                '}';
    }
}
