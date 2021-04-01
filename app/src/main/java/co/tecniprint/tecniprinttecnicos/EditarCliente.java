package co.tecniprint.tecniprinttecnicos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.EditText;
import android.widget.ImageView;

import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.maintecnico.MainTecnico;

public class EditarCliente extends AppCompatActivity {

    private EditText edtNombre, edtDireccion, edtCiudad, edtCelular, edtCelularDos, edtCorreo, edtSolicitante;
    private ImageView img;
    private CardView crvGuargarCambios;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cliente);

        setWidgets();

        String clienteString = getIntent().getStringExtra(Constantes.CLIENTE);
        if (clienteString != null){
            Cliente cliente = new Utilidades().stringToCliente(clienteString);
            llenarCliente(cliente);
        }
    }

    private void llenarCliente(Cliente cliente) {

    }



    private void setWidgets() {

        img = findViewById(R.id.img_dos_cliente_edt);
        edtNombre = findViewById(R.id.edt_nom_new_cli_dos_edt);
        edtDireccion = findViewById(R.id.edt_dir_new_cli_dos_edt);
        edtCiudad = findViewById(R.id.edt_ciu_new_cli_dos_edt);
        edtCelular = findViewById(R.id.edt_cel_new_cli_dos_edt);
        edtCelularDos = findViewById(R.id.edt_cel_dos_new_cli_dos_edt);
        edtCorreo = findViewById(R.id.edt_cor_new_cli_dos_edt);
        edtSolicitante = findViewById(R.id.edt_sol_new_cli_edt);
        crvGuargarCambios = findViewById(R.id.crv_btn_atras_dos_cre_tik_edt);

    }



    @Override
    public void onBackPressed() {

        Intent intent = new Intent(EditarCliente.this, MainTecnico.class);
        startActivity(intent);
        finish();
    }
}
