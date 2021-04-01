package co.tecniprint.tecniprinttecnicos.entidades;

import java.util.ArrayList;

public class MarcasModelos {

    private ArrayList<String> marcas;
    private ArrayList<String> modelos;

    public MarcasModelos() {
    }

    public MarcasModelos(ArrayList<String> marcas, ArrayList<String> modelos) {
        this.marcas = marcas;
        this.modelos = modelos;
    }

    public ArrayList<String> getMarcas() {
        return marcas;
    }

    public void setMarcas(ArrayList<String> marcas) {
        this.marcas = marcas;
    }

    public ArrayList<String> getModelos() {
        return modelos;
    }

    public void setModelos(ArrayList<String> modelos) {
        this.modelos = modelos;
    }
}
