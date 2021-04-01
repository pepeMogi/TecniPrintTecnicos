package co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.R;

public class AdapImgTiket extends RecyclerView.Adapter<AdapImgTiket.ImgAdapterViewHolder> {


    ArrayList<String> uris;
    Context context;

    public AdapImgTiket(ArrayList<String> uris, Context context) {
        this.uris = uris;
        this.context = context;
    }

    @NonNull
    @Override
    public ImgAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_img_tik,parent,false);
        return new ImgAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgAdapterViewHolder holder, int position) {
        Picasso.with(context).load(uris.get(position)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    class ImgAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView img;

        public ImgAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_item_tik);
        }
    }
}
