package pack.tomainventario.tomadeinventario.Objects;

import pack.tomainventario.tomadeinventario.DataBase.SIP501V;

/**
 * Created by tmachado on 16/01/2015.
 */
public class ObservacionEdo {
    private int numero, edo;
    private String observacion;
    private SIP501V rpu;

    public SIP501V getRpu() {return rpu;}

    public void setRpu(SIP501V rpu) {this.rpu = rpu;}

    public int getEdo() {return edo;}

    public void setEdo(int edo) {this.edo = edo;}

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }


}
