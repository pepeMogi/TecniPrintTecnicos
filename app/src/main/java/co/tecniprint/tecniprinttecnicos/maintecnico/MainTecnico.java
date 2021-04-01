package co.tecniprint.tecniprinttecnicos.maintecnico;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import co.tecniprint.tecniprinttecnicos.DatosInternos;

import co.tecniprint.tecniprinttecnicos.R;

import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Tecnico;

public class MainTecnico extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Vibrator vibrator;
    private Switch swtInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main_tecnico);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        funSwich();


        activarDrawer();
        String tecnicoString = getIntent().getStringExtra(Constantes.TECNICOS);
        boolean internet = getIntent().getBooleanExtra(Constantes.INTERNET,true);
        if (!internet){
            swtInternet.setChecked(true);
            fab.setVisibility(View.INVISIBLE);

            new DatosInternos(this).setInternet(false);
        }else{
            new DatosInternos(this).setInternet(true);
        }

        if (tecnicoString != null){
            new DatosInternos(this).setTecnico(tecnicoString);
        }




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainTecnico.this,CrearTiket.class);
                vibrator.vibrate(50);
                startActivity(intent);
            }
        });
    }

    private void funSwich() {

        swtInternet = findViewById(R.id.swt_main);

        swtInternet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    swtInternet.setText("Off Line");
                }else{
                    swtInternet.setText("On Line");
                }

            }
        });
    }

    private void activarDrawer() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        final View v = navigationView.inflateHeaderView(R.layout.nav_header_main_tecnico);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                 Tecnico tecnico = new DatosInternos(MainTecnico.this).getTecnico();

                if (tecnico != null) {




                        ImageView imgdrawer = v.findViewById(R.id.img_nav_tec);
                        TextView titulo = v.findViewById(R.id.txt_nom_nav);
                        TextView tipo = v.findViewById(R.id.txt_nav_tipo);
                        //   LinearLayout lnlFondo = v.findViewById(R.id.lnl_drawer);

                        titulo.setText(tecnico.getNombre());
                        tipo.setText(tecnico.getTipo());
                        Picasso.with(MainTecnico.this).load(tecnico.getImg()).fit().into(imgdrawer);
                        // lnlFondo.setBackgroundColor(1000);


                }
            }
        }, 3000);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_tecnico, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
