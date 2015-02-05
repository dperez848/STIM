package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.GridViewAdapter;
import pack.tomainventario.tomadeinventario.Config.BaseDrawer;
import pack.tomainventario.tomadeinventario.DataBase.SBN054D;
import pack.tomainventario.tomadeinventario.Interfaces.IGaleria;


public class Galeria extends BaseDrawer implements IGaleria {
    private GridView gridView;
    private GridViewAdapter customGridAdapter;
    private ActionBar actionBar;
    private List<SBN054D> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ------------------- Configuracion - inicio
        RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_galeria, null,false);
        rLayout.addView(activityView);
        navAdapter.setActual(3);
        actionBar= getActionBar();
        actionBar.setTitle("Galería");

        // ------------------- Configuracion - inicio
        gridView = (GridView) findViewById(R.id.gridView);
        data= SBN054D.getFirstFotos();
        customGridAdapter = new GridViewAdapter(Galeria.this, R.layout.row_grid, data,0);
        gridView.setAdapter(customGridAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_galeria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void detalle(int numero, int position) {
        Intent intent = new Intent(Galeria.this, DetalleGaleria.class);
        Bundle bundle = new Bundle();
        bundle.putInt("numero", numero);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onLong(int numero, int numero2) {

    }
}
