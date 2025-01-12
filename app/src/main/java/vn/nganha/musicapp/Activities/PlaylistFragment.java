package vn.nganha.musicapp.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import vn.nganha.musicapp.R;
import vn.nganha.musicapp.adapter.SongsListAdapter;

public class PlaylistFragment extends Fragment {

    private RecyclerView songsRecyclerView;
    private SongsListAdapter songsListAdapter;
    private List<String> songIdList;
    private FirebaseFirestore firestore;
    private SearchView searchView;

    public PlaylistFragment() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        songsRecyclerView = view.findViewById(R.id.songs_recycler_view);
        searchView = view.findViewById(R.id.search_view);

        firestore = FirebaseFirestore.getInstance();
        songIdList = new ArrayList<>();
        songsListAdapter = new SongsListAdapter(songIdList);
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songsRecyclerView.setAdapter(songsListAdapter);

        // Lấy tất cả bài hát từ Firebase
        firestore.collection("songs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        songIdList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            songIdList.add(document.getId());
                        }
                        songsListAdapter.notifyDataSetChanged();
                    }
                });

        // Tìm kiếm bài hát theo tên
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSongsByName(newText);
                return false;
            }
        });

        return view;
    }

    private void filterSongsByName(String query) {
        FirebaseFirestore.getInstance().collection("songs")
                .orderBy("title")
                .startAt(query)
                .endAt(query + "\uf8ff")  // Tìm kiếm gần đúng
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    songIdList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        songIdList.add(document.getId());
                    }
                    songsListAdapter.notifyDataSetChanged();
                });
    }
}