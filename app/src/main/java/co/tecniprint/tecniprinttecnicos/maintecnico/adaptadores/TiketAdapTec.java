package co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;

public class TiketAdapTec extends FirestoreRecyclerAdapter<Tiket, TiketAdapTec.TiketVieHoldetTec> {

    OnItemClickListener listener;

    public TiketAdapTec(@NonNull FirestoreRecyclerOptions<Tiket> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final TiketVieHoldetTec holder, int position, @NonNull Tiket tiket) {

        holder.crvPrioridad.setCardBackgroundColor(getPrioridad(tiket.getPrioridad()));

        Log.e("Tikets => ", String.valueOf(position));
        holder.txtNumero.setText(tiket.getId());
        holder.txtTipo.setText(obtenerTipo(tiket.getTipo()));
        holder.txtPrioridad.setText(getPrioridadText(tiket.getPrioridad()));
        holder.txtNombre.setText(obtenerNombre(tiket));
        holder.txtMaquinas.setText(tiket.getIdMaquina().replaceAll("_"," "));
        holder.txtFallo.setText(tiket.getFalla());






    }

    private String obtenerNombre(Tiket tiket) {

        String nombre = tiket.getNombre();
        if (!tiket.getSolicitante().equals("")){
            nombre += "/" + tiket.getSolicitante();
        }

        return nombre;
    }

    private String obtenerTipo(String tipo) {

        if (tipo.equals("")){
            return " ";
        }else{
            return tipo;
        }

    }


    private int getPrioridad(int prioridad) {

        switch (prioridad) {
            case 0:
                return -9277328;
            case 1:
                return -15754899;

            case 2:
                return -601831;

            case 3:
                return -91882;

            case 4:
                return -1303749;

            default:
                return -9277328;
        }

    }

    private String getPrioridadText(int prioridad) {
        switch (prioridad) {
            case 0:
                return "Sin Prioridad";
            case 1:
                return "Baja";

            case 2:
                return "Media";

            case 3:
                return "Alta";

            case 4:
                return "Rellamada";

            default:
                return "Sin Prioridad";
        }
    }

    @NonNull
    @Override
    public TiketVieHoldetTec onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tiket_main, parent, false);
        return new TiketVieHoldetTec(v);
    }

    class TiketVieHoldetTec extends RecyclerView.ViewHolder {

        TextView txtPrioridad, txtTipo, txtNumero, txtNombre, txtMaquinas,txtFallo;
        CardView crvPrioridad;

        public TiketVieHoldetTec(@NonNull View itemView) {
            super(itemView);

            txtPrioridad = itemView.findViewById(R.id.txt_crd_pri);
            txtTipo = itemView.findViewById(R.id.txt_crd_tipo);
            txtNombre = itemView.findViewById(R.id.txt_crd_nom);
            txtNumero = itemView.findViewById(R.id.txt_crd_num);
            txtFallo = itemView.findViewById(R.id.txt_crd_lis_fall);
            txtMaquinas = itemView.findViewById(R.id.txt_crd_lis_maq);

            crvPrioridad = itemView.findViewById(R.id.crv_it_pri);



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
