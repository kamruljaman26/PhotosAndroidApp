package com.example.photos.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.example.photos.model.Photo;
import java.io.File;
import java.util.List;

public class ImageSlideAdapter extends PagerAdapter {

    private List<Photo> photos;

    public ImageSlideAdapter(List<Photo> photos) {
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

        try {
            // Load the image into the ImageView
            Photo photo = photos.get(position);
            if (photo.getUri() != null) {
                File imgFile = new  File(photo.getUri());
                if(imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            } else {
                imageView.setImageResource(photo.getImageResourceId());
            }
            container.addView(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }

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
