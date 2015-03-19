package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.UbicacionesAdapter;
import pack.tomainventario.tomadeinventario.Interfaces.Selected;
import pack.tomainventario.tomadeinventario.Models.SBN010D;
import pack.tomainventario.tomadeinventario.Models.SBN090D;
import pack.tomainventario.tomadeinventario.Models.SIP517V;


public class ActivacionPorSede extends Activity implements Selected {

    private Button bIngresar;
    private List<SBN010D> data= new ArrayList<SBN010D>();
    private ArrayAdapter<SIP517V> adapSede;
    private UbicacionesAdapter adaptador;
    private ListView lstOpciones;
    private Spinner cmbSede;
    private EditText eFecha,eRPP;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activacion_por_sede);

        actionBar= getActionBar();
        actionBar.setTitle("Activación");

        eFecha = (EditText) findViewById(R.id.editFecha);
        eFecha.setText(fechaActual());
        eRPP = (EditText) findViewById(R.id.editRPP);

        Bundle extras = getIntent().getExtras();
        if(extras != null) eRPP.setText(SBN090D.getUser(extras.get("login")).nombre);


        adapSede = new ArrayAdapter<SIP517V>(this,R.layout.layout_item_spinner);
        adapSede.addAll(SIP517V.getAll());
        cmbSede = (Spinner)findViewById(R.id.cmbSede);
        cmbSede.setAdapter(adapSede);
        cmbSede.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                data = SBN010D.getUbicOfSede(adapSede.getItem(safeLongToInt(cmbSede.getSelectedItemId())).codSede);
                adaptador.updateAdapter(data);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        adaptador =  new UbicacionesAdapter(this,data);
        lstOpciones = (ListView)findViewById(R.id.listUbic);
        lstOpciones.setAdapter(adaptador);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activacion_por_sede, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_aceptar) {



            Intent intent = new Intent(ActivacionPorSede.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void deshacer(int pos) {

    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    public String fechaActual(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c.getTime());
    }
}
