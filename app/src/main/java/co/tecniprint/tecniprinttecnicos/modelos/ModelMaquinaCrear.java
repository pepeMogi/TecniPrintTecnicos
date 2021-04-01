package co.tecniprint.tecniprinttecnicos.modelos;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import co.tecniprint.tecniprinttecnicos.entidades.Tiket;

public class ModelMaquinaCrear extends ViewModel {

    MutableLiveData<String> img = new MutableLiveData<>();
    MutableLiveData<Tiket> tiket = new MutableLiveData<>();
    MutableLiveData<Uri> uri = new MutableLiveData<>();

    private MutableLiveData<Boolean> subImg = new MutableLiveData<>();
    private MutableLiveData<Boolean> subDiagnostico = new MutableLiveData<>();
    private MutableLiveData<Boolean> subAcualizado = new MutableLiveData<>();


    private MutableLiveData<Boolean> terminar = new MutableLiveData<>();



    public MutableLiveData<String> getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img.setValue(img);
    }



    public MutableLiveData<Tiket> getTiket() {
        return tiket;
    }

    public void setTiket(Tiket tiket) {
        this.tiket.setValue(tiket);
    }

    public MutableLiveData<Uri> getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri.setValue(uri);
    }


    public MutableLiveData<Boolean> getSubImg() {
        return subImg;
    }

    public void setSubImg(Boolean subImg) {
        this.subImg.setValue(subImg);
    }

    public MutableLiveData<Boolean> getSubDiagnostico() {
        return subDiagnostico;
    }

    public void setSubDiagnostico(Boolean subDiagnostico) {
        this.subDiagnostico.setValue(subDiagnostico);
    }

    public MutableLiveData<Boolean> getSubAcualizado() {
        return subAcualizado;
    }

    public void setSubAcualizado(Boolean subAcualizado) {
        this.subAcualizado.setValue(subAcualizado);
    }


    public MutableLiveData<Boolean> getTerminar() {
        return terminar;
    }

    public void setTerminar(Boolean terminar) {
        this.terminar.setValue(terminar);
    }
}
