package co.tecniprint.tecniprinttecnicos.entidades;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class DiagnosticoExport {

    private String id;
    private String tecnico;
    private String diagnostico;
    private ArrayList<String> repuestos;
    private ArrayList<String> imgs;
    private String comentario;
    private Timestamp fecha;
    private String idTiket;
    private String estado;
    private boolean isActContadores;
    private ArrayList<Maquina> maquinas;

    public DiagnosticoExport() {
    }

    public DiagnosticoExport(Diagnostico diagnostico, String idTiket, String estado, boolean isActContadores, ArrayList<Maquina> maquinas) {
        this.id = diagnostico.getId();
        this.tecnico = diagnostico.getTecnico();
        this.diagnostico = diagnostico.getDiagnostico();
        this.repuestos = diagnostico.getRepuestos();
        this.imgs = new ArrayList<>();
        this.comentario = diagnostico.getComentario();
        this.fecha = diagnostico.getFecha();
        this.idTiket = idTiket;
        this.estado = estado;
        this.isActContadores = isActContadores;
        this.maquinas = maquinas;
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

    public String getIdTiket() {
        return idTiket;
    }

    public void setIdTiket(String idTiket) {
        this.idTiket = idTiket;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isActContadores() {
        return isActContadores;
    }

    public void setActContadores(boolean actContadores) {
        isActContadores = actContadores;
    }

    public ArrayList<Maquina> getMaquinas() {
        return maquinas;
    }

    public void setMaquinas(ArrayList<Maquina> maquinas) {
        this.maquinas = maquinas;
    }
}
