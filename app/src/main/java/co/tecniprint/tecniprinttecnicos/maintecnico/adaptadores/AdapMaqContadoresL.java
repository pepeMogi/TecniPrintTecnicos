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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.ModelDiagnostico;

public class AdapMaqContadoresL extends RecyclerView.Adapter<AdapMaqContadoresL.MaquinasViewHolder> {

    private ArrayList<Maquina> maquinas;
    private Context context;
    private ModelDiagnostico model;

    public AdapMaqContadoresL(ArrayList<Maquina> maquinas, Context context, ModelDiagnostico model) {
        this.maquinas = maquinas;
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public MaquinasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maquinas_contador,parent,false);
        return new MaquinasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MaquinasViewHolder holder, int position) {
        final Maquina maquina = maquinas.get(position);
        Picasso.with(context).load(maquina.getImg()).fit().into(holder.img);
        holder.txtId.setText(maquina.getId().replaceAll("_", " "));
        holder.txtTipo.setText(maquina.getTipo());
        holder.edtConBn.setText(maquina.getContadorBN());
        holder.edtConColor.setText(maquina.getContadorColor());


       /* holder.edtConBn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!maquina.getContadorBN().equals(holder.edtConBn.getText().toString())){
                        model.setActulizarContadores(true);
                        maquina.setContadorBN(holder.edtConBn.getText().toString());
                        model.setMaquinas(maquina);
                    }
                }
            }
        });*/

      /*  holder.edtConColor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!maquina.getContadorColor().equals(holder.edtConBn.getText().toString())){
                        model.setActulizarContadores(true);
                        maquina.setContadorColor(holder.edtConColor.getText().toString());
                        model.setMaquinas(maquina);
                    }
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return maquinas.size();
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
