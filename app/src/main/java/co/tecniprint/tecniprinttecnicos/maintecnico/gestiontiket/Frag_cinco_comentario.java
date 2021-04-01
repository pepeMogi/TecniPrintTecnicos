package co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;

public class Frag_cinco_comentario extends Fragment {

    private EditText edtcomentario;

    private CardView crvSiguiente, crvAtras;

    private ModelDiagnostico model;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets(view);
        model = new ViewModelProvider(requireActivity()).get(ModelDiagnostico.class);


        funBotonesNav();
    }


    private void funBotonesNav() {

        crvSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtcomentario.getText().toString().isEmpty()){
                    String comentario = edtcomentario.getText().toString();
                    model.setComentario(comentario);
                }
                Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(5,true);
            }
        });

        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               requireActivity().onBackPressed();
            }
        });
    }

    private void setWidgets(View v) {



        edtcomentario = v.findViewById(R.id.edt_com_tik);
        crvSiguiente = v.findViewById(R.id.crv_btn_tres_sig);
        crvAtras = v.findViewById(R.id.crv_btn_tres_atr);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_cinco_comentario,container,false);
        return v;
    }
}
