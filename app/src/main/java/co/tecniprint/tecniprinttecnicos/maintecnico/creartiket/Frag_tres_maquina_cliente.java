package co.tecniprint.tecniprinttecnicos.maintecnico.creartiket;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores.AdapMaquinas;

public class Frag_tres_maquina_cliente extends Fragment {

    private RecyclerView rcvMaquinas;

    private CardView crvReferenciar, crvAtras;
    private AdapMaquinas adaptador;
    private Vibrator vibrator;
    private ModelNewTikete model;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidget(view);
        model = new ViewModelProvider(requireActivity()).get(ModelNewTikete.class);
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        escucharCliente();
        funBotones();

    }

    private void funBotones() {

        crvReferenciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(7,true);
            }
        });

        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(1,true);
            }
        });
    }

    private void escucharCliente() {

        model.getCliente().observe(requireActivity(), new Observer<Cliente>() {
            @Override
            public void onChanged(Cliente cliente) {
                llenarRecycler(cliente);
            }
        });


    }

    private void llenarRecycler(Cliente cliente) {

        rcvMaquinas.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        rcvMaquinas.setLayoutManager(llm);

        Query query = FirebaseFirestore.getInstance()
                .collection(Constantes.MAQUINAS).whereEqualTo("cliente",cliente.getId());

        FirestoreRecyclerOptions<Maquina> options = new FirestoreRecyclerOptions.Builder<Maquina>()
                .setQuery(query, Maquina.class)
                .build();
        // adaptador
        adaptador = new AdapMaquinas(options, getContext());
        adaptador.startListening();
        rcvMaquinas.setAdapter(adaptador);

        adaptador.setOnItemClickListener(new AdapMaquinas.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Maquina maquina = documentSnapshot.toObject(Maquina.class);
                model.setMaquina(maquina);
                Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(3,true);
            }
        });
    }


    private void setWidget(View v) {

        rcvMaquinas = v.findViewById(R.id.rcv_tres_maquinas);


        crvAtras = v.findViewById(R.id.crv_btn_atras_dos_cre_tik);

        crvReferenciar = v.findViewById(R.id.crv_btn_ref_tres_maq);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tres_maquina_cliente, container, false);
        return v;
    }

    @Override
    public void onStart() {
        if (adaptador != null){
            adaptador.startListening();
        }
        super.onStart();
    }
}
