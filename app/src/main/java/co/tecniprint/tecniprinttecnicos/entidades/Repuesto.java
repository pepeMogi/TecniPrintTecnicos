package co.tecniprint.tecniprinttecnicos.entidades;

import java.text.DecimalFormat;

public class Repuesto {

    private String id;
    private String tipo;
    private String nombre;
    private String referencia;
    private int cantidad;
    private long valor;

    public Repuesto() {
    }

    public Repuesto(String tipo, String nombre, String referencia, int cantidad, Long valor) {
        this.id = referencia;
        this.tipo = tipo;
        this.nombre = nombre;
        this.referencia = referencia;
        this.cantidad = cantidad;
        this.valor = valor;
    }


    public String display() {

        DecimalFormat formato = new DecimalFormat("#,###.");
        String valorFormateado = formato.format(valor);

        System.out.println(valorFormateado);
        String tip = tipo.substring(0,1).toUpperCase() + tipo.substring(1,3);

        return tip + "  " + referencia + ":  " + nombre + " - " + cantidad + "Und - $" + valorFormateado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }
}
