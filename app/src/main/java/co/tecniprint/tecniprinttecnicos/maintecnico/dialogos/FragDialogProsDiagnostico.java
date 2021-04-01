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

import co.tecniprint.tecniprinttecnicos.EditarCliente;
import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.ReporteTecnico;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.DiagnosticoCompleto;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.MainTecnico;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.ModelDiagnostico;

public class FragDialogProsDiagnostico extends DialogFragment {

    private ProgressBar prbImg, prbSubido, prbActualizado, prbNotoficacion;
    private TextView txtTitulo, txtImg, txtSubido, txtActualizado, txtNotificacion;
    private CardView crvTerminar, crvCompartir, crvActualizarTerminar;

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
        crvCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compartir();
            }
        });
        crvActualizarTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cliente cliente = model.getCliente().getValue();
                String clienteString = new Utilidades().clienteToString(cliente);
                Intent intent = new Intent(requireActivity(), EditarCliente.class);
                intent.putExtra(Constantes.CLIENTE,clienteString);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }

    private void compartir() {

        // este es para que podamos hacer la foto y compartirla
        Cliente cliente = model.getCliente().getValue();
        Diagnostico diagnostico = model.getDiagnosticoCompleto().getValue();
        Tiket tiket = model.getTiket().getValue();
        Maquina maquina = model.getMaquina().getValue();

        DiagnosticoCompleto diagnosticoCompleto = new DiagnosticoCompleto(diagnostico,maquina,cliente,tiket);
        String reporteString = new Utilidades().reporteToString(diagnosticoCompleto);
        Intent intent = new Intent(requireActivity(), ReporteTecnico.class);
        intent.putExtra(Constantes.REPORTE,reporteString);
        startActivity(intent);
        requireActivity().finish();


    }

    private void observarCambios() {
        model.getSubImg().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                prbImg.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
                if (!aBoolean){
                    txtImg.setText("Anexos integrados con exito");
                }
            }
        });

        model.getSubAcualizado().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                prbActualizado.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
                   if (!aBoolean) {
                       txtActualizado.setText("Informacion actualizada con exito");
                   }
            }
        });

        model.getSubDiagnostico().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                prbSubido.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
                if (!aBoolean) {
                    txtSubido.setText("Diagnostico creado con exito");
                }
            }
        });

        model.getSubNotificacion().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                prbNotoficacion.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
                if (!aBoolean) {
                    txtNotificacion.setText("Torre de control notificada");
                }
            }
        });

        model.getTerminar().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                txtTitulo.setText("Diagostico creado y porcesado con Exito");
                verificarCambios();

            }
        });



    }

    private void verificarCambios() {

        Cliente cliente = model.getCliente().getValue();
        if (cliente != null) {


            if (cliente.getEmail().equals("")
                    || cliente.getCc().equals("")
                    || cliente.getCiudad().equals("")) {

                crvActualizarTerminar.setVisibility(View.VISIBLE);
                crvTerminar.setVisibility(View.INVISIBLE);
            }
        }


    }

    private void setWidgets(View v) {

        prbImg = v.findViewById(R.id.prb_img_pros_diag);
        prbSubido = v.findViewById(R.id.prb_sub_pros_diag);
        prbActualizado = v.findViewById(R.id.prb_act_pros_diag);
        prbNotoficacion = v.findViewById(R.id.prb_not_pros_diag);
        txtTitulo = v.findViewById(R.id.txt_tit_pros_diag);

        txtImg = v.findViewById(R.id.txt_pros_imgs);
        txtActualizado = v.findViewById(R.id.txt_act_sub);
        txtSubido = v.findViewById(R.id.txt_pros_sub);
        txtNotificacion = v.findViewById(R.id.txt_not_sub);

        crvTerminar = v.findViewById(R.id.crv_btn_terminar);
        crvCompartir = v.findViewById(R.id.crv_btn_compartir);
        crvActualizarTerminar = v.findViewById(R.id.crv_btn_terminar_act);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_dilog_diag_exito, container);
    }



}
