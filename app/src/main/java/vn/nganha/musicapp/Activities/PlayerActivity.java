package vn.nganha.musicapp.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import vn.nganha.musicapp.R;
import vn.nganha.musicapp.databinding.ActivityPlayerBinding;
import vn.nganha.musicapp.models.SongModel;

public class PlayerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;
    private ExoPlayer exoPlayer;

    Player.Listener playerListener = new Player.Listener() {
        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            Player.Listener.super.onIsPlayingChanged(isPlaying); // Gọi hàm `super`
            showGif(isPlaying); // Gọi hàm showGif
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SongModel currentSong = MyExoplayer.getCurrentSong();
        if (currentSong != null) {
            // Cập nhật giao diện với thông tin bài hát
            binding.songTitleTextView.setText(currentSong.getTitle());
            binding.songSubtitleTextView.setText(currentSong.getSubtitle());
            Glide.with(binding.songCoverImageView.getContext())
                    .load(currentSong.getCoverUrl()) // Tải ảnh từ URL
                    .circleCrop()
                    .into(binding.songCoverImageView); // Đặt vào ImageView
            Glide.with(binding.songGifImageView.getContext())
                    .load(R.drawable.media_playing)
                    .circleCrop()
                    .into(binding.songGifImageView);
            // Lấy instance của ExoPlayer từ MyExoplayer
            exoPlayer = MyExoplayer.getInstance();
            // Gán exoPlayer vào PlayerView
            binding.playerView.setPlayer(exoPlayer);
            // Lắng nghe sự kiện để hiện gif
            exoPlayer.addListener(playerListener);
        }
    }

    public void showGif(boolean show){
        if(show)
            binding.songGifImageView.setVisibility(View.VISIBLE);
        else binding.songGifImageView.setVisibility(View.INVISIBLE);
    }
}