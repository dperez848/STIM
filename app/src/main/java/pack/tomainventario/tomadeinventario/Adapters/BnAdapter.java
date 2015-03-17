package pack.tomainventario.tomadeinventario.Adapters;

import android.app.Activity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pack.tomainventario.tomadeinventario.AjustarRPU;
import pack.tomainventario.tomadeinventario.AsignarLote;
import pack.tomainventario.tomadeinventario.Models.SBN001D;
import pack.tomainventario.tomadeinventario.Models.SBN010D;
import pack.tomainventario.tomadeinventario.Models.SBN054D;
import pack.tomainventario.tomadeinventario.DetalleGaleria;
import pack.tomainventario.tomadeinventario.Galeria;
import pack.tomainventario.tomadeinventario.Interfaces.Configuracion;
import pack.tomainventario.tomadeinventario.Interfaces.IGaleria;
import pack.tomainventario.tomadeinventario.Interfaces.Rpu;
import pack.tomainventario.tomadeinventario.Interfaces.Selected;
import pack.tomainventario.tomadeinventario.NuevaToma;
import pack.tomainventario.tomadeinventario.Objects.Inventoried;
import pack.tomainventario.tomadeinventario.R;

/**
 * Created by tmachado on 27/11/2014.
 */
public class BnAdapter extends ArrayAdapter<SBN001D> {
    private Activity context;
    private int hasChecked;
    private List<SBN001D> data;
    private Configuracion listener;
    private IGaleria listener2;
    private Rpu listener3;
    private Selected listener4;
    private SparseBooleanArray mSelectedItemsIds;


    public BnAdapter(Activity context, List<SBN001D> data, int hasChecked) {
        super(context, R.layout.layout_list_bn,data);
        this.context = context;
        this.data=data;
        mSelectedItemsIds = new SparseBooleanArray();
        this.hasChecked = hasChecked;
        if(context instanceof Galeria || context instanceof DetalleGaleria
                || context instanceof NuevaToma) {
            this.listener2 = (IGaleria) context;
            this.listener = (Configuracion)context;
        }
        if( context instanceof NuevaToma) {
            this.listener3 = (Rpu) context;
            this.listener = (Configuracion)context;
            this.listener4 = (Selected)context;
        }
        if( context instanceof AsignarLote || context instanceof AjustarRPU) {
            this.listener = (Configuracion)context;
        }
    }

    static class ViewHolder {
        TextView numero, descripcion, serial, ubic;
        CheckBox ck1;
        LinearLayout items;
        ImageView camara,ajustes,rpu;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        View item = convertView;
        final ViewHolder holder;
        if(item == null){
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.layout_list_bn, null);
            holder = new ViewHolder();
            holder.numero = (TextView)item.findViewById(R.id.numero);
            holder.descripcion = (TextView)item.findViewById(R.id.descripcion);
            holder.serial = (TextView)item.findViewById(R.id.serial);
            holder.ubic = (TextView)item.findViewById(R.id.ubic);
            holder.ck1 = (CheckBox)item.findViewById(R.id.check);
            holder.items = (LinearLayout)item.findViewById(R.id.items);
            holder.camara = (ImageView)item.findViewById(R.id.camera);
            holder.ajustes = (ImageView)item.findViewById(R.id.settings);
            holder.rpu = (ImageView)item.findViewById(R.id.rpu);

            item.setTag(holder);
        }
        else{
            holder = (ViewHolder)item.getTag();
        }

            holder.numero.setText("" + data.get(position).numero);
            holder.descripcion.setText(data.get(position).nombre);
            holder.serial.setText(data.get(position).serial);
            holder.ubic.setText(SBN010D.getUbicacion(data.get(position).codUbic));

            if (hasChecked == 1) { //Si tiene solo check para "Ajustar RPU" y "Asignar por lote"
                holder.items.setVisibility(View.GONE);
                holder.ck1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (holder.ck1.isChecked()) {
                            SBN001D bN = SBN001D.getBn(data.get(position).numero);
                            bN.checked = 1;
                            bN.save();
                            if (SBN001D.isFull()) {
                                Log.e("aaa", "esta full " );
                                listener.setCheck(true);
                            }
                        } else {
                            SBN001D bN = SBN001D.getBn(data.get(position).numero);
                            bN.checked = 0;
                            bN.save();
                            if (listener.isAll()) {
                                listener.setCheck(false);
                            }
                        }
                    }
                });
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!holder.ck1.isChecked()) {
                            SBN001D bN = SBN001D.getBn(data.get(position).numero);
                            bN.checked = 1;
                            bN.save();
                            if (SBN001D.isFull()) {
                                listener.setCheck(true);
                            }
                            holder.ck1.setChecked(true);
                        } else {

                            SBN001D bN = SBN001D.getBn(data.get(position).numero);
                            bN.checked = 0;
                            bN.save();
                            if (listener.isAll()) {
                                listener.setCheck(false);
                            }
                            holder.ck1.setChecked(false);
                        }
                    }
                });
                if (data.get(position).checked == 1) {
                    holder.ck1.setChecked(true);
                } else {
                    holder.ck1.setChecked(false);
                }
            } else if (hasChecked == 2){  //Si tiene camarita y ajustes SIN check para "Nueva Toma"
                holder.ck1.setVisibility(View.GONE);

                if (!(SBN054D.isEmpty(data.get(position).numero))) {
                    holder.camara.setImageResource(R.drawable.camerar);
                } else {
                    holder.camara.setImageResource(R.drawable.camera);
                }

                holder.camara.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener2.detalle(data.get(position).numero, position);
                    }
                });
                holder.ajustes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.configDialog(data.get(position).numero);
                    }
                });
                holder.rpu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener3.openRpuDialog(data.get(position).numero);
                    }
                });
                item.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(context, "ADAPTER mandare a ponerle al item "+data.get(position).numero+" en la pos "+position, Toast.LENGTH_LONG).show();
                        listener4.deshacer(data.get(position).numero);}
                });

            } else{
                holder.items.setVisibility(View.GONE);
                holder.ck1.setVisibility(View.GONE);
            }
        return (item);
    }

    public void updateAdapter(List<SBN001D> data){
        this.data=data;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return data.size();
    }


    public void remove(Inventoried object) {
        data.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));

    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, true);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    @Override
    public SBN001D getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).hashCode();
    }

}
