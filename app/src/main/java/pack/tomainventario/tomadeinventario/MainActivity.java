package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.MainAdapter;
import pack.tomainventario.tomadeinventario.Config.BaseDrawer;
import pack.tomainventario.tomadeinventario.Config.EndlessListView;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SBN050D;
import pack.tomainventario.tomadeinventario.DataBase.SBN051D;
import pack.tomainventario.tomadeinventario.DataBase.SBN053D;
import pack.tomainventario.tomadeinventario.DataBase.SBN054D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.DataBase.SIP517V;
import pack.tomainventario.tomadeinventario.Objects.Inventoried;


public class MainActivity extends BaseDrawer implements EndlessListView.EndlessListener{

    private ActionBar actionBar;
    private SharedPreferences prefs;
    private EndlessListView lstOpciones;
    private MainAdapter adaptador;
    private Inventoried inv;
    private List<Inventoried> data;
    private List<SBN051D> inventariados;
    private List<SBN050D> inventarios;
    private TextView txtEmpty ;
    private final static int ITEM_PER_REQUEST = 20;
    private int ini = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // ------------------- Configuracion - inicio

        RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_main, null,false);
        rLayout.addView(activityView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        super.navAdapter.setActual(0);
        actionBar= getActionBar();
        actionBar.setTitle("Tomas realizadas");
        // ------------------- Configuracion - fin
        prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
        txtEmpty = (TextView)findViewById(R.id.empty);
        data = new ArrayList<Inventoried>();
        lstOpciones = (EndlessListView)findViewById(R.id.LstOpciones);
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
        boolean drawerOpen = navDrawerLayout.isDrawerOpen(navList);
        menu.findItem(R.id.action_add).setVisible(!drawerOpen);
        if(prefs.getInt("Login",0)!=2){
            menu.findItem(R.id.action_add).setVisible(false);
        }
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
                ini=0;
                lstOpciones.setDone(false);
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

            adaptador = new MainAdapter(this, createItems());
            lstOpciones.setLoadingView(R.layout.loading_layout);
            lstOpciones.setAdapter(adaptador);
            lstOpciones.setListener(this);

            lstOpciones.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            if(prefs.getInt("Login",0)==2) {

                lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        sendData(position);
                    }
                });
            }
                lstOpciones.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                        if (prefs.getInt("Login", 0) == 2) {
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
                        else{
                            mode.setSubtitle("Modificaciones no permitidas");
                        }
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        if (prefs.getInt("Login", 0) == 2) {
                            Inventoried selecteditem;
                            switch (item.getItemId()) {
                                case R.id.delete:
                                    SparseBooleanArray selected = adaptador.getSelectedIds();
                                    for (int i = (selected.size() - 1); i >= 0; i--) {
                                        if (selected.valueAt(i)) {
                                            selecteditem = adaptador.getItem(selected.keyAt(i));
                                            SBN051D delete = SBN051D.getBn(selecteditem.getNumero());
                                            delete.delete();
                                            SBN001D change = SBN001D.getBn(selecteditem.getNumero());
                                            change.selected = 0;
                                            change.checked = 0;
                                            change.show = 1;
                                            change.taken = 0;
                                            change.save();
                                        }
                                    }
                                    data.clear();
                                    inventariados = SBN051D.getAll();
                                    setData();
                                    adaptador.updateAdapter(data);
                                    inventarios = SBN050D.getAll();

                                    for (SBN050D aData : inventarios) {
                                        if (SBN051D.getInv(aData.idInventario) == null) {
                                            Log.e("AAA", "Inventario q esta null en 051 "+aData.idInventario);
                                            aData.status = 0;
                                            aData.save();
                                            SBN010D ubicActual = SBN010D.getUbic(aData.codUbic);
                                            ubicActual.show=1;
                                            ubicActual.save();
                                            Log.e("AAA", "La ubicacion "+ ubicActual.nombre+" tiene show 1");
                                        }
                                    }
                                    mode.finish();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        if (prefs.getInt("Login", 0) == 2)
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
    private List<Inventoried> createItems() {
        List<Inventoried> result = new ArrayList<Inventoried>();
        int i;
        Log.e("CREATE", "data es "+data.size());
        Log.e("CREATE2", "ini es "+ini);
        for (i= 0; i < ITEM_PER_REQUEST; i++) {
            if((i+ini)>=data.size()) {
                Log.e("if", "Dice q i= "+i+"+"+ini+" es mayor q "+data.size()+" y x eso pone done");
                lstOpciones.setDone(true);
                Log.e("i2", "mandare u result de tamaño "+result.size());
                return result;
            }
            else {
                result.add(data.get(i+ini));
            }
        }
        ini= ini+i;
        return result;
    }

    @Override
    public void loadData() {
        FakeNetLoader fl = new FakeNetLoader();
        fl.execute();
    }

    private class FakeNetLoader extends AsyncTask<String, Void, List<Inventoried>> {

        @Override
        protected List<Inventoried> doInBackground(String... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return createItems();
        }

        @Override
        protected void onPostExecute(List<Inventoried> result) {
            super.onPostExecute(result);
            Log.e("POST", "el result q voy a mandar es de tamaño "+ result.size());
            lstOpciones.addNewData(result);
        }
    }
}