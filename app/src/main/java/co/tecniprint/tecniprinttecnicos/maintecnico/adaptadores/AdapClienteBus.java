package co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.ClienteAlgolia;

public class AdapClienteBus extends RecyclerView.Adapter<AdapClienteBus.ClineteViewHolder> {

    private ArrayList<ClienteAlgolia> clientes = new ArrayList<>();
    private Context context;
    OnItemClickListener listener;

    public AdapClienteBus(ArrayList<ClienteAlgolia> clientes, Context context) {
        this.clientes = clientes;
        this.context = context;
    }

    @NonNull
    @Override
    public ClineteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente_crear_tiket,parent,false);
        return new ClineteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClineteViewHolder holder, int position) {

        ClienteAlgolia cliente = clientes.get(position);

        Picasso.with(context).load(cliente.getImg()).placeholder(context.getResources().getDrawable(R.drawable.ic_holder)).fit().into(holder.img);
        holder.txtNombre.setText(cliente.getNombre());
        holder.txtCc.setText(cliente.getCc());
        holder.txtDireccion.setText(cliente.getDireccion());
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    class ClineteViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView txtNombre, txtCc, txtDireccion;

        public ClineteViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_it_cli);
            txtNombre = itemView.findViewById(R.id.txt_nom_it_cli);
            txtCc = itemView.findViewById(R.id.txt_cc_it_cli);
            txtDireccion = itemView.findViewById(R.id.txt_dir_it_cli);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
