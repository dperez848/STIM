package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SBN050D;
import pack.tomainventario.tomadeinventario.DataBase.SBN053D;
import pack.tomainventario.tomadeinventario.DataBase.SBN203D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.DataBase.SIP517V;
import pack.tomainventario.tomadeinventario.DataBase.SIP528V;
import pack.tomainventario.tomadeinventario.Dialogs.RpuDialog;
import pack.tomainventario.tomadeinventario.Interfaces.Rpu;


public class ActivacionInventario extends FragmentActivity implements Rpu {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;
    private EditText editFecha, editRpp, editSede, editNum;
    private ActionBar actionBar;
    private SIP501V pUsuario;
    private ArrayAdapter<SIP528V> adapUCosto;
    private ArrayAdapter<SBN010D> adapUbic;
    private ArrayAdapter<SBN203D> adapStatus;
    private Spinner cmbCosto,cmbUbic,cmbStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activacion_inventario);
        actionBar= getActionBar();
        actionBar.setTitle("Activación de Inventario");

        prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
        edit = prefs.edit();
        edit.putInt("Act", 1);
        edit.putInt("Activar",prefs.getInt("Activar", 0)+1);
        edit.apply();

        editNum = (EditText)findViewById(R.id.editNum);
        editFecha = (EditText)findViewById(R.id.editFecha);
        editFecha.setText(fechaActual());
        editRpp = (EditText)findViewById(R.id.editRPP);
        editSede = (EditText)findViewById(R.id.editSede);

        editSede.setText(SIP517V.getSede(SBN053D.getAll().get(0).sede).desUbic);

        adapUCosto = new ArrayAdapter<SIP528V>(this,R.layout.layout_item_spinner);
        adapUCosto.addAll(SIP528V.getAll());
        cmbCosto = (Spinner)findViewById(R.id.cmbCosto);
        cmbCosto.setAdapter(adapUCosto);

        adapUbic = new ArrayAdapter<SBN010D>(this,R.layout.layout_item_spinner);
        adapUbic.addAll(SBN010D.getAllShow());
        cmbUbic = (Spinner)findViewById(R.id.cmbUbic);
        cmbUbic.setAdapter(adapUbic);

        adapStatus = new ArrayAdapter<SBN203D>(this,R.layout.layout_item_spinner);
        adapStatus.addAll(SBN203D.getAll());
        cmbStatus = (Spinner)findViewById(R.id.cmbStatus);
        cmbStatus.setAdapter(adapStatus);

        editNum.setText(""+prefs.getInt("Activar", 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activacion_inventario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_aceptar) {


            SBN050D inventario= new SBN050D();
            inventario.idInventario= prefs.getInt("Activar", 0);
            inventario.idInventarioActivo= SBN053D.getAll().get(0).idInventarioActivo;
            inventario.fechaInventario= fechaActual();
            inventario.codUnidad= adapUCosto.getItem(safeLongToInt(cmbCosto.getSelectedItemId())).codUejec;
            inventario.codUbic= adapUbic.getItem(safeLongToInt(cmbUbic.getSelectedItemId())).codUbic;
            inventario.fechaUa= fechaActual();
            inventario.status= 1;
            inventario.save();

            prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
            edit.putInt("Act", 1);
            edit.apply();

            Intent intent = new Intent(ActivacionInventario.this, NuevaToma.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivityForResult(intent, 1009);


            Intent intent2 = new Intent();
            setResult(RESULT_OK, intent2);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void rpuDialog(View v){
        RpuDialog dialog = new RpuDialog(ActivacionInventario.this);
        dialog.show(getFragmentManager(),"RpuDialog");
        dialog.setTargetFragment(dialog, 1);
    }

    public String fechaActual(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c.getTime());
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1009:
                    break;
            }
        }
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
    @Override
    public void onBackPressed() {
        edit.putInt("Activar", prefs.getInt("Activar",0)-1);
        edit.apply();
        Intent intent2 = new Intent();
        setResult(RESULT_OK, intent2);
        finish();
    }

    @Override
    public void openRpuDialog(int bn) {

    }

    @Override
    public void onRpuItemClick(SIP501V rpu) {
        this.pUsuario=rpu;
        editRpp.setText(rpu.nombre);
    }
}
