package vn.nganha.musicapp.models;

import java.util.List;

public class CategoryModel {
    private String name;
    private String coverURL; // Trường lưu URL ảnh
    private List<String> songs;

    public CategoryModel() {
        // Constructor mặc định cho Firestore
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }
}
