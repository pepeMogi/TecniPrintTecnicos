package co.tecniprint.tecniprinttecnicos.adaptadoresglobales;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.R;

public class AdapImgAnexos extends RecyclerView.Adapter<AdapImgAnexos.AnexoViewHolder> {

    private ArrayList<Uri> imagenes;
    private Context context;

    public AdapImgAnexos(ArrayList<Uri> imagenes, Context context) {
        this.imagenes = imagenes;
        this.context = context;
    }

    @NonNull
    @Override
    public AnexoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_anexos,parent,false);
        return new AnexoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnexoViewHolder holder, int position) {
        Picasso.with(context).load(imagenes.get(position)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }

    class AnexoViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        public AnexoViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_it_anexos);
        }
    }
}
