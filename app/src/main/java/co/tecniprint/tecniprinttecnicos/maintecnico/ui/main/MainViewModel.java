package co.tecniprint.tecniprinttecnicos.maintecnico.ui.main;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;

public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Uri>> uris = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> imagenes = new MutableLiveData<>();
    private MutableLiveData<Tiket> tiket = new MutableLiveData<>();
    private MutableLiveData<Maquina> maquina = new MutableLiveData<>();
    private MutableLiveData<Diagnostico> diagnostico = new MutableLiveData<>();
    private MutableLiveData<Cliente> cliente = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Uri>> getUris() {
        return uris;
    }

    public void setUris(Uri uri) {
        if (uris.getValue() == null) {
            ArrayList<Uri> nuevos = new ArrayList<>();
            nuevos.add(uri);
            uris.setValue(nuevos);
        } else {
            ArrayList<Uri> nuevos = uris.getValue();
            nuevos.add(uri);
            uris.setValue(nuevos);
        }
    }

    public MutableLiveData<ArrayList<String>> getImagenes() {
        return imagenes;
    }

    public void setImagenes(String imagen) {
        if (imagenes.getValue() == null) {
            ArrayList<String> nuevos = new ArrayList<>();
            nuevos.add(imagen);
            imagenes.setValue(nuevos);
        } else {
            ArrayList<String> nuevos = imagenes.getValue();

            nuevos.add(imagen);


            imagenes.setValue(nuevos);
        }
    }

    public MutableLiveData<Tiket> getTiket() {
        return tiket;
    }

    public void setTiket(Tiket tiket) {
        this.tiket.setValue(tiket);
    }

    public MutableLiveData<Maquina> getMaquina() {
        return maquina;
    }

    public void setMaquina(Maquina maquina) {
        this.maquina.setValue(maquina);
    }

    public MutableLiveData<Diagnostico> getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico.setValue(diagnostico);
    }

    public MutableLiveData<Cliente> getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente.setValue(cliente);
    }
}