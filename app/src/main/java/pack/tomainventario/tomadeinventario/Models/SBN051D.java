package pack.tomainventario.tomadeinventario.Models;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Toma de cada bien nacional*/

@Table(name = "SBN051D")
public class SBN051D extends Model {
    private static final String LOGTAG = "INFORMACION";
    @Column(name = "numeroBn")
    public Integer numeroBn;
    @Column(name = "idInventario")
    public Integer idInventario;
    @Column(name = "idInventarioActivo")
    public Integer idInventarioActivo;
    @Column(name = "observacion")
    public String observacion;
    @Column(name = "fecha")
    public String fecha;

    public SBN051D()
    {
        super();
    }

    public SBN051D(Integer numeroBn,Integer idInventario,Integer idInventarioActivo,String observacion, String fecha){
        super();
        this.numeroBn = numeroBn;
        this.idInventario = idInventario;
        this.idInventarioActivo = idInventarioActivo;
        this.observacion = observacion;
        this.fecha = fecha;
    }
    public static List<SBN051D> getAll() {
        return new Select()
                .from(SBN051D.class)
                .execute();
    }

    public static SBN051D getBn(int numero) {
        return new Select()
                .from(SBN051D.class)
                .where("numeroBn = ?", numero)
                .executeSingle();
    }
    public static SBN051D getInv(int inventario) {
        return new Select()
                .from(SBN051D.class)
                .where("idInventario = ?", inventario)
                .executeSingle();
    }
    public static String getObservacion(SBN051D bn) {
        if(bn!=null){
            Log.e(LOGTAG, "No llegó null");
        if(bn.observacion.equals("")) {
            Log.e(LOGTAG, "Es espacio en blanco");

        }
            else {
            Log.e(LOGTAG, "No es espacio en blanco");
            return bn.observacion;
        }
        }
        else{
            Log.e(LOGTAG, "Si llegó null");
            return "";
        }
        return "";
    }
    public static List<SBN051D> getInventario(int inventario) {
        return new Select()
                .from(SBN051D.class)
                .where("idInventario = ?", inventario)
                .execute();
    }
}
