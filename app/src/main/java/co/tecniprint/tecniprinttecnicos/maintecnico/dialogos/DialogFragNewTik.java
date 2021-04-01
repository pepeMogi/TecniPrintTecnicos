package co.tecniprint.tecniprinttecnicos.maintecnico.dialogos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.MainTecnico;
import co.tecniprint.tecniprinttecnicos.maintecnico.creartiket.ModelNewTikete;

public class DialogFragNewTik extends DialogFragment {

    private ProgressBar prbImg,prbData,prbCont;
    private TextView txtTitulo, txtData, txtCont, txtImg;



    private ModelNewTikete model;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets(view);
        model = new ViewModelProvider(requireActivity()).get(ModelNewTikete.class);
        observarCambios();

    }



    private void observarCambios() {

        model.getTerminar().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                txtTitulo.setText("Tiket Creado Con Exito");
                prbImg.setVisibility(View.INVISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                        Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(4,true);
                    }
                },1000);


            }
        });

        model.getTerminarImg().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                prbImg.setVisibility(View.INVISIBLE);
                txtImg.setText("Anexos cargados con exito");

            }
        });

        model.getTerminarData().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                prbData.setVisibility(View.INVISIBLE);
                txtData.setText("Datos de Tiket cargados con Exito");
            }
        });

        model.getTerminarCon().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                prbCont.setVisibility(View.INVISIBLE);
                txtCont.setText("Consecutivo Actualizado con Exito");
            }
        });

    }



    private void setWidgets(View v) {

        prbImg = v.findViewById(R.id.prb_img_new_tik_img);
        prbData = v.findViewById(R.id.prb_img_new_tik_tik);
        prbCont = v.findViewById(R.id.prb_img_new_tik_cont);


        txtTitulo = v.findViewById(R.id.txt_tit_pros_diag_maq_tik);


        txtImg = v.findViewById(R.id.txt_new_tik_img);
        txtData = v.findViewById(R.id.txt_new_tik_tik);
        txtCont = v.findViewById(R.id.txt_new_tik_cont);




    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_frag_crae_tik, container);
    }


}
