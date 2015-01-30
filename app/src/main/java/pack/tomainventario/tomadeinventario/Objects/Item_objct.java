package pack.tomainventario.tomadeinventario.Objects;

public class Item_objct {

    private String titulo;
    private int icono;

    public Item_objct(String title, int icon) {
        this.titulo = title;
        this.icono = icon;
    }
    public String getTitulo() {
        return titulo;
    }
    public int getIcono() {
        return icono;
    }
}