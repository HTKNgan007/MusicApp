package vn.nganha.musicapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import vn.nganha.musicapp.Activities.MyExoplayer;
import vn.nganha.musicapp.Activities.PlayerActivity;
import vn.nganha.musicapp.databinding.SongListItemRecyclerBinding;
import vn.nganha.musicapp.models.SongModel;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.MyViewHolder> {
    private final List<String> songIdList;

    public SongsListAdapter(List<String> songIdList) {
        this.songIdList = songIdList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final SongListItemRecyclerBinding binding;

        public MyViewHolder(SongListItemRecyclerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // lấy dữ liệu từ Firebase và hiển thị nó trong recyclerview
        public void bindData(String songId) {
            FirebaseFirestore.getInstance().collection("songs")
                    .document(songId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        SongModel song = documentSnapshot.toObject(SongModel.class);
                        if (song != null) {
                            binding.songTitleTextView.setText(song.getTitle());
                            binding.songSubtitleTextView.setText(song.getSubtitle());
                            Glide.with(binding.songCoverImageView.getContext())
                                    .load(song.getCoverUrl())
                                    .apply(new RequestOptions().transform(new RoundedCorners(32)))
                                    .into(binding.songCoverImageView);
                            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyExoplayer.startPlaying(v.getContext(), song);
                                    Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                                    v.getContext().startActivity(intent);
                                }
                            });
                        }
                    });
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SongListItemRecyclerBinding binding = SongListItemRecyclerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return songIdList.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bindData(songIdList.get(position));
    }
}
