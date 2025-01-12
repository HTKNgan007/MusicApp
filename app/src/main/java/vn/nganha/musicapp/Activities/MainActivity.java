package vn.nganha.musicapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.nganha.musicapp.R;
import vn.nganha.musicapp.models.SongModel;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout playerView;
    private TextView songTitleTextView;
    private ImageView songCoverImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.player_view);
        songTitleTextView = findViewById(R.id.song_title_text_view);
        songCoverImageView = findViewById(R.id.song_cover_image_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationMenu);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new DiscoverFragment())
                    .commit();
        }
        // Đều hướng cho bottom menu
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.discover) {
                    selectedFragment = new DiscoverFragment();
                } else if (item.getItemId() == R.id.playlist) {
                    selectedFragment = new PlaylistFragment();
                } else if (item.getItemId() == R.id.favorite) {
                    selectedFragment = new FavoriteFragment();
                } else if (item.getItemId() == R.id.user) {
                    selectedFragment = new UserFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, selectedFragment)
                            .commit();
                }

                return true;
            }
        });
        // Hiển thị PlayerView nếu cần khi khởi động
        showPlayerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật PlayerView khi quay lại Activity
        showPlayerView();
    }

    private void showPlayerView() {
        playerView.setOnClickListener(v -> {
            startActivity(new Intent(this, PlayerActivity.class));
        });
        SongModel currentSong = MyExoplayer.getCurrentSong();

        if (currentSong != null) {
            playerView.setVisibility(View.VISIBLE);
            songTitleTextView.setText(currentSong.getTitle());

            Glide.with(this)
                    .load(currentSong.getCoverUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                    .into(songCoverImageView);
        } else {
            playerView.setVisibility(View.GONE);
        }
    }
}