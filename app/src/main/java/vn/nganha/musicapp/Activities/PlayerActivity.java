package vn.nganha.musicapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import vn.nganha.musicapp.R;
import vn.nganha.musicapp.databinding.ActivityPlayerBinding;
import vn.nganha.musicapp.models.SongModel;

public class PlayerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;
    private ExoPlayer exoPlayer;
    private boolean isFavorite = false; // Biến kiểm tra trạng thái yêu thích
    private String songId; // ID của bài hát hiện tại
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    Player.Listener playerListener = new Player.Listener() {
        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            Player.Listener.super.onIsPlayingChanged(isPlaying); // Gọi hàm `super`
            showGif(isPlaying); // Gọi hàm showGif
        }
    };

    @UnstableApi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();

        SongModel currentSong = MyExoplayer.getCurrentSong();
        if (currentSong != null) {
            // Cập nhật giao diện với thông tin bài hát
            binding.songTitleTextView.setText(currentSong.getTitle());
            binding.songSubtitleTextView.setText(currentSong.getSubtitle());
            Glide.with(binding.songCoverImageView.getContext())
                    .load(currentSong.getCoverUrl()) // Tải ảnh từ URL
                    .circleCrop()
                    .into(binding.songCoverImageView); // Đặt vào ImageView
            // Hiện media_playing.gif
            Glide.with(binding.songGifImageView.getContext())
                    .load(R.drawable.media_playing)
                    .circleCrop()
                    .into(binding.songGifImageView);
            // Lấy instance của ExoPlayer từ MyExoplayer
            exoPlayer = MyExoplayer.getInstance();
            // Gán exoPlayer vào PlayerView
            binding.playerView.setPlayer(exoPlayer);
            binding.playerView.showController();
            // Lắng nghe sự kiện để hiện gif
            exoPlayer.addListener(playerListener);

            songId = currentSong.getId(); // Lấy ID của bài hát

            // Kiểm tra xem bài hát này có trong danh sách yêu thích của người dùng không
            checkIfFavorite();

            // Xử lý sự kiện nhấn vào icon yêu thích
            binding.favoriteIcon.setOnClickListener(v -> {
                if (isFavorite) {
                    removeFromFavorites(); // Xoá khỏi yêu thích
                } else {
                    addToFavorites(currentSong); // Thêm vào yêu thích
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.removeListener(playerListener);
        }
    }

    public void showGif(boolean show) {
        if (show)
            binding.songGifImageView.setVisibility(View.VISIBLE);
        else
            binding.songGifImageView.setVisibility(View.INVISIBLE);
    }

    private void checkIfFavorite() {
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.getUid())
                    .collection("favorites").document(songId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            isFavorite = true;
                            binding.favoriteIcon.setImageResource(R.drawable.ic_favorite_filled); // Thay đổi icon nếu đã yêu thích
                        } else {
                            isFavorite = false;
                            binding.favoriteIcon.setImageResource(R.drawable.ic_favorite); // Icon mặc định
                        }
                    });
        }
    }

    private void addToFavorites(SongModel song) {
        if (currentUser != null) {
            firestore.collection("users")
                    .document(currentUser.getUid())
                    .collection("favorites")
                    .document(song.getId())
                    .set(song)
                    .addOnSuccessListener(aVoid -> {
                        isFavorite = true;
                        binding.favoriteIcon.setImageResource(R.drawable.ic_favorite_filled); // Cập nhật icon
                        Toast.makeText(PlayerActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PlayerActivity.this, "Lỗi khi thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Nếu người dùng chưa đăng nhập
            Toast.makeText(PlayerActivity.this, "Vui lòng đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFromFavorites() {
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.getUid())
                    .collection("favorites").document(songId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        isFavorite = false;
                        binding.favoriteIcon.setImageResource(R.drawable.ic_favorite); // Cập nhật icon
                        Toast.makeText(this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Xóa khỏi yêu thích thất bại", Toast.LENGTH_SHORT).show());
        }
    }
}
