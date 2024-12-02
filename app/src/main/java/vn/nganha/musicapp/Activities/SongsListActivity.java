package vn.nganha.musicapp.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import vn.nganha.musicapp.R;
import vn.nganha.musicapp.adapter.SongsListAdapter;
import vn.nganha.musicapp.databinding.ActivitySongsListBinding;
import vn.nganha.musicapp.models.CategoryModel;

public class SongsListActivity extends AppCompatActivity {

    public static CategoryModel category;
    private ActivitySongsListBinding binding;
    private SongsListActivity songsListActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySongsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set data to the views
        binding.nameTextView.setText(category.getName());

        // Load image using Glide
        Glide.with(binding.coverImageView.getContext()).load(category.getCoverURL())
                .apply(new RequestOptions(). transform(new RoundedCorners(32)))
                .into(binding.coverImageView);


        setupSongsListRecyclerView();
    }

    public void setupSongsListRecyclerView() {
        // Create the adapter
        SongsListAdapter songsListAdapter = new SongsListAdapter(category.getSongs());

        // Set the LayoutManager
        binding.songsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set the Adapter
        binding.songsListRecyclerView.setAdapter(songsListAdapter);
    }

}