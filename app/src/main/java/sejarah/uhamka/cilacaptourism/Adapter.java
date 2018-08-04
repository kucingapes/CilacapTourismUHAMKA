package sejarah.uhamka.cilacaptourism;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    private List<Model> models;
    private Context context;
    private View view;

    public Adapter(List<Model> models, Context context) {
        this.models = models;
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
        final Model model = models.get(i);
        holder.tvTitle.setText(model.getName());
        holder.tvRegional.setText(model.getRegional());
        holder.tvAddress.setText(model.getAddress());
        Glide.with(context).load(model.getImg()).into(holder.imgCard);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", model.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void setFilter(List<Model> filterModels) {
        models = new ArrayList<>();
        models.addAll(filterModels);
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imgCard;
        TextView tvTitle, tvAddress, tvRegional;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imgCard = itemView.findViewById(R.id.img_card);
            tvTitle = itemView.findViewById(R.id.title_card);
            tvAddress = itemView.findViewById(R.id.alamat_card);
            tvRegional = itemView.findViewById(R.id.regional_card);
        }
    }
}
