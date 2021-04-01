package co.tecniprint.tecniprinttecnicos.maintecnico.creartiket;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.ClienteAlgolia;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;

public class Frag_dos_datos_cliente extends Fragment {

    private EditText edtNombre, edtDireccion, edtCiudad, edtCelular, edtCelularDos, edtCorreo, edtSolicitante;
    private ImageView img;
    private CardView crvSiguiente, crvAtras;
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
        crvSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades utilidades = new Utilidades();
                if (utilidades.verificarEdt(edtNombre)
                        & utilidades.verificarEdt(edtDireccion)
                        & utilidades.verificarEdt(edtCelular)){

                    siguiente();


                }
            }
        });

        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(0,true);
            }
        });
    }

    private void siguiente() {

        String solicitante = edtSolicitante.getText().toString().isEmpty() ? "" : edtSolicitante.getText().toString();
        String celularSolicitante = edtCelularDos.getText().toString().isEmpty() ? "" : edtCelularDos.getText().toString();

        Cliente clienteFinal = getCliente();
        model.setClienteFinal(clienteFinal);
        model.setSolicitante(solicitante);
        model.setCelularSolicitante(celularSolicitante);
        vibrator.vibrate(50);
        Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(2,true);
    }

    private Cliente getCliente() {


        String direccion = edtDireccion.getText().toString();
        String ciudad = edtCiudad.getText().toString();
        String celular = edtCelular.getText().toString();
        String email = edtCorreo.getText().toString();


        Cliente cliente = model.getCliente().getValue();
        assert cliente != null;
        cliente.setDireccion(direccion);
        cliente.setCiudad(ciudad);
        cliente.setCelular(celular);
        cliente.setEmail(email);

        return cliente;

    }

    private void escucharCliente() {

        model.getCliente().observe(requireActivity(), new Observer<Cliente>() {
            @Override
            public void onChanged(Cliente cliente) {
                llenarCliente(cliente);
            }
        });
    }

    private void llenarCliente(Cliente cliente) {

        Picasso.with(requireContext()).load(cliente.getImg()).placeholder(requireContext().getResources().getDrawable(R.drawable.ic_holder)).fit().into(img);
        edtNombre.setText(cliente.getNombre());
        edtDireccion.setText(cliente.getDireccion());
        edtCiudad.setText(cliente.getCiudad());
        edtCelular.setText(cliente.getCelular());
       // edtCelularDos.setText(cliente.getCelularDos());
        edtCorreo.setText(cliente.getEmail());

    }


    private void setWidget(View v) {

        img = v.findViewById(R.id.img_dos_cliente);
        edtNombre = v.findViewById(R.id.edt_nom_new_cli_dos);
        edtDireccion = v.findViewById(R.id.edt_dir_new_cli_dos);
        edtCiudad = v.findViewById(R.id.edt_ciu_new_cli_dos);
        edtCelular = v.findViewById(R.id.edt_cel_new_cli_dos);
        edtCelularDos = v.findViewById(R.id.edt_cel_dos_new_cli_dos);
        edtCorreo = v.findViewById(R.id.edt_cor_new_cli_dos);
        edtSolicitante = v.findViewById(R.id.edt_sol_new_cli);
        crvAtras = v.findViewById(R.id.crv_btn_atras_dos_cre_tik);
        crvSiguiente = v.findViewById(R.id.crv_btn_sig_dos_cre_tik);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_dos_datos_cliente, container, false);
        return v;
    }
}
