package vn.nganha.musicapp.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.nganha.musicapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationMenu);

        // Set Fragment1 as the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new DiscoverFragment())
                    .commit();
        }

        // Set a listener to handle navigation item selections
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Determine which fragment to display using if-else
                if (item.getItemId() == R.id.discover) {
                    selectedFragment = new DiscoverFragment();
                } else if (item.getItemId() == R.id.playlist) {
                    selectedFragment = new PlaylistFragment();
                } else if (item.getItemId() == R.id.favorite) {
                    selectedFragment = new FavoriteFragment();
                } else if (item.getItemId() == R.id.user) {
                    selectedFragment = new UserFragment();
                }

                // Replace the current fragment with the selected fragment
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, selectedFragment)
                            .commit();
                }

                return true;
            }
        });
    }
}