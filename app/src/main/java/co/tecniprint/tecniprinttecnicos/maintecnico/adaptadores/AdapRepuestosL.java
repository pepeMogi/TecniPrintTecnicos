package co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Repuesto;

public class AdapRepuestosL extends RecyclerView.Adapter<AdapRepuestosL.RepuestosViewHolder> {

    private ArrayList<Repuesto> repuestos;
    private Context context;
    OnItemClickListener listener;

    public AdapRepuestosL(ArrayList<Repuesto> repuestos, Context context) {
        this.repuestos = repuestos;
        this.context = context;
    }

    @NonNull
    @Override
    public RepuestosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repuestos,parent,false);
        return new RepuestosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RepuestosViewHolder holder, int position) {
        Repuesto repuesto = repuestos.get(position);
        holder.txtTipo.setText(repuesto.getTipo());
        holder.txtRef.setText(repuesto.getReferencia());
        holder.txtNombre.setText(repuesto.getNombre());
        holder.txtCan.setText(String.valueOf(repuesto.getCantidad()));

    }

    @Override
    public int getItemCount() {
        return repuestos.size();
    }

    class RepuestosViewHolder extends RecyclerView.ViewHolder {

        TextView txtTipo, txtRef, txtNombre, txtCan;
        ImageView imgBorrar;

        public RepuestosViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTipo = itemView.findViewById(R.id.txt_it_tipo);
            txtRef = itemView.findViewById(R.id.txt_it_ref);
            txtNombre = itemView.findViewById(R.id.txt_it_nom);
            txtCan = itemView.findViewById(R.id.txt_it_can);
            imgBorrar = itemView.findViewById(R.id.img_it_pro);
            imgBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(repuestos.get(position));
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Repuesto repuesto);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
