package sejarah.uhamka.cilacaptourism.Cluster;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerCluster implements ClusterItem {

    final String title;
    private final LatLng latLng;
    private final String id;
    final String snippet;

    public MarkerCluster(String title, LatLng latLng, String id, String snippet) {
        this.title = title;
        this.latLng = latLng;
        this.id = id;
        this.snippet = snippet;
    }

    @Override public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public String getId() {
        return id;
    }


}
