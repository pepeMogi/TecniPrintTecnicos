package co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;

public class AdapMaquinasContadores extends FirestoreRecyclerAdapter<Maquina, AdapMaquinasContadores.MaquinasViewHolder> {

    private Context context;

    public AdapMaquinasContadores(@NonNull FirestoreRecyclerOptions<Maquina> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MaquinasViewHolder holder, int position, @NonNull Maquina maquina) {
        Picasso.with(context).load(maquina.getImg()).fit().into(holder.img);
        holder.txtId.setText(maquina.getId().replaceAll(" ", "_"));
        holder.txtTipo.setText(maquina.getTipo());
        holder.edtConBn.setText(maquina.getContadorBN());
        holder.edtConColor.setText(maquina.getContadorColor());
    }

    @NonNull
    @Override
    public MaquinasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maquinas_contador, parent, false);
        return new MaquinasViewHolder(v);
    }

    class MaquinasViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txtId, txtTipo;
        EditText edtConBn, edtConColor;

        public MaquinasViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_maq_con);
            txtId = itemView.findViewById(R.id.txt_tip_con);
            txtTipo = itemView.findViewById(R.id.txt_id_con);
            edtConBn = itemView.findViewById(R.id.edt_bn_con);
            edtConColor = itemView.findViewById(R.id.edt_col_con);

        }
    }
}
