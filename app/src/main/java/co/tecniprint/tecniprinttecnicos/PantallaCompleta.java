package co.tecniprint.tecniprinttecnicos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import co.tecniprint.tecniprinttecnicos.entidades.Constantes;

public class PantallaCompleta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_completa);

        final PhotoView photoView = findViewById(R.id.photo_view);


        String url = getIntent().getStringExtra(Constantes.IMG);
        if (url != null){
            Picasso.with(this)
                    .load(url)
                    .into(photoView);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
