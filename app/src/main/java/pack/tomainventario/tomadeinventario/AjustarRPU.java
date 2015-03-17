package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.BnAdapter;
import pack.tomainventario.tomadeinventario.Config.BaseDrawer;
import pack.tomainventario.tomadeinventario.Config.EndlessListView;
import pack.tomainventario.tomadeinventario.Models.SBN001D;
import pack.tomainventario.tomadeinventario.Models.SBN010D;
import pack.tomainventario.tomadeinventario.Models.SBN052D;
import pack.tomainventario.tomadeinventario.Models.SBN053D;
import pack.tomainventario.tomadeinventario.Models.SIP501V;
import pack.tomainventario.tomadeinventario.Models.SIP517V;
import pack.tomainventario.tomadeinventario.Dialogs.BnFilterDialog;
import pack.tomainventario.tomadeinventario.Dialogs.RpuDialog;
import pack.tomainventario.tomadeinventario.Interfaces.Configuracion;
import pack.tomainventario.tomadeinventario.Interfaces.Filter;


public class AjustarRPU extends BaseDrawer implements RpuDialog.NoticeDialogListener,EndlessListView.EndlessListener,Filter,Configuracion{

    private SharedPreferences prefs;
    private List<SBN001D> data;
    private CheckBox ckAll;
    private Boolean all=false;
    private BnAdapter adaptador;
    private ImageView filter;
    private EditText eRpu;
    private EndlessListView lstOpciones;
    private ActionBar actionBar;
    private SIP501V rpuSelected;
    private final static int ITEM_PER_REQUEST = 20;
    private int ini = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ------------------- Configuracion - inicio
        RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_ajustar_rpu, null,false);
        rLayout.addView(activityView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        navAdapter.setActual(2);
        actionBar= getActionBar();
        actionBar.setTitle("Ajustar RPU");
        prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
        // ------------------- Configuracion - fin

        data = SBN001D.getAllFiltered(4, "", "");
        eRpu = (EditText)findViewById(R.id.edit_rpu);

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
                    SBN001D.setAllChecked(0,2);
                    all = false;
                    adaptador.notifyDataSetChanged();
                }
                else {
                    ckAll.setChecked(true);
                    SBN001D.setAllChecked(1,2);
                    all=true;
                    adaptador.notifyDataSetChanged();
                }
            }
        });

        filter = (ImageView)findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {  BnFilterDialog dialog = new BnFilterDialog(AjustarRPU.this);
                dialog.show(getFragmentManager(),"BnFilterDialog");
                dialog.setTargetFragment(dialog, 1);
            }
        });
    }

    public void rpuDialog(View v){
        RpuDialog dialog = new RpuDialog(AjustarRPU.this);
        dialog.show(getFragmentManager(),"RpuDialog");
        dialog.setTargetFragment(dialog, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ajustar_rpu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final List<SBN001D> listChecked;
        int id = item.getItemId();
        if (id == R.id.action_aceptar) {
        listChecked = SBN001D.getChecked();
            if(eRpu.getText().toString().equals("")){
                DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                break;
                        }
                    }
                };
                AlertDialog.Builder builder2 = new AlertDialog.Builder(AjustarRPU.this);
                builder2.setMessage("Debe elegir un RPU").setPositiveButton("Aceptar", dialogClickListener2).show();
            }
            else if(listChecked.size()==0){
                DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                break;
                        }
                    }
                };
                AlertDialog.Builder builder2 = new AlertDialog.Builder(AjustarRPU.this);
                builder2.setMessage("Debe elegir por lo menos 1 bien nacional").setPositiveButton("Aceptar", dialogClickListener2).show();
            }
            else{
                SBN001D.setAllChecked(0, 2);
                ajustarRpu(rpuSelected.ficha, listChecked);
                Intent intent = new Intent(AjustarRPU.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ajustarRpu(String ficha, List<SBN001D> list) {
        for (SBN001D aData : list) {
            SBN001D bN = SBN001D.getBn(aData.numero);
            bN.pUsuario = ficha;
            bN.save();
            SBN052D historialRpu=new SBN052D(bN.numero,fechaActual(),ficha,
                    prefs.getInt("Activar", 0), SBN053D.getAll().get(0).idInventarioActivo);
            historialRpu.save();
        }
    }

    @Override
    public void setCheck(Boolean value) {
        ckAll.setChecked(value);
        all=value;
    }

    @Override
    public void onStop() {
        super.onStop();
        SBN001D.setAllChecked(0,2);
    }

    @Override
    public Boolean isAll() {
        if (all)
            return true;
        return false;
    }

   @Override
    public void onDialogItemClick(SIP501V rpu,int num) {
       rpuSelected=rpu;
       eRpu.setText(rpu.nombre);
       Log.e("RPU", ""+eRpu.getText());
   }

    @Override
    public void filterSelect(SIP517V sede, SBN010D ubicacion) {
        if(sede==null && ubicacion==null) {
            data = SBN001D.getAllFilteredRPU(4, "", "");
            filter.setImageResource(R.drawable.filter_empty);
        }
        else if(sede!=null && ubicacion ==null) {
            data = SBN001D.getAllFilteredRPU(1, sede.codSede, "");
            filter.setImageResource(R.drawable.filter_filled);
        }
        else if(sede == null) {
            data = SBN001D.getAllFilteredRPU(2, "", ubicacion.codUbic);
            filter.setImageResource(R.drawable.filter_filled);
        }
        else {
            data = SBN001D.getAllFilteredRPU(3, sede.codSede, ubicacion.codUbic);
            filter.setImageResource(R.drawable.filter_filled);
        }

        if (data.size()==0)
            lstOpciones.setVisibility(View.GONE);
        else if (data.size()!=0){
            adaptador.updateAdapter(data);
            lstOpciones.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void configDialog(int num) {}

    public String fechaActual(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c.getTime());
    }

    @Override
    public void loadData() {
        FakeNetLoader fl = new FakeNetLoader();
        fl.execute();
    }

    //@Override
    public void openRpuDialog(int bn) {}

    /*@Override
    public void onRpuItemClick(SIP501V rpu) {
        rpuSelected=rpu;
        eRpu.setText(rpu.nombre);
    }*/

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

}
