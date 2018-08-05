package sejarah.uhamka.cilacaptourism.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import sejarah.uhamka.cilacaptourism.Activity.FullPhoto;
import sejarah.uhamka.cilacaptourism.Model.ModelPhotos;
import sejarah.uhamka.cilacaptourism.R;

public class AdapterThumnail extends RecyclerView.Adapter<AdapterThumnail.Holder> {
    private List<ModelPhotos> modelPhotos;
    private Context context;
    private View view;

    public AdapterThumnail(List<ModelPhotos> modelPhotos, Context context) {
        this.modelPhotos = modelPhotos;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.item_gallery, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        final ModelPhotos photos = modelPhotos.get(i);
        holder.setIsRecyclable(false);
        Glide.with(context).load(photos.getImg()).into(holder.imagePhotos);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullPhoto.class);
                intent.putExtra("position", i);
                intent.putExtra("id", photos.getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelPhotos.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView imagePhotos;
        Holder(@NonNull View itemView) {
            super(itemView);
            imagePhotos = itemView.findViewById(R.id.item_photo);
        }
    }
}
