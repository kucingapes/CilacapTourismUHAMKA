package sejarah.uhamka.cilacaptourism.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.utils.IconicsMenuInflaterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import sejarah.uhamka.cilacaptourism.Adapter.AdapterSearchMaps;
import sejarah.uhamka.cilacaptourism.Cluster.MarkerCluster;
import sejarah.uhamka.cilacaptourism.Cluster.MarkerClusterRender;
import sejarah.uhamka.cilacaptourism.Model.ModelList;
import sejarah.uhamka.cilacaptourism.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gmap;
    private ClusterManager<MarkerCluster> clusterManager;

    private List<ModelList> modelList = new ArrayList<>();
    private AdapterSearchMaps adapterList;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BottomSheetBehavior sheetBehavior;
    int paddingMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        checkConnection();

        recyclerView = findViewById(R.id.search_recycler);
        RelativeLayout bottomLayout = findViewById(R.id.bottom_card);
        sheetBehavior = BottomSheetBehavior.from(bottomLayout);
        sheetBehavior.setPeekHeight(0);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void checkConnection() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean connected = snapshot.getValue(Boolean.class);
                if (!connected) {
                    System.out.println("not connected");
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator),
                            "Anda sedang offline, mungkin tidak dapat memuat sebagian data!",
                            99999);
                    View view = snackbar.getView();
                    TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cloud_off, 0, 0, 0);
                    textView.setCompoundDrawablePadding(50);
                    textView.setTextSize(13);
                    snackbar.setAction("OKE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                } else {
                    System.out.println("connected");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gmap = googleMap;
        clusterManager = new ClusterManager<>(this, gmap);
        gmap.setOnCameraIdleListener(clusterManager);



        FirebaseDatabase databaseDefault = FirebaseDatabase.getInstance();
        DatabaseReference referenceDefault = databaseDefault.getReference("other");
        referenceDefault.keepSynced(false);
        referenceDefault.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double lat = dataSnapshot.child("lat").getValue(Double.class);
                Double lng = dataSnapshot.child("lng").getValue(Double.class);

                if (lat != null && lng != null) {
                    gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 9));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ArrayList<ModelList> markers = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    final String title = snapshot.child("nama").getValue(String.class);
                    String regional = snapshot.child("region").getValue(String.class);
                    String address = snapshot.child("alamat").getValue(String.class);
                    String image = snapshot.child("image/0").getValue(String.class);
                    String id = snapshot.child("id").getValue(String.class);
                    String latitude = snapshot.child("lat").getValue(String.class);
                    String longitude = snapshot.child("lng").getValue(String.class);
                    String body = snapshot.child("keterangan").getValue(String.class);
                    String identifier = snapshot.child("id").getValue(String.class);

                    //int id = Integer.valueOf(identifier);
                    Double lat = Double.parseDouble(latitude);
                    Double lng = Double.parseDouble(longitude);
                    addMarker(lat, lng, title, regional, identifier);

                    //ModelList model = new ModelList(latitude, longitude, id);
                    markers.add(new ModelList(latitude, longitude, identifier));
                    //addmarker(markers);

                    ModelList model = new ModelList(title, address, image, regional, latitude, longitude, id, body);
                    modelList.add(model);

                    adapterList = new AdapterSearchMaps(modelList, getApplicationContext(), new AdapterSearchMaps.OnItemClick() {
                        @Override
                        public void onItemClick(final ModelList itemList) {
                            searchView.onActionViewCollapsed();
                            recyclerView.setVisibility(View.GONE);
                            Double lat = Double.parseDouble(itemList.getLat());
                            Double lng = Double.parseDouble(itemList.getLng());

                            final LatLng position = new LatLng(lat, lng);
                            MarkerOptions options = new MarkerOptions().position(position)
                                    .title(itemList.getName())
                                    .snippet(itemList.getRegional())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            Marker marker = googleMap.addMarker(options);
                            marker.showInfoWindow();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(position)
                                            .zoom(17)
                                            .bearing(360)
                                            .tilt(45)
                                            .build();
                                    gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                        }
                                    }, 300);
                                }
                            }, 300);


                            TextView titleCard = findViewById(R.id.title_card_maps);
                            TextView regionCard = findViewById(R.id.region_card_maps);
                            ImageView imgCard = findViewById(R.id.img_card_maps);
                            titleCard.setText(itemList.getName());
                            regionCard.setText(itemList.getRegional());
                            Glide.with(getApplicationContext()).load(itemList.getImg()).into(imgCard);

                            findViewById(R.id.more_card).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intentDetail(itemList.getName());
                                }
                            });

                            findViewById(R.id.card_sheet).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intentDetail(itemList.getName());
                                }
                            });

                        }
                    });
                    recyclerView.setAdapter(adapterList);
                    adapterList.notifyDataSetChanged();


                    gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<ModelList> filter(List<ModelList> modelLists, String query) {
        query = query.toLowerCase();
        final List<ModelList> filteredModelList = new ArrayList<>();
        for (ModelList modelList : modelLists) {
            final String text = modelList.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(modelList);
            }
        }
        return filteredModelList;
    }

    private List<ModelList> unfilter(List<ModelList> modelLists, String query) {
        query = query.toLowerCase();
        final List<ModelList> filteredModelList = new ArrayList<>();
        for (ModelList modelList : modelLists) {
            final String text = modelList.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.clear();
            }
        }
        return filteredModelList;
    }

    private void addMarker(final Double lat, final Double lng, final String title, String regional, final String id) {

        final LatLng marker = new LatLng(lat, lng);
        final MarkerCluster markerCluster = new MarkerCluster(title, marker, id, regional);

        clusterManager.addItem(markerCluster);
        MarkerClusterRender clusterRender = new MarkerClusterRender(this, gmap, clusterManager);
        clusterManager.setRenderer(clusterRender);

        //gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 9));
        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                String position = marker.getTitle();



                /*CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lng))
                        .zoom(17)
                        .bearing(360)
                        .tilt(45)
                        .build();
                gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("data/"+position);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String title = dataSnapshot.child("nama").getValue(String.class);
                        String regional = dataSnapshot.child("region").getValue(String.class);
                        String image = dataSnapshot.child("image/0").getValue(String.class);
                        String lat = dataSnapshot.child("lat").getValue(String.class);
                        String lng = dataSnapshot.child("lng").getValue(String.class);

                        if (title != null) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            }, 300);

                            LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(latLng)
                                    .zoom(17)
                                    .bearing(360)
                                    .tilt(45)
                                    .build();
                            gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        } else {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }

                        TextView titleCard = findViewById(R.id.title_card_maps);
                        TextView regionCard = findViewById(R.id.region_card_maps);
                        ImageView imgCard = findViewById(R.id.img_card_maps);
                        titleCard.setText(title);
                        regionCard.setText(regional);
                        Glide.with(getApplicationContext()).load(image).into(imgCard);

                        findViewById(R.id.more_card).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                intentDetail(title);
                            }
                        });

                        findViewById(R.id.card_sheet).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                intentDetail(title);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            }
        });
    }

    private void intentDetail(String title) {
        Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
        intent.putExtra("id", title);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        IconicsMenuInflaterUtil.inflate(getMenuInflater(), this, R.menu.search_menu, menu);
        final MenuItem item = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);

        final LinearLayout searchFrame = searchView.findViewById(R.id.search_edit_frame);
        ((LinearLayout.LayoutParams) searchFrame.getLayoutParams()).leftMargin = 0;

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ModelList> filterList = unfilter(modelList, "a");
                adapterList.setFilter(filterList);
                adapterList.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0) {
                    List<ModelList> filterList = unfilter(modelList, s);
                    adapterList.setFilter(filterList);
                    adapterList.notifyDataSetChanged();
                } else {
                    List<ModelList> filterList = filter(modelList, s);
                    adapterList.setFilter(filterList);
                    adapterList.notifyDataSetChanged();
                }
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                recyclerView.setVisibility(View.GONE);
                searchView.onActionViewCollapsed();
                return true;
            }
        });
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
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
