package co.tecniprint.tecniprinttecnicos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.adaptadoresglobales.AdapImgMaquinas;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.MaquinaDefault;
import co.tecniprint.tecniprinttecnicos.entidades.MarcasModelos;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.dialogos.FragDialogCreaMaq;
import co.tecniprint.tecniprinttecnicos.maintecnico.dialogos.FragDialogProsDiagnostico;
import co.tecniprint.tecniprinttecnicos.modelos.ModelMaquinaCrear;

public class CrearMaquina extends AppCompatActivity {

    private static final int IMG = 4;

    private TextView txtReferencia, txtImg;
    private AutoCompleteTextView autMarca, autModelo;
    private EditText edtContadorBn, edtContadorColor, edtSerial;
    private RecyclerView rcvMaquinas;
    private ImageView imgMaquina, imgAtras;
    private CheckBox chbSinSerial;
    private Spinner spnTipo;
    private CardView crvCargarImagenes, crvSubirImagnes, crvCrear;
    private ProgressBar prbImgs;

    private ModelMaquinaCrear model;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterModelo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_maquina);

        setWidgets();
        model = new ViewModelProvider(this).get(ModelMaquinaCrear.class);
        String tiketStrign = getIntent().getStringExtra(Constantes.TIKETS);
        if (tiketStrign != null) {
            Tiket tiket = new Utilidades().stringToTiket(tiketStrign);
            model.setTiket(tiket);
            llenar();
            llenarTipo();
            traerMarcas();
            funBotones();
            escucharImg();
        }


    }

    private void escucharImg() {

        model.getImg().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Picasso.with(CrearMaquina.this).load(s).fit().into(imgMaquina);
            }
        });
    }

    private void funBotones() {

        crvCargarImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // llenar recycler con imagenes de tipo, marca, modelo de *maquina default
                Utilidades utilidades = new Utilidades();
                if (utilidades.verificarAut(autMarca) & utilidades.verificarAut(autModelo)) {
                    final String marca = autMarca.getText().toString();
                    final String tipo = spnTipo.getSelectedItem().toString();
                    if (verificamosMarca(marca)) {
                        final String modelo = autModelo.getText().toString();
                        if (verificamosModelo(modelo)) {

                            llenarRecylcerImg(tipo, marca, modelo);
                        } else {
                            Snackbar.make(crvCargarImagenes, "el modelo " + modelo.toUpperCase() + " de " + tipo
                                    + " no se encuentra registrada.", 5000).setAction("Crear", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    crearModelo(modelo);
                                }
                            }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();

                        }

                    } else {
                        Snackbar.make(crvCrear, "la marca " + marca.toUpperCase() + " de " + tipo
                                + " no se encuentra registrada.", 5000).setAction("Crear", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                crearMarca(marca);
                            }
                        }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();

                    }
                }
            }
        });

        crvSubirImagnes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(false)
                        .setAspectRatio(4, 4)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        // metodo para cambiar la calidad y tamaño de iamgen
                        .setOutputCompressQuality(20)
                        .getIntent(CrearMaquina.this);

                startActivityForResult(intent, IMG);


            }
        });

        crvCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades utilidades = new Utilidades();
                if (utilidades.verificarAut(autMarca) &
                        utilidades.verificarAut(autModelo)) {

                    verificamosExistencias();
                }
            }
        });

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void verificamosExistencias() {

        final String marca = autMarca.getText().toString();
        final String modelo = autModelo.getText().toString();
        String serial = "NS" + edtSerial.getText().toString();
        final String tipo = spnTipo.getSelectedItem().toString();


        if (verificamosMarca(marca)) {

            if (verificamosModelo(modelo)) {
                // crear maquina y subirla en firestore
                crearMaquina(tipo, marca, modelo, serial);

            } else {
                Snackbar.make(crvCrear, "el modelo " +
                                modelo.toUpperCase() + " de " + tipo + " no se encuentra registrada.",
                        5000).setAction("Crear", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crearModelo(modelo);
                    }
                }).show();

            }

        } else {
            Snackbar.make(crvCrear, "la marca " + marca.toUpperCase() + " de " + tipo
                    + " no se encuentra registrada.", 5000).setAction("Crear", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    crearMarca(marca);
                }
            }).show();

        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FragDialogCreaMaq dialogFragment = new FragDialogCreaMaq();
        dialogFragment.setCancelable(false);
        dialogFragment.show(fm, "fragment_edit_name");
    }

    private void crearMaquina(final String tipo, final String marca, final String modelo, final String serial) {


        if (txtImg.getText().toString().isEmpty() && model.getImg().getValue() == null){
            Toast.makeText(this, "La imagen de la maquina es necesario", Toast.LENGTH_SHORT).show();
        }else

        if (chbSinSerial.isChecked() || edtSerial.getText().toString().isEmpty()) {
            Snackbar.make(crvCrear, "Se creara esta maquina sin numero de serie", 5000)
                    .setAction("crear", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            seguirProceseso(tipo, marca, modelo, serial);
                        }
                    }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();
        } else {
            seguirProceseso(tipo, marca, modelo, serial);
        }


    }

    private void seguirProceseso(final String tipo, final String marca, final String modelo, final String serial) {

        showEditDialog();
        model.setSubImg(true);
        if (txtImg.getText().toString().isEmpty()) {

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
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.setSubImg(false);
                          //  subirMaquina(tipo, marca, modelo, serial);
                        }
                    }, 500);

                }
            });

        } else {
           // subirMaquina(tipo, marca, modelo, serial);
        }
    }

    private void subirImagen() {

        if (model.getUri().getValue() != null) {

            Log.e("Subiendo img", String.valueOf(model.getUri().getValue()));
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReference();
            final StorageReference ref = storageRef
                    .child("maquinas/" + Timestamp.now() + ".jpg");

            UploadTask uploadTask = ref.putFile(model.getUri().getValue());

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
        } else {
            Toast.makeText(this, "Ninguna imagen escogida", Toast.LENGTH_SHORT).show();
        }
    }
/*
    private void subirMaquina(String tipo, String marca, final String modelo, String serial) {

        model.setSubDiagnostico(true);
        int contadorBn = edtContadorBn.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtContadorBn.getText().toString());
        int  contadorColor = edtContadorColor.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtContadorColor.getText().toString());
        String img = txtImg.getText().toString();

        String idCliente = Objects.requireNonNull(model.getTiket().getValue()).getIdCliente();
        final Maquina maquina = new Maquina(idCliente, tipo, img, marca, modelo, serial.toUpperCase(), contadorBn, contadorColor);


        db.collection(Constantes.MAQUINAS).document(maquina.getId()).set(maquina).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CrearMaquina.this, "Creada con exito", Toast.LENGTH_SHORT).show();
                    // modificar el tiket ( maquinas item 0 => maquina id son "_"
                    // adicionar maquina al array
                    model.setSubDiagnostico(false);
                    model.setSubAcualizado(true);
                    model.setSubImg(false);
                    db.collection(Constantes.TIKETS).document(model.getTiket().getValue().getId())
                            .update("maquinas", FieldValue.arrayUnion(maquina.getId()), "maquinas",
                                 //   FieldValue.arrayRemove(model.getTiket().getValue().getMaquinas().get(0)),
                                  //  "maquinas", FieldValue.arrayUnion(model.getTiket().getValue().getMaquinas().get(0)
                                   //         + " ( " + maquina.getId().replaceAll("_", " ") + " )"))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CrearMaquina.this, "Actualizado con exito", Toast.LENGTH_SHORT).show();
                                        model.setSubAcualizado(false);
                                        model.setTerminar(true);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(CrearMaquina.this, "Error en base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
*/
    private void crearMarca(final String marca) {


        db.collection(Constantes.MARCAS).document(Constantes.FOTOCOPIADORAS)
                .update("marcas", FieldValue.arrayUnion(marca)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CrearMaquina.this, "Marca " + marca +
                        " Agregado", Toast.LENGTH_SHORT).show();
                new DatosInternos(CrearMaquina.this).setMarca(marca);
            }
        });
    }

    private void crearModelo(final String modelo) {


        db.collection(Constantes.MARCAS).document(Constantes.FOTOCOPIADORAS)
                .update("modelos", FieldValue.arrayUnion(modelo)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CrearMaquina.this, "Modelo " + modelo + " Agregado", Toast.LENGTH_SHORT).show();
                new DatosInternos(CrearMaquina.this).setModelo(modelo);
            }
        });
    }

    private void llenarRecylcerImg(String tipo, String marca, final String modelo) {

        prbImgs.setVisibility(View.VISIBLE);
        crvSubirImagnes.setVisibility(View.INVISIBLE);
        rcvMaquinas.setVisibility(View.VISIBLE);

        Log.e("llenando imgs...", tipo);
        rcvMaquinas.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.HORIZONTAL);
        rcvMaquinas.setLayoutManager(llm);

        Query query = db
                .collection(Constantes.MAQUINASDEFAULT)
                .whereEqualTo("marca", marca)
                .whereEqualTo("modelo", modelo);
        // .orderBy(Constantes.MARCAS, Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<MaquinaDefault> options = new FirestoreRecyclerOptions.Builder<MaquinaDefault>()
                .setQuery(query, MaquinaDefault.class)
                .build();

        final AdapImgMaquinas adaptador = new AdapImgMaquinas(options, CrearMaquina.this);
        adaptador.startListening();
        rcvMaquinas.setAdapter(adaptador);

        adaptador.setOnItemClickListener(new AdapImgMaquinas.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                MaquinaDefault maquina = documentSnapshot.toObject(MaquinaDefault.class);
                assert maquina != null;
                model.setImg(maquina.getImg());
                txtImg.setText(maquina.getImg());
            }
        });

        // lineas para experiencia de usuario al no encotrar imagen despues de 3.5 seg
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                prbImgs.setVisibility(View.INVISIBLE);
                if (adaptador.getItemCount() > 0) {


                } else {
                    Toast.makeText(CrearMaquina.this, "Sin ninguna resultado", Toast.LENGTH_LONG).show();
                    rcvMaquinas.setVisibility(View.INVISIBLE);
                    crvSubirImagnes.setVisibility(View.VISIBLE);
                    crvCargarImagenes.setCardBackgroundColor(getResources().getColor(R.color.colorGrisOscuro));
                }
            }
        }, 3500);


    }

    private boolean verificamosMarca(String marca) {

        MarcasModelos marcasModelos = new DatosInternos(this).getMarcasModelos();
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

        MarcasModelos marcasModelos = new DatosInternos(this).getMarcasModelos();
        if (marcasModelos != null) {

            for (int i = 0; i < marcasModelos.getModelos().size(); i++) {

                if (marcasModelos.getModelos().get(i).equals(modelo)) {
                    return true;
                }

            }
        }

        return false;

    }

    private void llenarTipo() {

        final ArrayList<String> tipos = new ArrayList<>();
        tipos.add("Fotocopiadora");
        tipos.add("Impresora");
        tipos.add("Otro");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, tipos);
        spnTipo.setAdapter(adapter);

        // linea para obtener la seleccion del spinner
        String text = spnTipo.getSelectedItem().toString();
        Log.e("selecionado", text);

    }

    private void traerMarcas() {

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
                    Toast.makeText(CrearMaquina.this, "Error al carga base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void llenarAutocompletes(MarcasModelos marcasModelos) {
        // actualizamos marcas modelos
        new DatosInternos(this).setMarcasModelos(marcasModelos);

        Log.e("lenando marcas", String.valueOf(marcasModelos.getModelos().size()));

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, marcasModelos.getMarcas());
        autMarca.setAdapter(adapter);

        adapterModelo = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, marcasModelos.getModelos());
        autModelo.setAdapter(adapterModelo);
    }

    private void llenar() {

       // txtReferencia.setText(model.getTiket().getValue().getMaquinas().get(0));
    }

    private void setWidgets() {


        txtReferencia = findViewById(R.id.txt_ref_cre_maq);
        autMarca = findViewById(R.id.aut_mar_cre_maq);
        autModelo = findViewById(R.id.aut_mod_cre_maq);
        edtContadorBn = findViewById(R.id.edt_con_bn_cre_maq);
        edtContadorColor = findViewById(R.id.edt_con_col_cre_maq);
        edtSerial = findViewById(R.id.edt_num_cre_maq);
        crvCargarImagenes = findViewById(R.id.crv_btn_esc_img_cre_maq);
        crvSubirImagnes = findViewById(R.id.crv_btn_sub_img_cre_maq);
        crvCrear = findViewById(R.id.crv_btn_cre_cre_maq);
        imgMaquina = findViewById(R.id.img_cre_maq);
        spnTipo = findViewById(R.id.spn);
        rcvMaquinas = findViewById(R.id.rcv_img_cre_maq);
        txtImg = findViewById(R.id.txt_img);
        prbImgs = findViewById(R.id.prb_imgs);
        imgAtras = findViewById(R.id.img_cre_atras_maq);
        chbSinSerial = findViewById(R.id.chb_no_serial);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMG) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri uriResult = result.getUri();
            Log.e("caragamos", String.valueOf(result.getUri()));

            model.setImg(String.valueOf(uriResult));
            txtImg.setText("");
            model.setUri(uriResult);


        }

    }
}
