package pack.tomainventario.tomadeinventario.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import pack.tomainventario.tomadeinventario.DataBase.SBN054D;
import pack.tomainventario.tomadeinventario.Interfaces.IGaleria;
import pack.tomainventario.tomadeinventario.Objects.Inventoried;
import pack.tomainventario.tomadeinventario.R;

/**
 * Created by tmachado on 28/11/2014.
 */
public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private SparseBooleanArray mSelectedItemsIds;
    private List<SBN054D> data;
    private IGaleria listener;

    public GridViewAdapter(Context context, int layoutResourceId, List<SBN054D> data) {
        super(context, layoutResourceId, data);
        mSelectedItemsIds = new SparseBooleanArray();
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.listener = (IGaleria)context;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
        RelativeLayout layout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.layout = (RelativeLayout) row.findViewById(R.id.layout);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        byte[] imageAsBytes = Base64.decode(data.get(position).imagen.getBytes(),Base64.DEFAULT);
        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.imageTitle.setText(data.get(position).nombre);

        holder.layout.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {listener.detalle(data.get(position).numeroBn, position);}
                                        });
        return row;
    }

    public void addFoto(SBN054D foto) {
        this.data.add(foto);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    public void updateAdapter(List<SBN054D> data){
        this.data=data;
        this.notifyDataSetChanged();
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

}
