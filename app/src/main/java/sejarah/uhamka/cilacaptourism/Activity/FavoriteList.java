package sejarah.uhamka.cilacaptourism.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import sejarah.uhamka.cilacaptourism.Adapter.AdapterList;
import sejarah.uhamka.cilacaptourism.Model.ModelList;
import sejarah.uhamka.cilacaptourism.R;
import sejarah.uhamka.cilacaptourism.RecyclerConfig.EmptyRecyclerView;
import sejarah.uhamka.cilacaptourism.SharedPreference.SharedPref;

public class FavoriteList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        EmptyRecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayout layoutEmpty = findViewById(R.id.empty);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LayoutMarginDecoration layoutMargin = new LayoutMarginDecoration(20);
        layoutMargin.setPadding(recyclerView, 20);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(layoutMargin);
        recyclerView.setEmptyView(layoutEmpty);

        addData(recyclerView);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    private void addData(RecyclerView recyclerView) {
        List<ModelList> itemList;
        AdapterList adapterList;

        SharedPref sharedPref = new SharedPref();
        itemList = sharedPref.getFavorites(this);

        if (itemList == null) {
            itemList = new ArrayList<>();
        }

        adapterList = new AdapterList(itemList, this);
        Collections.reverse(itemList);
        recyclerView.setAdapter(adapterList);
        adapterList.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String output = data.getStringExtra("refresh");
                if (output.equals("true")) {
                    recreate();
                }
            }
        }
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
