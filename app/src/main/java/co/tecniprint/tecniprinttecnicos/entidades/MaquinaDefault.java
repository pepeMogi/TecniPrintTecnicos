package co.tecniprint.tecniprinttecnicos.entidades;

public class MaquinaDefault {

    private String marca;
    private String modelo;
    private String img;

    public MaquinaDefault() {
    }

    public MaquinaDefault(String marca, String modelo, String img) {
        this.marca = marca;
        this.modelo = modelo;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
