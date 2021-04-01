package co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Repuesto;
import co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores.AdapRepuestosL;

public class Frag_dos_repuestos extends Fragment {

    private RecyclerView rcvRepuestos;
    private EditText edtRepuestoNom, edtRepuestoRef, edtRepuestoCan, edtValor;
    private CardView crvAgregar, crvSiguinente, crvAtras;
    private RadioGroup rdgTipo;


    private ModelDiagnostico model;
    private Utilidades utilidades;
    private ArrayList<Repuesto> repuestosGlobal;
    private AdapRepuestosL adaptador;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidget(view);
        model = new ViewModelProvider(requireActivity()).get(ModelDiagnostico.class);
        utilidades = new Utilidades();


        funBoton();
        inicializarRecycler();
        escucharRepuestos();

    }

    private void escucharRepuestos() {

        model.getRepuestos().observe(requireActivity(), new Observer<ArrayList<Repuesto>>() {
            @Override
            public void onChanged(ArrayList<Repuesto> repuestos) {
                adaptador = new AdapRepuestosL(repuestos,getContext());
                rcvRepuestos.setAdapter(adaptador);

                adaptador.setOnItemClickListener(new AdapRepuestosL.OnItemClickListener() {
                    @Override
                    public void onItemClick(Repuesto repuesto) {
                        model.borrarRepuesto(repuesto);
                    }
                });
            }
        });
    }

    private void inicializarRecycler() {

        rcvRepuestos.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        rcvRepuestos.setLayoutManager(llm);
    }


    private void funBoton() {
        crvSiguinente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            siguiente();

            }
        });

        crvAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utilidades.verificarEdt(edtRepuestoNom) & utilidades.verificarEdt(edtRepuestoRef)) {
                    llenarRepuesto();
                }
            }
        });

        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });


    }

    private void siguiente() {

        Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(2,true);
    }

    private void llenarRepuesto() {

        String tipo = getTipoRepuesto(rdgTipo.getCheckedRadioButtonId());

        String nombre = edtRepuestoNom.getText().toString();
        String referencia = edtRepuestoRef.getText().toString();
        String valor = edtValor.getText().toString();
        int cantidad = 1;

        if (!edtRepuestoCan.getText().toString().isEmpty()) {
            cantidad = Integer.parseInt(edtRepuestoCan.getText().toString());
        }

        Repuesto repuesto = new Repuesto(tipo, nombre, referencia, cantidad,Long.parseLong(valor));



        if (model.getRepuestos().getValue() == null) {
            ArrayList<Repuesto> repuestos = new ArrayList<>();
            repuestos.add(repuesto);
            model.setRepuestos(repuestos);

        } else {
            model.setRepuesto(repuesto);
        }

        edtRepuestoCan.setText("");
        edtRepuestoNom.setText("");
        edtRepuestoRef.setText("");
        edtValor.setText("");



    }

    private String getTipoRepuesto(int id) {

        switch (id) {
            case R.id.rdbOne:
                return "facturable";

            case R.id.rdbTwo:
                return "cotizacion";

            case R.id.rdbThree:
                return "garantia";


            default:
                return "facturable";

        }
    }


    private void setWidget(View v) {


        rcvRepuestos = v.findViewById(R.id.txt_rep_usa_gest);

        edtRepuestoNom = v.findViewById(R.id.edt_nom_rep);
        edtRepuestoRef = v.findViewById(R.id.edt_ref_rep);
        edtRepuestoCan = v.findViewById(R.id.edt_und_rep);
        edtValor = v.findViewById(R.id.edt_val_rep);
        crvAgregar = v.findViewById(R.id.crv_agre_rep);
        crvSiguinente = v.findViewById(R.id.crv_btn_uno_sig);
        crvAtras = v.findViewById(R.id.crv_btn_uno_atra);
        rdgTipo = v.findViewById(R.id.rdgGrupo);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_dos_repuestos, container, false);
        return v;
    }


}
