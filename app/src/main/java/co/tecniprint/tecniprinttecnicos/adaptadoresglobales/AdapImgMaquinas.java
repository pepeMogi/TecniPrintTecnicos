package co.tecniprint.tecniprinttecnicos.adaptadoresglobales;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.MaquinaDefault;

public class AdapImgMaquinas extends FirestoreRecyclerAdapter<MaquinaDefault, AdapImgMaquinas.MaquinasViewHolder> {

    private Context context;
    OnItemClickListener listener;
    public AdapImgMaquinas(@NonNull FirestoreRecyclerOptions<MaquinaDefault> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MaquinasViewHolder holder, int position, @NonNull MaquinaDefault maquina) {
        Picasso.with(context).load(maquina.getImg()).fit().into(holder.img);
        Log.e("maquina encotrada", maquina.getImg());
    }

    @NonNull
    @Override
    public MaquinasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_maquinas,parent,false);
        return new MaquinasViewHolder(v);
    }

    class MaquinasViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        public MaquinasViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_it_maq);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
