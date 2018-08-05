package sejarah.uhamka.cilacaptourism;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gmap;
    private ClusterManager<MarkerCluster> clusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        clusterManager = new ClusterManager<>(this, gmap);
        gmap.setOnCameraIdleListener(clusterManager);


        final ArrayList<Model> markers = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    //findViewById(R.id.progressbar).setVisibility(View.GONE);
                    String title = snapshot.child("nama").getValue(String.class);
                    String regional = snapshot.child("region").getValue(String.class);
                    String address = snapshot.child("alamat").getValue(String.class);
                    String image = snapshot.child("image/0").getValue(String.class);
                    String latitude = snapshot.child("lat").getValue(String.class);
                    String longitude = snapshot.child("lng").getValue(String.class);
                    String identifier = snapshot.child("id").getValue(String.class);

                    //int id = Integer.valueOf(identifier);
                    Double lat = Double.parseDouble(latitude);
                    Double lng = Double.parseDouble(longitude);
                    addMarker(lat, lng, title, regional, identifier);

                    //Model model = new Model(latitude, longitude, id);
                    markers.add(new Model(latitude, longitude, identifier));
                    //addmarker(markers);

                    /*LatLng marker = new LatLng(lat, lng);
                    gmap.addMarker(new MarkerOptions().position(marker).title(title));
                    gmap.moveCamera(CameraUpdateFactory.newLatLng(marker));*/

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addMarker(final Double lat, final Double lng, final String title, String regional, final String id) {

        final LatLng marker = new LatLng(lat, lng);
        final MarkerCluster markerCluster = new MarkerCluster(title, marker, id, regional);

        clusterManager.addItem(markerCluster);
        MarkerClusterRender clusterRender = new MarkerClusterRender(this, gmap, clusterManager);
        clusterManager.setRenderer(clusterRender);

        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 7));
        gmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String position = marker.getTitle();

                Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
                intent.putExtra("id", position);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
