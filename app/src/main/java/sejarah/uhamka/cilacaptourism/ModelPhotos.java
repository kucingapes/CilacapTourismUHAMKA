package sejarah.uhamka.cilacaptourism;

public class ModelPhotos {
    private String img;
    private String id;
    private String title;

    public ModelPhotos(String img, String id) {
        this.img = img;
        this.id = id;
    }

    public ModelPhotos(String img, String id, String title) {
        this.img = img;
        this.id = id;
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
