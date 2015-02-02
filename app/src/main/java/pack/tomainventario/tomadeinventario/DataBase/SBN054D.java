package pack.tomainventario.tomadeinventario.DataBase;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/*Fotos de un bien inventariado*/

@Table(name = "SBN054D")
public class SBN054D extends Model implements Parcelable {

    @Column(name = "numeroBn")
    public Integer numeroBn;
    @Column(name = "idInventario")
    public Integer idInventario;
    @Column(name = "idInventarioActivo")
    public Integer idInventarioActivo;
    @Column(name = "imagen")
    public String imagen;
    @Column(name = "nombre")
    public String nombre;

    public SBN054D()
    {
        super();
    }

    public SBN054D(Integer idInventario,Integer idInventarioActivo,Integer numeroBn, String imagen, String nombre){
        super();
        this.idInventario = idInventario;
        this.idInventarioActivo = idInventarioActivo;
        this.numeroBn = numeroBn;
        this.imagen = imagen;
        this.nombre = nombre;
    }

    public static List<SBN054D> getAll() {
        return new Select()
                .from(SBN054D.class)
                .orderBy("numeroBn ASC")
                .execute();
    }
    public static SBN054D getSpecificFoto(int numeroBn, String foto) {
        return new Select()
                .from(SBN054D.class)
                .where("numeroBn = ?", numeroBn)
                .and("imagen = ?", foto)
                .executeSingle();
    }
    public static String getFoto (int num){
        List<SBN054D> data = getAll();
        for (SBN054D aData : data) {
            if (aData.numeroBn == num)
                return aData.imagen;
        }
        return "";
    }
    public static List<SBN054D> getBnC(int value) {
        return new Select()
                .from(SBN054D.class)
                .where("numeroBn = ?", value)
                .orderBy("numeroBn ASC")
                .execute();
    }
    public static ArrayList<SBN054D> getBn(int value) {
        ArrayList<SBN054D> data= new ArrayList<SBN054D>(getBnC(value));
        return data;
    }
    public static List<SBN054D> getFirstFotos() {
        List<SBN054D> data = getAll();
        List<SBN054D> result= new ArrayList<SBN054D>();
        int distinto = 0;
        for (int i = 0; i < data.size(); i++) {
            if(distinto!=data.get(i).numeroBn){
                result.add(data.get(i));
                distinto=data.get(i).numeroBn;
            }
        }
        return result;
    }

    public static Boolean isEmpty(int bn) {
        List<SBN054D> data = getAll();
        for (SBN054D aData : data) {
            if (aData.numeroBn == bn)
                return true;
        }
        return false;
    }
    public SBN054D(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<SBN054D> CREATOR =
            new Parcelable.Creator<SBN054D>() {
                public SBN054D createFromParcel(Parcel in) {
                    return new SBN054D(in);
                }

                @Override
                public SBN054D[] newArray(int size) {
                    return new SBN054D[size];
                }
            };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(imagen);
        dest.writeInt(idInventario);
        dest.writeInt(idInventarioActivo);
        dest.writeInt(numeroBn);
    }

    private void readFromParcel(Parcel in) {
        nombre = in.readString();
        imagen = in.readString();
        idInventario = in.readInt();
        idInventarioActivo = in.readInt();
        numeroBn = in.readInt();
    }


}
