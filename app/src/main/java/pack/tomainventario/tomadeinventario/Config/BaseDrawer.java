package pack.tomainventario.tomadeinventario.Config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pack.tomainventario.tomadeinventario.Adapters.NavigationAdapter;
import pack.tomainventario.tomadeinventario.AjustarRPU;
import pack.tomainventario.tomadeinventario.ConsultarBien;
import pack.tomainventario.tomadeinventario.Galeria;
import pack.tomainventario.tomadeinventario.MainActivity;
import pack.tomainventario.tomadeinventario.Objects.Item_objct;
import pack.tomainventario.tomadeinventario.R;
import pack.tomainventario.tomadeinventario.Reportes;

public class BaseDrawer extends FragmentActivity {
    protected String[] titulos;
    protected DrawerLayout NavDrawerLayout;
    protected ListView NavList;
    protected ArrayList<Item_objct> NavItms;
    protected TypedArray NavIcons;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected NavigationAdapter NavAdapter;
    private SharedPreferences.Editor edit;
    private CharSequence mTitle, mDrawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        NavDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavList = (ListView) findViewById(R.id.lista);
        NavIcons = getResources().obtainTypedArray(R.array.navigation_iconos);
        titulos = getResources().getStringArray(R.array.nav_options);
        NavItms = new ArrayList<Item_objct>();

        for ( int i = 0; i <= 5; i ++ ) {
                NavItms.add(new Item_objct(titulos[i], NavIcons.getResourceId(i, -1)));
        }

        NavAdapter= new NavigationAdapter(this,NavItms);
        NavList.setAdapter(NavAdapter);
        mTitle = mDrawerTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                NavDrawerLayout,         /* DrawerLayout object */
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

        NavDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parent, View view,int position, long id) {
        Bundle bundle = new Bundle();
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
                    Intent intent = new Intent(BaseDrawer.this, AjustarRPU.class);
                    startActivity(intent);
                    break;
                }
                case 3: {
                    Intent intent = new Intent(BaseDrawer.this, Galeria.class);
                    startActivity(intent);
                    break;
                }
                case 4: {
                    Intent intent = new Intent(BaseDrawer.this, Reportes.class);
                    startActivity(intent);
                    break;
                }
                default: {
                    SharedPreferences prefs = getSharedPreferences("invPreferences", Context.MODE_PRIVATE);

                    edit = prefs.edit();
                    edit.putInt("Login", 0);
                    edit.apply();
                    finish();
                    System.exit(0);
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
}
