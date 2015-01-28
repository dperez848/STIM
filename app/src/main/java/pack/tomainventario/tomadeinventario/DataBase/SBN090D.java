package pack.tomainventario.tomadeinventario.DataBase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/*Usuaros del sistema que pueden iniciar sesion*/

@Table(name = "SBN090D")
public class SBN090D extends Model {

    @Column(name = "ficha")
    public String ficha;
    @Column(name = "nombre")
    public String nombre;
    @Column(name = "passwd")
    public String passwd;
    @Column(name = "user")
    public String user;

    public SBN090D()
    {
        super();
    }

    public SBN090D(String ficha,String nombre,String passwd,String user){
        super();
        this.ficha = ficha;
        this.nombre = nombre;
        this.user = user;
        this.passwd = passwd;
    }

    public static SBN090D getLog(Object value, Object value2) {
        return new Select()
                .from(SBN090D.class)
                .where("user = ?", value.toString())
                .where("passwd = ?", value2.toString())
                .executeSingle();
    }
}
