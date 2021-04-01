package co.tecniprint.tecniprinttecnicos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.adaptadoresglobales.AdapImgAnexos;
import co.tecniprint.tecniprinttecnicos.adaptadoresglobales.AdapImgAnexosL;
import co.tecniprint.tecniprinttecnicos.adaptadoresglobales.AdapMaqTik;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.GestionTiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores.AdapDiagTiketDetalle;
import co.tecniprint.tecniprinttecnicos.pruebas.ModelDetalleTiket;

public class DetalleTiket extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;

    private TextView txtPrioridad, txtTipo, txtNumero, txtNombre, txtSinDiag, txtMaquina, txtNoImg,
            txtFalloDescripcion, txtVisita, txtAsignado, txtEstado, txtDireccion, txtCelular, txtCelularDos;
    private CardView crvGestionar, crvPrioridad, crvCelularDos;
    private ImageView imgAtras;
    private RecyclerView rcvDiagnosticos, rcvAnexos;

    private ModelDetalleTiket model;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AdapDiagTiketDetalle adaptador;
    private Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detalle_tiket);

        setWidget();
        model = new ViewModelProvider(this).get(ModelDetalleTiket.class);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        String tiketString = getIntent().getStringExtra(Constantes.TIKETS);
        if (tiketString != null) {

            Tiket tiket = new Utilidades().stringToTiket(tiketString);
            llenarTiket(tiket);

            funBotones(tiketString);
          //  escucharDiagnosticos(tiket);
            escucharLlenado(tiket);
          //  Log.e("numero de Diagnoticos => ", String.valueOf(tiket.getDiagnostico().size()));
            // empezar el llenado dinamico
            model.setNumero(0);

        }

        funAtras();
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));





    }



    private String getFecha(Timestamp fecha) {

        SimpleDateFormat format = new SimpleDateFormat("dd - MMMM - yyyy");
        return format.format(fecha.toDate()).toUpperCase();
    }

    private int getPrioridad(int prioridad) {

        switch (prioridad) {
            case 0:
                return -9277328;
            case 1:
                return -15754899;

            case 2:
                return -601831;

            case 3:
                return -91882;

            case 4:
                return -1303749;

            default:
                return -9277328;
        }

    }

    private String getPrioridadText(int prioridad) {
        switch (prioridad) {
            case 0:
                return "Sin Prioridad";
            case 1:
                return "Baja";

            case 2:
                return "Media";

            case 3:
                return "Urgente";

            case 4:
                return "Rellamada";

            default:
                return "Sin Prioridad";
        }
    }


    private void escucharLlenado(final Tiket tiket) {

        rcvDiagnosticos.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        rcvDiagnosticos.setLayoutManager(llm);

        final String tiketString = new Utilidades().tiketToString(tiket);

        model.getDiagnosticos().observe(this, new Observer<ArrayList<Diagnostico>>() {
            @Override
            public void onChanged(ArrayList<Diagnostico> diagnosticos) {
                Log.e("llenoDiagnostico => ", String.valueOf(diagnosticos.size()));
                adaptador = new AdapDiagTiketDetalle(diagnosticos);
                rcvDiagnosticos.setAdapter(adaptador);
                adaptador.setOnItemClickListener(new AdapDiagTiketDetalle.OnItemClickListener() {
                    @Override
                    public void onItemClick(Diagnostico diagnostico) {
                        String diagnosticoString = new Utilidades().diagnosticoToString(diagnostico);
                        Intent intent = new Intent(DetalleTiket.this,DetalleDiagnostico.class);
                        intent.putExtra(Constantes.TIKETS, tiketString);
                        intent.putExtra(Constantes.DIAGNOSTICOS,diagnosticoString);
                        startActivity(intent);
                    }
                });
            }
        });

    }




    private void funBotones(final String tiketString) {

        crvGestionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleTiket.this, GestionTiket.class);
                intent.putExtra(Constantes.TIKETS, tiketString);
                vibrator.vibrate(100);
                startActivity(intent);
            }
        });

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtCelular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llamar(txtCelular);


            }
        });

        txtCelularDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    llamar(txtCelularDos);
            }
        });
    }

    private void llamar(TextView textView){

        if (ContextCompat.checkSelfPermission(DetalleTiket.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            String tel = textView.getText().toString();
            if (tel.substring(0,1).equals("7")){
                tel = "+032" + tel;
            }

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
            startActivity(intent);
        } else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permiso de llamada necesario")
                    .setMessage("para completar esta acccion es necesario otorgar el permiso de llamar")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(DetalleTiket.this,
                                    new String[] {Manifest.permission.CALL_PHONE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CALL_PHONE}, STORAGE_PERMISSION_CODE);
        }
    }


    private void funAtras() {

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void llenarTiket(Tiket tiket) {

        txtPrioridad.setText(getPrioridadText(tiket.getPrioridad()));
        crvPrioridad.setCardBackgroundColor(getPrioridad(tiket.getPrioridad()));
        txtTipo.setText(obtenerTipo(tiket.getTipo()));
        txtNumero.setText(tiket.getId());
        txtNombre.setText(tiket.getNombre());
        txtMaquina.setText(tiket.getIdMaquina().replaceAll("_"," "));
        txtEstado.setText(tiket.getEstado());
        txtFalloDescripcion.setText(tiket.getFalla());

        if (getFecha(tiket.getUltimaVisita()).equals("31 - DICIEMBRE - 1969")){
            txtVisita.setVisibility(View.INVISIBLE);
            txtSinDiag.setVisibility(View.VISIBLE);

        }

        txtVisita.setText("Ult Visita: " + getFecha(tiket.getUltimaVisita()));
        txtAsignado.setText(tiket.getAsignado());
        txtDireccion.setText(tiket.getDireccion());
        txtCelular.setText(tiket.getCelularCliente());
        if (tiket.getCelularSolicitante() == null || tiket.getCelularSolicitante().equals("")){
            crvCelularDos.setVisibility(View.INVISIBLE);
        }else {
            txtCelularDos.setText(tiket.getCelularSolicitante());
        }

        txtAsignado.setText(tiket.getAsignado());

        llenarAnexos(tiket);




    }

    private void llenarAnexos(Tiket tiket) {

        rcvAnexos.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.HORIZONTAL);
        rcvAnexos.setLayoutManager(llm);

        if (tiket.getAnexos() != null && tiket.getAnexos().size() > 0) {
            txtNoImg.setVisibility(View.INVISIBLE);
            AdapImgAnexosL adaptador = new AdapImgAnexosL(tiket.getAnexos(), DetalleTiket.this);
            rcvAnexos.setAdapter(adaptador);

            adaptador.setOnItemClickListener(new AdapImgAnexosL.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    vibrator.vibrate(50);
                    Intent intent = new Intent(DetalleTiket.this, PantallaCompleta.class);
                    intent.putExtra(Constantes.IMG, tiket.getAnexos().get(position));
                    startActivity(intent);
                }
            });
        }else{
            txtNoImg.setVisibility(View.VISIBLE);
        }

    }

    private String obtenerTipo(String tipo) {

        String text = tipo;
        if (tipo.equals("")){
            text = " ";
        }

        return text;
    }

    private void setWidget() {

        txtPrioridad = findViewById(R.id.txt_det_pri);
        txtTipo = findViewById(R.id.txt_det_tip);
        txtNumero = findViewById(R.id.txt_det_num);
        txtNombre = findViewById(R.id.txt_det_nom);
        txtMaquina = findViewById(R.id.txt_det_maq);
        txtSinDiag = findViewById(R.id.txt_det_sin_diag);

        txtFalloDescripcion = findViewById(R.id.txt_det_des_fallo);
        txtVisita = findViewById(R.id.txt_det_vis);
        txtAsignado = findViewById(R.id.txt_det_asi);
        txtEstado = findViewById(R.id.txt_det_est);
        txtDireccion = findViewById(R.id.txt_det_dir);
        txtCelular = findViewById(R.id.txt_crd_cel);
        txtCelularDos = findViewById(R.id.txt_crd_cel_dos);
        txtNoImg = findViewById(R.id.txt_no_img);

        imgAtras = findViewById(R.id.img_det_tik_atras);
        crvGestionar = findViewById(R.id.crv_btn_det_tik_ges);
        rcvDiagnosticos = findViewById(R.id.rcv_det_diag);
        rcvAnexos = findViewById(R.id.rcv_det_ane);


        crvGestionar = findViewById(R.id.crv_btn_det_tik_ges);
        crvPrioridad = findViewById(R.id.crv_det_prio);
        crvCelularDos = findViewById(R.id.crv_det_cel_dos);
    }
}
