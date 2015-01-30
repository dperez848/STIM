package pack.tomainventario.tomadeinventario.DataBase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Tomas de invntario por unidad administrativa*/

@Table(name = "SBN050D")
public class SBN050D extends Model {

    @Column(name = "idInventario")
    public Integer idInventario;
    @Column(name = "idInventarioActivo")
    public Integer idInventarioActivo;
    @Column(name = "fechaInventario")
    public String fechaInventario;
    @Column(name = "codUnidad")
    public String codUnidad;
    @Column(name = "codUbic")
    public String codUbic;
    @Column(name = "fechaUa")
    public String fechaUa;
    @Column(name = "status")
    public Integer status;

    public SBN050D()
    {
        super();
    }


    public SBN050D(Integer idInventario,Integer idInventarioActivo, String fechaInventario,String codUnidad,
                   String codUbic,String fechaUa,Integer status){
        super();
        this.idInventario = idInventario;
        this.idInventarioActivo = idInventarioActivo;
        this.fechaInventario = fechaInventario;
        this.codUnidad = codUnidad;
        this.codUbic = codUbic;
        this.fechaUa = fechaUa;
        this.status = status;
    }

    public static List<SBN050D> getAll() {
        return new Select()
                .from(SBN050D.class)
                .orderBy("idInventario ASC")
                .execute();
    }
    public static SBN050D getInv(int numero) {
        return new Select()
                .from(SBN050D.class)
                .where("idInventario = ?", numero)
                .executeSingle();
    }

}
