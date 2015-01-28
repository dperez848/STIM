package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN052D;
import pack.tomainventario.tomadeinventario.DataBase.SBN053D;
import pack.tomainventario.tomadeinventario.DataBase.SBN203D;
import pack.tomainventario.tomadeinventario.DataBase.SBN206D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.Dialogs.RpuDialog;


public class AsignarManual extends Activity implements RpuDialog.NoticeDialogListener{

    private static final String LOGTAG = "INFORMACION";
    private SharedPreferences prefs;
    private ActionBar actionBar;
    private ArrayAdapter<SBN206D> adapEdo;
    private ArrayAdapter<SBN203D> adapStatus;
    private Spinner cmbEstado;
    private SIP501V pUsuario;
    private int spinnerPosition;
    private EditText eNumero,eRpu,eSerial,eDescripcion,eStatus, eObservacion;
    private Button bEditar, bBn, bSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_manual);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Asignación manual");

        prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);

        eNumero = (EditText)findViewById(R.id.editNum);
        eDescripcion = (EditText)findViewById(R.id.editDesc);
        eSerial = (EditText)findViewById(R.id.editSerial);
        eStatus = (EditText)findViewById(R.id.editStatus);
        eRpu = (EditText)findViewById(R.id.editRPU);
        eObservacion = (EditText)findViewById(R.id.editObs);
        bEditar = (Button)findViewById(R.id.editar);
        bBn = (Button)findViewById(R.id.buscarBN);
        bSerial = (Button)findViewById(R.id.buscarSR);



        adapEdo = new ArrayAdapter<SBN206D>(this,android.R.layout.simple_spinner_item);
        adapEdo.addAll(SBN206D.getAll());
        cmbEstado = (Spinner)findViewById(R.id.cmbEstado);
        cmbEstado.setAdapter(adapEdo);

        adapStatus = new ArrayAdapter<SBN203D>(this,android.R.layout.simple_spinner_item);
        adapStatus.addAll(SBN203D.getAll());


        bBn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                if(SBN001D.exists(Integer.parseInt(eNumero.getText().toString()))) {
                    if(SBN001D.isSelected(Integer.parseInt(eNumero.getText().toString()))
                            || SBN001D.isTaken(Integer.parseInt(eNumero.getText().toString()))){
                        DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AsignarManual.this);
                        builder2.setMessage("Bien Nacional repetido").setPositiveButton("Aceptar", dialogClickListener2).show();
                    }else {
                        eNumero.setFocusable(true);
                        eSerial.setFocusable(false);
                        bBn.setEnabled(true);
                        bSerial.setEnabled(false);
                        eSerial.setText(SBN001D.getBn(Integer.parseInt(eNumero.getText().toString())).serial);
                        eDescripcion.setText(SBN001D.getBn(Integer.parseInt(eNumero.getText().toString())).nombre);
                        eStatus.setText(SBN203D.getStatus(SBN001D.getBn(Integer.parseInt(eNumero.getText().toString())).status).descripcion);
                        eRpu.setText(SIP501V.getPersonal(SBN001D.getBn(Integer.parseInt(eNumero.getText().toString())).pUsuario).nombre);
                        spinnerPosition = adapEdo.getPosition(SBN206D.getEdoDB(SBN001D.getBn(Integer.parseInt(eNumero.getText().toString())).edoFis));
                        cmbEstado.setSelection(spinnerPosition);
                        pUsuario=SIP501V.getPersonal(SBN001D.getBn(Integer.parseInt(eNumero.getText().toString())).pUsuario);
                    }
                }else{
                    DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(AsignarManual.this);
                    builder2.setMessage("Numero de Bien incorrecto").setPositiveButton("Aceptar", dialogClickListener2).show();
                }
            }
        });
        bSerial.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                if(SBN001D.exists(SBN001D.getSerial(eSerial.getText().toString()).numero)) {
                    if(SBN001D.isSelected(SBN001D.getSerial(eSerial.getText().toString()).numero)
                            || SBN001D.isTaken(SBN001D.getSerial(eSerial.getText().toString()).numero) ){
                        DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AsignarManual.this);
                        builder2.setMessage("Bien Nacional repetido").setPositiveButton("Aceptar", dialogClickListener2).show();
                    }else {
                        eSerial.setFocusable(true);
                        eNumero.setFocusable(false);
                        bBn.setEnabled(false);
                        bSerial.setEnabled(true);
                        eNumero.setText("" +SBN001D.getSerial(eSerial.getText().toString()).numero);
                        eDescripcion.setText(SBN001D.getSerial(eSerial.getText().toString()).nombre);
                        eStatus.setText(SBN203D.getStatus(SBN001D.getSerial(eSerial.getText().toString()).status).descripcion);
                        eRpu.setText(SIP501V.getPersonal(SBN001D.getSerial(eSerial.getText().toString()).pUsuario).nombre);
                        cmbEstado.setPrompt(SBN206D.getEdo(SBN001D.getSerial(eSerial.getText().toString()).edoFis));
                        spinnerPosition = adapEdo.getPosition(SBN206D.getEdoDB(SBN001D.getSerial(eSerial.getText().toString()).edoFis));
                        cmbEstado.setSelection(spinnerPosition);
                        pUsuario=SIP501V.getPersonal(SBN001D.getBn(Integer.parseInt(eSerial.getText().toString())).pUsuario);
                    }
                }else{
                    DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(AsignarManual.this);
                    builder2.setMessage("Numero de Bien incorrecto").setPositiveButton("Aceptar", dialogClickListener2).show();
                }
            }
        });
        bEditar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                RpuDialog dialog = new RpuDialog(AsignarManual.this);
                dialog.show(getFragmentManager(),"RpuDialog");
                dialog.setTargetFragment(dialog, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_asignar_manual, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_aceptar) {
            SBN001D bN = SBN001D.getBn(Integer.parseInt(eNumero.getText().toString()));
            bN.selected = 1;
            bN.edoFis=adapEdo.getItem(safeLongToInt(cmbEstado.getSelectedItemId())).codEdo;
            bN.pUsuario=pUsuario.ficha;
            bN.save();
            SBN052D historialRpu=new SBN052D(SBN001D.getBn(Integer.parseInt(eNumero.getText().toString())).numero,
                    fechaActual(),pUsuario.ficha,prefs.getInt("Activar", 0), SBN053D.getAll().get(0).idInventarioActivo);
            historialRpu.save();
            Intent intent = new Intent();
           // Bundle bundle = new Bundle();
            intent.putExtra("observacion",eObservacion.getText().toString());
            intent.putExtra("numero",Integer.parseInt(eNumero.getText().toString()));
            //intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent();
        setResult(RESULT_OK, intent1);
        finish();
    }
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    @Override
    public void onDialogItemClick(SIP501V rpu, int num) {
        this.pUsuario=rpu;
        eRpu.setText(rpu.nombre);
    }

    public String fechaActual(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c.getTime());
    }
}
