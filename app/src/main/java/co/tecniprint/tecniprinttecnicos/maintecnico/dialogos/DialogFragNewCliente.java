package co.tecniprint.tecniprinttecnicos.maintecnico.dialogos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.maintecnico.MainTecnico;
import co.tecniprint.tecniprinttecnicos.maintecnico.creartiket.ModelNewTikete;

public class DialogFragNewCliente extends DialogFragment {

    private ProgressBar prb;
    private TextView txtTitulo;
    private ModelNewTikete model;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets(view);
        model = new ViewModelProvider(requireActivity()).get(ModelNewTikete.class);
        observarCambios();

    }

    private void observarCambios() {

        model.getTerminarCliente().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                txtTitulo.setText("Cliente Creado con Exito");
                prb.setVisibility(View.INVISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Objects.requireNonNull(model.getViewPager().getValue()).setCurrentItem(2, true);
                        dismiss();
                    }
                }, 500);

            }
        });

    }

    private void setWidgets(View v) {

        prb = v.findViewById(R.id.prb_new_cli);
        txtTitulo = v.findViewById(R.id.txt_new_cli);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_frag_new_cliente, container);
    }
}
