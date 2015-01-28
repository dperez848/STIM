package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.MainAdapter;
import pack.tomainventario.tomadeinventario.Config.BaseDrawer;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SBN050D;
import pack.tomainventario.tomadeinventario.DataBase.SBN051D;
import pack.tomainventario.tomadeinventario.DataBase.SBN053D;
import pack.tomainventario.tomadeinventario.DataBase.SBN054D;
import pack.tomainventario.tomadeinventario.DataBase.SBN203D;
import pack.tomainventario.tomadeinventario.DataBase.SBN206D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.DataBase.SIP517V;
import pack.tomainventario.tomadeinventario.Objects.Inventoried;


public class MainActivity extends BaseDrawer {

    private String opcionSeleccionada;
    private ActionBar actionBar;
    private ListView lstOpciones;
    private MainAdapter adaptador;
    private Inventoried inv;
    private List<Inventoried> data;
    private List<SBN051D> inventariados;
    private TextView txtEmpty ;
    private final CharSequence[] items = { "Deshacer"};
    private static final String LOGTAG = "INFORMACION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // ------------------- Configuracion - inicio

        RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_main, null,false);
        rLayout.addView(activityView);
        super.NavAdapter.setActual(0);
        actionBar= getActionBar();
        actionBar.setTitle("Tomas realizadas");
        // ------------------- Configuracion - fin

        txtEmpty = (TextView)findViewById(R.id.empty);
        data = new ArrayList<Inventoried>();
        lstOpciones = (ListView)findViewById(R.id.LstOpciones);

        inventariados= SBN051D.getAll();
                    if (!inventariados.isEmpty()){
                        for (SBN051D aData : inventariados) {
                            if (!has(aData.numeroBn, data)) {
                                inv = new Inventoried();
                                inv.setNumero(aData.numeroBn);
                                inv.setDescripcion(SBN001D.getDescripcion(aData.numeroBn));
                                inv.setFecha(aData.fecha);
                                inv.setFoto(SBN054D.getFoto(aData.numeroBn));
                                inv.setSede(SIP517V.getSede(SBN053D.getAll().get(0).sede).desUbic); //viene de la 53
                                inv.setUbic(SBN010D.getUbicacion(SBN050D.getInv(SBN051D.getBn(aData.numeroBn).idInventario).codUbic)); //viene de la 50

                                inv.setRpu(SIP501V.getPersonal(SBN001D.getBn(aData.numeroBn).pUsuario).nombre); //viene de 001
                                data.add(inv);
                            }
                        }
                    }

        lstOpciones = (ListView)findViewById(R.id.LstOpciones);

        if (data.size()==0) {
                        lstOpciones.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                    }
                    else if (data.size()!=0){
                        lstOpciones.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.GONE);
                        adaptador =  new MainAdapter(this,data);
                        lstOpciones.setAdapter(adaptador);
                        lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, DetalleToma.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("numero",data.get(position).getNumero());
                                bundle.putString("status", SBN203D.getStatus(SBN001D.getBn(data.get(position).getNumero()).status).descripcion);
                                bundle.putString("descripcion", data.get(position).getDescripcion());
                                bundle.putString("fecha",data.get(position).getFecha());
                                bundle.putString("foto",data.get(position).getFoto());
                                bundle.putString("sede",data.get(position).getSede());
                                bundle.putString("ubic",data.get(position).getUbic());
                                bundle.putString("rpu",data.get(position).getRpu());
                                bundle.putString("observacion",SBN051D.getObservacion(SBN051D.getBn(data.get(position).getNumero())));
                                bundle.putString("edo", SBN206D.getEdo(SBN001D.getBn(data.get(position).getNumero()).edoFis));
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 1008);
                            }
                        });
                    }
                    SBN001D.setTaken(); // una vez en main novuelven a Nuevatoma
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = NavDrawerLayout.isDrawerOpen(NavList);
        menu.findItem(R.id.action_add).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, ActivacionInventario.class);
            startActivityForResult(intent, 1008);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Boolean has(int num,List<Inventoried> data){
        for (Inventoried aData : data) {
            if (aData.getNumero() == num) return true;
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1) {
        super.onActivityResult(requestCode, resultCode, data1);
        if (resultCode == RESULT_OK) {
            if(requestCode==1008) {

                Log.e("TAAAG", "Entro y soy " + requestCode);
                data = new ArrayList<Inventoried>();
                    inventariados= SBN051D.getAll();
                    if (!inventariados.isEmpty()){
                        for (SBN051D aData : inventariados) {
                            if (!has(aData.numeroBn, data)) {
                                inv = new Inventoried();
                                inv.setNumero(aData.numeroBn);
                                inv.setDescripcion(SBN001D.getDescripcion(aData.numeroBn));
                                inv.setFecha(aData.fecha);
                                inv.setFoto(SBN054D.getFoto(aData.numeroBn));
                                inv.setSede(SIP517V.getSede(SBN053D.getAll().get(0).sede).desUbic); //viene de la 53
                                inv.setUbic(SBN010D.getUbicacion(SBN050D.getInv(SBN051D.getBn(aData.numeroBn).idInventario).codUbic)); //viene de la 50

                                inv.setRpu(SIP501V.getPersonal(SBN001D.getBn(aData.numeroBn).pUsuario).nombre); //viene de 001
                                Log.e("TAAAG", "El RPU es  " + SIP501V.getPersonal(SBN001D.getBn(aData.numeroBn).pUsuario).nombre);
                                data.add(inv);
                            }
                        }
                    }
                    if (data.size()==0) {
                        lstOpciones.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                    }
                    else if (data.size()!=0){
                        lstOpciones.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.GONE);
                        adaptador =  new MainAdapter(this,data);
                        lstOpciones.setAdapter(adaptador);
                        lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, DetalleToma.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("numero",data.get(position).getNumero());
                                bundle.putString("status", SBN203D.getStatus(SBN001D.getBn(data.get(position).getNumero()).status).descripcion);
                                bundle.putString("descripcion", data.get(position).getDescripcion());
                                bundle.putString("fecha",data.get(position).getFecha());
                                bundle.putString("foto",data.get(position).getFoto());
                                bundle.putString("sede",data.get(position).getSede());
                                bundle.putString("ubic",data.get(position).getUbic());
                                bundle.putString("rpu",data.get(position).getRpu());
                                bundle.putString("observacion",SBN051D.getObservacion(SBN051D.getBn(data.get(position).getNumero())));
                                bundle.putString("edo", SBN206D.getEdo(SBN001D.getBn(data.get(position).getNumero()).edoFis));
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 1008);
                            }
                        });
                    }
                    SBN001D.setTaken(); // una vez en main novuelven a Nuevatoma


            }

        }
    }
}
