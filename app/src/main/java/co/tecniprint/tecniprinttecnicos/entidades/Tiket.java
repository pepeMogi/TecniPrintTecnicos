package co.tecniprint.tecniprinttecnicos.entidades;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.ArrayList;

public class Tiket {


    private String id;
    private String tipo;
    private String nombre;
    private String nit;
    private String celularCliente;
    private String solicitante;
    private String celularSolicitante;
    private String direccion;
    private String ciudad;
    private String email;
    private Timestamp fechaCreacion;
    private String idCliente;
    private Timestamp legalizacion;
    private String idMaquina;
    private int contadorBN;
    private int contadorColor;
    private String falla;
    private ArrayList<String> anexos;
    private int prioridad;
    private String estado;
    private String asignado;
    private Timestamp ultimaVisita;
    private ArrayList<String> diagnosticos;
    private ArrayList<String> facturas;
    private boolean pendiente;

    public Tiket() {
    }


    public Tiket(int contador,Cliente cliente, String solicitante, String celularSolicitante, String direccion, String ciudad, String email, Maquina maquina, String falla, ArrayList<String> anexos) {
        this.id = "t" + contador;
        this.tipo = "";
        this.nombre = cliente.getNombre();
        this.nit = cliente.getCc();
        this.celularCliente = cliente.getCelular();
        this.solicitante = solicitante;
        this.celularSolicitante = celularSolicitante;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.email = email;
        this.fechaCreacion = Timestamp.now();
        this.idCliente = cliente.getId();
        this.legalizacion = new Timestamp(0,0);
        this.idMaquina = maquina.getId();
        this.contadorBN = maquina.getContadorBN();
        this.contadorColor = maquina.getContadorColor();
        this.falla = falla;
        this.anexos = anexos;
        this.prioridad = 2;
        this.estado = "nuevo";
        this.asignado = "";
        this.ultimaVisita = new Timestamp(0,0);
        this.diagnosticos = new ArrayList<>();
        this.facturas = new ArrayList<>();
        this.pendiente = false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCelularCliente() {
        return celularCliente;
    }

    public void setCelularCliente(String celularCliente) {
        this.celularCliente = celularCliente;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getCelularSolicitante() {
        return celularSolicitante;
    }

    public void setCelularSolicitante(String celularSolicitante) {
        this.celularSolicitante = celularSolicitante;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Timestamp getLegalizacion() {
        return legalizacion;
    }

    public void setLegalizacion(Timestamp legalizacion) {
        this.legalizacion = legalizacion;
    }

    public String getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(String idMaquina) {
        this.idMaquina = idMaquina;
    }

    public int getContadorBN() {
        return contadorBN;
    }

    public void setContadorBN(int contadorBN) {
        this.contadorBN = contadorBN;
    }

    public int getContadorColor() {
        return contadorColor;
    }

    public void setContadorColor(int contadorColor) {
        this.contadorColor = contadorColor;
    }

    public String getFalla() {
        return falla;
    }

    public void setFalla(String falla) {
        this.falla = falla;
    }

    public ArrayList<String> getAnexos() {
        return anexos;
    }

    public void setAnexos(ArrayList<String> anexos) {
        this.anexos = anexos;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAsignado() {
        return asignado;
    }

    public void setAsignado(String asignado) {
        this.asignado = asignado;
    }

    public Timestamp getUltimaVisita() {
        return ultimaVisita;
    }

    public void setUltimaVisita(Timestamp ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }

    public ArrayList<String> getDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(ArrayList<String> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    public ArrayList<String> getFacturas() {
        return facturas;
    }

    public void setFacturas(ArrayList<String> facturas) {
        this.facturas = facturas;
    }

    public boolean isPendiente() {
        return pendiente;
    }

    public void setPendiente(boolean pendiente) {
        this.pendiente = pendiente;
    }
}