package pack.tomainventario.tomadeinventario.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Tabla maestro: unidades administrativas*/

@Table(name = "SBN010D")
public class SBN010D extends Model
{

    @Column(name = "codUbic")
    public String codUbic;
    @Column(name = "nombre")
    public String nombre;
    @Column(name = "respUa")
    public String respUa;
    @Column(name = "codUejec")
    public String codUejec;
    @Column(name = "codSede")
    public String codSede;
    @Column(name = "show")
    public int show;

       public SBN010D()
       {
           super();
       }

       public SBN010D(String codUbic,String nombre,String respUa,
                   String codUejec,String codSede){
        super();
        this.codUbic = codUbic;
        this.nombre = nombre;
        this.respUa = respUa;
        this.codUejec = codUejec;
        this.codSede = codSede;
    }
    public static List<SBN010D> getAll() {
        return new Select()
                .from(SBN010D.class)
                .orderBy("nombre ASC")
                .execute();
    }
    public static String getUbicacion(String ubic) {
        List<SBN010D> data = getAll();
        for (SBN010D aData : data) {
            if (aData.codUbic.equals(ubic))
                return aData.nombre;
        }
        return "Ubicación desconocida";
    }

    public static SBN010D getUbic(String cod) {
        return new Select()
                .from(SBN010D.class)
                .where("codUbic = ?", cod)
                .executeSingle();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCodUbic(String codUbic) {
        this.codUbic = codUbic;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public static List<SBN010D> getAllShow() {
        return new Select()
                .from(SBN010D.class)
                .where("show = ?", 1)
                .orderBy("nombre ASC")
                .execute();
    }

}
