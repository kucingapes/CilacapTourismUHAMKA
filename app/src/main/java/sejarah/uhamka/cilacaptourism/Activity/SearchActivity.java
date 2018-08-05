package sejarah.uhamka.cilacaptourism.Activity;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.utils.IconicsMenuInflaterUtil;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import sejarah.uhamka.cilacaptourism.Adapter.AdapterList;
import sejarah.uhamka.cilacaptourism.Model.ModelList;
import sejarah.uhamka.cilacaptourism.R;

public class SearchActivity extends AppCompatActivity {
    private List<ModelList> modelList = new ArrayList<>();
    private AdapterList adapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapterList = new AdapterList(modelList, getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LayoutMarginDecoration layoutMargin = new LayoutMarginDecoration(20);
        layoutMargin.setPadding(recyclerView, 20);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(layoutMargin);
        recyclerView.setAdapter(adapterList);

        addData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    private void addData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("nama").getValue(String.class);
                    String regional = snapshot.child("region").getValue(String.class);
                    String address = snapshot.child("alamat").getValue(String.class);
                    String image = snapshot.child("image/0").getValue(String.class);
                    String id = snapshot.child("id").getValue(String.class);
                    String lat = snapshot.child("lat").getValue(String.class);
                    String lng = snapshot.child("lng").getValue(String.class);
                    String body = snapshot.child("keterangan").getValue(String.class);

                    ModelList modelList = new ModelList(title, address, image, regional, lat, lng, id, body);
                    SearchActivity.this.modelList.add(modelList);
                    adapterList.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        IconicsMenuInflaterUtil.inflate(getMenuInflater(), this, R.menu.search_menu, menu);
        final MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<ModelList> filterList = filter(modelList, s);
                adapterList.setFilter(filterList);
                adapterList.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

   /* @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        List<ModelList> filterList = filter(modelList, s);
        AdapterList adapterList = new AdapterList(modelList, getApplicationContext());
        adapterList.setFilter(filterList);
        return true;
    }*/

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
