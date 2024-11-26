package vn.nganha.musicapp.models;

public class CategoryModel {
    private String name;
    private String coverUrl;

    public CategoryModel(String name, String coverUrl) {
        this.name = name;
        this.coverUrl = coverUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
