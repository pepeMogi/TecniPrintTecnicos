package co.tecniprint.tecniprinttecnicos.entidades;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListaUsuario {

    private ArrayList<String> admins;
    private ArrayList<String> tecnicos;
    private ArrayList<String> supervisor;

    public ListaUsuario() {
    }

    public ListaUsuario(ArrayList<String> admins, ArrayList<String> tecnicos, ArrayList<String> supervisor) {
        this.admins = admins;
        this.tecnicos = tecnicos;
        this.supervisor = supervisor;
    }

    public ArrayList<String> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<String> admins) {
        this.admins = admins;
    }

    public ArrayList<String> getTecnicos() {
        return tecnicos;
    }

    public void setTecnicos(ArrayList<String> tecnicos) {
        this.tecnicos = tecnicos;
    }

    public ArrayList<String> getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(ArrayList<String> supervisor) {
        this.supervisor = supervisor;
    }
}
