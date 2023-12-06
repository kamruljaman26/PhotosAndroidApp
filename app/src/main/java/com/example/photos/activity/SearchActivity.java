package com.example.photos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photos.R;
import com.example.photos.adapter.SearchResultsAdapter;
import com.example.photos.model.Photo;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchResultsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ImageView backButton = findViewById(R.id.backImageViewId);
        TextView backButtonText = findViewById(R.id.backTextViewId);
        backButton.setOnClickListener(view -> onBackPressed());
        backButtonText.setOnClickListener(view -> onBackPressed());

        recyclerView = findViewById(R.id.searchPhotoRecycler);
        adapter = new SearchResultsAdapter();
        // Get the search results from the Intent
        List<Photo> searchResults = (List<Photo>) getIntent().getSerializableExtra("searchResults");

        // Set the search results to the adapter
        adapter.setSearchResults(searchResults);
        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}