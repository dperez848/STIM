package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.BnAdapter;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SIP517V;
import pack.tomainventario.tomadeinventario.Dialogs.BnFilterDialog;
import pack.tomainventario.tomadeinventario.Interfaces.Configuracion;
import pack.tomainventario.tomadeinventario.Interfaces.Filter;


public class AsignarLote extends Activity implements Configuracion,Filter {
    private BnAdapter adaptador;
    private ImageButton bSearch;
    private CheckBox ckAll;
    private ImageView filter;
    private ListView lstOpciones;
    private Boolean all=false;
    private ActionBar actionBar;
    private List<SBN001D> data;
    private EditText eSearch;
    private boolean boolSearch=true;
    private LayoutInflater inflater2;
    private final CharSequence[] items = { "Deshacer"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_lote);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Asignar por grupo");

        bSearch = (ImageButton) findViewById(R.id.search);
        eSearch = (EditText) findViewById(R.id.edit_search);

        filter = (ImageView)findViewById(R.id.filter);
        inflater2 = this.getLayoutInflater();

        data = SBN001D.getAllFiltered(0, "", "");
        adaptador =  new BnAdapter(this,data,1);
        lstOpciones = (ListView)findViewById(R.id.LstOpciones);
        lstOpciones.setAdapter(adaptador);

        filter.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {  BnFilterDialog dialog = new BnFilterDialog(AsignarLote.this);
                dialog.show(getFragmentManager(),"BnFilterDialog");
                dialog.setTargetFragment(dialog, 1);
            }
        });

        bSearch.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                if(boolSearch && !(eSearch.getText().toString().equals(""))){
                    bSearch.setImageResource(R.drawable.x);
                    boolSearch=false;
                }
                else{
                    bSearch.setImageResource(R.drawable.search);
                    eSearch.setText("");
                    boolSearch=true;
                }
            }
        });
        eSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bSearch.setImageResource(R.drawable.search);
                boolSearch=true;
            }
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
        });

        eSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    bSearch.setImageResource(R.drawable.x);
                    boolSearch=false;
                    return true;
                }
                return false;
            }
        });
        ckAll = (CheckBox) findViewById(R.id.ck_all);
        ckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(all) {
                    ckAll.setChecked(false);
                    SBN001D.setAllChecked(0);
                    SBN001D.setAllSelected(0);
                    all = false;
                    adaptador.notifyDataSetChanged();
                }
                else {
                    ckAll.setChecked(true);
                    SBN001D.setAllChecked(1);
                    SBN001D.setAllSelected(1);
                    all=true;
                    adaptador.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_asignar_lote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_aceptar) {
            SBN001D.setAllChecked(0);
            Intent intent1 = new Intent();
            setResult(RESULT_OK, intent1);
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
    public Boolean isAll() {
        return all;
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

            List<SBN001D> all=SBN001D.getAll();
            for (SBN001D aData : all){
                if(has(aData.numero,data)){
                    SBN001D inv;
                    inv= SBN001D.getBn(aData.numero);
                    inv.show=1;
                    inv.save();
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
        Intent intent1 = new Intent();
        setResult(RESULT_OK, intent1);
        finish();
    }
}
