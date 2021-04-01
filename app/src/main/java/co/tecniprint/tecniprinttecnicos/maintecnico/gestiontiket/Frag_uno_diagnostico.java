package co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;

public class Frag_uno_diagnostico extends Fragment {

    private TextView txtFallo, txtRepuestos, txtMaquina;
    private EditText edtDiagnostico, edtSolucion;
    private CardView  crvSiguinente, crvCrearMaquina;


    private ModelDiagnostico model;
    private Vibrator vibrator;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidget(view);
        model = new ViewModelProvider(requireActivity()).get(ModelDiagnostico.class);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (model.getTiket().getValue() != null){
            llenarTiket(model.getTiket().getValue());
        }


        funBoton();
    }

    private void funBoton() {

        crvSiguinente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtDiagnostico.getText().toString().isEmpty() & edtSolucion.getText().toString().isEmpty()) {

                    Toast.makeText(requireContext(),"Diagnostico y Solucion en necesario para conrinuar",Toast.LENGTH_LONG).show();

                }else{
                    siguiente();
                }

            }
        });

        crvCrearMaquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                    Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(7,true);
            }
        });




    }

    private void siguiente() {

        model.setDiagnostico(edtDiagnostico.getText().toString());
        model.setSolcucion(edtSolucion.getText().toString());

        if (!txtMaquina.getText().toString().substring(0,3).equals("REF")){

        Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(1,true);
        }else{
            Toast.makeText(requireContext(), "Crea Maquina referenciada para continuar", Toast.LENGTH_LONG).show();
        }

    }

    private void llenarTiket(Tiket tiket) {

        txtMaquina.setText(tiket.getIdMaquina().replaceAll("_"," "));
        txtFallo.setText(tiket.getFalla());

        if (tiket.getIdMaquina().substring(0,3).equals("REF")){
            crvCrearMaquina.setVisibility(View.VISIBLE);
        }

    }

    private void setWidget(View v) {



        txtFallo = v.findViewById(R.id.txt_ges_des_fallo);
        txtRepuestos = v.findViewById(R.id.txt_rep_usa_gest);
        txtMaquina = v.findViewById(R.id.txt_det_maq);
        edtDiagnostico = v.findViewById(R.id.edt_dia_dia);
        edtSolucion = v.findViewById(R.id.edt_dia_sol);





        crvSiguinente = v.findViewById(R.id.crv_btn_uno_sig);
        crvCrearMaquina = v.findViewById(R.id.crv_crear_maquina);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_uno_diagnostico, container, false);
        return v;
    }
}
