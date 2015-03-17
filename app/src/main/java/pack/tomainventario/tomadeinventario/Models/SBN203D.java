package pack.tomainventario.tomadeinventario.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Estatus de un bien (activo, inactivo..)*/

@Table(name = "SBN203D")
public class SBN203D extends Model {

    @Column(name = "codStatus")
    public Integer codStatus;
    @Column(name = "descripcion")
    public String descripcion;

    public SBN203D()
    {
        super();
    }

    public void setCodStatus(Integer codStatus) {
        this.codStatus = codStatus;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public SBN203D(Integer codStatus,String descripcion){
        super();
        this.codStatus = codStatus;
        this.descripcion = descripcion;
    }
    public static List<SBN203D> getAll() {
        return new Select()
                .from(SBN203D.class)
                .orderBy("descripcion ASC")
                .execute();
    }
    public static SBN203D getStatus(int numero) {
        return new Select()
                .from(SBN203D.class)
                .where("codStatus = ?", numero)
                .executeSingle();
    }
    @Override
    public String toString() {
        return this.descripcion;
    }
}
