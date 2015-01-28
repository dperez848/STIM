package pack.tomainventario.tomadeinventario.DataBase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Unidades ejecutoras con su ubicación*/

@Table(name = "SIP528V")
public class SIP528V extends Model {

    @Column(name = "desUejec")
    public String desUejec;
    @Column(name = "codUejec")
    public String codUejec;
    @Column(name = "codUbic")
    public String codUbic;

    public SIP528V()
    {
        super();
    }

    public SIP528V(String codUejec,String desUejec, String codUbic){
        super();
        this.codUejec = codUejec;
        this.desUejec = desUejec;
        this.codUbic = codUbic;
    }

    public static List<SIP528V> getAll() {
        return new Select()
                .from(SIP528V.class)
                .orderBy("desUejec ASC")
                .execute();
    }
    public static SIP528V getUnidad(String unidad) {
        return new Select()
                .from(SIP528V.class)
                .where("codUejec = ?", unidad)
                .executeSingle();
    }
    @Override
    public String toString() {
        return this.desUejec;
    }
}
