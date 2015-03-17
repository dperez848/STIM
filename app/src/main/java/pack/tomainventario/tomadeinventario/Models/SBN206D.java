package pack.tomainventario.tomadeinventario.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Estado fisico de un bien (optimo, regular...)*/

@Table(name = "SBN206D")
public class SBN206D extends Model {

    @Column(name = "codEdo")
    public Integer codEdo;
    @Column(name = "descripcion")
    public String descripcion;

    public SBN206D()
    {
        super();
    }

    public SBN206D(Integer codEdo,String descripcion){
        super();
        this.codEdo = codEdo;
        this.descripcion = descripcion;
    }

    public static List<SBN206D> getAll() {
        return new Select()
                .from(SBN206D.class)
                .orderBy("descripcion ASC")
                .execute();
    }
    public static SBN206D getEdoDB(int numero) {
        return new Select()
                .from(SBN206D.class)
                .where("codEdo = ?", numero)
                .executeSingle();
    }
    public static String getEdo(int numero) {
        return getEdoDB(numero).descripcion;
    }
    @Override
    public String toString() {
        return this.descripcion;
    }
}