package sejarah.uhamka.cilacaptourism.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;

import sejarah.uhamka.cilacaptourism.Adapter.AdapterThumnail;
import sejarah.uhamka.cilacaptourism.Model.ModelList;
import sejarah.uhamka.cilacaptourism.Model.ModelPhotos;
import sejarah.uhamka.cilacaptourism.R;
import sejarah.uhamka.cilacaptourism.SharedPreference.SharedPref;

public class DetailActivity extends AppCompatActivity {
    private List<ModelPhotos> photosList = new ArrayList<>();

    private static final String PREFS_NAME = "FILE_PREFERENCES";
    private static final String FAVORITES = "ITEM_FAVORITE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String id = getIntent().getStringExtra("id");

        final ImageView imgDetail = findViewById(R.id.image_detail);
        final TextView bodyDetail = findViewById(R.id.body_content);
        final TextView addressDetail = findViewById(R.id.address_content);
        final TextView regionDetail = findViewById(R.id.regional_content);
        final RecyclerView gallery = findViewById(R.id.gallery);
        final NestedScrollView scrollView = findViewById(R.id.nested);
        final FloatingActionButton actionButton = findViewById(R.id.favorite_button);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        LayoutMarginDecoration layoutMargin = new LayoutMarginDecoration(15);
        layoutMargin.setPadding(gallery, 15);
        gallery.setLayoutManager(layoutManager);
        gallery.addItemDecoration(layoutMargin);

        setupButton(scrollView, actionButton);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("data/"+id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String title = dataSnapshot.child("nama").getValue(String.class);
                final String regional = dataSnapshot.child("region").getValue(String.class);
                final String address = dataSnapshot.child("alamat").getValue(String.class);
                final String image = dataSnapshot.child("image/0").getValue(String.class);
                final String id = dataSnapshot.child("id").getValue(String.class);
                final String lat = dataSnapshot.child("lat").getValue(String.class);
                final String lng = dataSnapshot.child("lng").getValue(String.class);
                final String body = dataSnapshot.child("keterangan").getValue(String.class);
                Glide.with(DetailActivity.this).load(image).into(imgDetail);
                bodyDetail.setText(body);
                addressDetail.setText(address);
                regionDetail.setText(regional);

                getSupportActionBar().setTitle(title);
                setupMapLite(mapFragment, lat, lng);

                /* pasang dulu sharedpreferencesnya */
                SharedPreferences preferences = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                final String json = preferences.getString(FAVORITES, null); // panggil string json nye

                if (lng != null) {
                    if (json != null && json.contains(lng)) {
                        actionButton.setImageResource(R.drawable.ic_favorite);
                    } else {
                        actionButton.setImageResource(R.drawable.ic_favorite_border);
                    }

                }

                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addFavorite(json, actionButton, title, regional, address, image, id, lat, lng, body);
                    }
                });


                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    for (DataSnapshot data: ds.getChildren()) {
                        String url = data.getValue(String.class);
                        setupPhotos(gallery, url, id, title);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addFavorite(String json, FloatingActionButton actionButton,
                             String title, String regional, String address,
                             String image, String id, String lat, String lng, String body) {

        ModelList modelList = new ModelList(title, address, image, regional, lat, lng, id, body);

        /* ngecek apakah itemnya udeh ada di favorit ape belum */
        if (json != null && json.contains(lng)) { // andaikata udah ada
            actionButton.setImageResource(R.drawable.ic_favorite_border);
            SharedPref sharedPref = new SharedPref();
            int position = sharedPref.setIndex(getApplicationContext(), lng);
            sharedPref.removeFavorite(getApplicationContext(), position); // maka metodenya adalah remove item
            sharedPref.removeIndex(getApplicationContext(), lng); // nah ini remove string single nya buat acuan posisi
            actionButton.hide();
            actionButton.show();
            Snackbar.make(findViewById(R.id.coordinator),"Dihapus dari favorit", Snackbar.LENGTH_SHORT).show();
            actionButton.setEnabled(false);

        } else { // nah kalau belom
            actionButton.setImageResource(R.drawable.ic_favorite);
            SharedPref sharedPref = new SharedPref();
            sharedPref.addFavorite(getApplicationContext(), modelList); // ya tambahin deh
            sharedPref.addIndex(getApplicationContext(), lng);
            actionButton.hide();
            actionButton.show();
            Snackbar.make(findViewById(R.id.coordinator),"Lokasi difavoritkan", Snackbar.LENGTH_SHORT).show();
            actionButton.setEnabled(false);

        }

    }

    private void setupButton(final NestedScrollView scrollView, final FloatingActionButton actionButton) {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
                    actionButton.hide();
                } else {
                    actionButton.show();
                }

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    private void setupMapLite(SupportMapFragment mapFragment, final String lat, final String lng) {
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Double latitude = Double.parseDouble(lat);
                    Double longitude = Double.parseDouble(lng);

                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
                }
            });
        }
    }

    private void setupPhotos(RecyclerView gallery, String url, String id, String title) {
        AdapterThumnail adapterThumnail = new AdapterThumnail(photosList, getApplicationContext());
        ModelPhotos modelPhotos = new ModelPhotos(url, id, title);
        photosList.add(modelPhotos);
        adapterThumnail.notifyDataSetChanged();
        gallery.setAdapter(adapterThumnail);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("refresh", "true");
        setResult(RESULT_OK, intent);
        finish();
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
