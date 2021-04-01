package co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.adaptadoresglobales.AdapImgMaquinas;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;

public class AdapMaquinas extends FirestoreRecyclerAdapter<Maquina, AdapMaquinas.MaquinasViewHolder> {

    private Context context;
    OnItemClickListener listener;

    public AdapMaquinas(@NonNull FirestoreRecyclerOptions<Maquina> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MaquinasViewHolder holder, int position, @NonNull Maquina maquina) {
        Picasso.with(context).load(maquina.getImg()).fit().into(holder.img);
        holder.txtNombre.setText(maquina.getId().replaceAll("_", " "));
        holder.txtTipo.setText(maquina.getTipo());
    }

    @NonNull
    @Override
    public MaquinasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maquinas, parent, false);
        return new MaquinasViewHolder(v);
    }

    class MaquinasViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txtNombre, txtTipo;

        public MaquinasViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_it_maq);
            txtNombre = itemView.findViewById(R.id.txt_it_maq);
            txtTipo = itemView.findViewById(R.id.txt_it_maq_tipo);
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
