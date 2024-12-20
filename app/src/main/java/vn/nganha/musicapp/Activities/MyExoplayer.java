package vn.nganha.musicapp.Activities;

import android.content.Context;
import android.util.Log;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import vn.nganha.musicapp.models.SongModel;

public class MyExoplayer {
    private static ExoPlayer exoPlayer = null;
    private static SongModel currentSong = null;

    // Trả về instance của ExoPlayer
    public static ExoPlayer getInstance() {
        return exoPlayer;
    }

    // Bắt đầu phát nhạc
    public static void startPlaying(Context context, SongModel song) {
        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(context).build();
        }

        currentSong = song;

        if (currentSong != null && currentSong.getUrl() != null) {
            String url = currentSong.getUrl();
            MediaItem mediaItem = MediaItem.fromUri(url);

            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.play();
        }
    }
}
