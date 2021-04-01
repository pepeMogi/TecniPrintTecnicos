package co.tecniprint.tecniprinttecnicos.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.DatosInternos;
import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.Utilidades;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.ListaUsuario;
import co.tecniprint.tecniprinttecnicos.entidades.Tecnico;
import co.tecniprint.tecniprinttecnicos.maintecnico.MainTecnico;
import co.tecniprint.tecniprinttecnicos.pruebas.Pruebas;

public class Login extends AppCompatActivity {

    private EditText edtCorreo, edtPass;
    private CardView btnEntrar;
    private CheckBox chbRecuerdame;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.act_login);

        setWidgets();
        mAuth = FirebaseAuth.getInstance();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {

            if (isOnlineNet()) {
                Log.e("CON INTERNET", "si");
                funcionesInternet();
            } else {
                funcionesOffline();
            }


        } else {
            funcionesOffline();
        }


        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));


      /*  Tecnico tecnico = new Tecnico("Pedro Javier Coral Gaviria", "Pedro Coral", "108592756", "318308822", "A+", "tecnico", "none", "2", "pedro@gmail.com", "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constantes.TECNICOS).document(tecnico.getId()).set(tecnico).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                   Toast.makeText(Login.this, "Creado", Toast.LENGTH_SHORT).show();
               }
            }
        });*/


        // linea para pruebas
        // startActivity(new Intent(Login.this, Pruebas.class));

    }

    private void funcionesOffline() {

        Log.e("CON INTERNET", "NO");
        Tecnico tecnico = new DatosInternos(this).getTecnico();
        if (tecnico != null) {

            String tecnicoString = new Utilidades().tecnicoToString(tecnico);
            Intent intent = new Intent(Login.this, MainTecnico.class);
            intent.putExtra(Constantes.TECNICOS, tecnicoString);
            intent.putExtra(Constantes.INTERNET, false);
            startActivity(intent);

        } else {
            btnEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Login.this, "Sin Conexion a Internet", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void funcionesInternet() {

        funBtnEntrar();
        verificarRecuerdame();


    }


    private void verificarRecuerdame() {

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String correo = sharedPref.getString(Constantes.CORREO, null);
        String pass = sharedPref.getString(Constantes.PASS, null);

        if (correo != null && pass != null) {
            loguearse(correo, pass);
        }


    }

    private void funBtnEntrar() {


        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new Utilidades().verificarEdt(edtPass) & new Utilidades().verificarEdt(edtCorreo)) {
                    String email = edtCorreo.getText().toString();
                    String pass = edtPass.getText().toString();

                    loguearse(email, pass);
                }
            }
        });
    }

    private void loguearse(final String email, String pass) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logeado", Toast.LENGTH_SHORT).show();
                            verificarTecnico(email);
                        } else {
                            Toast.makeText(Login.this, "No login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void verificarTecnico(final String email) {

        db.collection(Constantes.USUARIOS).whereEqualTo("correo", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        switch (Objects.requireNonNull(document.getString("rol"))) {
                            case "tecnico":
                                entrarActTecnicos(email, db);
                                break;

                            case "supervisor":
                                Toast.makeText(Login.this, "Es supervisor", Toast.LENGTH_SHORT).show();
                                break;
                        }


                        break;
                    }
                } else {
                    Toast.makeText(Login.this, "Error en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void entrarActTecnicos(final String email, FirebaseFirestore db) {

        if (chbRecuerdame.isChecked()) {
            String pass = edtPass.getText().toString();
            SharedPreferences sharedPref = this.getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constantes.CORREO, email);
            editor.putString(Constantes.PASS, pass);
            editor.apply();
        }

        Tecnico tecnico = new DatosInternos(this).getTecnico();
        if (tecnico != null && tecnico.getEmail().equals(email)) {

            String tecnicoString = new Utilidades().tecnicoToString(tecnico);
            Intent intent = new Intent(Login.this, MainTecnico.class);
            intent.putExtra(Constantes.TECNICOS, tecnicoString);
            startActivity(intent);

        } else {

            db.collection(Constantes.TECNICOS).whereEqualTo("email", email)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (DocumentSnapshot doc : task.getResult()) {
                            Tecnico tecnico = doc.toObject(Tecnico.class);
                            actualizamosToken(tecnico, db);


                        }


                    }
                }
            });
        }


    }

    private void actualizamosToken(Tecnico tecnico, FirebaseFirestore db) {

        FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
        instanceId.getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {

                    db.collection(Constantes.TECNICOS).document(tecnico.getId()).update("token", task.getResult().getToken())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    new DatosInternos(Login.this).setTecnico(tecnico);
                                    Intent intent = new Intent(Login.this, MainTecnico.class);
                                    startActivity(intent);
                                }
                            });

                }
            }
        });


    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private void setWidgets() {

        edtCorreo = findViewById(R.id.edt_log_cor);
        edtPass = findViewById(R.id.edt_log_con);
        btnEntrar = findViewById(R.id.crv_btn_log);
        chbRecuerdame = findViewById(R.id.chb_loging);
    }
}
