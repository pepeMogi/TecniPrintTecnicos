package co.tecniprint.tecniprinttecnicos.maintecnico.dialogos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
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
import co.tecniprint.tecniprinttecnicos.modelos.ModelMaquinaCrear;

public class FragDialogCreaMaq extends DialogFragment {

    private ProgressBar prbImg, prbSubido, prbActualizado;
    private TextView txtTitulo, txtImg, txtSubido, txtActualizado;
    private CardView crvTerminar;


    private ModelDiagnostico model;
    private Vibrator vibrator;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets(view);
        model = new ViewModelProvider(requireActivity()).get(ModelDiagnostico.class);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        observarCambios();
        funBoton();
    }

    private void funBoton() {
        crvTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), MainTecnico.class);
                startActivity(intent);
                vibrator.vibrate(50);
                requireActivity().finish();
            }
        });
    }

    private void observarCambios() {

        model.getSubImgMaquina().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                prbImg.setVisibility(View.INVISIBLE);
                txtImg.setText("Imagen Subida");

            }
        });

        model.getSubActTiket().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                prbActualizado.setVisibility(View.INVISIBLE);

                txtActualizado.setText("Tiket actualizado");

            }
        });

        model.getSubDatoMaquina().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                prbSubido.setVisibility(View.INVISIBLE);

                txtSubido.setText("Datos de Maquina creados");

            }
        });


        model.getImgTerminar().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                crvTerminar.setVisibility(View.VISIBLE);
                txtTitulo.setText("Maquina creada con exito");

            }
        });

    }

    private void setWidgets(View v) {

        prbImg = v.findViewById(R.id.prb_img_pros_diag_maq);
        prbSubido = v.findViewById(R.id.prb_sub_pros_diag_maq);
        prbActualizado = v.findViewById(R.id.prb_act_pros_diag_maq);

        txtTitulo = v.findViewById(R.id.txt_tit_pros_diag_maq);

        txtImg = v.findViewById(R.id.txt_pros_imgs_maq);
        txtActualizado = v.findViewById(R.id.txt_act_sub_maq);
        txtSubido = v.findViewById(R.id.txt_pros_sub_maq);


        crvTerminar = v.findViewById(R.id.crv_btn_terminar_maq);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_dilog_crae_maq, container);
    }
}
