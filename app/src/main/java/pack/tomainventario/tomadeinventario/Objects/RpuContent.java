package pack.tomainventario.tomadeinventario.Objects;

/**
 * Created by tmachado on 02/12/2014.
 */
public class RpuContent {

    private String nombre;
    private String ficha;

    public RpuContent( String nombre, String ficha){
        this.nombre = nombre;
        this.ficha = ficha;
    }
    public String getFicha() {
        return ficha;
    }

    public void setFicha(String ficha) {
        this.ficha = ficha;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}