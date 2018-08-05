package sejarah.uhamka.cilacaptourism.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import sejarah.uhamka.cilacaptourism.Activity.DetailActivity;
import sejarah.uhamka.cilacaptourism.Model.ModelList;
import sejarah.uhamka.cilacaptourism.R;

public class AdapterList extends RecyclerView.Adapter<AdapterList.Holder> {
    private List<ModelList> modelLists;
    private Context context;
    private View view;

    public AdapterList(List<ModelList> modelLists, Context context) {
        this.modelLists = modelLists;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorFilter(ContextCompat.getColor(context, R.color.accent), PorterDuff.Mode.SRC_IN );
        circularProgressDrawable.start();
        final ModelList modelList = modelLists.get(i);
        holder.setIsRecyclable(false);
        holder.tvTitle.setText(modelList.getName());
        holder.tvRegional.setText(modelList.getRegional());
        Glide.with(context)
                .load(modelList.getImg())
                .apply(new RequestOptions()
                        .placeholder(circularProgressDrawable))
                .into(holder.imgCard);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", modelList.getName());
                ((AppCompatActivity) context).startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelLists.size();
    }

    public void setFilter(List<ModelList> filterModelLists) {
        modelLists = new ArrayList<>();
        modelLists.addAll(filterModelLists);
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imgCard;
        TextView tvTitle, tvRegional;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imgCard = itemView.findViewById(R.id.img_card);
            tvTitle = itemView.findViewById(R.id.title_card);
            tvRegional = itemView.findViewById(R.id.regional_card);
        }
    }
}
