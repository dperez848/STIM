package pack.tomainventario.tomadeinventario.DataBase;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/*Tabla maestro: bienes nacionales*/

@Table(name = "SBN001D")
public class SBN001D extends Model
{
    // This is a regular field
    @Column(name = "numero")
    public Integer numero;
    @Column(name = "nombre")
    public String nombre;
    @Column(name = "status")
    public Integer status;
    @Column(name = "serial")
    public String serial;
    @Column(name = "codUnidad")
    public String codUnidad;
    @Column(name = "codUbic")
    public String codUbic;
    @Column(name = "codSede")
    public String codSede;
    @Column(name = "numFicha")
    public String numFicha;
    @Column(name = "pUsuario")
    public String pUsuario;
    @Column(name = "edoFis")
    public Integer edoFis;
    @Column(name = "checked")
    public Integer checked;
    @Column(name = "selected")
    public Integer selected;
    @Column(name = "taken")
    public Integer taken;
    @Column(name = "show")
    public Integer show;

    public SBN001D()
    {
        super();
    }

    public SBN001D(Integer numero, String nombre, Integer status, String serial,
                   String codUnidad, String codUbic, String codSede,
                   String numFicha, String pUsuario, int edoFis, Integer checked) {
        super();
        this.numero = numero;
        this.nombre = nombre;
        this.status = status;
        this.serial = serial;
        this.codUnidad = codUnidad;
        this.codUbic = codUbic;
        this.codSede = codSede;
        this.numFicha = numFicha;
        this.pUsuario = pUsuario;
        this.edoFis = edoFis;
        this.checked = checked;
    }

    /*******************************METODOS GET LIST***************************************/

    public static List<SBN001D> getAll() {
        return new Select()
                .from(SBN001D.class)
                .orderBy("numero ASC")
                .execute();
    }

    public static List<SBN001D> getUbic(String ubic) {
        return new Select()
                .from(SBN001D.class)
                .where("codUbic = ?", ubic)
                .execute();
    }

    public static List<SBN001D> getSelected() {
        return new Select()
                .from(SBN001D.class)
                .orderBy("numero ASC")
                .where("selected = ?", 1)
                .execute();
    }
    public static List<SBN001D> getChecked() {
        return new Select()
                .from(SBN001D.class)
                .orderBy("numero ASC")
                .where("checked = ?", 1)
                .execute();
    }
    public static List<SBN001D> getShow() {
        return new Select()
                .from(SBN001D.class)
                .orderBy("numero ASC")
                .where("show = ?", 1)
                .execute();
    }
    public static List<SBN001D> getTaken() {
        return new Select()
                .from(SBN001D.class)
                .orderBy("numero ASC")
                .where("taken = ?", 1)
                .execute();
    }

    public static List<SBN001D> getNoSelected() {
        return new Select()
                .from(SBN001D.class)
                .orderBy("numero ASC")
                .where("selected = ?", 0)
                .and("taken = ?", 0)
                .execute();
    }

    public static List<SBN001D> getAllFiltered(int opc, String sede, String ubicacion) {
        List<SBN001D> data= getNoSelected();
        List<SBN001D> retorna= new ArrayList<SBN001D>();
        switch (opc) {
            case 0:
                return data;
            case 1:
                for (SBN001D aData : data)
                    if (aData.codSede.equals(sede))
                        retorna.add(aData);
                return retorna;
            case 2:
                for (SBN001D aData : data)
                    if (aData.codUbic.equals(ubicacion))
                        retorna.add(aData);
                return retorna;
            case 4:
                return getAll();
            default:
                for (SBN001D aData : data)
                    if (aData.codSede.equals(sede) && aData.codUbic.equals(ubicacion))
                        retorna.add(aData);
                return retorna;
        }
    }

    public static List<SBN001D> getAllFilteredRPU(int opc, String sede, String ubicacion) {
        List<SBN001D> data = getAll();
        List<SBN001D> retorna = new ArrayList<SBN001D>();
        switch (opc) {
            case 1:
                for (SBN001D aData : data)
                    if (aData.codSede.equals(sede))
                        retorna.add(aData);
                return retorna;
            case 2:
                for (SBN001D aData : data)
                    if (aData.codUbic.equals(ubicacion))
                        retorna.add(aData);
                return retorna;
            case 4:
                return data;
            default:
                for (SBN001D aData : data)
                    if (aData.codSede.equals(sede) && aData.codUbic.equals(ubicacion))
                        retorna.add(aData);
                return retorna;
        }
    }

    /*******************************METODOS GET SINGLE***************************************/

    public static SBN001D getBn(int numero) {
        return new Select()
                .from(SBN001D.class)
                .where("numero = ?", numero)
                .executeSingle();
    }

    public static SBN001D getSerial(String serial) {
        return new Select()
                .from(SBN001D.class)
                .where("serial = ?", serial)
                .executeSingle();
    }

    public static String getDescripcion(int num) {
        List<SBN001D> data= getAll();
        for (SBN001D aData : data)
            if (aData.numero.equals(num))
                return aData.nombre;
        return "";
    }

    /*******************************METODOS BOOLEAN***************************************/

    public static Boolean isFull (){
        List<SBN001D> data = getShow();
        for (SBN001D aData : data) {
            SBN001D bN = SBN001D.getBn(aData.numero);
            if (bN.checked == 0)
                return false;
        }
        return true;
    }

    public static Boolean exists (int numero){
        List<SBN001D> data = getAll();
        for (SBN001D aData : data)
            if (aData.numero == numero)
                return true;
        return false;
    }

    public static Boolean existsSerial (String serial){
        List<SBN001D> data = getAll();
        for (SBN001D aData : data)
            if (aData.serial.equals(serial))
                return true;
        return false;
    }

    public static Boolean isSelected (int numero){
        SBN001D data = getBn(numero);
        return data.selected == 1;
    }
    public static Boolean isTaken (int numero){
        SBN001D data = getBn(numero);
        return data.taken == 1;
    }

   /*******************************METODOS SET***************************************/

    public static void setAllSelected (int num){
        List<SBN001D> data = getAll();
        for (SBN001D aData : data) {
            if(aData.show==1 ) {
                SBN001D bN = SBN001D.getBn(aData.numero);
                bN.selected = num;
                bN.save();
            }
        }
    }

    public static void setOneSelected (int numeroBn){
        List<SBN001D> data = getAll();
        for (SBN001D aData : data) {
            if(aData.numero==numeroBn ) {
                SBN001D bN = SBN001D.getBn(aData.numero);
                bN.selected = 0;
                bN.checked=0;
                bN.save();
                Log.e("aa", "le puse selected 0 al item "+numeroBn);
            }
        }
    }

    public static void setAllChecked (int value,int from){ // from 1 = lote, from 2=ajustar
        List<SBN001D> data = getAll();
        for (SBN001D aData : data) {
            if(from==1) {
                if (aData.show == 1) {
                    SBN001D bN = SBN001D.getBn(aData.numero);
                    bN.checked = value;
                    bN.save();
                }
            }else{
                SBN001D bN = SBN001D.getBn(aData.numero);
                bN.checked = value;
                bN.save();
            }
        }
    }

    public static void setTaken (){
        List<SBN001D> data = getAll();
        for (SBN001D aData : data) {
            SBN001D bN = SBN001D.getBn(aData.numero);
            bN.selected = 0;
            bN.save();
        }
    }

    public static void setShow (int num){
        List<SBN001D> data = getAll();
        for (SBN001D aData : data) {
            SBN001D bN = SBN001D.getBn(aData.numero);
            bN.show = num;
            bN.save();
        }
    }

    public static void allSelectedToTaken() {
        List<SBN001D> data = getSelected();
        for (SBN001D aData : data) {            
                aData.taken = 1;
                aData.save();
        }
    }

    public static void allCheckedToSelected() {
        List<SBN001D> data = getChecked();
        for (SBN001D aData : data) {
            aData.selected = 1;
            aData.save();
        }
    }
}