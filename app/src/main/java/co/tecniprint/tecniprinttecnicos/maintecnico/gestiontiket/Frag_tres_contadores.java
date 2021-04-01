package co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;

public class Frag_tres_contadores extends Fragment {


    private CardView crvSiguiente, crvAtras;
    private ModelDiagnostico model;
    private ImageView imgMaquina;
    private EditText edtConBN, edtConCol;
    private TextView txtTipo, txtIdMaquina;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets(view);
        model = new ViewModelProvider(requireActivity()).get(ModelDiagnostico.class);
        //  llenarRecycler();

        funBotonesNav();
        escucharMaquina();
    }

    private void escucharMaquina() {
        model.getMaquina().observe(requireActivity(), new Observer<Maquina>() {
            @Override
            public void onChanged(Maquina maquina) {
                Log.e("llenarMaquina", maquina.getId());
                llenarMaquina(maquina);
            }
        });
    }


    private void llenarMaquina(Maquina maquina) {

        Picasso.with(requireContext()).load(maquina.getImg()).fit().into(imgMaquina);
        txtTipo.setText(maquina.getTipo());
        txtIdMaquina.setText(maquina.getId().replaceAll("_", " "));
        edtConBN.setHint(String.valueOf(maquina.getContadorBN()));
        edtConCol.setHint(String.valueOf(maquina.getContadorColor()));
    }


    private void funBotonesNav() {


        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        crvSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                siguiente();

            }
        });
    }

    private void siguiente() {

        String hintBn = edtConBN.getHint().toString();
        String hintCol = edtConCol.getHint().toString();

        String conCol = hintCol;
        String conBn = hintBn;

        if (!edtConCol.getText().toString().isEmpty()){
            conCol = edtConCol.getText().toString();
        }

        if (!edtConBN.getText().toString().isEmpty()){
            conBn = edtConBN.getText().toString();
        }






        if (!hintBn.equals(conBn) || !hintCol.equals(conCol)){

            model.setActContadores(true);
            Maquina maquina = model.getMaquina().getValue();
            if (maquina != null) {
                maquina.setContadorBN(Integer.parseInt(conBn));
                maquina.setContadorColor(Integer.parseInt(conCol));
                model.setMaquina(maquina);
            }else{
                if (model.getTiket().getValue().getIdMaquina() != null &&
                        !model.getTiket().getValue().getIdMaquina().substring(0,3).equals("REF"))
                maquina = new Maquina();
                maquina.setId(model.getTiket().getValue().getIdMaquina());
                if (!edtConBN.getText().toString().isEmpty()){
                    maquina.setContadorBN(Integer.parseInt(edtConBN.getText().toString()));
                }
                if (!edtConCol.getText().toString().isEmpty()){
                    maquina.setContadorColor(Integer.parseInt(edtConCol.getText().toString()));
                }
                model.setMaquina(maquina);

            }
        }



        Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(3, true);
    }

    private void setWidgets(View v) {


        crvSiguiente = v.findViewById(R.id.crv_btn_tres_sig_mas);
        crvAtras = v.findViewById(R.id.crv_btn_tres_atr_mas);

        imgMaquina = v.findViewById(R.id.img_maq_cua);
        edtConBN = v.findViewById(R.id.edt_con_btn_car);
        edtConCol = v.findViewById(R.id.edt_con_col_car);
        txtTipo = v.findViewById(R.id.txt_tipo_tres);
        txtIdMaquina = v.findViewById(R.id.txt_id_tres);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tres_contadores, container, false);
        return v;
    }


}
