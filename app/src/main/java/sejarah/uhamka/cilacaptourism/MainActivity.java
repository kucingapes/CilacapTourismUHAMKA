package sejarah.uhamka.cilacaptourism;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.mikepenz.iconics.utils.IconicsMenuInflaterUtil;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<Model> modelList = new ArrayList<>();
        List<Model> list = new ArrayList<>();



        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        Adapter adapter = new Adapter(modelList, getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LayoutMarginDecoration layoutMargin = new LayoutMarginDecoration(20);
        layoutMargin.setPadding(recyclerView, 20);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(layoutMargin);
        recyclerView.setAdapter(adapter);

        addData(adapter, modelList);

        setupDrawer(modelList, adapter);
    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }*/


    private void setupDrawer(final List<Model> modelList, final Adapter adapter) {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SecondaryDrawerItem drawerItem1 = new SecondaryDrawerItem().withIdentifier(1).withName("Cilacap Selatan");
        SecondaryDrawerItem drawerItem2 = new SecondaryDrawerItem().withIdentifier(2).withName("Cilacap Tengah");

        ExpandableDrawerItem expandableDrawerItem = new ExpandableDrawerItem().withIdentifier(1000).withName("Regional")
                .withSubItems(drawerItem1, drawerItem2);

        Drawer drawer = new DrawerBuilder()
                .withActivity(MainActivity.this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header)
                .withHeaderHeight(DimenHolder.fromDp(200))
                .addDrawerItems(expandableDrawerItem)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1000) {
                            List<Model> filterList = unfilter(modelList);
                            adapter.setFilter(filterList);
                            adapter.notifyDataSetChanged();
                        } if (drawerItem.getIdentifier() == 1) {
                            List<Model> filterList = filter(modelList, "Cilacap Selatan");
                            adapter.setFilter(filterList);
                            adapter.notifyDataSetChanged();
                        } if (drawerItem.getIdentifier() == 2) {
                            List<Model> filterList = filter(modelList, "Cilacap Tengah");
                            adapter.setFilter(filterList);
                            adapter.notifyDataSetChanged();
                        }
                        return false;
                    }
                })
                .build();

        View view = drawer.getHeader().getRootView();
        ImageView img = view.findViewById(R.id.img);
        Glide.with(view).load("https://source.unsplash.com/n8HAQ26GnMc").into(img);

    }

    private List<Model> filter(List<Model> models, String query) {
        query = query.toLowerCase();
        final List<Model> filteredModelList = new ArrayList<>();
        for (Model model : models) {
            final String text = model.getRegional().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private List<Model> unfilter(List<Model> models) {
        return new ArrayList<>(models);
    }

    private void addData(final Adapter adapter, final List<Model> modelList) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    findViewById(R.id.progressbar).setVisibility(View.GONE);
                    String title = snapshot.child("nama").getValue(String.class);
                    String regional = snapshot.child("region").getValue(String.class);
                    String address = snapshot.child("alamat").getValue(String.class);
                    String image = snapshot.child("image/0").getValue(String.class);
                    String id = snapshot.child("id").getValue(String.class);
                    String lat = snapshot.child("lat").getValue(String.class);
                    String lng = snapshot.child("lng").getValue(String.class);

                    Model model = new Model(title, address, image, regional, lat, lng, id);
                    modelList.add(model);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        IconicsMenuInflaterUtil.inflate(getMenuInflater(), this, R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_maps) {
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
