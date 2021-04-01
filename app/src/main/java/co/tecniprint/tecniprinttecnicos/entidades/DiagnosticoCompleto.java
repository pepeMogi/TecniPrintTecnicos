package co.tecniprint.tecniprinttecnicos.entidades;

public class DiagnosticoCompleto {

    private String idDiagnostico;
    private Diagnostico diagnostico;
    private Maquina maquina;
    private Cliente cliente;
    private Tiket tiket;

    public DiagnosticoCompleto() {
    }

    public DiagnosticoCompleto(Diagnostico diagnostico, Maquina maquina, Cliente cliente, Tiket tiket) {
        this.idDiagnostico = diagnostico.getId();
        this.diagnostico = diagnostico;
        this.maquina = maquina;
        this.cliente = cliente;
        this.tiket = tiket;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Maquina getMaquina() {
        return maquina;
    }

    public void setMaquina(Maquina maquina) {
        this.maquina = maquina;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Tiket getTiket() {
        return tiket;
    }

    public void setTiket(Tiket tiket) {
        this.tiket = tiket;
    }

    public String getIdDiagnostico() {
        return idDiagnostico;
    }

    public void setIdDiagnostico(String idDiagnostico) {
        this.idDiagnostico = idDiagnostico;
    }
}
