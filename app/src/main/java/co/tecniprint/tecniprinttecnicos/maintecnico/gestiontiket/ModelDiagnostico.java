package co.tecniprint.tecniprinttecnicos.maintecnico.gestiontiket;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import java.io.BufferedOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import co.tecniprint.tecniprinttecnicos.entidades.Cliente;
import co.tecniprint.tecniprinttecnicos.entidades.Diagnostico;
import co.tecniprint.tecniprinttecnicos.entidades.Maquina;
import co.tecniprint.tecniprinttecnicos.entidades.Repuesto;
import co.tecniprint.tecniprinttecnicos.entidades.Tiket;

public class ModelDiagnostico extends ViewModel {

    private MutableLiveData<ViewPager> viewPager = new MutableLiveData<>();

    private MutableLiveData<Tiket> tiket = new MutableLiveData<>();
    private MutableLiveData<Cliente> cliente = new MutableLiveData<>();
    private MutableLiveData<Maquina> maquina = new MutableLiveData<>();
    private MutableLiveData<String> diagnostico = new MutableLiveData<>();
    private MutableLiveData<String> solcucion = new MutableLiveData<>(); // creado
    private MutableLiveData<ArrayList<Repuesto>> repuestos = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> anexos = new MutableLiveData<>();
    private MutableLiveData<String> comentario = new MutableLiveData<>();
    private MutableLiveData<String> estado = new MutableLiveData<>();
    private MutableLiveData<Integer> numero = new MutableLiveData<>();
    private MutableLiveData<Diagnostico> diagnosticoCompleto = new MutableLiveData<>();

    private MutableLiveData<Boolean> actContadores = new MutableLiveData<>();
    private MutableLiveData<Boolean> actCliente = new MutableLiveData<>();



    private MutableLiveData<Boolean> subImg = new MutableLiveData<>();
    private MutableLiveData<Boolean> subDiagnostico = new MutableLiveData<>();
    private MutableLiveData<Boolean> subAcualizado = new MutableLiveData<>();
    private MutableLiveData<Boolean> subNotificacion = new MutableLiveData<>();


    private MutableLiveData<ArrayList<Maquina>> maquinas = new MutableLiveData<>();



    ////////////////////////////////////////////////////////////////
    private MutableLiveData<Uri> imgMaquina = new MutableLiveData<>();
    private MutableLiveData<Boolean> subImgMaquina = new MutableLiveData<>();
    private MutableLiveData<Boolean> subDatoMaquina = new MutableLiveData<>();
    private MutableLiveData<Boolean> subActTiket = new MutableLiveData<>();

    private MutableLiveData<Boolean> imgTerminar = new MutableLiveData<>();

    private MutableLiveData<Boolean> terminar = new MutableLiveData<>();



    public MutableLiveData<Tiket> getTiket() {
        return tiket;
    }

    public void setTiket(Tiket tiket) {
        this.tiket.setValue(tiket);
    }

    public MutableLiveData<ViewPager> getViewPager() {
        return viewPager;
    }

    public MutableLiveData<String> getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico.setValue(diagnostico);
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager.setValue(viewPager);
    }

    public MutableLiveData<ArrayList<Repuesto>> getRepuestos() {
        return repuestos;
    }

    public void setRepuestos(ArrayList<Repuesto> repuestos) {
        this.repuestos.setValue(repuestos);
    }

    public void setRepuesto(Repuesto repuesto) {
        Objects.requireNonNull(this.repuestos.getValue()).add(repuesto);
        ArrayList<Repuesto> newRepuestos = repuestos.getValue();
        setRepuestos(newRepuestos);

    }

    public MutableLiveData<ArrayList<String>> getAnexos() {
        return anexos;
    }

    public void setAnexos(ArrayList<String> anexos) {
        this.anexos.setValue(anexos);
    }

    public void setAnexo(String uri){

        if (anexos.getValue() == null){
            ArrayList<String> uris = new ArrayList<>();
            uris.add(uri);
            setAnexos(uris);
        }else{
            Objects.requireNonNull(this.getAnexos().getValue()).add(uri);
            ArrayList<String> uris = anexos.getValue();
            setAnexos(uris);
        }

    }

    public MutableLiveData<String> getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario.setValue(comentario);
    }

    public MutableLiveData<String> getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado.setValue(estado);
    }

    public MutableLiveData<Integer> getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero.setValue(numero);
    }

    public MutableLiveData<Boolean> getSubImg() {
        return subImg;
    }

    public void setSubImg(Boolean subImg) {
        this.subImg.setValue(subImg);
    }

    public MutableLiveData<Boolean> getSubDiagnostico() {
        return subDiagnostico;
    }

    public void setSubDiagnostico(Boolean subDiagnostico) {
        this.subDiagnostico.setValue(subDiagnostico);
    }

    public MutableLiveData<Boolean> getSubAcualizado() {
        return subAcualizado;
    }

    public void setSubAcualizado(Boolean subAcualizado) {
        this.subAcualizado.setValue(subAcualizado);
    }

    public MutableLiveData<Boolean> getSubNotificacion() {
        return subNotificacion;
    }

    public void setSubNotificacion(Boolean subNotificacion) {
        this.subNotificacion.setValue(subNotificacion);
    }

    public MutableLiveData<Boolean> getTerminar() {
        return terminar;
    }

    public void setTerminar(Boolean terminar) {
        this.terminar.setValue(terminar);
    }



    public MutableLiveData<ArrayList<Maquina>> getMaquinas() {
        return maquinas;
    }

    public void setMaquinas(Maquina maquina) {
        if (maquinas.getValue() == null){
            ArrayList<Maquina> maquinasNew = new ArrayList<>();
            maquinasNew.add(maquina);
            maquinas.setValue(maquinasNew);
        }else{
            ArrayList<Maquina> maquinasNew = maquinas.getValue();

            Maquina maquinaUpdate = null;
            int num = -1;

            for (int i = 0; i < maquinasNew.size(); i++){
                if (maquinasNew.get(i).getId().equals(maquina.getId())){
                    num = i;
                    maquinaUpdate = maquinasNew.get(i);
                    if (maquina.getContadorColor() != maquinasNew.get(i).getContadorColor()){
                        maquinaUpdate.setContadorColor(maquina.getContadorColor());
                    }

                    if (maquina.getContadorBN() != maquinasNew.get(i).getContadorBN()){
                        maquinaUpdate.setContadorBN(maquina.getContadorBN());
                    }
                }else{
                    maquinaUpdate = maquina;
                }
            }

            if (num != -1){
                maquinasNew.remove(num);
            }

            maquinasNew.add(maquinaUpdate);
            maquinas.setValue(maquinasNew);
        }

    }




    /////////////////////////////////////////////////////////////

    public MutableLiveData<String> getSolcucion() {
        return solcucion;
    }

    public void setSolcucion(String solcucion) {
        this.solcucion.setValue(solcucion);
    }

    public MutableLiveData<Uri> getImgMaquina() {
        return imgMaquina;
    }

    public void setImgMaquina(Uri imgMaquina) {
        this.imgMaquina.setValue(imgMaquina);
    }

    public MutableLiveData<Boolean> getSubImgMaquina() {
        return subImgMaquina;
    }

    public void setSubImgMaquina(Boolean subImgMaquina) {
        this.subImgMaquina.setValue(subImgMaquina);
    }

    public MutableLiveData<Boolean> getSubDatoMaquina() {
        return subDatoMaquina;
    }

    public void setSubDatoMaquina(Boolean subDatoMaquina) {
        this.subDatoMaquina.setValue(subDatoMaquina);
    }

    public MutableLiveData<Boolean> getSubActTiket() {
        return subActTiket;
    }

    public void setSubActTiket(Boolean subActTiket) {
        this.subActTiket.setValue(subActTiket);
    }

    public MutableLiveData<Boolean> getImgTerminar() {
        return imgTerminar;
    }

    public void setImgTerminar(Boolean imgTerminar) {
        this.imgTerminar.setValue(imgTerminar);
    }

    public void borrarRepuesto(Repuesto repuesto) {

        ArrayList<Repuesto> repues = repuestos.getValue();
        if (repues != null){
            repues.remove(repuesto);
            repuestos.setValue(repues);
        }
    }

    public MutableLiveData<Cliente> getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        Log.e("setea","cliente");
        this.cliente.setValue(cliente);
    }

    public MutableLiveData<Maquina> getMaquina() {
        return maquina;
    }

    public void setMaquina(Maquina maquina) {
        this.maquina.setValue(maquina);
    }

    public MutableLiveData<Boolean> getActContadores() {
        return actContadores;
    }

    public void setActContadores(Boolean actContadores) {
        this.actContadores.setValue(actContadores);
    }

    public MutableLiveData<Boolean> getActCliente() {
        return actCliente;
    }

    public void setActCliente(Boolean actCliente) {
        this.actCliente.setValue(true);
    }

    public MutableLiveData<Diagnostico> getDiagnosticoCompleto() {
        return diagnosticoCompleto;
    }

    public void setDiagnosticoCompleto(Diagnostico diagnosticoCompleto) {
        this.diagnosticoCompleto.setValue(diagnosticoCompleto);
    }
}
