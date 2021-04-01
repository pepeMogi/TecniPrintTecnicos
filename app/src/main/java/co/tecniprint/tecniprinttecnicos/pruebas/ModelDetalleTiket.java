package co.tecniprint.tecniprinttecnicos.pruebas;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;

public class ModelDetalleTiket extends ViewModel {

    MutableLiveData<Integer> numero = new MutableLiveData<>();
    MutableLiveData<ArrayList<Diagnostico>> diagnosticos = new MutableLiveData<>();

    public MutableLiveData<Integer> getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero.setValue(numero);
    }

    public MutableLiveData<ArrayList<Diagnostico>> getDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(ArrayList<Diagnostico> diagnosticos) {
        this.diagnosticos.setValue(diagnosticos);
    }

    public void setDiagnostico(Diagnostico diagnistico){

        if (diagnosticos.getValue() == null){
            ArrayList<Diagnostico> newDiagnosticos = new ArrayList<>();
            newDiagnosticos.add(diagnistico);
            diagnosticos.setValue(newDiagnosticos);
        }else{
            ArrayList<Diagnostico> diag =  diagnosticos.getValue();
            diag.add(diagnistico);
            setDiagnosticos(diag);
        }
    }
}
