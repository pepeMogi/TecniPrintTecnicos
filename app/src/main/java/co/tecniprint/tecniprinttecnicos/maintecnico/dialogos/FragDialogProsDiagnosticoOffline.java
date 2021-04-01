package co.tecniprint.tecniprinttecnicos.maintecnico.dialogos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.maintecnico.MainTecnico;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.ModelDiagnostico;

public class FragDialogProsDiagnosticoOffline extends DialogFragment {

    private ProgressBar prbImg;
    private TextView txtTitulo;
    private CardView crvTerminar;

    private ModelDiagnostico model;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets(view);
        model = new ViewModelProvider(requireActivity()).get(ModelDiagnostico.class);
        observarCambios();
        funBoton();
    }

    private void funBoton() {
        crvTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), MainTecnico.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }

    private void observarCambios() {

        model.getTerminar().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                crvTerminar.setVisibility(View.VISIBLE);
                if (aBoolean){
                    prbImg.setVisibility(View.INVISIBLE);
                }
                txtTitulo.setText("Cartepa Creada con exito");

            }
        });

    }

    private void setWidgets(View v) {

        prbImg = v.findViewById(R.id.prb_img_pros_diag_off);

        txtTitulo = v.findViewById(R.id.txt_tit_pros_diag_off);




        crvTerminar = v.findViewById(R.id.crv_btn_terminar_off);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_dilog_diag_exito_offline, container);
    }
}
