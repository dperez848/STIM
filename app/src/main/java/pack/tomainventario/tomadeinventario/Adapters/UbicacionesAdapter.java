package pack.tomainventario.tomadeinventario.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import pack.tomainventario.tomadeinventario.Interfaces.Selected;
import pack.tomainventario.tomadeinventario.Models.SBN010D;
import pack.tomainventario.tomadeinventario.R;

/**
 * Created by tmachado on 19/03/2015.
 */
public class UbicacionesAdapter extends ArrayAdapter<SBN010D> {

    private Activity context;
    private List<SBN010D> data;
    private Selected listener;
    private String codSelected="";
    private int selectedPosition=0;


    public UbicacionesAdapter(Activity context, List<SBN010D> data) {
        super(context, R.layout.layout_list_bn,data);
        this.context = context;
        this.data=data;
        this.listener = (Selected)context;
    }

    static class ViewHolder {
        TextView ubicacion;
        RadioButton rb1;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        View item = convertView;
        final ViewHolder holder;
        if(item == null){
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.layout_item_ubic, null);
            holder = new ViewHolder();
            holder.ubicacion = (TextView)item.findViewById(R.id.txtUbic);
            holder.rb1 = (RadioButton)item.findViewById(R.id.radio);
            item.setTag(holder);
        }
        else{
            holder = (ViewHolder)item.getTag();
        }
        holder.rb1.setChecked(selectedPosition==position);
        holder.ubicacion.setText("" + data.get(position).nombre);

        holder.rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=position;
                notifyDataSetChanged();
            }
        });
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=position;
                notifyDataSetChanged();
            }
        });
        return (item);
    }

    public void updateAdapter(List<SBN010D> data){
        this.data=data;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return data.size();
    }

}
