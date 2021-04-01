package co.tecniprint.tecniprinttecnicos.adaptadoresglobales;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.R;

public class AdapMaqTik extends RecyclerView.Adapter<AdapMaqTik.MaquinaViewHolder> {

    ArrayList<String> maquinas;

    public AdapMaqTik(ArrayList<String> maquinas) {
        this.maquinas = maquinas;
    }

    @NonNull
    @Override
    public MaquinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maq_det,parent,false);
        return new MaquinaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MaquinaViewHolder holder, int position) {
        holder.txtMaquina.setText(maquinas.get(position).replaceAll("_"," "));

    }

    @Override
    public int getItemCount() {
        return maquinas.size();
    }

    class MaquinaViewHolder extends RecyclerView.ViewHolder{

        TextView txtMaquina;
        public MaquinaViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMaquina = itemView.findViewById(R.id.txt_it_maq_id);
        }
    }
}
