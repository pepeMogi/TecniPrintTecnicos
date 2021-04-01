package co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;

public class AdapDiagTiketDetalle extends RecyclerView.Adapter<AdapDiagTiketDetalle.DiagnosticosViewHolder> {


    ArrayList<Diagnostico> diagnosticos;
    OnItemClickListener listener;

    public AdapDiagTiketDetalle(ArrayList<Diagnostico> diagnosticos ) {
        this.diagnosticos = diagnosticos;

    }

    @NonNull
    @Override
    public DiagnosticosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diagnosticos_tik_det,parent,false);
        return new DiagnosticosViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull DiagnosticosViewHolder holder, int position) {
        Diagnostico dia = diagnosticos.get(position);

        holder.txtNombre.setText(dia.getTecnico());
        holder.txtFecha.setText(getFecha(dia.getFecha()));
        holder.txtDiagnostico.setText(dia.getDiagnostico());
    }

    @Override
    public int getItemCount() {
        return diagnosticos.size();
    }

    private String getFecha(Timestamp fecha) {

        SimpleDateFormat format = new SimpleDateFormat("dd - MMMM - yyyy");
        return format.format(fecha.toDate()).toUpperCase();
    }


    class DiagnosticosViewHolder extends RecyclerView.ViewHolder{

        TextView txtNombre,txtFecha,txtDiagnostico;

        public DiagnosticosViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txt_it_dia_nom);
            txtFecha = itemView.findViewById(R.id.txt_it_dia_fec);
            txtDiagnostico = itemView.findViewById(R.id.txt_it_dia_dia);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(diagnosticos.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Diagnostico diagnostico);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
