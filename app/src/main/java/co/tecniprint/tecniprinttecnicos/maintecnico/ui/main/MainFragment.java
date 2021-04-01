package co.tecniprint.tecniprinttecnicos.maintecnico.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.DatosInternos;
import co.tecniprint.tecniprinttecnicos.DetalleTiket;
import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;
import co.tecniprint.tecniprinttecnicos.entidades.DiagnosticoCompleto;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.Tecnico;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.MainTecnico;
import co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores.AdapTiketMain;


public class MainFragment extends Fragment {

    private RecyclerView rcvMain;
    private Vibrator vibrator;
    private MainViewModel model;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        setWidgets(view);
        model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        String tecnicoString = requireActivity().getIntent().getStringExtra(Constantes.TECNICOS);
        boolean intenet = requireActivity().getIntent().getBooleanExtra(Constantes.INTERNET, true);
        if (intenet) {

            if (tecnicoString != null) {
                Tecnico tecnico = new Utilidades().stringToTecnico(tecnicoString);
                funLlenarRcv(tecnico);
            }

            funLlenarRcv(null);


        } else {
            llenarRcvLocal();
        }

        escuchar();

    }

    private void escuchar() {

        model.getImagenes().observe(requireActivity(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                if (model.getUris().getValue().size() == strings.size()) {
                    // model.setTerminarImg(true);
                    subirDiagnostico(strings);
                }
            }
        });
    }

    private void subirDiagnostico(ArrayList<String> strings) {

        Log.e("Subiendo", "diagnostico");
        Diagnostico diagnostico = model.getDiagnostico().getValue();

        if (diagnostico != null) {
            diagnostico.setImgs(strings);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(Constantes.DIAGNOSTICOS).document(diagnostico.getId()).set(diagnostico)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.e("Actualizando Tiket", "Si");
                                actualizarTiket(db);
                            }
                        }
                    });


        } else {
            Toast.makeText(requireContext(), "Archivo local Corrompido.. D", Toast.LENGTH_SHORT).show();
        }


    }

    private void actualizarTiket(FirebaseFirestore db) {

        Tiket tiket = model.getTiket().getValue();


        if (tiket != null) {
            db.collection(Constantes.TIKETS).document(tiket.getId()).set(tiket)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            actualizarAlgolia(tiket);

                            if (task.isSuccessful()) {
                                if (model.getCliente().getValue() != null) {
                                    actualizarCliente(db);
                                } else if (model.getMaquina().getValue() != null) {
                                    actualizarMaquina(db);
                                } else {
                                    finalizar();
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "Archivo local Corrompido", Toast.LENGTH_SHORT).show();
        }
    }


    private void actualizarAlgolia(Tiket tiket) {

        Log.e("Actualizaondo Algolia", tiket.getId());
        final Client client = new Client(Constantes.APPID, Constantes.APIKEYADMIN);
        Index index = client.getIndex("tikets");



        JSONObject object = null;

            try {
                object = new JSONObject().put("objectID", tiket.getId()).put("estado", tiket.getEstado())
                        .put("maquina", tiket.getIdMaquina()).put("nit",tiket.getNit());
                index.partialUpdateObjectAsync(object, tiket.getId(), false, null);
            } catch (Exception e) {
                e.printStackTrace();
            }







    }

    private void finalizar() {
        Log.e("Finalizar", "Todo");

        ArrayList<DiagnosticoCompleto> completos = new DatosInternos(requireContext()).getArrayReportes();
        int num = 0;
        Diagnostico diagnostico = model.getDiagnostico().getValue();
        for (int i = 0; i < completos.size(); i++) {
            if (completos.get(i).getIdDiagnostico().equals(diagnostico.getId())) {
                num = i;
            }
        }

        completos.remove(num);
        new DatosInternos(requireContext()).setReportes(completos);
        startActivity(new Intent(requireActivity(), MainTecnico.class));
        requireActivity().finish();

    }

    private void actualizarMaquina(FirebaseFirestore db) {
        Log.e("Actualizando", "Maquina");
        Maquina maquina = model.getMaquina().getValue();
        db.collection(Constantes.MAQUINAS).document(maquina.getId())
                .update("contadorBN", maquina.getContadorBN(),
                        "contadorColor", maquina.getContadorColor()
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    finalizar();
                }
            }
        });
    }

    private void actualizarCliente(FirebaseFirestore db) {

        Log.e("Actualizando", "Cliente");
        Cliente cliente = model.getCliente().getValue();
        db.collection(Constantes.CLIENTES).document(cliente.getId()).update("cc", cliente.getCc())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (model.getMaquina().getValue() != null) {
                                actualizarMaquina(db);
                            } else {
                                finalizar();
                            }
                        }
                    }
                });
    }

    private void llenarRcvLocal() {
        Log.e("llenando", "tiketes locales");
        ArrayList<Tiket> tikets = new DatosInternos(requireContext()).getTikets();

        ArrayList<DiagnosticoCompleto> diagComples = new DatosInternos(requireContext()).getArrayReportes();
        for (int j = 0; j < tikets.size(); j++) {
            Log.e("Veridicando", "tikets");
            for (int i = 0; i < diagComples.size(); i++) {
                if (diagComples.get(i).getTiket().getId().equals(tikets.get(j).getId())) {
                    Log.e("ID TIK local", diagComples.get(i).getTiket().getId());
                    Log.e("ID TIK pueso", tikets.get(j).getId());
                    tikets.get(j).setPendiente(true);
                }
            }
        }


        rcvMain.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        rcvMain.setLayoutManager(llm);

        AdapTiketMain adaptador = new AdapTiketMain(tikets);
        rcvMain.setAdapter(adaptador);
        adaptador.setOnItemClickListener(new AdapTiketMain.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                vibrator.vibrate(100);
                Tiket tiket = tikets.get(position);
                if (tiket.isPendiente()) {
                    Toast.makeText(requireContext(),
                            "Se requiere de internet para subir este Reporte", Toast.LENGTH_SHORT).show();
                } else {
                    String tikeString = new Utilidades().tiketToString(tiket);
                    Intent intent = new Intent(requireActivity(), DetalleTiket.class);
                    intent.putExtra(Constantes.TIKETS, tikeString);
                    startActivity(intent);
                }
            }
        });

    }

    private void funLlenarRcv(Tecnico tecnico) {

        if (tecnico == null) {
            tecnico = new DatosInternos(requireContext()).getTecnico();
        }


        Log.e("llenando... ", "recycler");
        rcvMain.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        rcvMain.setLayoutManager(llm);

        ArrayList<Tiket> tikets = new ArrayList<>();
        AdapTiketMain adaptador = new AdapTiketMain(tikets);
        rcvMain.setAdapter(adaptador);
        adaptador.setOnItemClickListener(new AdapTiketMain.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                vibrator.vibrate(100);
                Tiket tiket = tikets.get(position);
                if (tiket.isPendiente()) {
                    Toast.makeText(requireContext(), "Subiendo reporte", Toast.LENGTH_SHORT).show();
                    subirReporte(tiket.getId());
                } else {
                    String tikeString = new Utilidades().tiketToString(tiket);
                    Intent intent = new Intent(requireActivity(), DetalleTiket.class);
                    intent.putExtra(Constantes.TIKETS, tikeString);
                    startActivity(intent);
                }
            }
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constantes.TIKETS).whereEqualTo("asignado", tecnico.getNombre())
                .whereEqualTo("estado","asignado")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.e("busco", "Si");
                if (task.isSuccessful()) {
                    ArrayList<DiagnosticoCompleto> diagComples = new DatosInternos(requireContext()).getArrayReportes();
                    for (DocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                        Tiket tiket = doc.toObject(Tiket.class);
                        for (int i = 0; i < diagComples.size(); i++) {
                            if (diagComples.get(i).getTiket().getId().equals(tiket.getId())) {
                                tiket.setPendiente(true);
                            }
                        }
                        tikets.add(tiket);
                        Log.e("encontrado", "Si");
                        adaptador.notifyDataSetChanged();
                    }

                    // guardamos los datos para offline
                    new DatosInternos(requireContext()).setTikets(tikets);

                }

            }
        });


        //adaptador = new TiketAdapTec(options);
        //adaptador.startListening();


    }

    private void subirReporte(String idTiket) {

        ArrayList<DiagnosticoCompleto> diagComples = new DatosInternos(requireContext()).getArrayReportes();

        DiagnosticoCompleto completo = new DiagnosticoCompleto();
        for (int i = 0; i < diagComples.size(); i++) {
            if (diagComples.get(i).getTiket().getId().equals(idTiket)) {
                completo = diagComples.get(i);
                break;
            }
        }

        model.setTiket(completo.getTiket());
        model.setMaquina(completo.getMaquina());
        model.setDiagnostico(completo.getDiagnostico());
        model.setCliente(completo.getCliente());
        subirDiagnosticoCompleto(completo);



    }

    private void subirDiagnosticoCompleto(DiagnosticoCompleto completo) {

        Diagnostico diagnostico = completo.getDiagnostico();
        subirImagenes(diagnostico);

    }

    private void subirImagenes(Diagnostico diagnostico) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        ArrayList<Uri> uris = new ArrayList<>();

        for (int i = 0; i < diagnostico.getImgs().size(); i++) {

            uris.add(Uri.parse(diagnostico.getImgs().get(i)));
            model.setUris(Uri.parse(diagnostico.getImgs().get(i)));
            Log.e("Pasamos es", String.valueOf(uris.get(i)));

        }
        if (uris.size() == 0){
            subirDiagnostico(new ArrayList<>());
        }

        Log.e("Numero Array", String.valueOf(uris.size()));

        for (int i = 0; i < uris.size(); i++) {

            Log.e("Sube," + String.valueOf(i), " VEz");
            StorageReference ref = storageRef
                    .child("diagnostico/evidencia" + diagnostico.getId() + i + ".jpg");
            UploadTask uploadTask = ref.putFile(uris.get(i));


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //   llenarReferencia(ref);

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            llenarUri(uri);
                        }
                    });
                }
            });
        }

    }

    private void llenarUri(Uri uri) {

        if (model.getUris().getValue() != null) {

            boolean exite = false;
            for (int i = 0; i < model.getUris().getValue().size(); i++) {
                if (model.getUris().getValue().get(i).equals(uri)) {
                    exite = true;
                    break;
                }
            }
            if (!exite) {
                model.setImagenes(String.valueOf(uri));
            }

        } else {

            model.setImagenes(String.valueOf(uri));
        }
    }

    private void setWidgets(View v) {

        rcvMain = v.findViewById(R.id.rcv_main_tec);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_tecnico, container, false);
        return v;
    }
}
