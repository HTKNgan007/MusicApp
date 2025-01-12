package vn.nganha.musicapp.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import vn.nganha.musicapp.R;
import vn.nganha.musicapp.adapter.SongsListAdapter;

public class FavoriteFragment extends Fragment {

    private RecyclerView favoriteRecyclerView;
    private SongsListAdapter songsListAdapter;
    private List<String> favoriteSongsList;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private TextView noFavoritesTextView;  // TextView để hiển thị thông báo

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        // Khởi tạo các view
        favoriteRecyclerView = view.findViewById(R.id.favorite_recycler_view);
        noFavoritesTextView = view.findViewById(R.id.noFavoritesTextView);  // Lấy TextView
        favoriteSongsList = new ArrayList<>();
        songsListAdapter = new SongsListAdapter(favoriteSongsList);

        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteRecyclerView.setAdapter(songsListAdapter);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();

        // Kiểm tra người dùng và tải danh sách yêu thích
        if (currentUser != null) {
            loadFavoriteSongs();
        }

        return view;
    }

    private void loadFavoriteSongs() {
        firestore.collection("users").document(currentUser.getUid())
                .collection("favorites")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        favoriteSongsList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            favoriteSongsList.add(document.getId());
                        }

                        // Kiểm tra nếu danh sách yêu thích trống
                        if (favoriteSongsList.isEmpty()) {
                            noFavoritesTextView.setVisibility(View.VISIBLE); // Hiển thị thông báo
                            favoriteRecyclerView.setVisibility(View.GONE);  // Ẩn RecyclerView
                        } else {
                            noFavoritesTextView.setVisibility(View.GONE); // Ẩn thông báo
                            favoriteRecyclerView.setVisibility(View.VISIBLE);  // Hiển thị RecyclerView
                            songsListAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi khi tải danh sách yêu thích", Toast.LENGTH_SHORT).show());
    }
}
