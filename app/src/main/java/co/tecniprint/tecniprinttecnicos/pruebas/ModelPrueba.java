package co.tecniprint.tecniprinttecnicos.pruebas;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ModelPrueba extends ViewModel {

    private MutableLiveData<Integer> numero = new MutableLiveData<>();

    public MutableLiveData<Integer> getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero.setValue(numero);
    }
}
