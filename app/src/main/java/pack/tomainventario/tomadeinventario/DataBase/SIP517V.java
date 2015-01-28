package pack.tomainventario.tomadeinventario.DataBase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Todas las sedes con respectivas ubicaciones*/
@Table(name = "SIP517V")
public class SIP517V extends Model {

    @Column(name = "desUbic")
    public String desUbic;
    @Column(name = "codSede")
    public String codSede;
    @Column(name = "codUbic")
    public String codUbic;

    public String getCodUbic() {
        return codUbic;
    }

    public void setCodUbic(String codUbic) {
        this.codUbic = codUbic;
    }

    public void setCodSede(String codSede) {
        this.codSede = codSede;
    }

    public void setDesUbic(String desUbic) {
        this.desUbic = desUbic;
    }

    public SIP517V()
    {
        super();
    }

    public SIP517V(String codUbic,String desUbic, String codSede){
        super();
        this.codUbic = codUbic;
        this.desUbic = desUbic;
        this.codSede = codSede;
    }
    public static List<SIP517V> getAll() {
        return new Select()
                .from(SIP517V.class)
                .orderBy("desUbic ASC")
                .execute();
    }
    public static SIP517V getSede(String sede) {
        return new Select()
                .from(SIP517V.class)
                .where("codUbic = ?", sede)
                .executeSingle();
    }
    @Override
    public String toString() {
        return this.desUbic;
    }
}
