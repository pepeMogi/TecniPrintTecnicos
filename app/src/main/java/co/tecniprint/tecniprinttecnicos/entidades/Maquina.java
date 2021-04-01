package co.tecniprint.tecniprinttecnicos.entidades;

public class Maquina {

    private String id;
    private String cliente;
    private String tipo;
    private String img;
    private String marca;
    private String modelo;
    private String serial;
    private int contadorBN;
    private int contadorColor;

    public Maquina() {
    }

    public Maquina(String cliente, String tipo, String img, String marca, String modelo, String serial, int contadorBN, int contadorColor) {
        this.id = marca +"_"+modelo+"_"+serial;
        this.cliente = cliente;
        this.tipo = tipo;
        this.img = img;
        this.marca = marca;
        this.modelo = modelo;
        if (!serial.equals("")){
            this.serial = "S/" + serial;
        }else{
            this.serial = serial;
        }
        this.contadorBN = contadorBN;
        this.contadorColor = contadorColor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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
}
