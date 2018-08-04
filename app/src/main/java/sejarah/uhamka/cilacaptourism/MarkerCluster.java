package sejarah.uhamka.cilacaptourism;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerCluster implements ClusterItem {

    final String title;
    final LatLng latLng;
    final String id;
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
