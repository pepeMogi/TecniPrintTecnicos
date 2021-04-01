package co.tecniprint.tecniprinttecnicos.maintecnico.dialogos;

import android.content.Intent;
import android.os.Bundle;
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

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.maintecnico.MainTecnico;
import co.tecniprint.tecniprinttecnicos.modelos.ModelMaquinaCrear;

public class FragDialogSubImg extends DialogFragment {

    private ProgressBar prbImg;
    private TextView txtTitulo;






    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets(view);
    }




    private void setWidgets(View v) {

        prbImg = v.findViewById(R.id.prb_img_pros_diag_maq_img);
        txtTitulo = v.findViewById(R.id.txt_tit_pros_diag_maq_img);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_dilog_sub_img, container);
    }
}
