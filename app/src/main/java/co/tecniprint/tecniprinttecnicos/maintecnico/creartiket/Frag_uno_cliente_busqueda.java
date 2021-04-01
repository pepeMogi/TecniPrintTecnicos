package co.tecniprint.tecniprinttecnicos.maintecnico.creartiket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.ClienteAlgolia;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores.AdapClienteBus;

public class Frag_uno_cliente_busqueda extends Fragment {

    private EditText edtBusqueda;
    private RecyclerView rcvClientes;
    private ImageView imgBuscar;
    private CardView crvCrear;
    private ProgressBar prbBuscar;
    private Vibrator vibrator;
    private TextView txtNoResultado;

    private ModelNewTikete model;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidget(view);
        model = new ViewModelProvider(requireActivity()).get(ModelNewTikete.class);
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        funBuscar();
    }

    private void funBuscar() {

        imgBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new Utilidades().verificarEdt(edtBusqueda)) {
                    vibrator.vibrate(50);
                    prbBuscar.setVisibility(View.VISIBLE);
                    txtNoResultado.setVisibility(View.INVISIBLE);
                    bajarTeclado();
                    buscarMotorBusqueda();
                }
            }
        });

        crvCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(5,true);
            }
        });
    }

    private void bajarTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(edtBusqueda.getWindowToken(), 0);
    }


    private void buscarMotorBusqueda() {

        String palabra = edtBusqueda.getText().toString();
        final Client client = new Client(Constantes.APPID, Constantes.APIKEYADMIN);
        Index index = client.getIndex("clientes");

        Query query = new Query(palabra)
                .setHitsPerPage(10);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                try {
                    JSONArray array = content.getJSONArray("hits");
                    Log.e("recuperado", array.toString());

                    if (array.length() < 1){
                        prbBuscar.setVisibility(View.INVISIBLE );
                        txtNoResultado.setVisibility(View.VISIBLE);
                    }

                    ArrayList<ClienteAlgolia> clientes = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        Gson gson = new Gson();

                        ClienteAlgolia cliente = gson.fromJson(String.valueOf(array.getJSONObject(i)), ClienteAlgolia.class);
                        clientes.add(cliente);
                    }

                    if (clientes.size() > 0){
                        prbBuscar.setVisibility(View.INVISIBLE);
                        llenarRecycler(clientes);
                    }

                } catch (JSONException e) {
                    prbBuscar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        });
    }

    private void llenarRecycler(final ArrayList<ClienteAlgolia> clientes) {

        rcvClientes.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        rcvClientes.setLayoutManager(llm);

        AdapClienteBus adaptador = new AdapClienteBus(clientes,requireContext());
        rcvClientes.setAdapter(adaptador);

        adaptador.setOnItemClickListener(new AdapClienteBus.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                siguientePagina(clientes.get(position));
            }
        });
    }

    private void siguientePagina(ClienteAlgolia clienteAlgolia) {

        Cliente cliente = new Cliente(clienteAlgolia);
        model.setCliente(cliente);
        vibrator.vibrate(50);

        Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(1,true);
    }


    private void setWidget(View v) {

        edtBusqueda = v.findViewById(R.id.edt_bus_cre);
        txtNoResultado = v.findViewById(R.id.txt_bus_cli);
        crvCrear = v.findViewById(R.id.crv_btn_cre_cli);
        imgBuscar = v.findViewById(R.id.img_btn_bus_cli);

        rcvClientes = v.findViewById(R.id.rcv_cli_cre);
        prbBuscar = v.findViewById(R.id.prb_bus_cli);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_uno_cliente_busqueda, container, false);
        return v;
    }
}
