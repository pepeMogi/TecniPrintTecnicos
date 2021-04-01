package co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.DatosInternos;
import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;

import static android.app.Activity.RESULT_OK;

public class Frag_seis_estado extends Fragment {

    private static final int IMG = 4;

    private CardView crvLegalizar, crvEspera, crvReAsignar, crvPausa, crvAtras, crvSiguiente,
            crvNit,crvRut;
    private ModelDiagnostico model;
    private ImageView imgSiguiente, imgRut;
    private EditText edtNit;
    private ProgressBar prbRut;
    private TextView txtRut;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets(view);
        model = new ViewModelProvider(requireActivity()).get(ModelDiagnostico.class);

        funBotones();
        funBotonesNav();
        escucharNit();
    }

    private void escucharNit() {

        model.getCliente().observe(requireActivity(), new Observer<Cliente>() {
            @Override
            public void onChanged(Cliente cliente) {
                llenarCliente(cliente);
            }
        });

    }

    private void llenarCliente(Cliente cliente) {

        edtNit.setHint(cliente.getCc());
        Picasso.with(requireContext()).load(cliente.getRut()).fit().into(imgRut);

    }

    private void funBotones() {

        crvLegalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setEstado(Constantes.PARA_LEGALIZAR);
                paraLegalizar();

            }
        });

        crvEspera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setEstado(Constantes.EN_ESPERA_REPESTO);
                siguiente();
            }
        });

        crvReAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setEstado(Constantes.RE_ASIGNAR);
                siguiente();
            }
        });

        crvPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setEstado(Constantes.EN_PAUSA);
                siguiente();
            }
        });

        imgSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = edtNit.getHint().toString();
                if (edtNit.getText().toString().isEmpty()){
                    if (hint == "0"){
                        Toast.makeText(requireActivity(), "Cc/Nit es necesario para continuar", Toast.LENGTH_SHORT).show();
                    }else{
                        siguiente();
                    }

                }else{
                    model.setActCliente(true);
                    String cc = edtNit.getText().toString();
                    String imgRut = null;
                    if (!txtRut.getText().toString().isEmpty()){
                        imgRut = txtRut.getText().toString();

                    }

                    Cliente cliente = model.getCliente().getValue();


                    if (imgRut != null){
                        assert cliente != null;
                        cliente.setRut(imgRut);
                    }
                    if (cliente != null){
                        cliente.setCc(cc);
                    }else{
                        Log.e("Ingresamos","Cliente");
                        cliente = new Cliente();
                        cliente.setId(model.getTiket().getValue().getIdCliente());
                        cliente.setCc(cc);
                    }

                    model.setCliente(cliente);

                    siguiente();
                }





            }
        });

        crvRut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()

                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(false)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        // metodo para cambiar la calidad y tamaño de iamgen
                        .setOutputCompressQuality(50)
                        .getIntent(requireActivity());
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, IMG);
            }
        });

        txtRut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prbRut.setVisibility(View.INVISIBLE);
                        Toast.makeText(requireContext(), "Img Lista", Toast.LENGTH_SHORT).show();
                        Picasso.with(requireContext()).load(txtRut.getText().toString()).fit().into(imgRut);
                        Cliente cliente = model.getCliente().getValue();
                        assert cliente != null;
                        cliente.setRut(txtRut.getText().toString());
                        model.setCliente(cliente);
                    }
                },500);

            }
        });
    }

    private void paraLegalizar() {


        crvNit.setVisibility(View.VISIBLE);
        crvRut.setVisibility(View.VISIBLE);

    }

    private void funBotonesNav() {


        crvSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguiente();
            }
        });
        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

    }

    private void siguiente(){


        Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(6, true);
    }

    private void setWidgets(View v) {


        crvAtras = v.findViewById(R.id.crv_btn_cuatro_atr);
        crvSiguiente = v.findViewById(R.id.crv_btn_cuatro_sig);
        crvLegalizar = v.findViewById(R.id.crv_btn_legalizar);
        crvEspera = v.findViewById(R.id.crv_btn_espera);
        crvReAsignar = v.findViewById(R.id.crv_btn_re_asignar);
        crvPausa = v.findViewById(R.id.crv_btn_pausa);
        imgSiguiente = v.findViewById(R.id.img_btn_sig_leg);
        crvNit = v.findViewById(R.id.crv_edt_nit);
        edtNit = v.findViewById(R.id.edt_nit_dia);
        crvRut = v.findViewById(R.id.crv_rut_img);
        imgRut = v.findViewById(R.id.img_rut);
        prbRut = v.findViewById(R.id.prb_rut);
        txtRut = v.findViewById(R.id.txt_rut);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_seis_estado, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == IMG) {

            boolean internet = new DatosInternos(requireContext()).isInternet();
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri uriResult = result.getUri();
            Log.e("caragamos",String.valueOf(result.getUri()));
            model.setActCliente(true);
            if (internet){
                subirImagen(uriResult);
            }else{
                Cliente cliente =  model.getCliente().getValue();
                if (cliente == null){
                    cliente = new Cliente();
                    cliente.setId(model.getTiket().getValue().getIdCliente());
                    cliente.setRut(String.valueOf(uriResult));
                }else{
                    cliente.setRut(String.valueOf(uriResult));
                }
                Picasso.with(requireContext()).load(uriResult).fit().into(imgRut);
            }




        }


    }

    private void subirImagen(Uri uri) {


        prbRut.setVisibility(View.VISIBLE);
        Log.e("Subiendo img", String.valueOf(uri));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef
                .child("ruts/rut" +
                        model.getTiket() .getValue().getIdCliente() + ".jpg");

        UploadTask uploadTask = ref.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task task = taskSnapshot.getStorage().getDownloadUrl();
                while (!task.isSuccessful());
                Uri uri1 = (Uri) task.getResult();
                Log.e("img subida", String.valueOf(uri1));
                txtRut.setText(String.valueOf(uri1));

            }
        });
    }
}
