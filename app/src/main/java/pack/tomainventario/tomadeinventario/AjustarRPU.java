package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.BnAdapter;
import pack.tomainventario.tomadeinventario.Config.BaseDrawer;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.DataBase.SIP517V;
import pack.tomainventario.tomadeinventario.Dialogs.BnFilterDialog;
import pack.tomainventario.tomadeinventario.Dialogs.RpuDialog;
import pack.tomainventario.tomadeinventario.Interfaces.Configuracion;
import pack.tomainventario.tomadeinventario.Interfaces.Filter;


public class AjustarRPU extends BaseDrawer implements Filter,Configuracion,RpuDialog.NoticeDialogListener{

    private List<SBN001D> data;
    private CheckBox ckAll;
    private Boolean all=false;
    private BnAdapter adaptador;
    private ImageView filter;
    private EditText eRpu;
    private ListView lstOpciones;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ------------------- Configuracion - inicio
        RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_ajustar_rpu, null,false);
        rLayout.addView(activityView);
        navAdapter.setActual(2);
        actionBar= getActionBar();
        actionBar.setTitle("Ajustar RPU");
        // ------------------- Configuracion - fin

       data = SBN001D.getAllFiltered(4, "", "");
        eRpu = (EditText)findViewById(R.id.edit_rpu);
        lstOpciones = (ListView)findViewById(R.id.LstOpciones);
        adaptador =  new BnAdapter(this,data,1);
        lstOpciones.setAdapter(adaptador);

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
        int id = item.getItemId();
        if (id == R.id.action_aceptar) {
            SBN001D.setAllChecked(0,2);
            Intent intent = new Intent(AjustarRPU.this, MainActivity.class);
            startActivity(intent);
            finish();
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
    public void onDialogItemClick(SIP501V rpu, int num) {
        eRpu.setText(rpu.nombre);
    }

    @Override
    public void filterSelect(SIP517V sede, SBN010D ubicacion) {
        if(sede==null && ubicacion==null)
            data= SBN001D.getAllFiltered(0, "", "");
        else if(sede!=null && ubicacion ==null)
            data= SBN001D.getAllFiltered(1, sede.codSede, "");
        else if(sede == null)
            data= SBN001D.getAllFiltered(2, "", ubicacion.codUbic);
        else
            data= SBN001D.getAllFiltered(3, sede.codSede, ubicacion.codUbic);

        if (data.size()==0)
            lstOpciones.setVisibility(View.GONE);
        else if (data.size()!=0){
            adaptador.updateAdapter(data);
            lstOpciones.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void configDialog(int num) {}

}
