package pack.tomainventario.tomadeinventario.Adapters;

import android.app.Activity;
import android.graphics.Color;
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
    private Activity context;
    private String rpu;
    private List<SIP501V> data;

    public RpuAdapter(Activity context, List<SIP501V> data) {
        super(context, R.layout.layout_item_rpu,data);
        this.context = context;
        this.data = data;
    }
    public RpuAdapter(Activity context, List<SIP501V> data3, String rpu) {
        super(context, R.layout.layout_item_rpu,data3);
        this.context = context;
        this.data = data3;
        this.rpu = rpu;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        View item = convertView;
        ViewHolder holder;

        if(item == null){
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.layout_item_rpu, null);

            holder = new ViewHolder();
            holder.nombre = (TextView)item.findViewById(R.id.numero);
            holder.ficha = (TextView)item.findViewById(R.id.fecha);
            item.setTag(holder);
        }
        else{
            holder = (ViewHolder)item.getTag();
        }

        holder.nombre.setText(data.get(position).nombre);
        holder.ficha.setText(data.get(position).ficha);
        if( (context instanceof NuevaToma || context instanceof DetalleToma) &&
                data.get(position).ficha.equals(rpu)){

            holder.nombre.setTextColor(Color.parseColor("#A9CBE9"));
        }
        else{
            holder.nombre.setTextColor(Color.parseColor("#000000"));
        }

       /* item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaa", "Entro al clik ");
                listener.onRpuItemClick(data.get(position));
            }
        });*/
        return(item);
    }

    static class ViewHolder {
        TextView nombre;
        TextView ficha;
    }

    public void updateAdapter(List<SIP501V> data){
        this.data=data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
