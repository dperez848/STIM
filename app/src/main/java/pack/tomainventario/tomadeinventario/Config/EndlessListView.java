package pack.tomainventario.tomadeinventario.Config;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.BnAdapter;
import pack.tomainventario.tomadeinventario.Adapters.GridViewAdapter;
import pack.tomainventario.tomadeinventario.Adapters.MainAdapter;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN054D;
import pack.tomainventario.tomadeinventario.Objects.Inventoried;

public class EndlessListView extends ListView implements AbsListView.OnScrollListener {

    private View footer;
    private boolean isLoading;
    private EndlessListener listener;
    private MainAdapter adapter;
    private BnAdapter adapterBn;
    private boolean done=false;
    private GridViewAdapter adapterGrid;

    public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnScrollListener(this);
    }

    public EndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(this);
    }

    public EndlessListView(Context context) {
        super(context);
        this.setOnScrollListener(this);
    }

    public void setListener(EndlessListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (getAdapter() == null) {
            Log.e("LOG", "getadapter() es null");
            return;
        }
        if (getAdapter().getCount() == 0) {
            Log.e("LOG", "getadapter() es 000");
            return;
        }
        if (isDone()) {
            Log.e("LOG", "isDone()");
            return;
        }
        int l = visibleItemCount + firstVisibleItem;
        if (l >= totalItemCount && !isLoading) {
            this.addFooterView(footer);
            isLoading = true;
            listener.loadData();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    public void setLoadingView(int resId) {
        LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = (View) inflater.inflate(resId, null);
        this.addFooterView(footer);

    }


    public void setAdapter(MainAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
        this.removeFooterView(footer);
    }
    public void setAdapterBn(BnAdapter adapter) {
        super.setAdapter(adapter);
        this.adapterBn = adapter;
        this.removeFooterView(footer);
    }
    public void setAdapterGrid(GridViewAdapter adapter) {
        super.setAdapter(adapter);
        this.adapterGrid = adapter;
        this.removeFooterView(footer);
    }

    public void addNewData(List<Inventoried> data) {

        this.removeFooterView(footer);
        Log.e("addnewdata", "Voy a meter esta cantidad de elementos nueva: "+ data.size());
        for (int i= 0; i < data.size(); i++) {
            adapter.add(data.get(i));
            Log.e("LOG", "adapter va por:  "+adapter.getCount());
        }
        //adapter.addAll(data);
        Log.e("LOG", "COUNT: "+adapter.getCount());
        adapter.notifyDataSetChanged();
        isLoading = false;
    }
    public void addNewDataBn(List<SBN001D> data) {

        this.removeFooterView(footer);

        adapterBn.addAll(data);
        adapterBn.notifyDataSetChanged();
        isLoading = false;
    }
    public void addNewDataGrid(List<SBN054D> data) {

        this.removeFooterView(footer);


        adapterGrid.addAll(data);
        adapterGrid.notifyDataSetChanged();
        isLoading = false;
    }

    public EndlessListener setListener() {
        return listener;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }


    public static interface EndlessListener {
        public void loadData() ;
    }
}