package co.tecniprint.tecniprinttecnicos.maintecnico.creartiket;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.DatosInternos;
import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.adaptadoresglobales.AdapImgAnexos;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.ClienteAlgolia;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Contador;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.Tecnico;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.entidades.TiketAlgolia;
import co.tecniprint.tecniprinttecnicos.maintecnico.dialogos.DialogFragNewCliente;
import co.tecniprint.tecniprinttecnicos.maintecnico.dialogos.DialogFragNewTik;
import co.tecniprint.tecniprinttecnicos.otros.MyNotificacion;

import static android.app.Activity.RESULT_OK;

public class Frag_cuatro_fallo_cliente extends Fragment {

    private static final int IMG = 45;

    private EditText edtFallo;
    private TextView txtNoResultado;    private RecyclerView rcvAnexos;
    private CardView crvAgregar, crvCrearTiket, crvAtras;
    private Vibrator vibrator;

    private ModelNewTikete model;
    private ArrayList<String> subidas = new ArrayList<>();



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidget(view);
        subidas.add("none");
        model = new ViewModelProvider(requireActivity()).get(ModelNewTikete.class);
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        funBotones();
        escucharImagenes();
        escuchar();

    }

    private void escuchar() {

        model.getImagenes().observe(requireActivity(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                if (model.getUris().getValue().size() == strings.size()){
                    model.setTerminarImg(true);
                    obtenerContador();
                }
            }
        });
    }



    private void obtenerContador() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constantes.CONTADORES).document(Constantes.NUMTIKETS).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        Contador contador = task.getResult().toObject(Contador.class);
                        crearTiket(contador,db);
                    }
                }
            }
        });

    }

    private void crearTiket(Contador contador, FirebaseFirestore db) {



            int num = contador.getNumero();
            num++;
            ArrayList<String> anexos = model.getImagenes().getValue();
            if (anexos == null) {
                anexos = new ArrayList<>();
            }

            Cliente cliente = model.getCliente().getValue();


            Maquina maquina = model.getMaquina().getValue();
            if (maquina == null) {
                maquina = new Maquina();
            }


            Tiket tiket = new Tiket(num,
                    cliente,
                    model.getSolicitante().getValue(),
                    model.getCelularSolicitante().getValue(),
                    cliente.getDireccion(),
                    cliente.getCiudad(),
                    cliente.getEmail(),
                    maquina,
                    edtFallo.getText().toString(),
                    model.getImagenes().getValue()
            );

            // subir tiket ok
            subirTiket(num, tiket, db);
            // subir algolia ok
            // actualizar contador (num) ok





    }

    private void subirTiket(int num, Tiket tiket, FirebaseFirestore db) {
        db.collection(Constantes.TIKETS).document(tiket.getId()).set(tiket)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            model.setTerminarData(true);
                            subirMotorBusqueda(num,tiket);
                        }else {
                            Toast.makeText(getContext(), "Error al subir Tiket", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void subirMotorBusqueda(int num, Tiket tiket) {

        TiketAlgolia tiketAlgolia = new TiketAlgolia(tiket);
        final Client client = new Client(Constantes.APPID, Constantes.APIKEYADMIN);
        Index index = client.getIndex("tikets");


        Gson gson = new Gson();
        String json = gson.toJson(tiketAlgolia);

        // para subir a algolia
        try {
            index.addObjectAsync(new JSONObject(json), null);
            Toast.makeText(requireContext(), "tiket subido a algolia", Toast.LENGTH_SHORT).show();
            actualizarContador(num,tiket);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void actualizarContador(int num,Tiket tiket) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constantes.CONTADORES).document(Constantes.NUMTIKETS)
                .update("numero",num).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "actualizado numero con exito", Toast.LENGTH_SHORT).show();
                    model.setTerminarCon(true);
                    model.setTerminar(true);
                    model.setTiket(tiket);
                    Tecnico tecnico = new DatosInternos(requireContext()).getTecnico();
                    new MyNotificacion(requireContext()).enviarNotificacion("Tiket Nuevo",tiket.getNombre(),tecnico.getImg());
                }else {
                    Toast.makeText(getContext(), "Error al alctualizar consecutivo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void escucharImagenes() {

        model.getUris().observe(requireActivity(), new Observer<ArrayList<Uri>>() {
            @Override
            public void onChanged(ArrayList<Uri> uris) {
                llenarRecycler(uris);
            }
        });


    }

    private void llenarRecycler(ArrayList<Uri> uris) {

        rcvAnexos.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(RecyclerView.HORIZONTAL);
        rcvAnexos.setLayoutManager(llm);

        AdapImgAnexos adaptador = new AdapImgAnexos(uris,requireContext());
        rcvAnexos.setAdapter(adaptador);
    }

    private void funBotones() {

        crvAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        // metodo para cambiar la calidad y tamaño de iamgen
                        .setOutputCompressQuality(20)
                        .getIntent(requireActivity());

                vibrator.vibrate(50);
                startActivityForResult(intent, IMG);
            }
        });

        crvCrearTiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // model.setNumImagenes(2);
                vibrator.vibrate(100);

                if (model.getCliente().getValue() == null){
                    Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(0,true);
                    Toast.makeText(requireContext(), "Datos Perdidos al Reiniciar la App", Toast.LENGTH_LONG).show();
                }else {

                    abrirDialog();
                    if (model.getUris().getValue() != null) {
                        subirMultiplesImgs(model.getUris().getValue());
                    } else {
                        model.setTerminarImg(true);
                        obtenerContador();
                    }

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

    private void subirMultiplesImgs(ArrayList<Uri> uris){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();


        for (int i = 0; i < uris.size(); i++) {
            StorageReference ref = storageRef
                    .child("tikets/anexo" + new Date().getTime() + i + ".jpg");
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

        if (model.getUris().getValue() != null){

            boolean exite = false;
            for (int i = 0; i < model.getUris().getValue().size();i++){
                if (model.getUris().getValue().get(i).equals(uri)){
                    exite = true;
                    break;
                }
            }
            if (!exite){
                model.setImagenes(String.valueOf(uri));
            }

        }else {

            model.setImagenes(String.valueOf(uri));
        }
    }

    private void abrirDialog(){

        FragmentManager fm = requireActivity().getSupportFragmentManager();
        DialogFragNewTik dialogFragment = new DialogFragNewTik();
        dialogFragment.setCancelable(false);
        dialogFragment.show(fm, "fragment_edit_name");

    }


    private void setWidget(View v) {

        edtFallo = v.findViewById(R.id.edt_fall_tik);
        crvAgregar = v.findViewById(R.id.crv_btn_agre_img_tik);
        crvCrearTiket = v.findViewById(R.id.crv_btn_sig_cuatro_maq);

        rcvAnexos = v.findViewById(R.id.rcv_img_cre_tik);
        crvAtras = v.findViewById(R.id.crv_btn_atras_cuatro_cre_tik);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_cuatro_fallo_maquina, container, false);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMG) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri uriResult = result.getUri();
            Log.e("caragamos", String.valueOf(result.getUri()));

           // Picasso.with(this).load(uriResult).into(imgMain);
            model.setUris(uriResult);

        }

    }
}
