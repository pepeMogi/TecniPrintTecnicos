package co.tecniprint.tecniprinttecnicos.maintecnico.creartiket;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;

public class ModelNewTikete extends ViewModel {

    private MutableLiveData<ViewPager> viewPager = new MutableLiveData<>();
    private MutableLiveData<Cliente> cliente = new MutableLiveData<>();
    private MutableLiveData<Maquina> maquina = new MutableLiveData<>();
    private MutableLiveData<String> solicitante = new MutableLiveData<>();
    private MutableLiveData<String> celularSolicitante = new MutableLiveData<>();

    private MutableLiveData<Tiket> tiket = new MutableLiveData<>();
    private MutableLiveData<Boolean> terminar = new MutableLiveData<>();
    private MutableLiveData<Boolean> terminarCliente = new MutableLiveData<>();

    private MutableLiveData<Cliente> clienteFinal = new MutableLiveData<>();

    // para cargar img todas
    private MutableLiveData<ArrayList<Uri>> uris = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> imagenes = new MutableLiveData<>();


    // cargar proceso
    private MutableLiveData<Boolean> terminarImg = new MutableLiveData<>();
    private MutableLiveData<Boolean> terminarData = new MutableLiveData<>();
    private MutableLiveData<Boolean> terminarCon = new MutableLiveData<>();



    public MutableLiveData<ViewPager> getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager.setValue(viewPager);
    }

    public MutableLiveData<Cliente> getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente.setValue(cliente);
    }


    public MutableLiveData<Cliente> getClienteFinal() {
        return clienteFinal;
    }

    public void setClienteFinal(Cliente cliente) {
        this.clienteFinal.setValue(cliente);
    }


    public MutableLiveData<Maquina> getMaquina() {
        return maquina;
    }

    public void setMaquina(Maquina maquina) {
        this.maquina.setValue(maquina);
    }

    public MutableLiveData<Tiket> getTiket() {
        return tiket;
    }

    public void setTiket(Tiket tiket) {
        this.tiket.setValue(tiket);
    }



    public MutableLiveData<Boolean> getTerminar() {
        return terminar;
    }

    public void setTerminar(Boolean terminar) {
        this.terminar.setValue(terminar);
    }

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


    public MutableLiveData<String> getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante.setValue(solicitante);
    }

    public MutableLiveData<String> getCelularSolicitante() {
        return celularSolicitante;
    }

    public void setCelularSolicitante(String celularSolicitante) {
        this.celularSolicitante.setValue(celularSolicitante);
    }




    public MutableLiveData<Boolean> getTerminarCliente() {
        return terminarCliente;
    }

    public void setTerminarCliente(Boolean terminarCliente) {
        this.terminarCliente.setValue(terminarCliente);
    }

    public MutableLiveData<Boolean> getTerminarImg() {
        return terminarImg;
    }

    public void setTerminarImg(Boolean terminarImg) {
        this.terminarImg.setValue(terminarImg);
    }

    public MutableLiveData<Boolean> getTerminarData() {
        return terminarData;
    }

    public void setTerminarData(Boolean terminarData) {
        this.terminarData.setValue(terminarData);
    }

    public MutableLiveData<Boolean> getTerminarCon() {
        return terminarCon;
    }

    public void setTerminarCon(Boolean terminarCon) {
        this.terminarCon.setValue(terminarCon);
    }
}
