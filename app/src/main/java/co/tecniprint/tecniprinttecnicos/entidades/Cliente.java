package co.tecniprint.tecniprinttecnicos.entidades;

import android.provider.ContactsContract;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.Date;

public class Cliente {

    private String id;
    private String nombre;
    private String img;
    private String cc;
    private String direccion;
    private String ciudad;
    private String celular;
    private String email;
    private String rut;

    public Cliente() {
    }

    public Cliente(String nombre, String img, String cc, String direccion, String ciudad, String celular, String email, String rut) {
        this.id = String.valueOf(Timestamp.now().toDate().getTime());
        this.nombre = nombre;
        this.img = img;
        this.cc = cc;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.celular = celular;
        this.email = email;
        this.rut = rut;
    }

    public Cliente(ClienteAlgolia cliente) {
        this.id = cliente.getObjectID();
        this.nombre = cliente.getNombre();
        this.img = cliente.getImg();
        this.cc = cliente.getCc();
        this.direccion = cliente.getDireccion();
        this.celular = cliente.getCelular();
        this.email = cliente.getEmail();
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }
}
