package pack.tomainventario.tomadeinventario.Models;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "SBN055D")
public class SBN055D {
    @Column(name = "codStatus")
    public Integer codStatus;
    @Column(name = "descripcion")
    public String descripcion;

    public SBN055D() {super();}
}
