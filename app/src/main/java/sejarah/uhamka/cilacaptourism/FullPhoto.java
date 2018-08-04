package sejarah.uhamka.cilacaptourism;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;

public class FullPhoto extends AppCompatActivity {
    private List<ModelPhotos> photosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);

        final String id = getIntent().getStringExtra("id");
        final int position = getIntent().getIntExtra("position", 0);

        final RecyclerViewPager fullImg = findViewById(R.id.full_photos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        fullImg.setLayoutManager(layoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("data/"+id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    for (DataSnapshot data: ds.getChildren()) {
                        String url = data.getValue(String.class);
                        setupPhotos(fullImg, url, position, id);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupPhotos(RecyclerView fullImg, String url, int position, String id) {
        AdapterPhotos adapterPhotos = new AdapterPhotos(photosList, getApplicationContext());
        ModelPhotos modelPhotos = new ModelPhotos(url, id);
        photosList.add(modelPhotos);
        adapterPhotos.notifyDataSetChanged();
        fullImg.setAdapter(adapterPhotos);
        fullImg.scrollToPosition(position);
    }
}
