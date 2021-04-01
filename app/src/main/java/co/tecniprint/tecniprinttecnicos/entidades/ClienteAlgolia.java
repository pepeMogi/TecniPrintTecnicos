package co.tecniprint.tecniprinttecnicos.entidades;

public class ClienteAlgolia {

    private String objectID;
    private String nombre;
    private String img;
    private String cc;
    private String direccion;
    private String celular;
    private String email;

    public ClienteAlgolia() {
    }

    public ClienteAlgolia(Cliente cliente) {
        this.objectID = cliente.getId();
        this.nombre = cliente.getNombre();
        this.img = cliente.getImg();
        this.cc = cliente.getCc();
        this.direccion = cliente.getDireccion() + " " + cliente.getCiudad();
        this.celular = cliente.getCelular();
        this.email = cliente.getEmail();
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
}
