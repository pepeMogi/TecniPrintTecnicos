package co.tecniprint.tecniprinttecnicos.maintecnico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.maintecnico.creartiket.Frag_cinco_compartir;
import co.tecniprint.tecniprinttecnicos.maintecnico.creartiket.Frag_crear_cliente;
import co.tecniprint.tecniprinttecnicos.maintecnico.creartiket.Frag_cuatro_fallo_cliente;
import co.tecniprint.tecniprinttecnicos.maintecnico.creartiket.Frag_dos_datos_cliente;
import co.tecniprint.tecniprinttecnicos.maintecnico.creartiket.Frag_referenciar_maquina_cliente;
import co.tecniprint.tecniprinttecnicos.maintecnico.creartiket.Frag_tres_maquina_cliente;
import co.tecniprint.tecniprinttecnicos.maintecnico.creartiket.Frag_uno_cliente_busqueda;
import co.tecniprint.tecniprinttecnicos.maintecnico.creartiket.ModelNewTikete;
import co.tecniprint.tecniprinttecnicos.otros.AdapViewPager;

public class CrearTiket extends AppCompatActivity {
    // activity principal de sistema de gestion Tikets
    // viene de Detalle Tikets
    private ViewPager vpgGestionar;
    private TextView txtTitulo;
    private AdapViewPager adaptador;
    private ModelNewTikete model;
    private Vibrator vibrator;
    private ImageView imAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tiket);

        setWidget();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        String nombreCliente = getIntent().getStringExtra(Constantes.CLIENTE);
        if (nombreCliente != null){

        }


        model = new ViewModelProvider(this).get(ModelNewTikete.class);
        model.setViewPager(vpgGestionar);



        activarFormularios();
        funBoton();
    }

    private void funBoton() {
        imAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                onBackPressed();
            }
        });
    }

    private void activarFormularios() {

        ArrayList<Fragment> fragments = new ArrayList<>();

        // ingresa diagnostico *obligatorio & repuestos usados 0
        fragments.add(new Frag_uno_cliente_busqueda());
        // verificar datos de cliente 1
        fragments.add(new Frag_dos_datos_cliente());
        // escoger maquina 2
        fragments.add(new Frag_tres_maquina_cliente());
        // fallo maquina y subida de tiket 3
        fragments.add(new Frag_cuatro_fallo_cliente());

        // compartir tiket 4
        fragments.add(new Frag_cinco_compartir());



        // crear tiket 5
        fragments.add(new Frag_crear_cliente());
        // referenciar tiket 6
        fragments.add(new Frag_referenciar_maquina_cliente());





        adaptador = new AdapViewPager(getSupportFragmentManager(),
                this,
                vpgGestionar,
                fragments);
        vpgGestionar.setAdapter(adaptador);
        vpgGestionar.setOffscreenPageLimit(6);


        //  agregarIndicadorPuntos(0);

        vpgGestionar.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setTiltulo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setTiltulo(int position) {

        switch (position){
            case 0:
                txtTitulo.setText("Escoge Cliente");
                break;

            case 1:
                txtTitulo.setText("Confirma Datos del Cliente");
                break;

            case 2:
                txtTitulo.setText("Escoge Maquina");
                break;

            case 3:
                txtTitulo.setText("Fallo de Maquina");
                break;

            case 4:
                txtTitulo.setText("Compatir Tiket");
                imAtras.setVisibility(View.INVISIBLE);
                break;

            case 5:
                txtTitulo.setText("Crear Cliente");
                break;
            case 6:
                txtTitulo.setText("Referenciando Maquina");
                break;
        }
    }


    private void setWidget() {

        vpgGestionar = findViewById(R.id.vpg_cre_tik);
        txtTitulo = findViewById(R.id.txt_tit_cre_tik);
        imAtras = findViewById(R.id.img_cre_atras);
    }

    @Override
    public void onBackPressed() {

        int position = vpgGestionar.getCurrentItem();
        switch (position){
            case 0:
                super.onBackPressed();
                break;
            case 1:
                vpgGestionar.setCurrentItem(0,true);
                break;
            case 2:
                vpgGestionar.setCurrentItem(1,true);
                break;
            case 3:
                vpgGestionar.setCurrentItem(2,true);
                break;
            case 4:
              //  vpgGestionar.setCurrentItem(3,true);
                Toast.makeText(this, "No se puede realizar cambios desde esta seccion", Toast.LENGTH_LONG).show();
                break;
            case 5:
                vpgGestionar.setCurrentItem(0,true);
                break;
            default:
                super.onBackPressed();
        }

    }
}
