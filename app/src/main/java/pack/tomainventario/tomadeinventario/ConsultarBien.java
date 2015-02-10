package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import pack.tomainventario.tomadeinventario.Config.BaseDrawer;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SBN203D;
import pack.tomainventario.tomadeinventario.DataBase.SBN206D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.DataBase.SIP517V;
import pack.tomainventario.tomadeinventario.DataBase.SIP528V;


public class ConsultarBien extends BaseDrawer implements AdapterView.OnItemSelectedListener {
    private ActionBar actionBar;
    private Button bConsultar;
    private Spinner cmbTipo;
    private LinearLayout layout;
    private EditText eCod, eNumero, eDescripcion, eSede, eUbic, eEdo, eRpp, eRpu, eSerial,eStatus, eUnidad;
    private int numero;
    private String serial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ------------------- Configuracion - inicio
        RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_consultar_bien, null,false);
        rLayout.addView(activityView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        navAdapter.setActual(1);
        actionBar= getActionBar();
        actionBar.setTitle("Consultar Bien Nacional");
        // ------------------- Configuracion - inicio
        bConsultar = (Button)findViewById(R.id.bConsultar);
        eCod = (EditText)findViewById(R.id.editCod);
        eNumero = (EditText)findViewById(R.id.editNum);
        eDescripcion = (EditText)findViewById(R.id.editDesc);
        eSede = (EditText)findViewById(R.id.editSede);
        eUbic = (EditText)findViewById(R.id.editUbic);
        eEdo = (EditText)findViewById(R.id.editEdo);
        eRpp = (EditText)findViewById(R.id.editRpp1);
        eRpu = (EditText)findViewById(R.id.editRpu1);
        eSerial = (EditText)findViewById(R.id.editSerial);
        eStatus = (EditText)findViewById(R.id.editStatus);
        eUnidad = (EditText)findViewById(R.id.editUni1);
        cmbTipo = (Spinner)findViewById(R.id.cmbTipo);

        cmbTipo.setOnItemSelectedListener(ConsultarBien.this);

        bConsultar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                if(!eCod.getText().toString().equals("")) {
                    if (cmbTipo.getSelectedItem().toString().equals("#")) {
                        if (SBN001D.exists(Integer.parseInt(eCod.getText().toString()))) {
                            numero = Integer.parseInt(eCod.getText().toString());
                            eNumero.setText("" + SBN001D.getBn(numero).numero);
                            eDescripcion.setText(SBN001D.getBn(numero).nombre);
                            eSede.setText(SIP517V.getSede(SBN001D.getBn(numero).codSede).desUbic);
                            eUbic.setText(SBN010D.getUbicacion(SBN001D.getBn(numero).codUbic));
                            eEdo.setText(SBN206D.getEdoDB(SBN001D.getBn(numero).edoFis).descripcion);
                            eRpp.setText(SIP501V.getPersonal(SBN001D.getBn(numero).numFicha).nombre);
                            eRpu.setText(SIP501V.getPersonal(SBN001D.getBn(numero).pUsuario).nombre);
                            eSerial.setText(SBN001D.getBn(numero).serial);
                            eStatus.setText(SBN203D.getStatus(SBN001D.getBn(numero).status).descripcion);
                            eUnidad.setText(SIP528V.getUnidad(SBN001D.getBn(numero).codUnidad).desUejec);
                        } else {
                            eNumero.setText("");
                            eDescripcion.setText("");
                            eSede.setText("");
                            eUbic.setText("");
                            eEdo.setText("");
                            eRpp.setText("");
                            eRpu.setText("");
                            eSerial.setText("");
                            eStatus.setText("");
                            eUnidad.setText("");
                            Toast.makeText(getBaseContext(), "Numero de Bien Nacional INVÁLIDO", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        if (SBN001D.existsSerial(eCod.getText().toString())) {
                            serial = eCod.getText().toString();
                            eNumero.setText("" + SBN001D.getSerial(eCod.getText().toString()).numero);
                            eDescripcion.setText(SBN001D.getSerial(serial).nombre);
                            eSede.setText(SIP517V.getSede(SBN001D.getSerial(serial).codSede).desUbic);
                            eUbic.setText(SBN010D.getUbicacion(SBN001D.getSerial(serial).codUbic));
                            eEdo.setText(SBN206D.getEdoDB(SBN001D.getSerial(serial).edoFis).descripcion);
                            eRpp.setText(SIP501V.getPersonal(SBN001D.getSerial(serial).numFicha).nombre);
                            eRpu.setText(SIP501V.getPersonal(SBN001D.getSerial(serial).pUsuario).nombre);
                            eSerial.setText(SBN001D.getSerial(serial).serial);
                            eStatus.setText(SBN203D.getStatus(SBN001D.getSerial(serial).status).descripcion);
                            eUnidad.setText(SIP528V.getUnidad(SBN001D.getSerial(serial).codUnidad).desUejec);
                        } else {
                            eNumero.setText("");
                            eDescripcion.setText("");
                            eSede.setText("");
                            eUbic.setText("");
                            eEdo.setText("");
                            eRpp.setText("");
                            eRpu.setText("");
                            eSerial.setText("");
                            eStatus.setText("");
                            eUnidad.setText("");
                            Toast.makeText(getBaseContext(), "Numero de serial INVÁLIDO", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else{
                    eNumero.setText("");
                    eDescripcion.setText("");
                    eSede.setText("");
                    eUbic.setText("");
                    eEdo.setText("");
                    eRpp.setText("");
                    eRpu.setText("");
                    eSerial.setText("");
                    eStatus.setText("");
                    eUnidad.setText("");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_consultar_bien, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
