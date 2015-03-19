package pack.tomainventario.tomadeinventario.Config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;

import pack.tomainventario.tomadeinventario.Login;
import pack.tomainventario.tomadeinventario.MainActivity;
import pack.tomainventario.tomadeinventario.Models.SBN001D;
import pack.tomainventario.tomadeinventario.Models.SBN010D;
import pack.tomainventario.tomadeinventario.Models.SBN053D;
import pack.tomainventario.tomadeinventario.Models.SBN054D;
import pack.tomainventario.tomadeinventario.Models.SBN090D;
import pack.tomainventario.tomadeinventario.Models.SBN203D;
import pack.tomainventario.tomadeinventario.Models.SBN206D;
import pack.tomainventario.tomadeinventario.Models.SIP501V;
import pack.tomainventario.tomadeinventario.Models.SIP517V;
import pack.tomainventario.tomadeinventario.Models.SIP528V;
import pack.tomainventario.tomadeinventario.NuevaToma;
import pack.tomainventario.tomadeinventario.R;


public class Splash extends Activity {
    private SBN053D inventarioActivo;
    private SBN054D fotoBN;
    private SBN001D bN;
    private SBN090D user;
    private SBN203D status;
    private SBN206D estado;
    private SharedPreferences.Editor edit;
    private SIP517V sede;
    private SIP501V rpu;
    private SIP528V uEjec;
    private SBN010D ubic;
    private String android_id ;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private SharedPreferences prefs;
    private  Bundle bundle;
    private Boolean cont=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        android_id = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
        bundle = new Bundle();
        edit = prefs.edit();

        File dest = new File(Environment.getExternalStorageDirectory() + "/SistemaInventario/BD_nueva");
        File org = new File(dest, "TomaInventario.db");
        if(!org.exists()) {
            Toast.makeText(getBaseContext(), "Archivo de base de datos no encontrado", Toast.LENGTH_LONG).show();

            edit.putBoolean("Cont", false);
            finish();
        }
        else {
            if (prefs.getInt("First", 0) != 1) { //Si es la primera vez q abre la app

                File sd = Environment.getExternalStorageDirectory();

                if (sd.canWrite()) {

                    Context context = getApplicationContext();
                    String pathDestino = "/data/data/" + context.getPackageName() + "/databases/";
                    File pathOrigen = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SistemaInventario/BD_nueva");
                    File destino = new File(pathDestino, "TomaInventario.db");
                    File origen = new File(pathOrigen, "TomaInventario.db");
                    if (destino.exists()) {
                        Log.e("EXISTE destino", ": si");
                        if (origen.exists()) {
                            Log.e("EXISTE origen", ": si");
                            try {
                                //copy(origen, destino);
                                // importDatabase(Environment.getExternalStorageDirectory() + "/SistemaInventario/BD_nueva/TomaInventario.db");
                                copyFile(origen, destino);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            edit.putInt("Formatear", 0);
                            edit.putInt("Activar", 0);

                        } else {
                            Log.e("EXISTE origen", ": no");

                            //System.exit(0);
                        }
                    } else Log.e("EXISTE destino", ": no");
                }

                edit.putInt("First", 1);
                edit.putInt("Activar", 0);
                edit.apply();
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (prefs.getInt("Login", 0) == 0) {
                        if (prefs.getInt("Formatear", 0) == 1) {
                            bundle.putBoolean("Formatear", true);
                        } else {
                            bundle.putBoolean("Formatear", false);
                        }
                        Intent intent = new Intent(Splash.this, Login.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else {

                        if (prefs.getInt("Act", 0) != 1) {

                            Intent intent = new Intent(Splash.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Toast.makeText(getBaseContext(), ""+act, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Splash.this, NuevaToma.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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
    public void onBackPressed() {
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

    public static boolean copyFile(File src, File dst) {
        boolean returnValue = true;

        FileChannel inChannel = null, outChannel = null;

        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dst).getChannel();
        }
        catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return false;
        }

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }
        catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            returnValue = false;
        }
        catch (NonReadableChannelException nrce) {
            nrce.printStackTrace();
            returnValue = false;
        }
        catch (NonWritableChannelException nwce) {
            nwce.printStackTrace();
            returnValue = false;
        }
        catch (ClosedByInterruptException cie) {
            cie.printStackTrace();
            returnValue = false;
        }
        catch (AsynchronousCloseException ace) {
            ace.printStackTrace();
            returnValue = false;
        }
        catch (ClosedChannelException cce) {
            cce.printStackTrace();
            returnValue = false;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            returnValue = false;
        }
        finally {
            if (inChannel != null)
                try {
                    inChannel.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            if (outChannel != null)
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return returnValue;
    }
}