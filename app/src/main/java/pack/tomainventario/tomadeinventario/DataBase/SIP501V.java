package pack.tomainventario.tomadeinventario.DataBase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Todos los usuarios con su ubicacion y unidad ejecutora*/

@Table(name = "SBN510D")
public class SIP501V extends Model {

    @Column(name = "nombre")
    public String nombre;
    @Column(name = "ficha")
    public String ficha;
    @Column(name = "codUejec")
    public String codUejec;
    @Column(name = "codUbic")
    public String codUbic;

    public SIP501V()
    {
        super();
    }

    public SIP501V(String ficha,String nombre,String codUejec,String codUbic){
        super();
        this.ficha = ficha;
        this.nombre = nombre;
        this.codUejec = codUejec;
        this.codUbic = codUbic;
    }
    public static List<SIP501V> getAll() {
        return new Select()
                .from(SIP501V.class)
                .orderBy("nombre ASC")
                .execute();
    }
    public static SIP501V getPersonal(String ficha) {
        return new Select()
                .from(SIP501V.class)
                .where("ficha = ?", ficha)
                .executeSingle();
    }
    }

