package co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.CrearMaquina;
import co.tecniprint.tecniprinttecnicos.DatosInternos;
import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.MarcasModelos;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores.AdapImgTiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.dialogos.FragDialogCreaMaq;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.VIBRATOR_SERVICE;

public class Frag_crear_maquina extends Fragment {

    private static final int IMG = 4;


    private TextView txtReferencia, txtImg;
    private AutoCompleteTextView autMarca, autModelo;
    private EditText edtContadorBn, edtContadorColor, edtSerial;

    private ImageView imgMaquina, imgAtras;
    private CheckBox chbSinSerial;
    private Spinner spnTipo;
    private CardView crvCargarImagenes,  crvCrear;


    private ModelDiagnostico model;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterModelo;
    private Vibrator vibrator;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets(view);
        model = new ViewModelProvider(requireActivity()).get(ModelDiagnostico.class);
        vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        if (model.getTiket().getValue() != null) {
            llenarReferencia(model.getTiket().getValue());
            llenarTipo();
            traerMarcas();
        }

        funBotones();

    }

    private void funBotones() {

        crvCargarImagenes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(false)
                        .setAspectRatio(4, 4)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        // metodo para cambiar la calidad y tamaño de iamgen
                        .setOutputCompressQuality(20)
                        .getIntent(requireActivity());
                vibrator.vibrate(50);

                startActivityForResult(intent, IMG);
            }
        });

        crvCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utilidades utilidades = new Utilidades();
                if (utilidades.verificarEdt(edtSerial)
                        & utilidades.verificarAut(autModelo)
                        & utilidades.verificarAut(autMarca)) {
                    vibrator.vibrate(50);
                    verificamosExistencias();
                }else{
                    vibrator.vibrate(50);
                    vibrator.vibrate(50);
                }

            }
        });
    }

    private void verificamosExistencias() {

        final String marca = autMarca.getText().toString();
        final String modelo = autModelo.getText().toString();
        String serial = edtSerial.getText().toString();
        final String tipo = spnTipo.getSelectedItem().toString();


        if (verificamosMarca(marca)) {

            if (verificamosModelo(modelo)) {
                // crear maquina y subirla en firestore
                seguirProceseso(tipo, marca, modelo, serial);

            } else {
                Snackbar.make(crvCrear, "el modelo " +
                                modelo.toUpperCase() + " de " + tipo + " no se encuentra registrada.",
                        5000).setActionTextColor(requireContext().getResources().getColor(R.color.colorPrimary)).setAction("Crear", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crearModelo(modelo);
                    }
                }).show();

            }

        } else {
            Snackbar.make(crvCrear, "la marca " + marca.toUpperCase() + " de " + tipo
                    + " no se encuentra registrada.", 5000).setActionTextColor(requireContext().getResources().getColor(R.color.colorPrimary)).setAction("Crear", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(50);
                    crearMarca(marca);
                }
            }).show();

        }
    }



    private void seguirProceseso(final String tipo, final String marca, final String modelo, final String serial) {

        showEditDialog();


            subirImagen();

            txtImg.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    model.setSubImgMaquina(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.setSubImg(true);
                            subirMaquina(tipo, marca, modelo, serial);
                        }
                    }, 500);

                }
            });


    }

    private void subirMaquina(String tipo, String marca, final String modelo, String serial) {


        int contadorBn = edtContadorBn.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtContadorBn.getText().toString());
        int  contadorColor = edtContadorColor.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtContadorColor.getText().toString());
        String img = txtImg.getText().toString();

        String idCliente = Objects.requireNonNull(model.getTiket().getValue()).getIdCliente();
        final Maquina maquina = new Maquina(idCliente, tipo, img, marca, modelo, serial.toUpperCase(), contadorBn, contadorColor);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Constantes.MAQUINAS).document(maquina.getId()).set(maquina).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    model.setSubDatoMaquina(true);
                    Toast.makeText(requireContext(), "Creada con exito", Toast.LENGTH_SHORT).show();
                    // modificar el tiket ( maquinas item 0 => maquina id son "_"
                    // adicionar maquina al array
                    model.setSubDiagnostico(false);
                    model.setSubAcualizado(true);
                    model.setSubImg(false);
                    db.collection(Constantes.TIKETS).document(model.getTiket().getValue().getId())
                            .update("idMaquina",maquina.getId())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(requireContext(), "Actualizado con exito", Toast.LENGTH_SHORT).show();
                                        model.setSubActTiket(true);
                                        model.setImgTerminar(true);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(requireContext(), "Error en base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void subirImagen() {

            Uri uri = model.getImgMaquina().getValue();

            if (uri == null){
                Toast.makeText(requireContext(), "Imagen de maquina requerida", Toast.LENGTH_SHORT).show();
            }else {

                Log.e("Subiendo img", String.valueOf(uri));
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageRef = storage.getReference();
                final StorageReference ref = storageRef
                        .child("maquinas/" + Timestamp.now() + ".jpg");

                UploadTask uploadTask = ref.putFile(uri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task task = taskSnapshot.getStorage().getDownloadUrl();
                        while (!task.isSuccessful()) ;
                        Uri uri1 = (Uri) task.getResult();
                        Log.e("img subida", String.valueOf(uri1));
                        // imagenes.add(String.valueOf(uri1));
                        // Integer num = numero + 1;
                        // model.setNumero(num);
                        txtImg.setText(String.valueOf(uri1));
                    }
                });
            }

    }

    private void showEditDialog() {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragDialogCreaMaq dialogFragment = new FragDialogCreaMaq();
        dialogFragment.setCancelable(false);
        dialogFragment.show(fm, "fragment_edit_name");
    }

    private boolean verificamosMarca(String marca) {

        MarcasModelos marcasModelos = new DatosInternos(requireContext()).getMarcasModelos();
        if (marcasModelos != null) {

            for (int i = 0; i < marcasModelos.getMarcas().size(); i++) {

                if (marcasModelos.getMarcas().get(i).equals(marca)) {
                    return true;
                }

            }
        }

        return false;

    }

    private boolean verificamosModelo(String modelo) {

        MarcasModelos marcasModelos = new DatosInternos(requireContext()).getMarcasModelos();
        if (marcasModelos != null) {

            for (int i = 0; i < marcasModelos.getModelos().size(); i++) {

                if (marcasModelos.getModelos().get(i).equals(modelo)) {
                    return true;
                }

            }
        }

        return false;

    }

    private void crearMarca(final String marca) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constantes.MARCAS).document(Constantes.FOTOCOPIADORAS)
                .update("marcas", FieldValue.arrayUnion(marca)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(requireContext(), "Marca " + marca +
                        " Agregado", Toast.LENGTH_SHORT).show();
                new DatosInternos(requireContext()).setMarca(marca);
            }
        });
    }

    private void crearModelo(final String modelo) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constantes.MARCAS).document(Constantes.FOTOCOPIADORAS)
                .update("modelos", FieldValue.arrayUnion(modelo)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(requireContext(), "Modelo " + modelo + " Agregado", Toast.LENGTH_SHORT).show();
                new DatosInternos(requireContext()).setModelo(modelo);
            }
        });
    }

    private void llenarTipo() {

        final ArrayList<String> tipos = new ArrayList<>();
        tipos.add("Fotocopiadora");
        tipos.add("Impresora");
        tipos.add("Otro");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, tipos);
        spnTipo.setAdapter(adapter);

        // linea para obtener la seleccion del spinner
        String text = spnTipo.getSelectedItem().toString();
        Log.e("selecionado", text);

    }

    private void traerMarcas() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constantes.MARCAS).document(Constantes.FOTOCOPIADORAS).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (Objects.requireNonNull(task.getResult()).exists()) {
                        MarcasModelos marcasModelos = task.getResult().toObject(MarcasModelos.class);
                        assert marcasModelos != null;
                        llenarAutocompletes(marcasModelos);
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al carga base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void llenarAutocompletes(MarcasModelos marcasModelos) {
        // actualizamos marcas modelos
        new DatosInternos(requireContext()).setMarcasModelos(marcasModelos);

        Log.e("lenando marcas", String.valueOf(marcasModelos.getModelos().size()));

        adapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1, marcasModelos.getMarcas());
        autMarca.setAdapter(adapter);

        adapterModelo = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1, marcasModelos.getModelos());
        autModelo.setAdapter(adapterModelo);
    }

    private void llenarReferencia(Tiket tiket) {
        txtReferencia.setText(tiket.getIdMaquina());
    }

    private void setWidgets(View v) {
        txtReferencia = v.findViewById(R.id.txt_ref_cre_maq);
        autMarca = v.findViewById(R.id.aut_mar_cre_maq);
        autModelo = v.findViewById(R.id.aut_mod_cre_maq);
        edtContadorBn = v.findViewById(R.id.edt_con_bn_cre_maq);
        edtContadorColor = v.findViewById(R.id.edt_con_col_cre_maq);
        edtSerial = v.findViewById(R.id.edt_num_cre_maq);
        crvCargarImagenes = v.findViewById(R.id.crv_btn_esc_img_cre_maq);
        crvCrear = v.findViewById(R.id.crv_btn_cre_cre_maq);
        imgMaquina = v.findViewById(R.id.img_cre_maq);
        spnTipo = v.findViewById(R.id.spn);
        txtImg = v.findViewById(R.id.txt_img);
        imgAtras = v.findViewById(R.id.img_cre_atras_maq);
        chbSinSerial = v.findViewById(R.id.chb_no_serial);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_crear_maquina, container, false);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == IMG) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri uriResult = result.getUri();
            Log.e("caragamos", String.valueOf(result.getUri()));

            //model.setAnexo(String.valueOf(uriResult));
            Picasso.with(requireContext()).load(uriResult).fit().into(imgMaquina);
            model.setImgMaquina(uriResult);

        }


    }
}
