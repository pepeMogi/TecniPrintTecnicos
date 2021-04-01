package co.tecniprint.tecniprinttecnicos.maintecnico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.Frag_siete_generar;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.Frag_crear_maquina;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.Frag_seis_estado;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.Frag_cuatro_imgs;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.Frag_dos_repuestos;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.Frag_cinco_comentario;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.Frag_uno_diagnostico;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.Frag_tres_contadores;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.ModelDiagnostico;
import co.tecniprint.tecniprinttecnicos.otros.AdapViewPager;

public class GestionTiket extends AppCompatActivity {

    // activity principal de sistema de gestion Tikets
    // viene de Detalle Tikets
    private ViewPager vpgGestionar;
    private AdapViewPager adaptador;
    private ModelDiagnostico model;
    private ImageView imgAtras;
    private TextView txtTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_getion_tiket);

        setWidget();
        model = new ViewModelProvider(this).get(ModelDiagnostico.class);
        model.setViewPager(vpgGestionar);
        activarFormularios();

        String tiketString = getIntent().getStringExtra(Constantes.TIKETS);
        if (tiketString != null) {
            Tiket tiket = new Utilidades().stringToTiket(tiketString);
            model.setTiket(tiket);
            activarFormularios();
            if (hayInternet()){
                buscamosCliente(tiket);
            }

        }


        funAtras();


    }

    private void buscamosCliente(Tiket tiket) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constantes.CLIENTES).document(tiket.getIdCliente()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().exists()){
                                Cliente cliente = task.getResult().toObject(Cliente.class);
                                Log.e("setCliente",cliente.getNombre());
                                model.setCliente(cliente);
                                bucarMaquina(tiket,db);
                            }else {
                                Log.e("Error","Buscando cliente");
                            }
                        }
                    }
                });
    }

    private void bucarMaquina(Tiket tiket, FirebaseFirestore db) {
     db.collection(Constantes.MAQUINAS).document(tiket.getIdMaquina()).get()
             .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                     if (task.isSuccessful()){
                         if (task.getResult().exists()){
                             Maquina maquina = task.getResult().toObject(Maquina.class);
                             Log.e("setMaquina",maquina.getId());
                             model.setMaquina(maquina);
                         }
                     }else{
                         Log.e("Error","Buscando Maquina");
                     }
                 }
             });
    }

    private boolean hayInternet() {
        return true;
    }

    private void funAtras() {
        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               onBackPressed();

            }
        });
    }

    private void activarFormularios() {

        ArrayList<Fragment> fragments = new ArrayList<>();
        // ingresa diagnostico *obligatorio & repuestos usados  0
        fragments.add(new Frag_uno_diagnostico());
        // ingresar repuestos 1
        fragments.add(new Frag_dos_repuestos());
        // contadores 2
        fragments.add(new Frag_tres_contadores());
        // ingresa imagenes "fotos" para sustentar visita 3
        fragments.add(new Frag_cuatro_imgs());
        // ingresa comentario adicional al servicio "tecnico" 4
        fragments.add(new Frag_cinco_comentario());
        // dar estado a tiket *obligatorio 5
        fragments.add(new Frag_seis_estado());
        // genera reporte local o en linea 6
        fragments.add(new Frag_siete_generar());
        // crear maquina 7
        fragments.add(new Frag_crear_maquina());


        adaptador = new AdapViewPager(getSupportFragmentManager(),
                this,
                vpgGestionar,
                fragments);
        vpgGestionar.setAdapter(adaptador);
        vpgGestionar.setOffscreenPageLimit(7);

        //  agregarIndicadorPuntos(0);

        vpgGestionar.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                // agregarIndicadorPuntos(position);
                // posicionFormulario = position;
                tituluFormulario(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void tituluFormulario(int position) {


        switch (position) {
            case 0:
                txtTitulo.setText("Diagnostico y Solucion");
                break;

            case 1:
                txtTitulo.setText("Repuestos Instalados");
                break;

            case 2:
                txtTitulo.setText("Actualizar Contadores");
                break;

            case 3:
                txtTitulo.setText("Adicionar Evidencias");
                break;

            case 4:
                txtTitulo.setText("Comentario Adicional");
                break;

            case 5:
                txtTitulo.setText("Estado de Tiket");
                break;
            case 6:
                txtTitulo.setText("Generar Diagnostico");
                break;
            case 7:
                txtTitulo.setText("Crear Maquina");
                break;

        }

    }


    private void setWidget() {

        vpgGestionar = findViewById(R.id.vpg_gest_tik);
        imgAtras = findViewById(R.id.img_ges_atras);
        txtTitulo = findViewById(R.id.txt_tit_ges);
    }

    @Override
    public void onBackPressed() {

        Log.e("back",String.valueOf(vpgGestionar.getCurrentItem()));

        if (vpgGestionar.getCurrentItem() == 7) {

            vpgGestionar.setCurrentItem(0,true);
        } else if (vpgGestionar.getCurrentItem() != 0){

            vpgGestionar.setCurrentItem(vpgGestionar.getCurrentItem() - 1, true);

        }else{
            Snackbar.make(imgAtras, "Al salir de gestion de tikets se perdera el progreso", 5000).setAction("salir", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GestionTiket.super.onBackPressed();
                }
            }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();
        }


    }
}
