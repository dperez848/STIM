package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.BnAdapter;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SBN050D;
import pack.tomainventario.tomadeinventario.DataBase.SBN051D;
import pack.tomainventario.tomadeinventario.DataBase.SBN052D;
import pack.tomainventario.tomadeinventario.DataBase.SBN053D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.Dialogs.RpuDialog;
import pack.tomainventario.tomadeinventario.Dialogs.TomaFilterDialog;
import pack.tomainventario.tomadeinventario.Interfaces.Configuracion;
import pack.tomainventario.tomadeinventario.Interfaces.IGaleria;
import pack.tomainventario.tomadeinventario.Interfaces.Observacion;
import pack.tomainventario.tomadeinventario.Interfaces.Rpu;
import pack.tomainventario.tomadeinventario.Interfaces.Selected;
import pack.tomainventario.tomadeinventario.Objects.ObservacionRpu;


public class NuevaToma extends Activity implements Selected,Rpu,RpuDialog.NoticeDialogListener, Configuracion,IGaleria,Observacion{
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;
    private BnAdapter adaptador;
    private List<SBN001D> data, info;
    private Button bLote,bManual;
    private int num,pos;
    private ListView lstOpciones;
    private ActionBar actionBar;
    private String observacion;
    private static final String LOGTAG = "INFORMACION";
    private List<ObservacionRpu> observacionList = new ArrayList<ObservacionRpu>();
    private ObservacionRpu dato;
    private final CharSequence[] items = { "Deshacer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_toma);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Nueva toma de inventario");
        bLote = (Button) findViewById(R.id.lote);
        bManual = (Button) findViewById(R.id.manual);
        prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);

        data = SBN001D.getSelected();

        adaptador =  new BnAdapter(this,data,2);
        lstOpciones = (ListView)findViewById(R.id.LstOpciones);
        lstOpciones.setAdapter(adaptador);

        bLote.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent intent = new Intent(NuevaToma.this, AsignarLote.class);
                startActivityForResult(intent,1005);
            }
        });

        bManual.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent intent = new Intent(NuevaToma.this, AsignarManual.class);
                startActivityForResult(intent,1006);
            }
        });
    }

    public void inventariar (List<SBN001D> data){
        for (int i = 0; i < data.size(); i++) {
            SBN001D bien = SBN001D.getBn(data.get(i).numero);
            SBN051D inv = new SBN051D(data.get(i).numero,prefs.getInt("Activar", 0),
                    SBN053D.getAll().get(0).idInventarioActivo, getObservacion(data.get(i).numero),fechaActual());
            inv.save();
        }
    }

    public String fechaActual(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c.getTime());
    }

    public String getObservacion(int bn){
        for (ObservacionRpu aData : observacionList)
            if(aData.getNumero()==bn)
                return aData.getObservacion();
        return "N/A";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Log.e("TAAAG", "entro a la "+requestCode);
            if (requestCode==1005 ||requestCode==1006) {

                if(requestCode==1006) {
                    observacion = data.getStringExtra("observacion");
                    num = data.getIntExtra("numero", 0);
                    dato = new ObservacionRpu();
                    dato.setObservacion(observacion);
                    dato.setNumero(num);
                    observacionList.add(dato);
                }
                info = SBN001D.getSelected();
                adaptador =  new BnAdapter(this,info,2);
                lstOpciones.setAdapter(adaptador);

                bLote.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View arg0)
                    {
                        Intent intent = new Intent(NuevaToma.this, AsignarLote.class);
                        startActivityForResult(intent,1005);
                    }
                });

                bManual.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View arg0)
                    {
                        Intent intent = new Intent(NuevaToma.this, AsignarManual.class);
                        startActivityForResult(intent,1006);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nueva_toma, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
                                edit = prefs.edit();
                                edit.putInt("Act", 0);
                                edit.putInt("Activar", prefs.getInt("Activar",0)-1);
                                edit.apply();
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
                builder1.setMessage("¿Desea abandonar?").setPositiveButton("Si", dialogClickListener1)
                        .setNegativeButton("No", dialogClickListener1).show();

                return true;

            case R.id.action_aceptar:
                DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
                                edit = prefs.edit();
                                edit.putInt("Act", 0);
                                edit.apply();
                                SBN001D.allSelectedToTaken();
                                inventariar(SBN001D.getTaken());
                                setShow(0,SBN001D.getTaken());
                                SBN001D.setAllSelected(0);
                                if(SBN001D.getTaken().isEmpty()){
                                    edit.putInt("Activar", prefs.getInt("Activar",0)-1);
                                    edit.apply();
                                }
                                SBN010D ubicActual = SBN010D.getUbic(SBN050D.getInv(prefs.getInt("Activar", 0)).codUbic);
                                ubicActual.show=0;
                                ubicActual.save();
                                Intent intent1 = new Intent();
                                setResult(RESULT_OK, intent1);
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("¿Desea guardar?").setPositiveButton("Si", dialogClickListener2)
                        .setNegativeButton("No", dialogClickListener2).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setShow(int i, List<SBN001D> taken) {
        for (SBN001D aTaken : taken){
            SBN001D inv;
            inv= SBN001D.getBn(aTaken.numero);
            inv.show=i;
            inv.save();
        }
    }

    @Override
    public void configDialog(int num) {
        TomaFilterDialog dialog = new TomaFilterDialog(num,NuevaToma.this,getObservacion(num),SBN001D.getBn(num).edoFis);
        dialog.show(getFragmentManager(), "TomaFilterDialog");
        dialog.setTargetFragment(dialog, 1);
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
                        edit = prefs.edit();
                        edit.putInt("Act", 0);
                        edit.putInt("Activar", prefs.getInt("Activar",0)-1);
                        edit.apply();
                        SBN001D.setAllSelected(0);
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
        builder1.setMessage("¿Desea abandonar?").setPositiveButton("Si", dialogClickListener1)
                .setNegativeButton("No", dialogClickListener1).show();
    }

    @Override
    public void putObservacion(String observacion, int bn, int edo) {
        ObservacionRpu obs = new ObservacionRpu();
        obs.setNumero(bn);
        obs.setObservacion(observacion);
        observacionList.add(obs);
        SBN001D bien2= SBN001D.getBn(bn);
        bien2.edoFis= edo;
        bien2.save();
    }

    @Override
    public void detalle(int numero, int position) {
        Intent intent = new Intent(NuevaToma.this, DetalleGaleria.class);
        Bundle bundle = new Bundle();
        bundle.putInt("numero", numero);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onLong(int numero, int numero2) {

    }

    @Override
    public void onDialogItemClick(SIP501V rpu, int num) {
        SBN001D bN = SBN001D.getBn(num);
        bN.pUsuario= SIP501V.getPersonal(rpu.ficha).ficha;
        bN.save();
        SBN052D historialRpu=new SBN052D(num, fechaActual(),rpu.ficha,
                                prefs.getInt("Activar", 0),SBN053D.getAll().get(0).idInventarioActivo);
        historialRpu.save();
    }

    @Override
    public void openRpuDialog(int bn) {
        RpuDialog dialog = new RpuDialog(NuevaToma.this,bn);
        dialog.show(getFragmentManager(),"RpuDialog");
        dialog.setTargetFragment(dialog, 1);
    }

    @Override
    public void setCheck(Boolean value) {}
    @Override
    public Boolean isAll() {
        return null;
    }


    @Override
    public void deshacer( final int numeroBn) {

        Toast.makeText(getApplicationContext(), "me llegó numeroBn "+numeroBn, Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(NuevaToma.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(), "ONCLICK numeroBn "+numeroBn, Toast.LENGTH_LONG).show();
                SBN001D.setOneSelected(numeroBn);
                info = SBN001D.getSelected();
                adaptador.updateAdapter(info);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
