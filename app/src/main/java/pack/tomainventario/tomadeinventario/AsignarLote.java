package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.BnAdapter;
import pack.tomainventario.tomadeinventario.Config.EndlessListView;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SIP517V;
import pack.tomainventario.tomadeinventario.Dialogs.BnFilterDialog;
import pack.tomainventario.tomadeinventario.Interfaces.Configuracion;
import pack.tomainventario.tomadeinventario.Interfaces.Filter;


public class AsignarLote extends Activity implements EndlessListView.EndlessListener,Configuracion,Filter {
    private BnAdapter adaptador;
    private CheckBox ckAll;
    private EndlessListView lstOpciones;
    private Boolean all=false;
    private ActionBar actionBar;
    private List<SBN001D> data;
    private Menu menu;
    private final static int ITEM_PER_REQUEST = 20;
    private int ini = 0;
    private final CharSequence[] items = { "Deshacer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_lote);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Asignar por grupo");

        data = SBN001D.getAllFiltered(0, "", "");
        for (SBN001D aData : data){
            SBN001D inv;
            inv= SBN001D.getBn(aData.numero);
            inv.show=1;
            inv.save();
        }

        adaptador =  new BnAdapter(this,createItems(),1);
        lstOpciones = (EndlessListView)findViewById(R.id.LstOpciones);
        lstOpciones.setLoadingView(R.layout.loading_layout);
        lstOpciones.setAdapterBn(adaptador);
        lstOpciones.setListener(this);

        ckAll = (CheckBox) findViewById(R.id.ck_all);
        ckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(all) {
                    ckAll.setChecked(false);
                    SBN001D.setAllChecked(0,1);
                    all = false;
                    adaptador.notifyDataSetChanged();
                }
                else {
                    ckAll.setChecked(true);
                    SBN001D.setAllChecked(1,1);
                    all=true;
                    adaptador.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_asignar_lote, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_aceptar) {
            DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            SBN001D.allCheckedToSelected();
                            SBN001D.setAllChecked(0,1);
                            Intent intent1 = new Intent();
                            setResult(RESULT_OK, intent1);
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("¿Confirmar acción?").setPositiveButton("Si", dialogClickListener1)
                    .setNegativeButton("No", dialogClickListener1).show();
            return true;
        }
        else if (id == android.R.id.home){
            SBN001D.setAllChecked(0,1);
            Intent intent1 = new Intent();
            setResult(RESULT_OK, intent1);
            finish();
            return true;
        }
        else if (id == R.id.action_filtro){
            BnFilterDialog dialog = new BnFilterDialog(AsignarLote.this);
            dialog.show(getFragmentManager(),"BnFilterDialog");
            dialog.setTargetFragment(dialog, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setCheck(Boolean value) {
        ckAll.setChecked(value);
        all=value;
    }

    @Override
    public Boolean isAll() {
        return all;
    }

    @Override
    public void filterSelect(SIP517V sede, SBN010D ubicacion) {
        MenuItem mItem = menu.findItem(R.id.action_filtro);
        if(sede==null && ubicacion==null) {
            data = SBN001D.getAllFiltered(0, "", "");
            mItem.setIcon(R.drawable.filter_empty);
        }
        else if(sede!=null && ubicacion ==null) {
            data = SBN001D.getAllFiltered(1, sede.codSede, "");
            mItem.setIcon(R.drawable.filter_filled);
        }
        else if(sede == null) {
            data = SBN001D.getAllFiltered(2, "", ubicacion.codUbic);
            mItem.setIcon(R.drawable.filter_filled);
        }
        else {
            data = SBN001D.getAllFiltered(3, sede.codSede, ubicacion.codUbic);
            mItem.setIcon(R.drawable.filter_filled);
        }

        if (data.size()==0)
            lstOpciones.setVisibility(View.GONE);
        else if (data.size()!=0){
            Log.e("aaa", "la data no es null ");
            List<SBN001D> all=SBN001D.getAll();
            for (SBN001D aData : all){
                if(has(aData.numero,data)){
                    SBN001D inv;
                    inv= SBN001D.getBn(aData.numero);
                    inv.show=1;
                    inv.save();
                    Log.e("aaa", "al bien "+aData.numero+" le puse show 1");
                }
                else{
                    SBN001D inv;
                    inv= SBN001D.getBn(aData.numero);
                    inv.show=0;
                    inv.save();
                 }
            }
            adaptador.updateAdapter(data);
            lstOpciones.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void configDialog(int num) {}
    public Boolean has(int num,List<SBN001D> filtered){
        for (SBN001D aFiltered : filtered) {
            if (aFiltered.numero == num) return true;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        SBN001D.setAllChecked(0,1);
        Intent intent1 = new Intent();
        setResult(RESULT_OK, intent1);
        finish();
    }

    private List<SBN001D> createItems() {
        List<SBN001D> result = new ArrayList<SBN001D>();
        int i;
        for (i= 0; i < ITEM_PER_REQUEST; i++) {
            if((i+ini)>=data.size()) {
                lstOpciones.setDone(true);
                return result;
            }
            else result.add(data.get(i+ini));
        }
        ini= ini+i;
        return result;
    }

    @Override
    public void loadData() {
        FakeNetLoader fl = new FakeNetLoader();
        fl.execute();
    }

    private class FakeNetLoader extends AsyncTask<String, Void, List<SBN001D>> {

        @Override
        protected List<SBN001D> doInBackground(String... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return createItems();
        }

        @Override
        protected void onPostExecute(List<SBN001D> result) {
            super.onPostExecute(result);
            lstOpciones.addNewDataBn(result);
        }



    }
}
