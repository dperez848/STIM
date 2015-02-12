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
import java.nio.channels.FileChannel;

import pack.tomainventario.tomadeinventario.DataBase.SBN051D;
import pack.tomainventario.tomadeinventario.DataBase.SBN053D;
import pack.tomainventario.tomadeinventario.DataBase.SBN090D;


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

                            try {
                                File sd = Environment.getExternalStorageDirectory();
                                File data = Environment.getDataDirectory();

                                if (sd.canWrite()) {
                                    String currentDBPath = "/data/data/" + getPackageName() + "/databases/TomaInventario.db";
                                    String backupDBPath = "/SistemaInventario/BD_nueva/TomaInventario.db";
                                    File currentDB = new File(data, currentDBPath);
                                    File backupDB = new File(sd, backupDBPath);

                                    if (currentDB.exists()) {
                                        FileChannel src = new FileInputStream(backupDB).getChannel();
                                        FileChannel dst = new FileOutputStream(currentDB).getChannel();
                                        dst.transferFrom(src, 0, src.size());
                                        src.close();
                                        dst.close();
                                        edit.putInt("Formatear", 0);
                                        edit.putInt("Activar", 0);
                                        Toast.makeText(getBaseContext(), "Nuevo inventario activado", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
                Log.e("AAA", "el user 1 de 090 es " + SBN090D.getAll().get(0).user);
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
                    Intent intent = new Intent(Login.this, MainActivity.class);
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
    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
