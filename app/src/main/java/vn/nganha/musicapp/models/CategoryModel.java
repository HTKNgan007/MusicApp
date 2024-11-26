package vn.nganha.musicapp.models;

public class CategoryModel {
    private String name;
    private String coverURL; // Trường lưu URL ảnh

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
}
