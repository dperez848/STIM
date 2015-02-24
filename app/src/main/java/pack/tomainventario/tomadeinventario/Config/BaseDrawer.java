package pack.tomainventario.tomadeinventario.Config;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import pack.tomainventario.tomadeinventario.Adapters.NavigationAdapter;
import pack.tomainventario.tomadeinventario.AjustarRPU;
import pack.tomainventario.tomadeinventario.ConsultarBien;
import pack.tomainventario.tomadeinventario.Galeria;
import pack.tomainventario.tomadeinventario.Login;
import pack.tomainventario.tomadeinventario.MainActivity;
import pack.tomainventario.tomadeinventario.Objects.Item_objct;
import pack.tomainventario.tomadeinventario.R;
import pack.tomainventario.tomadeinventario.Reportes;

public class BaseDrawer extends FragmentActivity {
    protected String[] titulos;
    protected DrawerLayout navDrawerLayout;
    protected ListView navList;
    protected ArrayList<Item_objct> navItms;
    protected TypedArray navIcons;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected NavigationAdapter navAdapter;
    private SharedPreferences.Editor edit;
    private CharSequence mTitle, mDrawerTitle;
    private SharedPreferences prefs;
    private  Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);



        prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);
        navDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navList = (ListView) findViewById(R.id.lista);
        navIcons = getResources().obtainTypedArray(R.array.navigation_iconos);
        titulos = getResources().getStringArray(R.array.nav_options);
        navItms = new ArrayList<Item_objct>();

        for ( int i = 0; i <= 6; i ++ ) {
            navItms.add(new Item_objct(titulos[i], navIcons.getResourceId(i, -1)));
        }

        navAdapter= new NavigationAdapter(this,navItms);
        navList.setAdapter(navAdapter);
        mTitle = mDrawerTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                navDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* Icono de navegacion*/
                R.string.app_name,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        navDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        navList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parent, View view,int position, long id) {
                switch (position) {
                    case 0: {
                        Intent intent = new Intent(BaseDrawer.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(BaseDrawer.this, ConsultarBien.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Log.e("LOG","El login es "+prefs.getInt("Login",0));
                        if(prefs.getInt("Login",0)==2) {
                            Intent intent = new Intent(BaseDrawer.this, AjustarRPU.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Invitado no puede ajustar RPU", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                    case 3: {
                        Intent intent = new Intent(BaseDrawer.this, Galeria.class);
                        startActivity(intent);
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent(BaseDrawer.this, Reportes.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    }
                    case 5: {   //Si finaliza inventario
                        if(prefs.getInt("Login",0)==2) {
                            DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            File directoryFile = new File(Environment.getExternalStorageDirectory() + "/SistemaInventario/BD_final");
                                            if (!directoryFile.exists()) {
                                                directoryFile.mkdirs();
                                            }
                                            File target = new File(directoryFile, "TomaInventario.db");
                                            if (!target.exists()) {
                                                try {
                                                    target.createNewFile();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Context context = getApplicationContext();
                                            String DB_PATH = "/data/data/"+ context.getPackageName()+ "/databases/";
                                            File origen = new File(DB_PATH, "TomaInventario.db");
                                            try {
                                                copy(origen, target);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            edit = prefs.edit();
                                            edit.putInt("Login", 0);
                                            Intent intent = new Intent(BaseDrawer.this, Splash.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            edit.putInt("Formatear", 1);
                                            edit.apply();

                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(getBaseContext(), "Inventario finalizado satisfactoriamente", Toast.LENGTH_LONG).show();
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(BaseDrawer.this);
                            builder2.setMessage("¿Esta seguro de finalizar el inventario actual? ").setPositiveButton("Si", dialogClickListener2)
                                    .setNegativeButton("No", dialogClickListener2).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Invitado no puede finalizar inventario", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                    default: {
                        edit = prefs.edit();
                        edit.putInt("Login", 0);
                        Intent intent = new Intent(BaseDrawer.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        edit.apply();
                        startActivity(intent);
                        finish();
                        break;

                    }
                }
            }
        });
        //--NavigationDrawer - Fin
    }


    @Override //permite cabia el icono de abrir y cerrar menu
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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
