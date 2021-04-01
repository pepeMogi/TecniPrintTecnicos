package co.tecniprint.tecniprinttecnicos.entidades;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Diagnostico {

    private String id;
    private String tecnico;
    private String diagnostico;
    private String solucion;
    private ArrayList<String> repuestos;
    private ArrayList<String> imgs;
    private String comentario;
    private Timestamp fecha;

    public Diagnostico() {
    }

    public Diagnostico(String tecnico, String diagnostico,String solucion, ArrayList<String> repuestos, ArrayList<String> imgs, String comentario, Timestamp fecha) {
        this.id = tecnico.replaceAll(" ", "_").concat(String.valueOf(Math.random() * 1000).substring(0, 4).replace(".", ""));
        this.tecnico = tecnico;
        this.diagnostico = diagnostico;
        this.repuestos = repuestos;
        this.imgs = imgs;
        this.comentario = comentario;
        this.fecha = fecha;
        this.solucion = solucion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public ArrayList<String> getRepuestos() {
        return repuestos;
    }

    public void setRepuestos(ArrayList<String> repuestos) {
        this.repuestos = repuestos;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }
}
