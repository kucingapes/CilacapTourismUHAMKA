package sejarah.uhamka.cilacaptourism.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sejarah.uhamka.cilacaptourism.Model.ModelList;
import sejarah.uhamka.cilacaptourism.R;

public class AdapterSearchMaps extends RecyclerView.Adapter<AdapterSearchMaps.Holder> {
    private List<ModelList> modelLists;
    private Context context;
    private View view;
    private String title;
    private String lat;
    private String lng;
    private OnItemClick itemClick;

    public interface OnItemClick{
        void onItemClick(ModelList itemList);
    }


    /*public AdapterSearchMaps(List<ModelList> modelLists, Context context) {
        this.modelLists = modelLists;
        this.context = context;
    }*/

    public AdapterSearchMaps(List<ModelList> modelLists, Context context, OnItemClick itemClick) {
        this.modelLists = modelLists;
        this.context = context;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_search_maps, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        /*final ModelList modelList = modelLists.get(position);
        holder.textView.setText(modelList.getName());
        *//*itemClick.onItemClick(modelLists.get(position));*//*
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = modelList.getName();
                lat = modelList.getLat();
                lng = modelList.getLng();

            }
        });*/
        holder.bind(modelLists.get(position), itemClick);
    }

    public void getLocation(Double dLat, Double dLng){
        dLat = Double.parseDouble(lat);
        dLng = Double.parseDouble(lng);
    }

    public void setFilter(List<ModelList> filterModelLists) {
        modelLists = new ArrayList<>();
        modelLists.addAll(filterModelLists);
    }

    @Override
    public int getItemCount() {
        return modelLists.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textView;
        public Holder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_search_maps);
        }

        public void bind(final ModelList modelList, final OnItemClick itemClick) {
            textView.setText(modelList.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.onItemClick(modelList);
                }
            });
        }
    }
}
