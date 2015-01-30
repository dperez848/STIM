package pack.tomainventario.tomadeinventario.DataBase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Tabla de actualizacion de RPU - BN */

@Table(name = "SBN052D")
public class SBN052D  extends Model {

    @Column(name = "numeroBn")
    public Integer numeroBn;
    @Column(name = "idInventario")
    public Integer idInventario;
    @Column(name = "idInventarioActivo")
    public Integer idInventarioActivo;
    @Column(name = "fichaRpu")
    public String fichaRpu;
    @Column(name = "fechaUa")
    public String fechaUa;

    public SBN052D()
    {
        super();
    }

    public SBN052D(Integer numeroBn,String fechaUa,
                   String fichaRpu,Integer idInventario,Integer idInventarioActivo){
        super();
        this.numeroBn = numeroBn;
        this.fichaRpu = fichaRpu;
        this.fechaUa = fechaUa;
        this.idInventario = idInventario;
        this.idInventarioActivo = idInventarioActivo;
    }

    public static List<SBN052D> getAll() {
        return new Select()
                .from(SBN052D.class)
                .execute();
    }
    public static SBN052D getInventario(int numeroBn) {
        return new Select()
                .from(SBN052D.class)
                .where("numeroBn = ?", numeroBn)
                .executeSingle();
    }
}
