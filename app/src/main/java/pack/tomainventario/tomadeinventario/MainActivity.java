package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.MainAdapter;
import pack.tomainventario.tomadeinventario.Config.BaseDrawer;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SBN050D;
import pack.tomainventario.tomadeinventario.DataBase.SBN051D;
import pack.tomainventario.tomadeinventario.DataBase.SBN053D;
import pack.tomainventario.tomadeinventario.DataBase.SBN054D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.DataBase.SIP517V;
import pack.tomainventario.tomadeinventario.Objects.Inventoried;


public class MainActivity extends BaseDrawer {

    private ActionBar actionBar;
    private ListView lstOpciones;
    private MainAdapter adaptador;
    private Inventoried inv;
    private List<Inventoried> data;
    private List<SBN051D> inventariados;
    private List<SBN050D> inventarios;
    private TextView txtEmpty ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // ------------------- Configuracion - inicio

        RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_main, null,false);
        rLayout.addView(activityView);
        super.NavAdapter.setActual(0);
        actionBar= getActionBar();
        actionBar.setTitle("Tomas realizadas");
        // ------------------- Configuracion - fin

        txtEmpty = (TextView)findViewById(R.id.empty);
        data = new ArrayList<Inventoried>();
        lstOpciones = (ListView)findViewById(R.id.LstOpciones);
        inventariados= SBN051D.getAll();
        setData();
        showList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = NavDrawerLayout.isDrawerOpen(NavList);
        menu.findItem(R.id.action_add).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, ActivacionInventario.class);
            startActivityForResult(intent, 1008);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1) {
        super.onActivityResult(requestCode, resultCode, data1);
        if (resultCode == RESULT_OK) {
            if(requestCode==1008) {
                data = new ArrayList<Inventoried>();
                inventariados= SBN051D.getAll();
                setData();
                showList();
            }
        }
    }

    public Boolean has(int num,List<Inventoried> data){
        for (Inventoried aData : data) {
            if (aData.getNumero() == num) return true;
        }
        return false;
    }

    public void setData(){
        if (!inventariados.isEmpty()){
            for (SBN051D aData : inventariados) {
                if (!has(aData.numeroBn, data)) {
                    inv = new Inventoried();
                    inv.setNumero(aData.numeroBn);
                    inv.setDescripcion(SBN001D.getDescripcion(aData.numeroBn));
                    inv.setFecha(aData.fecha);
                    inv.setFoto(SBN054D.getFoto(aData.numeroBn));
                    inv.setSede(SIP517V.getSede(SBN053D.getAll().get(0).sede).desUbic);
                    inv.setUbic(SBN010D.getUbicacion(SBN050D.getInv(SBN051D.getBn(aData.numeroBn).idInventario).codUbic));
                    inv.setRpu(SIP501V.getPersonal(SBN001D.getBn(aData.numeroBn).pUsuario).nombre);
                    data.add(inv);
                }
            }
        }
    }

    public void sendData(int position){
        Intent intent = new Intent(MainActivity.this, DetalleToma.class);
        Bundle bundle = new Bundle();
        bundle.putInt("numero",data.get(position).getNumero());
        intent.putExtras(bundle);
        startActivityForResult(intent, 1008);
    }

    public void showList(){
        if (data.size()==0) {
            lstOpciones.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
        else if (data.size()!=0){
            lstOpciones.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
            adaptador =  new MainAdapter(this,data);
            lstOpciones.setAdapter(adaptador);
            lstOpciones.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Log.e("AAA", "Click " + position);
                    sendData(position);
                }
            });
            lstOpciones.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode,int position, long id, boolean checked) {
                    final int checkedCount = lstOpciones.getCheckedItemCount();
                    switch (checkedCount) {
                        case 0:
                            mode.setSubtitle(null);
                            break;
                        case 1:
                            mode.setSubtitle("1 seleccionado");
                            break;
                        default:
                            mode.setSubtitle("" + checkedCount + " seleccionados");
                            break;
                    }
                    adaptador.toggleSelection(position);

                }
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    Inventoried selecteditem;
                    switch (item.getItemId()) {
                        case R.id.delete:
                            SparseBooleanArray selected = adaptador.getSelectedIds();
                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {
                                    selecteditem = adaptador.getItem(selected.keyAt(i));
                                    SBN051D delete=SBN051D.getBn(selecteditem.getNumero());
                                    delete.delete();
                                    SBN001D change=SBN001D.getBn(selecteditem.getNumero());
                                    change.selected=0;
                                    change.checked=0;
                                    change.show=1;
                                    change.taken=0;
                                    change.save();
                                }
                            }
                            data.clear();
                            setData();
                            adaptador.updateAdapter(data);
                            inventarios=SBN050D.getAll();

                            for (SBN050D aData : inventarios) {
                               if(SBN051D.getInv(aData.idInventario)==null) {
                                    aData.status=0;
                                    aData.save();
                                }
                            }
                            mode.finish();
                            return true;
                        default:
                            return false;
                    }
                }
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.menu_multiple_selection, menu);
                    return true;
                }
                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    adaptador.removeSelection();
                }
                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }
            });
        }
        SBN001D.setTaken();
    }


    public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 5;
        // The current offset index of data you have loaded
        private int currentPage = 0;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;
        // Sets the starting page index
        private int startingPageIndex = 0;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        public EndlessScrollListener(int visibleThreshold, int startPage) {
            this.visibleThreshold = visibleThreshold;
            this.startingPageIndex = startPage;
            this.currentPage = startPage;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount)
        {
            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) { this.loading = true; }
            }
            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
                currentPage++;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
                onLoadMore(currentPage + 1, totalItemCount);
                loading = true;
            }
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount);

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Don't take any action on changed
        }
    }
}