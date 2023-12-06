package com.example.photos.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.photos.model.Photo;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    private List<Photo> photos;

    public ImagePagerAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Load the image into the ImageView
        Photo photo = photos.get(position);
        if (photo.getUri() != null) {
            // Load image from Uri
            imageView.setImageURI(photo.getUri());
        } else {
            // Load image from resource ID
            imageView.setImageResource(photo.getImageResourceId());
        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }
}
