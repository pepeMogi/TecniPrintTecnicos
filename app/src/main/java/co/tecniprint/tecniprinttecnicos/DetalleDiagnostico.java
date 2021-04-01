package co.tecniprint.tecniprinttecnicos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import co.tecniprint.tecniprinttecnicos.adaptadoresglobales.AdapMaqTik;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores.AdapImgTiket;

public class DetalleDiagnostico extends AppCompatActivity {

    private TextView txtFallo,
            txtFecha, txtDiagnostico, txtRepuestos;
    private RecyclerView rcvAnexos, rcvMaquinas;
    private ImageView imgAtras;

    private AdapImgTiket adaptador;
    private AdapMaqTik adaptadorMaquina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_diagnostico);

        setWidgets();
        String diagnosticoString = getIntent().getStringExtra(Constantes.DIAGNOSTICOS);
        String tiketString = getIntent().getStringExtra(Constantes.TIKETS);
        if (diagnosticoString != null){
            Diagnostico diagnostico = new Utilidades().stringToDiagnostico(diagnosticoString);
            Tiket tiket = new Utilidades().stringToTiket(tiketString);
            llenarDiagnostico(diagnostico,tiket);
            llenarMaquinas(tiket);
            llenarRecycler(diagnostico);
        }

        funBotones();

    }

    private void funBotones() {

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void llenarRecycler(Diagnostico diagnostico) {

        rcvAnexos.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(this,2);
        glm.setOrientation(RecyclerView.VERTICAL);;
        rcvAnexos.setLayoutManager(glm);

        adaptador = new AdapImgTiket(diagnostico.getImgs(),this);
        rcvAnexos.setAdapter(adaptador);



    }

    private void llenarMaquinas(Tiket tiket) {

        rcvMaquinas.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        rcvMaquinas.setLayoutManager(llm);

       // adaptadorMaquina = new AdapMaqTik(tiket.getMaquinas());
        rcvMaquinas.setAdapter(adaptadorMaquina);


    }

    private void llenarDiagnostico(Diagnostico diagnostico, Tiket tiket) {

        txtFallo.setText(tiket.getFalla());




        txtFecha.setText(String.valueOf(diagnostico.getFecha().toDate().getYear()));
        txtDiagnostico.setText(diagnostico.getDiagnostico());

        String text = "";
        for (int i = 0; i < diagnostico.getRepuestos().size(); i++){
            text += diagnostico.getRepuestos().get(i) + "\n";
        }

        txtRepuestos.setText(text);





    }

    private void setWidgets() {


        txtFallo = findViewById(R.id.txt_det_dia_fallo);
        txtFecha = findViewById(R.id.txt_dia_det_fec);
        txtDiagnostico = findViewById(R.id.txt_det_dia_dia);
        txtRepuestos = findViewById(R.id.txt_uno_list_rep);

        rcvAnexos = findViewById(R.id.rcv_dia_det);
        rcvMaquinas = findViewById(R.id.rcv_det_dia_maq);
        imgAtras = findViewById(R.id.img_det_dig_atras);

    }
}
