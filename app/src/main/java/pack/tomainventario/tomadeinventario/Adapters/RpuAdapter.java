package pack.tomainventario.tomadeinventario.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.DetalleToma;
import pack.tomainventario.tomadeinventario.NuevaToma;
import pack.tomainventario.tomadeinventario.R;

/**
 * Created by tmachado on 02/12/2014.
 */
public class RpuAdapter extends ArrayAdapter<SIP501V> {
    Activity context;
    private String rpu;
    private List<SIP501V> data3;

    public RpuAdapter(Activity context, List<SIP501V> data3) {
        super(context, R.layout.extra,data3);
        this.context = context;
        this.data3 = data3;
    }
    public RpuAdapter(Activity context, List<SIP501V> data3, String rpu) {
        super(context, R.layout.extra,data3);
        this.context = context;
        this.data3 = data3;
        this.rpu = rpu;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View item = convertView;
        ViewHolder holder;

        if(item == null){
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.extra, null);

            holder = new ViewHolder();
            holder.nombre = (TextView)item.findViewById(R.id.numero);
            holder.ficha = (TextView)item.findViewById(R.id.fecha);
            item.setTag(holder);
        }
        else{
            holder = (ViewHolder)item.getTag();
        }

        holder.nombre.setText(data3.get(position).nombre);
        holder.ficha.setText(data3.get(position).ficha);
        if( (context instanceof NuevaToma || context instanceof DetalleToma) &&
                data3.get(position).ficha.equals(rpu)){

            Log.e("TAAAG", "Entro a cambiar el color " + data3.get(position).ficha);
            holder.nombre.setTextColor(Color.parseColor("#A9CBE9"));
        }
        else{
            holder.nombre.setTextColor(Color.parseColor("#000000"));
        }
        return(item);
    }

    static class ViewHolder {
        TextView nombre;
        TextView ficha;

    }
}