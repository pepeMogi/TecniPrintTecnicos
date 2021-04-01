package co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import co.tecniprint.tecniprinttecnicos.DatosInternos;
import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;
import co.tecniprint.tecniprinttecnicos.entidades.DiagnosticoCompleto;
import co.tecniprint.tecniprinttecnicos.entidades.DiagnosticoExport;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.Repuesto;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.MainTecnico;
import co.tecniprint.tecniprinttecnicos.maintecnico.dialogos.FragDialogProsDiagnostico;
import co.tecniprint.tecniprinttecnicos.maintecnico.dialogos.FragDialogProsDiagnosticoOffline;

public class Frag_siete_generar extends Fragment {


    private CardView crvAtras, crvGenerar;
    private TextView txtGenerar;
    private ProgressBar prbTiket;
    private ArrayList<String> urisImagenes = new ArrayList<>();
    ArrayList<String> imagenes = new ArrayList<>();
    private ModelDiagnostico model;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets(view);
        model = new ViewModelProvider(requireActivity()).get(ModelDiagnostico.class);

        boolean internet = new DatosInternos(requireContext()).isInternet();
        if (internet){
            escucharSubida();
            funBotones();
            funBotonesNav();
        }else{

            txtGenerar.setText("Generar Reporte Offline");
            funBotonesOffline();
        }


    }

    private void funBotonesOffline() {

        crvGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               generarReporteOffline();
            }
        });

        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
    }

    private void generarReporteOffline() {

        Diagnostico diagnostico = getDiagnostico();
        Tiket tiket = model.getTiket().getValue();
        tiket.setEstado(model.getEstado().getValue());
        ArrayList<String> diag = tiket.getDiagnosticos();
        diag.add(diagnostico.getId());
        tiket.setDiagnosticos(diag);
        tiket.setPrioridad(0);

        Maquina maquina = model.getMaquina().getValue();
        Cliente cliente = model.getCliente().getValue();
        if (cliente != null){
            tiket.setNit(cliente.getCc());
        }


        ArrayList<String> imagenes = new ArrayList<>();
        if (model.getAnexos().getValue() != null){
            for (int i = 0; i < model.getAnexos().getValue().size(); i++){
                imagenes.add(String.valueOf(model.getAnexos().getValue().get(i)));
            }
        }
        diagnostico.setImgs(imagenes);

        Log.e("Cliente offline",cliente.getId());
        DiagnosticoCompleto diagnosticoCompleto = new DiagnosticoCompleto(diagnostico,maquina,cliente,tiket);
        new DatosInternos(requireContext()).setReporte(diagnosticoCompleto);
        Log.e("listo Guardado"," reporte offline");
        Intent intent = new Intent(requireActivity(), MainTecnico.class);
        intent.putExtra(Constantes.INTERNET,false);
        startActivity(intent);
        requireActivity().finish();
    }

    private void escucharSubida() {

        model.getNumero().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (model.getAnexos().getValue() != null && Objects.requireNonNull(model.getAnexos().getValue()).size() > integer) {

                    model.setSubImg(true);
                    subirImagen(Uri.parse(model.getAnexos().getValue().get(integer)), integer);
                    prbTiket.setVisibility(View.VISIBLE);
                } else {
                    model.setSubImg(false);
                    prbTiket.setVisibility(View.INVISIBLE);
                    generarReporteOnLine();
                }
            }
        });
    }

    private void funBotones() {

        crvGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setNumero(0);
                showEditDialog();
            }
        });

        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
    }

    private void showEditDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragDialogProsDiagnostico dialogFragment = new FragDialogProsDiagnostico();
        dialogFragment.setCancelable(false);
        dialogFragment.show(fm, "fragment_edit_name");
    }



    private void copiarImagenesDirectorio(Tiket tiket, String json) {

        ArrayList<String> urisString = model.getAnexos().getValue();
        ArrayList<Bitmap> imagenes = new ArrayList<>();

        if (urisString != null) {
            for (int i = 0; i < urisString.size(); i++) {
                Bitmap bitmap = getBipmap(Uri.parse(urisString.get(i)));
                imagenes.add(bitmap);
            }
        }

        guardarReporte(json, imagenes, tiket);


    }

    private Bitmap getBipmap(Uri uri) {

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(requireContext()).getContentResolver(), uri);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void guardarReporte(final String json, final ArrayList<Bitmap> finalBitmaps, final Tiket tiket) {

        @SuppressLint("StaticFieldLeak") AsyncTask fileTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                // colocamos la ruta


                String root = Environment.getExternalStorageDirectory() + "/Diagnosticos/" + tiket.getId();
                File myDir = new File(root);
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }else{
                    String[] children = myDir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(myDir, children[i]).delete();
                    }
                    myDir.mkdirs();
                }


                for (int i = 0; i < finalBitmaps.size(); i++) {
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String name = " " + n + i + ".jpg";
                    File pictureFile = new File(myDir, name);
                    try {
                        pictureFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        FileOutputStream out = new FileOutputStream(pictureFile);


                        finalBitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 90, out);

                        out.close();
                        Log.e("Enviado => ", pictureFile.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                File file = new File(root, "reporte" + ".txt");
                try {
                    file.createNewFile();
                    BufferedWriter br = new BufferedWriter(new FileWriter(file, true));
                    br.write(json);
//                    Toast.makeText(getContext(), "Archivo creado con exito", Toast.LENGTH_SHORT).show();

                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // codigo para escribir el reporte


                return null;
            }

        };
        fileTask.execute();



    }


    private void generarReporteOnLine() {

        Diagnostico diagnostico = getDiagnostico();
        Tiket tiket = model.getTiket().getValue();
        model.setDiagnosticoCompleto(diagnostico);
        model.setSubDiagnostico(true);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        diagnostico.setImgs(imagenes);
        subirDiagnostico(diagnostico, db, tiket);


    }

    private Diagnostico getDiagnostico() {
        Tiket tiket = model.getTiket().getValue();

        final ArrayList<Repuesto> repuestos = model.getRepuestos().getValue();
        ArrayList<String> listaRepuestos = new ArrayList<>();

        if (repuestos != null) {
            for (int i = 0; i < repuestos.size(); i++) {
                listaRepuestos.add(repuestos.get(i).display());
            }
        }

        String comentario = "";
        if (model.getComentario().getValue() != null) {
            comentario = model.getComentario().getValue();
        }


        Diagnostico diagnostico = new Diagnostico(
                tiket.getAsignado(),
                model.getDiagnostico().getValue(),
                model.getSolcucion().getValue(),
                listaRepuestos,
                model.getAnexos().getValue(),
                comentario,
                Timestamp.now()
        );

        return diagnostico;
    }

    private void subirImagen(Uri uri, final Integer numero) {

        Log.e("Subiendo img", String.valueOf(uri));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef
                .child("diagnostico/anexo" +
                        Objects.requireNonNull(model.getTiket().getValue()).getId()
                        + numero + "_" + new Date().getTime() + ".jpg");

        UploadTask uploadTask = ref.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task task = taskSnapshot.getStorage().getDownloadUrl();
                while (!task.isSuccessful()) ;
                Uri uri1 = (Uri) task.getResult();
                Log.e("img subida", String.valueOf(uri1));
                imagenes.add(String.valueOf(uri1));
                Integer num = numero + 1;
                model.setNumero(num);
            }
        });
    }

    private void subirDiagnostico(final Diagnostico diagnostico, final FirebaseFirestore db, final Tiket tiket) {

        // sube el diagnostico
        db.collection(Constantes.DIAGNOSTICOS).document(diagnostico.getId())
                .set(diagnostico).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Diagnostico subido", Toast.LENGTH_SHORT).show();
                    model.setSubDiagnostico(false);
                    model.setSubAcualizado(true);
                    actualizarTiket(diagnostico, db, tiket);
                } else {
                    Toast.makeText(requireContext(), "Error al subir Diagnostico", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void actualizarTiket(Diagnostico diagnostico, final FirebaseFirestore db, Tiket tiket) {

        if (model.getActCliente().getValue() != null && model.getActCliente().getValue()){

        db.collection(Constantes.TIKETS).document(tiket.getId())
                .update("ultimaVisita", Timestamp.now(),
                        "estado", model.getEstado().getValue(),
                        "diagnosticos", FieldValue.arrayUnion(diagnostico.getId()),
                        "prioridad", 0,
                        "nit", Objects.requireNonNull(model.getCliente().getValue()).getCc()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Exito!!! diagnostico subido con exito", Toast.LENGTH_SHORT).show();
                    model.setSubAcualizado(false);
                    model.setSubNotificacion(true);

                    actualizarCliente(db);
                    actualizarAlgolia(tiket);

                } else {
                    Toast.makeText(getContext(), "Error al actualizar tiket", Toast.LENGTH_SHORT).show();
                }
            }
        });

        }else{

            db.collection(Constantes.TIKETS).document(tiket.getId())
                    .update("ultimaVisita", Timestamp.now(),
                            "estado", model.getEstado().getValue(),
                            "diagnosticos", FieldValue.arrayUnion(diagnostico.getId()),
                            "prioridad", 0
                            ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Exito!!! diagnostico subido con exito", Toast.LENGTH_SHORT).show();
                        model.setSubAcualizado(false);
                        model.setSubNotificacion(true);

                        actulizarContadores(db);
                        actualizarAlgolia(tiket);

                    } else {
                        Toast.makeText(getContext(), "Error al actualizar tiket", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void actualizarAlgolia(Tiket tiket) {

        Log.e("Actualizaondo Algolia", tiket.getId());
        final Client client = new Client(Constantes.APPID, Constantes.APIKEYADMIN);
        Index index = client.getIndex("tikets");



        JSONObject object = null;
        if (model.getActCliente().getValue() != null && model.getActCliente().getValue()) {
            try {
                object = new JSONObject().put("objectID", tiket.getId()).put("estado", model.getEstado().getValue())
                        .put("maquina", tiket.getIdMaquina()).put("nit",model.getCliente().getValue().getCc());
                index.partialUpdateObjectAsync(object, tiket.getId(), false, null);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else{

            try {
                object = new JSONObject().put("objectID",tiket.getId()).put("estado",model.getEstado().getValue())
                        .put("maquina",tiket.getIdMaquina());
                index.partialUpdateObjectAsync(object, tiket.getId(), false, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }




    }

    private void actualizarCliente(FirebaseFirestore db) {

        Cliente cliente = model.getCliente().getValue();
        if (cliente != null) {
            db.collection(Constantes.CLIENTES).document(cliente.getId())
                    .update("cc",cliente.getCc(), "rut", cliente.getRut())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        actulizarContadores(db);
                        actualizarClienteAlgolia();
                    }
                }
            });
        }
    }

    private void actualizarClienteAlgolia() {

        Log.e("Actualizaondo Cliente", "");
        final Client client = new Client(Constantes.APPID, Constantes.APIKEYADMIN);
        Index index = client.getIndex("clientes");

        Cliente cliente = model.getCliente().getValue();


        JSONObject object = null;

        if (cliente != null) {
            try {
                object = new JSONObject().put("objectID", cliente.getId()).put("cc", cliente.getCc());
                index.partialUpdateObjectAsync(object, cliente.getId(), false, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }




    }


    private void actulizarContadores(FirebaseFirestore db) {


        if (model.getActContadores().getValue() != null && model.getActContadores().getValue()){

            Maquina maquina = model.getMaquina().getValue();
            if (maquina != null){
                db.collection(Constantes.MAQUINAS).document(maquina.getId())
                        .update("contadorBN", maquina.getContadorBN(),
                                "contadorColor", maquina.getContadorColor())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    notificarCliente();
                                }
                            }
                        });
            }



        }else{
            notificarCliente();
        }


    }

    private void notificarCliente() {
        model.setSubNotificacion(false);
        Toast.makeText(getContext(), "Notificando clliente", Toast.LENGTH_SHORT).show();
        model.setTerminar(true);
    }

    private void funBotonesNav() {


        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
    }

    private void setWidgets(View v) {


        crvGenerar = v.findViewById(R.id.crv_btn_gen_on);
        txtGenerar = v.findViewById(R.id.txt_generar);
        crvAtras = v.findViewById(R.id.crv_btn_cinco_atras);
        prbTiket = v.findViewById(R.id.prb_tik);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_siete_generar, container, false);
        return v;
    }
}
