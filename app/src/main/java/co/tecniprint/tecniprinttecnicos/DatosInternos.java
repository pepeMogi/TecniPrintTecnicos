package co.tecniprint.tecniprinttecnicos;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import co.tecniprint.tecniprinttecnicos.entidades.Constantes;
import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;
import co.tecniprint.tecniprinttecnicos.entidades.DiagnosticoCompleto;
import co.tecniprint.tecniprinttecnicos.entidades.MarcasModelos;
import co.tecniprint.tecniprinttecnicos.entidades.Tecnico;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;
import co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket.ModelDiagnostico;

import static android.content.Context.MODE_PRIVATE;



public class DatosInternos {

    private Context context;

    public DatosInternos(Context context) {
        this.context = context;
    }

    public void setMarcasModelos(MarcasModelos marcasModelos){

        String marcasString =  new Utilidades().marcasModelosToString(marcasModelos);

        SharedPreferences sharedPref = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constantes.MARCASMODELOS, marcasString);
        editor.apply();
    }

    public MarcasModelos getMarcasModelos(){
        SharedPreferences preferences = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        String marcasString = preferences.getString(Constantes.MARCASMODELOS, null);

        if (marcasString != null){
            return new Utilidades().stringToMarcasModelos(marcasString);
        }else{
            return null;
        }

    }

    public void setMarca(String marca){
        MarcasModelos marcasModelos = getMarcasModelos();
        marcasModelos.getMarcas().add(marca);
        setMarcasModelos(marcasModelos);
    }

    public void setModelo(String modelo){
        MarcasModelos marcasModelos = getMarcasModelos();
        marcasModelos.getModelos().add(modelo);
        setMarcasModelos(marcasModelos);
    }

    public void setTecnico(String tecnico) {

        SharedPreferences sharedPref = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constantes.TECNICOS, tecnico);
        editor.apply();
    }

    public void setTecnico(Tecnico tecnico) {

        String tecniString = new Utilidades().tecnicoToString(tecnico);
        SharedPreferences sharedPref = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constantes.TECNICOS, tecniString);
        editor.apply();
    }

    public Tecnico getTecnico() {

        SharedPreferences sharedPref = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        String tecnicoString = sharedPref.getString(Constantes.TECNICOS,null);
        Tecnico tecnico = null;
        if (tecnicoString != null) {
            tecnico  = new Utilidades().stringToTecnico(tecnicoString);
        }
        return tecnico;

    }

    public void setTikets(ArrayList<Tiket> tikets) {

        String arrayTikets = new Utilidades().arrayTiketsToString(tikets);

        SharedPreferences sharedPref = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constantes.TIKETS, arrayTikets);
        editor.apply();

    }

    public ArrayList<Tiket> getTikets(){
        ArrayList<Tiket> tikes = new ArrayList<>();
        SharedPreferences sharedPref = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        String tiketsString = sharedPref.getString(Constantes.TIKETS,null);
        if (tiketsString != null){
            tikes = new Utilidades().stringToArrayTikets(tiketsString);
        }

        return tikes;
    }

    public void setInternet(boolean isInternet) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constantes.INTERNET, isInternet);
        editor.apply();
    }

    public boolean isInternet(){
        SharedPreferences sharedPref = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        return sharedPref.getBoolean(Constantes.INTERNET,true);
    }

    public void setReporte(DiagnosticoCompleto diaCompleto) {

        ArrayList<DiagnosticoCompleto> reportes  = getArrayReportes();
        reportes.add(diaCompleto);
        String reportesString = new Utilidades().arrayReportesToString(reportes);

        SharedPreferences sharedPref = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constantes.REPORTE, reportesString);
        editor.apply();
    }

    public void setReportes(ArrayList<DiagnosticoCompleto> diaCompletos) {


        String reportesString = new Utilidades().arrayReportesToString(diaCompletos);

        SharedPreferences sharedPref = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constantes.REPORTE, reportesString);
        editor.apply();
    }

    public ArrayList<DiagnosticoCompleto> getArrayReportes() {

        ArrayList<DiagnosticoCompleto> reportes = new ArrayList<>();
        SharedPreferences sharedPref = context.getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        String arrayReporteString =  sharedPref.getString(Constantes.REPORTE,null);

        if (arrayReporteString != null){
            reportes = new Utilidades().stringToArrayReportes(arrayReporteString);
        }

        return reportes;
    }
}
