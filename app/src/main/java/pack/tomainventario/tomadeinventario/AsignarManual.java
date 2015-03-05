package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import pack.tomainventario.tomadeinventario.Interfaces.Rpu;


public class AsignarManual extends Activity implements RpuDialog.NoticeDialogListener,AdapterView.OnItemSelectedListener{

    private SharedPreferences prefs;
    private ActionBar actionBar;
    private ArrayAdapter<SBN206D> adapEdo;
    private ArrayAdapter<SBN203D> adapStatus;
    private Spinner cmbEstado,cmbTipo;
    private SIP501V pUsuario;
    private int spinnerPosition,numeroBn;
    private EditText eCod, eNumero,eRpu,eSerial,eDescripcion,eStatus, eObservacion;
    private Button bConsultar, bEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_manual);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Asignación manual");

        prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);

        eCod = (EditText)findViewById(R.id.editCod);
        eDescripcion = (EditText)findViewById(R.id.editDesc);
        eStatus = (EditText)findViewById(R.id.editStatus);
        eRpu = (EditText)findViewById(R.id.editRPU);
        eObservacion = (EditText)findViewById(R.id.editObs);

        bEditar = (Button)findViewById(R.id.editar);
        bConsultar = (Button)findViewById(R.id.bConsultar);


        cmbTipo = (Spinner)findViewById(R.id.cmbTipo);

        cmbTipo.setOnItemSelectedListener(AsignarManual.this);

        adapEdo = new ArrayAdapter<SBN206D>(this,R.layout.layout_item_spinner);
        adapEdo.addAll(SBN206D.getAll());
        cmbEstado = (Spinner)findViewById(R.id.cmbEstado);
        cmbEstado.setAdapter(adapEdo);

        adapStatus = new ArrayAdapter<SBN203D>(this,R.layout.layout_item_spinner);
        adapStatus.addAll(SBN203D.getAll());



        bConsultar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                if(!eCod.getText().toString().equals("")) {
                    if(cmbTipo.getSelectedItem().toString().equals("#")){
                        if(SBN001D.exists(Integer.parseInt(eCod.getText().toString()))) {
                            if(SBN001D.isSelected(Integer.parseInt(eCod.getText().toString()))
                                    || SBN001D.isTaken(Integer.parseInt(eCod.getText().toString()))){
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

                                eDescripcion.setText(SBN001D.getBn(Integer.parseInt(eCod.getText().toString())).nombre);
                                eStatus.setText(SBN203D.getStatus(SBN001D.getBn(Integer.parseInt(eCod.getText().toString())).status).descripcion);
                                eRpu.setText(SIP501V.getPersonal(SBN001D.getBn(Integer.parseInt(eCod.getText().toString())).pUsuario).nombre);
                                spinnerPosition = adapEdo.getPosition(SBN206D.getEdoDB(SBN001D.getBn(Integer.parseInt(eCod.getText().toString())).edoFis));
                                cmbEstado.setSelection(spinnerPosition);
                                pUsuario=SIP501V.getPersonal(SBN001D.getBn(Integer.parseInt(eCod.getText().toString())).pUsuario);
                                numeroBn=SBN001D.getBn(Integer.parseInt(eCod.getText().toString())).numero;
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
                    else {
                        if(SBN001D.existsSerial(eCod.getText().toString())) {
                            if(SBN001D.isSelected(SBN001D.getSerial(eCod.getText().toString()).numero)
                                    || SBN001D.isTaken(SBN001D.getSerial(eCod.getText().toString()).numero) ){
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

                                eDescripcion.setText(SBN001D.getSerial(eCod.getText().toString()).nombre);
                                eStatus.setText(SBN203D.getStatus(SBN001D.getSerial(eCod.getText().toString()).status).descripcion);
                                eRpu.setText(SIP501V.getPersonal(SBN001D.getSerial(eCod.getText().toString()).pUsuario).nombre);
                                cmbEstado.setPrompt(SBN206D.getEdo(SBN001D.getSerial(eCod.getText().toString()).edoFis));
                                spinnerPosition = adapEdo.getPosition(SBN206D.getEdoDB(SBN001D.getSerial(eCod.getText().toString()).edoFis));
                                cmbEstado.setSelection(spinnerPosition);
                                pUsuario=SIP501V.getPersonal(SBN001D.getBn(Integer.parseInt(eCod.getText().toString())).pUsuario);
                                numeroBn=SBN001D.getSerial(eCod.getText().toString()).numero;
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
                }
                else{
                    eDescripcion.setText("");
                    eStatus.setText("");
                    eRpu.setText("");
                    eObservacion.setText("");
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
            SBN001D bN = SBN001D.getBn(numeroBn);
            bN.selected = 1;
            bN.edoFis=adapEdo.getItem(safeLongToInt(cmbEstado.getSelectedItemId())).codEdo;
            bN.pUsuario=pUsuario.ficha;
            bN.save();
            SBN052D historialRpu=new SBN052D(SBN001D.getBn(Integer.parseInt(eCod.getText().toString())).numero,
                    fechaActual(),pUsuario.ficha,prefs.getInt("Activar", 0), SBN053D.getAll().get(0).idInventarioActivo);
            historialRpu.save();
            Intent intent = new Intent();
           // Bundle bundle = new Bundle();
            intent.putExtra("observacion",eObservacion.getText().toString());
            intent.putExtra("numero",numeroBn);
            //intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        else if (id == android.R.id.home){
            Intent intent1 = new Intent();
            setResult(RESULT_OK, intent1);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0) eCod.setInputType(InputType.TYPE_CLASS_NUMBER);
        else eCod.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
