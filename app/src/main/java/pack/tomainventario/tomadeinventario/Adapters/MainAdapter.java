package pack.tomainventario.tomadeinventario.Adapters;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SBN050D;
import pack.tomainventario.tomadeinventario.DataBase.SBN051D;
import pack.tomainventario.tomadeinventario.Objects.Inventoried;
import pack.tomainventario.tomadeinventario.R;

    public class MainAdapter extends ArrayAdapter<Inventoried> {
        private List<Inventoried> data;
            Activity context;

            public MainAdapter(Activity context, List<Inventoried> data) {
            super(context, R.layout.layout_list_main,data);
            this.context = context;
            this.data=data;
            }

    public View getView(int position, View convertView, ViewGroup parent){
            View item = convertView;
            ViewHolder holder;

            if(item == null){
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.layout_list_main, null);

            holder = new ViewHolder();
            holder.numero = (TextView)item.findViewById(R.id.numero);
            holder.descripcion = (TextView)item.findViewById(R.id.descripcion);
            holder.fecha = (TextView)item.findViewById(R.id.fecha);
            holder.ubicacion = (TextView)item.findViewById(R.id.ubicacion);
            holder.rpu = (TextView)item.findViewById(R.id.rpu);
            holder.foto = (ImageView)item.findViewById(R.id.foto);
            item.setTag(holder);
            }
            else{
            holder = (ViewHolder)item.getTag();
            }

            holder.numero.setText("" +data.get(position).getNumero());
            holder.descripcion.setText(data.get(position).getDescripcion());
            holder.fecha.setText(data.get(position).getFecha());
            holder.rpu.setText(data.get(position).getRpu());
            holder.ubicacion.setText(SBN010D.getUbicacion(SBN050D.getInv(SBN051D.getBn(data.get(position).getNumero()).idInventario).codUbic));
            if(!data.get(position).getFoto().equals("")) {
                byte[] imageAsBytes = Base64.decode(data.get(position).getFoto().getBytes(), Base64.DEFAULT);
                holder.foto.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                holder.foto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        else
            holder.foto.setImageResource(R.drawable.noimagen);

        return(item);
    }

    static class ViewHolder {
    TextView numero,descripcion,rpu,ubicacion,fecha;
    ImageView foto;
}
    @Override
    public int getCount() {
        return data.size();
    }
    public void updateAdapter(List<Inventoried> data){
        this.data=data;
        this.notifyDataSetChanged();
    }
}