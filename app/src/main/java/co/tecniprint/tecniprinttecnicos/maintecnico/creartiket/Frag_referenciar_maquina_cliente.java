package co.tecniprint.tecniprinttecnicos.maintecnico.creartiket;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores.AdapMaquinas;

public class Frag_referenciar_maquina_cliente extends Fragment {

    private EditText edtReferenciar;
    private CardView crvAtras, crvReferenciar;
    private Vibrator vibrator;
    private ModelNewTikete model;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidget(view);
        model = new ViewModelProvider(requireActivity()).get(ModelNewTikete.class);
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        funBotones();


    }

    private void funBotones() {
        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(2, true);
            }
        });

        crvReferenciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Utilidades().verificarEdt(edtReferenciar)) {

                    String referencia = "REF: " + edtReferenciar.getText().toString();
                    Maquina maquina = new Maquina("","","",referencia,"","",0,0);
                    model.setMaquina(maquina);
                    vibrator.vibrate(100);
                    Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(3, true);
                }
            }
        });
    }


    private void setWidget(View v) {

        edtReferenciar = v.findViewById(R.id.edt_ref_maq);


        crvAtras = v.findViewById(R.id.crv_btn_atras_ref_maq);

        crvReferenciar = v.findViewById(R.id.crv_btn_crear_ref_maq);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_referenciar_maquina_cliente, container, false);
        return v;
    }
}
