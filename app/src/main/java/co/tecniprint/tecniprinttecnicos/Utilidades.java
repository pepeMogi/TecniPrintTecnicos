package co.tecniprint.tecniprinttecnicos;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;
import co.tecniprint.tecniprinttecnicos.entidades.DiagnosticoExport;
import co.tecniprint.tecniprinttecnicos.entidades.MarcasModelos;
import co.tecniprint.tecniprinttecnicos.entidades.DiagnosticoCompleto;
import co.tecniprint.tecniprinttecnicos.entidades.Tecnico;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.ModelDiagnostico;

public class Utilidades {

    public boolean verificarEdt (EditText editText) {
        if (editText.getText().toString().isEmpty()){
            Toast.makeText(editText.getContext(),
                    "El campo " + editText.getHint() + " es obligatorio",Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }

    public boolean verificarAut (AutoCompleteTextView autoComplete) {
        if (autoComplete.getText().toString().isEmpty()){
            Toast.makeText(autoComplete.getContext(),
                    "El campo " + autoComplete.getHint() + " es obligatorio",Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }

    public Tiket stringToTiket(String s){
        Gson gson = new Gson();
        return gson.fromJson(s, Tiket.class);
    }

    public String tiketToString(Tiket tiket){
        Gson gson = new Gson();
        return gson.toJson(tiket);
    }

    public Diagnostico stringToDiagnostico(String s){
        Gson gson = new Gson();
        return gson.fromJson(s, Diagnostico.class);
    }

    public String diagnosticoToString(Diagnostico diagnostico){
        Gson gson = new Gson();
        return gson.toJson(diagnostico);
    }


    public DiagnosticoExport stringToDiagnosticoExpor(String s){
        Gson gson = new Gson();
        return gson.fromJson(s, DiagnosticoExport.class);
    }

    public String diagnosticoExportToString(DiagnosticoExport diagnostico){
        Gson gson = new Gson();
        return gson.toJson(diagnostico);
    }


    public MarcasModelos stringToMarcasModelos (String s){
        Gson gson = new Gson();
        return gson.fromJson(s, MarcasModelos.class);
    }

    public String marcasModelosToString (MarcasModelos marcasModelos){
        Gson gson = new Gson();
        return gson.toJson(marcasModelos);
    }

    public Tecnico stringToTecnico (String s){
        Gson gson = new Gson();
        return gson.fromJson(s, Tecnico.class);
    }

    public String tecnicoToString (Tecnico tecnico){
        Gson gson = new Gson();
        return gson.toJson(tecnico);
    }

    public DiagnosticoCompleto stringToReporte (String s){
        Gson gson = new Gson();
        return gson.fromJson(s, DiagnosticoCompleto.class);
    }

    public String reporteToString (DiagnosticoCompleto diagnosticoCompleto){
        Gson gson = new Gson();
        return gson.toJson(diagnosticoCompleto);
    }

    public Cliente stringToCliente (String s){
        Gson gson = new Gson();
        return gson.fromJson(s, Cliente.class);
    }

    public String clienteToString (Cliente cliente){
        Gson gson = new Gson();
        return gson.toJson(cliente);
    }


    public ArrayList<Tiket> stringToArrayTikets (String s){
        ArrayList<Tiket> tikets = new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Tiket>>() {
        }.getType();

        tikets = gson.fromJson(s, type);
        return tikets;
    }

    public String arrayTiketsToString (ArrayList<Tiket> tikets){
        Gson gson = new Gson();
        return gson.toJson(tikets);
    }

    public ArrayList<DiagnosticoCompleto> stringToArrayReportes (String s){
        ArrayList<DiagnosticoCompleto> tikets = new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DiagnosticoCompleto>>() {
        }.getType();

        tikets = gson.fromJson(s, type);
        return tikets;
    }

    public String arrayReportesToString (ArrayList<DiagnosticoCompleto> reportes){
        Gson gson = new Gson();
        return gson.toJson(reportes);
    }
}
