package co.tecniprint.tecniprinttecnicos.entidades;

public class Tecnico {

   private String id;
    private String nombre;
    private String alias;
    private String cc;
    private String celular;
    private String rh;
    private String tipo;
    private String img;
    private String bodega;
    private String email;
    private String token;

    public Tecnico() {
    }

    public Tecnico(String nombre, String alias, String cc, String celular, String rh, String tipo, String img, String bodega, String email, String token) {
        this.id = alias.replaceAll(" ","");
        this.nombre = nombre;
        this.alias = alias;
        this.cc = cc;
        this.celular = celular;
        this.rh = rh;
        this.tipo = tipo;
        this.img = img;
        this.bodega = bodega;
        this.email = email;
        this.token = token;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getRh() {
        return rh;
    }

    public void setRh(String rh) {
        this.rh = rh;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBodega() {
        return bodega;
    }

    public void setBodega(String bodega) {
        this.bodega = bodega;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
