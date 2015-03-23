package pack.tomainventario.tomadeinventario.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/*Tomas de invntario por unidad administrativa*/

@Table(name = "SBN050D")
public class SBN050D extends Model {

    @Column(name = "idInventario")
    public Integer idInventario;
    @Column(name = "numToma")
    public Integer numToma;
    @Column(name = "deviceId")
    public String deviceId;
    @Column(name = "codUbic")
    public String codUbic;
    @Column(name = "fechaUa")
    public String fechaUa;
    @Column(name = "fechaIni")
    public String fechaIni;
    @Column(name = "fechaFin")
    public String fechaFin;
    @Column(name = "fechaSus")
    public String fechaSus;
    @Column(name = "fichaUa")
    public String fichaUa;
    @Column(name = "status")
    public Integer status;

    public SBN050D(){super();}

    public static List<SBN050D> getAll() {
        return new Select()
                .from(SBN050D.class)
                .where("status = ?",1)
                .orderBy("idInventario ASC")
                .execute();
    }
    public static List<SBN050D> getAllUbic(String ubic) {
        return new Select()
                .from(SBN050D.class)
                .where("status = ?",1)
                .and("codUbic = ?",ubic)
                .orderBy("idInventario ASC")
                .execute();
    }
    public static SBN050D getInv(int numero) {
        return new Select()
                .from(SBN050D.class)
                .where("idInventario = ?", numero)
                .executeSingle();
    }
    public static Boolean isValid(int numero) {
        SBN050D valid= new Select()
                .from(SBN050D.class)
                .where("idInventario = ?", numero)
                .executeSingle();
        return valid.status == 1;
    }
    public static List<SBN050D> getTomaNoCulminada() { //Toma deivnentario q no este culminada
        return new Select()
                .from(SBN050D.class)
                .and("fechaFin = ?","")
                .orderBy("idInventario ASC")
                .execute();
    }
    public static Boolean isIn(String codUbic) {
        List<SBN050D> data= getTomaNoCulminada();
        for (SBN050D aData : data)
            if (aData.codUbic.equals(codUbic))
                return true;
        return false;
    }
}
