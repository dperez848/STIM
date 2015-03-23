package pack.tomainventario.tomadeinventario.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Registro de tomas de inventario por sede*/
@Table(name = "SBN053D")
public class SBN053D extends Model {

    @Column(name = "idInventario")
    public Integer idInventario;
    @Column(name = "sede")
    public String sede;
    @Column(name = "status")
    public String status;

    public SBN053D()
    {
        super();
    }


    public static List<SBN053D> getAll() {
        return new Select()
                .from(SBN053D.class)
                .execute();
    }
}
