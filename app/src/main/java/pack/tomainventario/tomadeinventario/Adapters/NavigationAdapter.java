package pack.tomainventario.tomadeinventario.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pack.tomainventario.tomadeinventario.Objects.Item_objct;
import pack.tomainventario.tomadeinventario.R;

public class NavigationAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Item_objct> arrayitms;
    private int actual;

    public NavigationAdapter(Activity activity, ArrayList<Item_objct> listarry) {
        super();
        this.activity = activity;
        this.arrayitms=listarry;
    }

    @Override
    public Object getItem(int position) {
        return arrayitms.get(position);
    }
    public int getCount() {
        return arrayitms.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class Fila
    {
        TextView titulo_itm;
        ImageView icono;
    }
    public void setActual(int actual){
        this.actual=actual;
        notifyDataSetChanged();
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Fila view;
        LayoutInflater inflator = activity.getLayoutInflater();
        if(convertView==null)
        {
            view = new Fila();
            //Creo objeto item y lo obtengo del array
            Item_objct itm=arrayitms.get(position);
            convertView = inflator.inflate(R.layout.drawer, null);
            //Titulo
            view.titulo_itm = (TextView) convertView.findViewById(R.id.title_item);
            //Seteo en el campo titulo el nombre correspondiente obtenido del objeto
            view.titulo_itm.setText(itm.getTitulo());
            //Icono
            view.icono = (ImageView) convertView.findViewById(R.id.icon);
            //Seteo el icono
            view.icono.setImageResource(itm.getIcono());
            if(position == actual)
              convertView.setBackgroundColor(Color.parseColor("#A9CBE9"));
            convertView.setTag(view);
        }
        else
        {
            view = (Fila) convertView.getTag();
        }
        return convertView;
    }
}


