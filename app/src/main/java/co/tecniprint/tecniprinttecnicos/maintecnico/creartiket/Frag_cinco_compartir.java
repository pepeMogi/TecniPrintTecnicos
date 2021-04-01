package co.tecniprint.tecniprinttecnicos.maintecnico.creartiket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import co.tecniprint.tecniprinttecnicos.R;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.MainTecnico;

public class Frag_cinco_compartir extends Fragment {

    private TextView txtNumero, txtCliente, txtDireccion, txtIdMaquina;
    private CardView crvCompartir, crvTerminar;
    private Vibrator vibrator;

    private ModelNewTikete model;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidget(view);
        model = new ViewModelProvider(requireActivity()).get(ModelNewTikete.class);
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        funEscucharTiket();



    }

    private void funEscucharTiket() {

        model.getTiket().observe(requireActivity(), new Observer<Tiket>() {
            @Override
            public void onChanged(Tiket tiket) {
                llenarTiket(tiket);
                funBotones(tiket);
            }
        });
    }

    private void llenarTiket(Tiket tiket) {
        txtNumero.setText(tiket.getId().toUpperCase());
        txtCliente.setText(getCliente(tiket));
        txtDireccion.setText(tiket.getDireccion());
        txtIdMaquina.setText(tiket.getIdMaquina().replaceAll("_"," "));
    }

    private String getCliente(Tiket tiket) {

        String cliente = tiket.getNombre();

        if (tiket.getSolicitante() != null && !tiket.getSolicitante().equals("")){
            cliente += "/" + tiket.getSolicitante();
        }

        return cliente;
    }

    private void funBotones(final Tiket tiket) {


        crvCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Toast.makeText(requireContext(), "Compartiendo con link", Toast.LENGTH_SHORT).show();
                crearLink(tiket);
            }
        });

        crvTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                Intent intent = new Intent(requireActivity(), MainTecnico.class);
                startActivity(intent);
            }
        });

    }


    private void crearLink(Tiket tiket) {

        String link = getLinkLargo(tiket);

        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/ptecni.appspot.com/o/Sin-t%C3%ADtulo-2.jpg?alt=media&token=2662fee1-8a53-40e4-acda-57049c1953f7");
        String descripcion;
        if (tiket.getEmail() != null){
            descripcion = "Crea tu cuenta con tu correo " + tiket.getEmail();
        }else{
            descripcion = "Mira el estado de tu tiket con el numero " + tiket.getId();
        }

        DynamicLink.SocialMetaTagParameters parameters = new DynamicLink.SocialMetaTagParameters.Builder()
                // no da images
                //
                .setImageUrl(uri)
                .setTitle("Tiket de Servicio TecniPrint")
                .setDescription(descripcion).build();

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://tecniprinttecnicos.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("co.tecniprint.tecniprintcliente").build())
                .setSocialMetaTagParameters(parameters)
                // Set parameters
                // ...
                .buildShortDynamicLink()
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            abrirChooser(String.valueOf(shortLink));

                        } else {
                            // Error
                            // ...
                        }
                    }
                });


    }

    private String getLinkLargo(Tiket tiket) {

        String direccion = "https://tecniprintcliente.page.link?";
        String id = tiket.getId();
        String idcliente = tiket.getIdCliente();


        return direccion + "id=" + id + "&cliente=" + idcliente;

    }

    private void abrirChooser(String link) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, link);
        startActivity(Intent.createChooser(intent, "Compartir con: "));
    }


    private void setWidget(View v) {

        crvCompartir = v.findViewById(R.id.crv_btn_com_cinco);
        txtNumero = v.findViewById(R.id.txt_id_tik_crea);
        txtCliente = v.findViewById(R.id.txt_nom_tik_crea);
        txtDireccion = v.findViewById(R.id.txt_dir_tik_crea);
        txtIdMaquina = v.findViewById(R.id.txt_maq_tik_crea);
        crvTerminar = v.findViewById(R.id.crv_btn_com_cinco_ter);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_cinco_compartir, container, false);
        return v;
    }
}
