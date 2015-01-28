package pack.tomainventario.tomadeinventario.DataBase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Registro de tomas de inventario por sede*/
@Table(name = "SBN053D")
public class SBN053D extends Model {

    @Column(name = "idInventarioActivo")
    public Integer idInventarioActivo;
    @Column(name = "anno")
    public Integer anno;
    @Column(name = "trimestre")
    public Integer trimestre;
    @Column(name = "sede")
    public String sede;
    @Column(name = "fechaUa")
    public String fechaUa;
    @Column(name = "fichaUa")
    public String fichaUa;
    @Column(name = "status")
    public String status;
    @Column(name = "deviceId")
    public String deviceId;
    @Column(name = "obervacion")
    public String obervacion;

    public SBN053D()
    {
        super();
    }

    public SBN053D(Integer idInventarioActivo,Integer anno,Integer trimestre,String sede,
                   String fechaUa,String fichaUa,String status,String deviceId,String obervacion){
        super();
        this.anno = anno;
        this.trimestre = trimestre;
        this.sede = sede;
        this.fechaUa = fechaUa;
        this.fichaUa = fichaUa;
        this.status = status;
        this.deviceId = deviceId;
        this.obervacion = obervacion;
        this.idInventarioActivo = idInventarioActivo;
    }
    public static List<SBN053D> getAll() {
        return new Select()
                .from(SBN053D.class)
                .execute();
    }
}
