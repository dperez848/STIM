package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pack.tomainventario.tomadeinventario.Models.SBN051D;
import pack.tomainventario.tomadeinventario.Models.SBN053D;
import pack.tomainventario.tomadeinventario.Models.SBN090D;


public class Login extends Activity {
    private Button bIngresar;
    private EditText eUser,ePassword;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;
    private Bundle bundle;
    private Boolean formatear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
        edit = prefs.edit();

        bIngresar = (Button) findViewById(R.id.btn1);
        eUser = (EditText) findViewById(R.id.user);
        ePassword = (EditText) findViewById(R.id.password);

        //--- Configuraciones- Inicio
        ePassword.setTypeface(Typeface.DEFAULT);
        ePassword.setTransformationMethod(new PasswordTransformationMethod());
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        //--- Configuraciones- Fin

        bundle = getIntent().getExtras();
        if (bundle != null) {
            formatear = bundle.getBoolean("Formatear");


            if (formatear) {
                DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                File sd = Environment.getExternalStorageDirectory();

                                if (sd.canWrite()) {

                                    Context context = getApplicationContext();
                                    String pathDestino = "/data/data/"+ context.getPackageName()+ "/databases/";
                                    File pathOrigen = new File(Environment.getExternalStorageDirectory() + "/SistemaInventario/BD_nueva");
                                    File destino = new File(pathDestino, "TomaInventario.db");
                                    File origen = new File(pathOrigen, "TomaInventario.db");
                                    if (destino.exists()) {
                                        Log.e("EXISTE destino",": si");
                                        if(origen.exists()) {
                                            Log.e("EXISTE origen",": si");
                                            try {
                                                //copy(origen, destino);
                                                importDatabase(Environment.getExternalStorageDirectory() + "/SistemaInventario/BD_nueva/TomaInventario.db");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            edit.putInt("Formatear", 0);
                                            edit.putInt("Activar", 0);

                                        }
                                        else Log.e("EXISTE origen",": no");
                                    }
                                    else Log.e("EXISTE destino",": no");
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                edit.putInt("Formatear", 0);

                                break;
                            case DialogInterface.BUTTON_NEUTRAL:
                                onBackPressed();
                                break;
                        }
                        edit.apply();
                    }
                };
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("Si desea empezar un nuevo inventario es muy importante recordar actualizar la base de datos." +
                        " Si desea seguir con el inventario actual seleccione \" Actual\"").setPositiveButton("Nuevo", dialogClickListener2)
                        .setNegativeButton("Actual", dialogClickListener2).setNeutralButton("Cancelar",dialogClickListener2).setCancelable(false).show();
            }
        }

        bIngresar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Log.e("AAA", "el user 1 de 090 es " + SBN090D.getAll().get(0).user +" y su clave es "+SBN090D.getAll().get(0).passwd);
                Log.e("AAA", "el tamaño de la 51 es " + SBN051D.getAll().size());
                if(SBN090D.getLog(eUser.getText(),ePassword.getText()) == null)
                {
                    Toast.makeText(getApplicationContext(),"Usuario o clave incorrecto",Toast.LENGTH_LONG).show();
                    eUser.setText("");
                    ePassword.setText("");
                }
                else
                {
                    if(SBN090D.getUser(eUser.getText()).ficha.equals(SBN053D.getAll().get(0).fichaUa)){
                        edit.putInt("Login", 2);
                    }
                    else{
                        edit.putInt("Login", 1);
                        Toast.makeText(getApplicationContext(),"Usuario invitado",Toast.LENGTH_LONG).show();
                    }
                    edit.apply();
                    Intent intent = new Intent(Login.this, ActivacionPorSede.class);
                    intent.putExtra("login",eUser.getText());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)return true;
        return super.onOptionsItemSelected(item);
    }


    private void importDatabase(String inputFileName) throws IOException
    {Context context = getApplicationContext();
        InputStream mInput = new FileInputStream(inputFileName);
        String outFileName = "/data/data/"+ context.getPackageName()+ "/databases/TomaInventario.db";
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
        Toast.makeText(getBaseContext(), "Nuevo inventario activado", Toast.LENGTH_LONG).show();
    }
}