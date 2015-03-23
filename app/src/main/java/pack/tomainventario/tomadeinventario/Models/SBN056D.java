package pack.tomainventario.tomadeinventario.Models;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "SBN056D")
public class SBN056D {
    @Column(name = "idInventario")
    public Integer idInventario;
    @Column(name = "fechaIni")
    public String fechaIni;
    @Column(name = "fechaFin")
    public String fechaFin;
    @Column(name = "status")
    public Integer status;
    @Column(name = "observacion")
    public String observacion;

    public SBN056D(){super();}
}
