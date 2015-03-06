package pack.tomainventario.tomadeinventario;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SBN050D;
import pack.tomainventario.tomadeinventario.DataBase.SBN051D;
import pack.tomainventario.tomadeinventario.DataBase.SBN052D;
import pack.tomainventario.tomadeinventario.DataBase.SBN053D;
import pack.tomainventario.tomadeinventario.DataBase.SBN054D;
import pack.tomainventario.tomadeinventario.DataBase.SBN203D;
import pack.tomainventario.tomadeinventario.DataBase.SBN206D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.DataBase.SIP517V;
import pack.tomainventario.tomadeinventario.Dialogs.RpuDialog;
import pack.tomainventario.tomadeinventario.Dialogs.TomaFilterDialog;
import pack.tomainventario.tomadeinventario.Interfaces.Observacion;


public class DetalleToma extends FragmentActivity implements RpuDialog.NoticeDialogListener,Observacion {
    private int numeroBn;
    private String descripcion,fecha,foto,sede,ubic,rpu,observacion,edo,status;
    private EditText editNum,editDescripcion,editFecha,editUbic,editRpu,editObservacion,editEdo, editStatus;
    private ImageView iFoto;
    private Button  btnRpu,btnObs,btnFoto;
    private SIP501V pUsuario;
    private byte[] imageAsBytes;
    private SBN051D inventariado;
    private static final String LOGTAG = "INFORMACION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_toma);

        // ------------------- Obetiendo informacion de Main
        Bundle bundle = getIntent().getExtras();
        numeroBn = bundle.getInt("numero");
        inventariado = SBN051D.getBn(numeroBn);

        descripcion = SBN001D.getDescripcion(inventariado.numeroBn);
        fecha = inventariado.fecha;
        foto = SBN054D.getFoto(inventariado.numeroBn);
        sede = SIP517V.getSede(SBN053D.getAll().get(0).sede).desUbic;
        ubic = SBN010D.getUbicacion(SBN050D.getInv(SBN051D.getBn(inventariado.numeroBn).idInventario).codUbic);
        edo = SBN206D.getEdo(SBN001D.getBn(numeroBn).edoFis);
        rpu = SIP501V.getPersonal(SBN001D.getBn(inventariado.numeroBn).pUsuario).nombre;
        observacion = SBN051D.getObservacion(SBN051D.getBn(numeroBn));
        status = SBN203D.getStatus(SBN001D.getBn(numeroBn).status).descripcion;

        // ------------------- Obetiendo informacion de Main

        editNum = (EditText)findViewById(R.id.editNum);
        editDescripcion = (EditText)findViewById(R.id.editDesc);
        editFecha = (EditText)findViewById(R.id.editFecha);
        editUbic = (EditText)findViewById(R.id.editUbic);
        editRpu = (EditText)findViewById(R.id.editRPU);
        editObservacion = (EditText)findViewById(R.id.editObs);
        editEdo = (EditText)findViewById(R.id.editEdo);
        editStatus = (EditText)findViewById(R.id.edStatus);
        iFoto = (ImageView)findViewById(R.id.foto);
        btnObs = (Button)findViewById(R.id.btnObs);
        btnRpu = (Button)findViewById(R.id.btnRpu);
        btnFoto = (Button)findViewById(R.id.btnFoto);

        editNum.setText(""+numeroBn);
        editDescripcion.setText(descripcion);
        editFecha.setText(fecha);
        editUbic.setText(ubic);
        editRpu.setText(rpu);
        editObservacion.setText(observacion);
        editEdo.setText(edo);
        editStatus.setText(status);
        if(!foto.equals("")) {
            imageAsBytes = Base64.decode(foto.getBytes(), Base64.DEFAULT);
            iFoto.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            iFoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else{
            iFoto.setImageResource(R.drawable.noimagen);
        }
        btnRpu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                RpuDialog dialog = new RpuDialog(DetalleToma.this,numeroBn);
                dialog.show(getFragmentManager(),"RpuDialog");
                dialog.setTargetFragment(dialog, 1);
            }
        });

        btnObs.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                TomaFilterDialog dialog = new TomaFilterDialog(numeroBn,DetalleToma.this,observacion,SBN001D.getBn(numeroBn).edoFis);
                dialog.show(getFragmentManager(), "TomaFilterDialog");
                dialog.setTargetFragment(dialog, 1);
            }
        });
        btnFoto.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent intent = new Intent(DetalleToma.this, DetalleGaleria.class);
                Bundle bundle = new Bundle();
                bundle.putInt("numero", numeroBn);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_toma, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_aceptar) {
            /*Intent intent = new Intent(DetalleToma.this, MainActivity.class);
            startActivity(intent);
            finish();*/
            Intent intent2 = new Intent();
            setResult(RESULT_OK, intent2);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogItemClick(final SIP501V rpu,int num) {
        SBN001D bN= SBN001D.getBn(numeroBn);
        bN.pUsuario=rpu.ficha;
        bN.save();
        SBN052D historialRpu=new SBN052D(numeroBn, fechaActual(),rpu.ficha,
                SBN052D.getInventario(numeroBn).idInventario, SBN053D.getAll().get(0).idInventarioActivo);
        historialRpu.save();
                                /*for (SBN052D aData : SBN052D.getAll()){
                            Log.e("TAAASSSSSG", "Los selected son " + aData.numeroBn);
                        }*/
        pUsuario=rpu;
        editRpu.setText(rpu.nombre);

    }


    @Override
    public void putObservacion(String iobservacion, int bn, int edo) {
        SBN051D bien= SBN051D.getBn(bn);
        bien.observacion= iobservacion;
        bien.save();
        SBN001D bien2= SBN001D.getBn(bn);
        bien2.edoFis= edo;
        bien2.save();
        editEdo.setText(SBN206D.getEdo(edo));
        editObservacion.setText(iobservacion);
    }

    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent();
        setResult(RESULT_OK, intent2);
        finish();
    }

    public String fechaActual(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c.getTime());
    }



}
