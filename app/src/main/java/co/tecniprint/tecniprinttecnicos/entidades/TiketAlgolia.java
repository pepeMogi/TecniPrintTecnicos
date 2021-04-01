package co.tecniprint.tecniprinttecnicos.entidades;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TiketAlgolia {

    private String objectID;
    private String nombre;
    private String solicitante;
    private String asignado;
    private String ciudad;
    private String estado;
    private String factura;
    private String fechaCreacion;
    private int prioridad;
    private String tipo;
    private String maquina;
    private String nit;


    public TiketAlgolia() {
    }

    public TiketAlgolia(Tiket tiket) {
        String sol = "";
        if (tiket.getSolicitante() != null){
            sol = tiket.getSolicitante();
        }
        this.objectID = tiket.getId();
        this.nombre = tiket.getNombre();
        this.solicitante = sol;
        this.asignado = tiket.getAsignado();
        this.ciudad = tiket.getCiudad();
        this.estado = tiket.getEstado();
        this.factura = getFacturasString(tiket.getFacturas());
        this.fechaCreacion = getFecha(tiket.getFechaCreacion());
        this.prioridad = tiket.getPrioridad();
        this.tipo = tiket.getTipo();
        this.maquina = tiket.getIdMaquina();
        this.nit = tiket.getNit();
    }

    private String getFacturasString(ArrayList<String> facturas){
        String text = "";

        for (int i = 0; i< facturas.size(); i++){
            text += facturas.get(i) + " ";
        }

        return text;
    }

    private String getMaquinasString(ArrayList<String> maquinas) {

        String text = "";
        for (int i = 0; i < maquinas.size(); i++){
            text += maquinas.get(i).replaceAll("_"," ") + " ";
        }

        return text;
    }

    private String getFecha(Timestamp fechaCreacion) {

        SimpleDateFormat format = new SimpleDateFormat("dd-MMMM-yyyy");
        return format.format(fechaCreacion.toDate());

    }

    public TiketAlgolia(String objectID, String nombre, String asignado, String ciudad, String estado, String factura, String fechaCreacion, int prioridad, String tipo, String maquinas, String nit) {
        this.objectID = objectID;
        this.nombre = nombre;
        this.asignado = asignado;
        this.ciudad = ciudad;
        this.estado = estado;
        this.factura = factura;
        this.fechaCreacion = fechaCreacion;
        this.prioridad = prioridad;
        this.tipo = tipo;
        this.maquina = maquinas;
        this.nit = nit;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAsignado() {
        return asignado;
    }

    public void setAsignado(String asignado) {
        this.asignado = asignado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getMaquina() {
        return maquina;
    }

    public void setMaquina(String maquina) {
        this.maquina = maquina;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }
}
