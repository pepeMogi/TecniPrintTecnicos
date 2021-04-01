package co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.maintecnico.adaptadores.AdapImgTiket;

import static android.app.Activity.RESULT_OK;

public class Frag_cuatro_imgs extends Fragment {

    private static final int IMG = 4;


    private RecyclerView rcvImg;
    private CardView crvAgregar, crvSiguiente,crvAtras;
    private ImageView imgDefault;

    private ModelDiagnostico model;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets(view);
        model = new ViewModelProvider(requireActivity()).get(ModelDiagnostico.class);

        funBotones();
        funImagen();
        escucharImagenes();
    }

    private void escucharImagenes() {

        rcvImg.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(RecyclerView.HORIZONTAL);
        GridLayoutManager glm = new GridLayoutManager(requireContext(),2);
        glm.setOrientation(RecyclerView.VERTICAL);
        rcvImg.setLayoutManager(glm);


        model.getAnexos().observe(requireActivity(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> uris) {
                AdapImgTiket adap = new AdapImgTiket(uris,requireContext());
                rcvImg.setAdapter(adap);
                imgDefault.setVisibility(View.INVISIBLE);
            }
        });
    }



    private void funImagen() {

        crvAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()

                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(false)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        // metodo para cambiar la calidad y tamaño de iamgen
                        .setOutputCompressQuality(20)
                        .getIntent(requireActivity());
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, IMG);
            }
        });

    }


    private void funBotones() {

        crvSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(4,true);
            }
        });

        crvAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
    }

    private void setWidgets(View v) {




        rcvImg = v.findViewById(R.id.rcv_img_tik);
        crvAgregar = v.findViewById(R.id.crv_btn_agregar_img_tik);
        crvSiguiente = v.findViewById(R.id.crv_btn_dos_sig);
        crvAtras = v.findViewById(R.id.crv_btn_dos_atr);
        imgDefault = v.findViewById(R.id.img_default_dos);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_cuatro_imgs,container,false);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == IMG) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri uriResult = result.getUri();
            Log.e("caragamos",String.valueOf(result.getUri()));

            model.setAnexo(String.valueOf(uriResult));
            imgDefault.setVisibility(View.INVISIBLE);


        }


    }
}
