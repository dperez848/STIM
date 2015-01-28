package pack.tomainventario.tomadeinventario.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pack.tomainventario.tomadeinventario.R;
import pack.tomainventario.tomadeinventario.Objects.ReportesContent;

/**
 * Created by tmachado on 27/11/2014.
 */
public class ReportesAdapter extends ArrayAdapter<ReportesContent> {
    Activity context;
    ReportesContent[] data3;

    public ReportesAdapter(Activity context, ReportesContent[] data3) {
        super(context, R.layout.extra,data3);
        this.context = context;
        this.data3 = data3;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View item = convertView;
        ViewHolder holder;

        if(item == null){
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.extra, null);

            holder = new ViewHolder();
            holder.numero = (TextView)item.findViewById(R.id.numero);
            holder.descripcion = (TextView)item.findViewById(R.id.descripcion);
            holder.fecha = (TextView)item.findViewById(R.id.fecha);
            item.setTag(holder);
        }
        else{
            holder = (ViewHolder)item.getTag();
        }

        holder.numero.setText("" +data3[position].getNumero());
        holder.descripcion.setText(data3[position].getDescripcion());
        holder.fecha.setText(data3[position].getFecha());
        return(item);
    }

    static class ViewHolder {
        TextView numero;
        TextView descripcion;
        TextView fecha;

    }
}

