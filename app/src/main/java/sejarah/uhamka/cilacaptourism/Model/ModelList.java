package sejarah.uhamka.cilacaptourism.Model;

public class ModelList {
    private String name;
    private String address;
    private String img;
    private String regional;
    private String lat;
    private String lng;
    private String id;
    private String body;

    public ModelList(String lat, String lng, String identifier) {
        this.lat = lat;
        this.lng = lng;
    }

    public ModelList(String name, String address, String img, String regional, String lat, String lng, String id, String body) {
        this.name = name;
        this.address = address;
        this.img = img;
        this.regional = regional;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.body = body;
    }

    public ModelList(String regional) {
        this.regional = regional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
