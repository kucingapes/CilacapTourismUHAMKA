package sejarah.uhamka.cilacaptourism.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import sejarah.uhamka.cilacaptourism.Model.ModelPhotos;
import sejarah.uhamka.cilacaptourism.R;

public class AdapterPhotos extends RecyclerView.Adapter<AdapterPhotos.Holder> {
    private List<ModelPhotos> modelPhotos;
    private Context context;

    public AdapterPhotos(List<ModelPhotos> modelPhotos, Context context) {
        this.modelPhotos = modelPhotos;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo_full, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        ModelPhotos photos = modelPhotos.get(i);
        holder.setIsRecyclable(false);
        Glide.with(context).load(photos.getImg()).into(holder.imgFull);
    }

    @Override
    public int getItemCount() {
        return modelPhotos.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        PhotoView imgFull;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imgFull = itemView.findViewById(R.id.item_full_img);
        }
    }
}
