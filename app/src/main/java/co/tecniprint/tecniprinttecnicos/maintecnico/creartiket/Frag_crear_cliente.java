package co.tecniprint.tecniprinttecnicos.maintecnico.creartiket;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.ClienteAlgolia;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.maintecnico.dialogos.DialogFragNewCliente;

public class Frag_crear_cliente extends Fragment {

    private EditText edtNombre, edtDireccion, edtCiudad, edtCelular, edtCorreo;
    private CardView crvCrear, crvAtras;

    private ModelNewTikete model;
    private Vibrator vibrator;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidget(view);
        model = new ViewModelProvider(requireActivity()).get(ModelNewTikete.class);
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        funBotones();

    }

    private void funBotones() {

        crvCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades utilidades = new Utilidades();
                if (utilidades.verificarEdt(edtNombre)
                        & utilidades.verificarEdt(edtDireccion)
                        & utilidades.verificarEdt(edtCelular)
                ) {
                   abrirDialog();
                    vibrator.vibrate(100);
                    crearCliente();
                }
            }
        });

        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(0, true);
            }
        });
    }

    private void abrirDialog(){

        FragmentManager fm = requireActivity().getSupportFragmentManager();
        DialogFragNewCliente dialogFragment = new DialogFragNewCliente();
        dialogFragment.setCancelable(false);
        dialogFragment.show(fm, "fragment_edit_name");

    }

    private void crearCliente() {

        String nombre = edtNombre.getText().toString();
        String direccion = edtDireccion.getText().toString();
        String celular = edtCelular.getText().toString();
        String email = edtCorreo.getText().toString();
        String ciudad = edtCiudad.getText().toString();

        final Cliente cliente = new Cliente(nombre, "none", "0", direccion, ciudad, celular, email, "none");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constantes.CLIENTES).document(cliente.getId())
                .set(cliente).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    subirMotorBusqueda(cliente);
                } else {
                    Toast.makeText(requireContext(), "Error en la base de datos", Toast.LENGTH_SHORT).show();
                    Log.e("Error Firestore", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                }
            }
        });
    }

    private void subirMotorBusqueda(Cliente cliente) {

        ClienteAlgolia clienteAlgolia = new ClienteAlgolia(cliente);
        final Client client = new Client(Constantes.APPID, Constantes.APIKEYADMIN);
        Index index = client.getIndex("clientes");


        Gson gson = new Gson();
        String json = gson.toJson(clienteAlgolia);

        // para subir a algolia
        try {
            index.addObjectAsync(new JSONObject(json), null);
            Toast.makeText(requireContext(), "cliente Creado correctamente", Toast.LENGTH_SHORT).show();
            model.setCliente(cliente);
            model.setClienteFinal(cliente);
            model.setTerminarCliente(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setWidget(View v) {

        edtNombre = v.findViewById(R.id.edt_nom_new_cli);
        edtDireccion = v.findViewById(R.id.edt_dir_new_cli);
        edtCiudad = v.findViewById(R.id.edt_ciu_new_cli);
        edtCelular = v.findViewById(R.id.edt_cel_new_cli);
        edtCorreo = v.findViewById(R.id.edt_cor_new_cli);

        crvCrear = v.findViewById(R.id.crv_btn_cre_new_cli);
        crvAtras = v.findViewById(R.id.crv_btn_atras_cre_cli);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_crear_cliente, container, false);
        return v;
    }


}
