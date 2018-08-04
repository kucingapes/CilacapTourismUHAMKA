package sejarah.uhamka.cilacaptourism;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private List<ModelPhotos> photosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final String id = getIntent().getStringExtra("id");

        final ImageView imgDetail = findViewById(R.id.image_detail);
        final TextView titleDetail = findViewById(R.id.title_detail);
        final RecyclerView gallery = findViewById(R.id.gallery);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        LayoutMarginDecoration layoutMargin = new LayoutMarginDecoration(15);
        layoutMargin.setPadding(gallery, 15);
        gallery.setLayoutManager(layoutManager);
        gallery.addItemDecoration(layoutMargin);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("data/"+id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("nama").getValue(String.class);
                String regional = dataSnapshot.child("region").getValue(String.class);
                String address = dataSnapshot.child("alamat").getValue(String.class);
                String image = dataSnapshot.child("image/0").getValue(String.class);
                Glide.with(DetailActivity.this).load(image).into(imgDetail);
                titleDetail.setText(title);

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    for (DataSnapshot data: ds.getChildren()) {
                        String url = data.getValue(String.class);
                        setupPhotos(gallery, url, id);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupPhotos(RecyclerView gallery, String url, String id) {
        AdapterThumnail adapterThumnail = new AdapterThumnail(photosList, getApplicationContext());
        ModelPhotos modelPhotos = new ModelPhotos(url, id);
        photosList.add(modelPhotos);
        adapterThumnail.notifyDataSetChanged();
        gallery.setAdapter(adapterThumnail);
    }
}
