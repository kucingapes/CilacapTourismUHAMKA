package sejarah.uhamka.cilacaptourism.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.utils.IconicsMenuInflaterUtil;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.List;

import sejarah.uhamka.cilacaptourism.Adapter.AdapterList;
import sejarah.uhamka.cilacaptourism.Model.ModelList;
import sejarah.uhamka.cilacaptourism.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<ModelList> modelList = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        AdapterList adapterList = new AdapterList(modelList, getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LayoutMarginDecoration layoutMargin = new LayoutMarginDecoration(20);
        layoutMargin.setPadding(recyclerView, 20);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(layoutMargin);

        addData(adapterList, modelList, recyclerView);
        setupDrawer(modelList, adapterList, toolbar);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    private void setupDrawer(final List<ModelList> modelList, final AdapterList adapterList, Toolbar toolbar) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams")
        View dialogView = inflater.inflate(R.layout.about_layout, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        setupViewDialog(dialogView);


        DividerDrawerItem dividerDrawerItem = new DividerDrawerItem();

        PrimaryDrawerItem itemAbout = new PrimaryDrawerItem()
                .withIdentifier(90)
                .withIcon(GoogleMaterial.Icon.gmd_perm_identity)
                .withName("Tentang Aplikasi");

        SecondaryDrawerItem drawerItem1 = new SecondaryDrawerItem()
                .withIdentifier(1)
                .withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_pin_drop)
                .withName("Cilacap Selatan");

        SecondaryDrawerItem drawerItem2 = new SecondaryDrawerItem()
                .withIdentifier(2)
                .withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_pin_drop)
                .withName("Cilacap Tengah");

        SecondaryDrawerItem drawerItem3 = new SecondaryDrawerItem()
                .withIdentifier(3)
                .withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_pin_drop)
                .withName("Adipala");

        SecondaryDrawerItem drawerItem4 = new SecondaryDrawerItem()
                .withIdentifier(4)
                .withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_pin_drop)
                .withName("Kroya");

        SecondaryDrawerItem drawerItem5 = new SecondaryDrawerItem()
                .withIdentifier(5)
                .withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_pin_drop)
                .withName("Majenang");

        SecondaryDrawerItem drawerItem6 = new SecondaryDrawerItem()
                .withIdentifier(6)
                .withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_pin_drop)
                .withName("Kampung Laut");

        SecondaryDrawerItem drawerItem7 = new SecondaryDrawerItem()
                .withIdentifier(7)
                .withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_pin_drop)
                .withName("Kesugihan");

        SecondaryDrawerItem drawerItem8 = new SecondaryDrawerItem()
                .withIdentifier(8)
                .withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_pin_drop)
                .withName("Wanareja");

        ExpandableDrawerItem expandableDrawerItem = new ExpandableDrawerItem()
                .withIdentifier(1000)
                .withIcon(GoogleMaterial.Icon.gmd_pin_drop)
                .withName("Regional Cilacap")
                .withSubItems(drawerItem1, drawerItem2, drawerItem3, drawerItem4,
                        drawerItem5, drawerItem6, drawerItem7, drawerItem8);

        PrimaryDrawerItem itemAll = new PrimaryDrawerItem()
                .withIdentifier(20)
                .withIcon(GoogleMaterial.Icon.gmd_place)
                .withEnabled(true)
                .withName("Semua Lokasi");

        PrimaryDrawerItem itemFav = new PrimaryDrawerItem()
                .withIdentifier(21)
                .withIcon(GoogleMaterial.Icon.gmd_favorite)
                .withName("Lokasi Favorit");

        PrimaryDrawerItem itemMaps = new PrimaryDrawerItem()
                .withIdentifier(22)
                .withIcon(GoogleMaterial.Icon.gmd_map)
                .withName("Peta");

        PrimaryDrawerItem itemTrain = new PrimaryDrawerItem()
                .withIdentifier(23)
                .withIcon(GoogleMaterial.Icon.gmd_train)
                .withName("Stasiun");

        Drawer drawer = new DrawerBuilder()
                .withActivity(MainActivity.this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header)
                .withHeaderHeight(DimenHolder.fromDp(200))
                .addDrawerItems(itemAll,
                        expandableDrawerItem,
                        itemTrain,
                        itemMaps,
                        dividerDrawerItem,
                        itemFav,
                        itemAbout)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1000) {
                            List<ModelList> filterList = unfilter(modelList);
                            adapterList.setFilter(filterList);
                            adapterList.notifyDataSetChanged();
                        }
                        if (drawerItem.getIdentifier() == 20) {
                            List<ModelList> filterList = unfilter(modelList);
                            adapterList.setFilter(filterList);
                            adapterList.notifyDataSetChanged();
                        }
                        if (drawerItem.getIdentifier() == 1) {
                            List<ModelList> filterList = filter(modelList, "Cilacap Selatan");
                            adapterList.setFilter(filterList);
                            adapterList.notifyDataSetChanged();
                        }
                        if (drawerItem.getIdentifier() == 2) {
                            List<ModelList> filterList = filter(modelList, "Cilacap Tengah");
                            adapterList.setFilter(filterList);
                            adapterList.notifyDataSetChanged();
                        }
                        if (drawerItem.getIdentifier() == 3) {
                            List<ModelList> filterList = filter(modelList, "Adipala");
                            adapterList.setFilter(filterList);
                            adapterList.notifyDataSetChanged();
                        }
                        if (drawerItem.getIdentifier() == 4) {
                            List<ModelList> filterList = filter(modelList, "Kroya");
                            adapterList.setFilter(filterList);
                            adapterList.notifyDataSetChanged();
                        }
                        if (drawerItem.getIdentifier() == 5) {
                            List<ModelList> filterList = filter(modelList, "Majenang");
                            adapterList.setFilter(filterList);
                            adapterList.notifyDataSetChanged();
                        }
                        if (drawerItem.getIdentifier() == 6) {
                            List<ModelList> filterList = filter(modelList, "Kampung Laut");
                            adapterList.setFilter(filterList);
                            adapterList.notifyDataSetChanged();
                        }
                        if (drawerItem.getIdentifier() == 7) {
                            List<ModelList> filterList = filter(modelList, "Kesugihan");
                            adapterList.setFilter(filterList);
                            adapterList.notifyDataSetChanged();
                        }
                        if (drawerItem.getIdentifier() == 8) {
                            List<ModelList> filterList = filter(modelList, "Wanareja");
                            adapterList.setFilter(filterList);
                            adapterList.notifyDataSetChanged();
                        }

                        if (drawerItem.getIdentifier() == 21) {
                            startActivity(new Intent(getApplicationContext(), FavoriteList.class));
                        }
                        if (drawerItem.getIdentifier() == 22) {
                            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        }
                        if (drawerItem.getIdentifier() == 23) {
                            List<ModelList> filterList = filterTrain(modelList);
                            adapterList.setFilter(filterList);
                            adapterList.notifyDataSetChanged();
                        }

                        if (drawerItem.getIdentifier() == 90) {
                            alertDialog.show();
                        }
                        return false;
                    }
                })
                .build();

        View view = drawer.getHeader().getRootView();
        ImageView img = view.findViewById(R.id.img);
        setupImgHeader(view, img);

    }

    @SuppressLint("SetTextI18n")
    private void setupViewDialog(final View dialogView) {
        final ImageView imgAbout = dialogView.findViewById(R.id.header_about);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("other/imgheader");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imgUrl = dataSnapshot.getValue(String.class);
                Glide.with(dialogView).load(imgUrl).into(imgAbout);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dialogView.findViewById(R.id.link_prodi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "https://fkip.uhamka.ac.id/pendidikan-sejarah";
                setupOpenLink(data);
            }
        });
        dialogView.findViewById(R.id.email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("other/email");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String email = dataSnapshot.getValue(String.class);
                        setupEmail(email);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        String versionName = "";
        try {
            versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView nameApp = dialogView.findViewById(R.id.name_app);
        nameApp.setText("Wisata Cilacap Terpadu (versi "+versionName+")");
    }

    private void setupEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Aplikasi Wisata Cilacap");
        startActivity(intent);
    }

    private void setupImgHeader(final View view, final ImageView img) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("other/imgheader");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imgUrl = dataSnapshot.getValue(String.class);
                Glide.with(view).load(imgUrl).into(img);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupOpenLink(String data) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        builder.addDefaultShareMenuItem();
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(MainActivity.this, Uri.parse(data));
    }

    private List<ModelList> filter(List<ModelList> modelLists, String query) {
        query = query.toLowerCase();
        final List<ModelList> filteredModelList = new ArrayList<>();
        for (ModelList modelList : modelLists) {
            final String text = modelList.getRegional().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(modelList);
            }
        }
        return filteredModelList;
    }

    private List<ModelList> filterTrain(List<ModelList> modelLists) {
        String train = "Stasiun".toLowerCase();
        final List<ModelList> filteredModelList = new ArrayList<>();
        for (ModelList modelList : modelLists) {
            final String text = modelList.getName().toLowerCase();
            if (text.contains(train)) {
                filteredModelList.add(modelList);
            }
        }
        return filteredModelList;
    }

    private List<ModelList> unfilter(List<ModelList> modelLists) {
        return new ArrayList<>(modelLists);
    }

    private void addData(final AdapterList adapterList, final List<ModelList> modelList, final RecyclerView recyclerView) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    findViewById(R.id.progressbar).setVisibility(View.GONE);
                    String title = snapshot.child("nama").getValue(String.class);
                    String regional = snapshot.child("region").getValue(String.class);
                    String address = snapshot.child("alamat").getValue(String.class);
                    String image = snapshot.child("image/0").getValue(String.class);
                    String id = snapshot.child("id").getValue(String.class);
                    String lat = snapshot.child("lat").getValue(String.class);
                    String lng = snapshot.child("lng").getValue(String.class);
                    String body = snapshot.child("keterangan").getValue(String.class);

                    ModelList model = new ModelList(title, address, image, regional, lat, lng, id, body);
                    modelList.add(model);
                    adapterList.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterList);

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
        if (item.getItemId() == R.id.menu_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
