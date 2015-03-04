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
            Item_objct itm=arrayitms.get(position);
            convertView = inflator.inflate(R.layout.drawer, null);
            view.titulo_itm = (TextView) convertView.findViewById(R.id.title_item);
            view.titulo_itm.setText(itm.getTitulo());
            view.icono = (ImageView) convertView.findViewById(R.id.icon);
            view.icono.setImageResource(itm.getIcono());
            if(position == actual) {
                convertView.setBackgroundColor(Color.parseColor("#003882"));
                view.titulo_itm.setTextColor(Color.parseColor("#FFFFFF"));

                switch (actual) {
                    case 0:
                        view.icono.setImageResource(R.drawable.homeselect);
                        break;
                    case 1:
                        view.icono.setImageResource(R.drawable.searchselect);
                        break;
                    case 2:
                        view.icono.setImageResource(R.drawable.rpuselect);break;
                    case 3:
                        view.icono.setImageResource(R.drawable.galleryselect);break;
                    case 4:
                        view.icono.setImageResource(R.drawable.reportselect);break;
                    case 5:
                        view.icono.setImageResource(R.drawable.check);break;
                    default:
                        view.icono.setImageResource(R.drawable.cerrarselect); break;

                }
            }
            convertView.setTag(view);
        }
        else
        {
            view = (Fila) convertView.getTag();
        }
        return convertView;
    }
}


