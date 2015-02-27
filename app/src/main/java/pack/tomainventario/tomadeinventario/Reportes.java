package pack.tomainventario.tomadeinventario;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.TabPagerAdapter;
import pack.tomainventario.tomadeinventario.Config.BaseDrawer;
import pack.tomainventario.tomadeinventario.Config.SlidingTabLayout;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SBN050D;
import pack.tomainventario.tomadeinventario.DataBase.SBN051D;
import pack.tomainventario.tomadeinventario.DataBase.SBN053D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;


public class Reportes extends BaseDrawer {
    private ViewPager tab;
    private TabPagerAdapter tabAdapter;
    private ActionBar actionBar;
    private SlidingTabLayout sTabLayout;
    private String logo;
    private Spinner cmbUbic;
    private ArrayAdapter<SBN010D> adapUbic;
    private String ubicacion;
    private List<SBN001D> lstSobrantes,lstFaltantes,bienes;
    private List<SBN051D> inventariados;
    private List<SBN050D> tomas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ------------------- Configuracion - inicio
        RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_reportes, null, false);
        rLayout.addView(activityView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        navAdapter.setActual(4);
        actionBar = getActionBar();
        actionBar.setTitle("Sobrantes/Faltantes");
        // -------------------- Configuracion - final


        adapUbic = new ArrayAdapter<SBN010D>(this,R.layout.layout_item_spinner);
        adapUbic.addAll(SBN010D.getAll());
        cmbUbic = (Spinner)findViewById(R.id.cmbUbic);
        cmbUbic.setAdapter(adapUbic);

        ubicacion=adapUbic.getItem(safeLongToInt(cmbUbic.getSelectedItemId())).codUbic;

        logo=getResources().getString(R.string.logo);
        sTabLayout = (SlidingTabLayout) findViewById(R.id.sTabLayout);
        tabAdapter = new TabPagerAdapter(getSupportFragmentManager(),ubicacion);

        tab = (ViewPager) findViewById(R.id.pager);
        tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar = getActionBar();
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
        tab.setAdapter(tabAdapter);
        sTabLayout.setViewPager(tab);



        cmbUbic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                ubicacion = adapUbic.getItem(safeLongToInt(cmbUbic.getSelectedItemId())).codUbic;
                tabAdapter.updateAdapter(ubicacion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reportes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_aceptar) {
            DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:

                            // SOBRANTES inicio
                            lstSobrantes = new ArrayList<SBN001D>();
                            inventariados= SBN051D.getAll();
                            for (SBN051D aData : inventariados)
                                if (!SBN001D.getBn(aData.numeroBn).codUbic.equals(SBN050D.getInv(aData.idInventario).codUbic)
                                        && SBN050D.getInv(aData.idInventario).codUbic.equals(adapUbic.getItem(safeLongToInt(cmbUbic.getSelectedItemId())).codUbic))
                                    lstSobrantes.add(SBN001D.getBn(aData.numeroBn));
                            // SOBRANTES fin

                            // FALTATES inicio
                            lstFaltantes = new ArrayList<SBN001D>();
                            bienes= new ArrayList<SBN001D>();
                            tomas=SBN050D.getAllUbic(adapUbic.getItem(safeLongToInt(cmbUbic.getSelectedItemId())).codUbic);
                            for (SBN050D aData : tomas){
                                bienes= SBN001D.getUbic(aData.codUbic);
                                inventariados=SBN051D.getInventario(aData.idInventario);
                                for (SBN001D aBien : bienes){
                                    if(SBN051D.getBn(aBien.numero)==null){
                                        lstFaltantes.add(aBien);
                                    }
                                }
                            }
                            // FALTATES fin

                            File directoryFile = new File(Environment.getExternalStorageDirectory() + "/SistemaInventario/Reportes");
                            if (!directoryFile.exists()) {
                                directoryFile.mkdirs();
                            }

                            String content = writeToHtml(SBN053D.getAll().get(0).sede,
                                    adapUbic.getItem(safeLongToInt(cmbUbic.getSelectedItemId())).codUbic,
                                    SIP501V.getPersonal(SBN010D.getUbic(adapUbic.getItem(safeLongToInt(cmbUbic.getSelectedItemId())).codUbic).respUa).nombre,
                                    fechaActual(),lstSobrantes,lstFaltantes);

                            File target = new File(directoryFile, "Reporte_"+adapUbic.getItem(safeLongToInt(cmbUbic.getSelectedItemId())).codUbic+".html");
                            try {
                                    target.createNewFile();
                                    FileWriter fw = new FileWriter(target,false);
                                    BufferedWriter bw = new BufferedWriter(fw);
                                    bw.write(content);
                                    bw.flush();
                                    bw.close();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            Toast.makeText(getBaseContext(), "Reporte generado satisfactoriamente", Toast.LENGTH_LONG).show();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setMessage("¿Desea generar reporte de la ubicacion "+adapUbic.getItem(safeLongToInt(cmbUbic.getSelectedItemId())).codUbic+ "?").setPositiveButton("Si", dialogClickListener2)
                    .setNegativeButton("No", dialogClickListener2).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public String writeToHtml(String sede, String ubic, String rpa, String fecha,List<SBN001D> sobrantes,List<SBN001D> faltantes )  {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>");
        builder.append("<html lang=\"en\">");
        builder.append("<head><title>Sobrantes/Faltantes</title></head>");
        builder.append("<body>");
        builder.append("<div>");

        builder.append("<table  style=\"width:100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"7\">");
        builder.append("<tr>");
        builder.append("<td style=\"width:7%\"> <img src=\"\n").append(logo).append("\" alt=\"Mountain View\" style=\"width:70px;height:70px\"></td>");
        builder.append("<td ><p style=\"font-size:10px\"><b>REPUBLICA BOLIVARIANA DE VENEZUELA<br/>\n" +
                " \t\t\t\tUNIVERSIDAD NACIONAL EXPERIMENTAL DE GUAYANA<br/> \n" +
                " \t\t\t\tVICERRECTORADO ADMINISTRATIVO<br/> \n" +
                " \t\t\t\tCOORDINACION DE CONTABILIDAD<br/> \n" +
                " \t\t\t\tDEPARTAMENTO DE ADMINISTRACION DE BIENES NACIONALES</b></p>  </td>");
        builder.append("<tr>");

        builder.append("</table>");
        builder.append("</div>");
        builder.append("<div align=\"center\"> ");
        builder.append("<table border-collapse= \"collapse\" style=\"width:100%; margin-top: 20px; font-size:10px\" border=\"0\" \n" +
                "cellspacing=\"0\" cellpadding=\"4\">");

        builder.append("<tr>");
        builder.append("<td style=\"width:50%\"> UBICACION FISICA: ").append(ubic).append("</td>");
        builder.append("<td style=\"width:50%\"> FECHA: ").append(fecha).append("</td>");;
        builder.append("</tr> ");
        builder.append("<tr>");
        builder.append("<td> CODIGO DE SEDE: ").append(sede).append("</td>");
        builder.append("<td> RESPONSABLE PRIMARIO: ").append(rpa).append("</td>");
        builder.append("</tr> ");
        builder.append("</table> ");
        builder.append("</div>");
        builder.append("<h4 align=\"center\">INVENTARIO FISICO DE BIENES </h4>");

        builder.append("<div align=\"center\">");
        builder.append("<h5 align=\"center\">FALTANTES  </h5>");
        if(lstFaltantes.size()>0) {
            builder.append("<table style=\"width:100%; font-size:10px\" border=\"1\" cellspacing=\"0\" cellpadding=\"7\">");
            builder.append("<tr>");
            builder.append("<td style=\"width:2%\" bgcolor=\"#CED3D5\"><b> # </td></b>");
            builder.append("<td style=\"width:18%\" bgcolor=\"#CED3D5\"><b> NUMERO BN</b></td>");
            builder.append("<td style=\"width:45%\" bgcolor=\"#CED3D5\"><b> DETALLE </b></td>");
            builder.append("<td style=\"width:10%\" bgcolor=\"#CED3D5\"><b> SERIAL </b></td>");
            builder.append("<td style=\"width:18%\" bgcolor=\"#CED3D5\"><b> RPU </b></td>");
            builder.append("</tr>");
            for (int times = 0; times < lstFaltantes.size(); times++) {
                builder.append("<tr>");
                builder.append("<td> ").append(times + 1).append("</td>");
                builder.append("<td> ").append(faltantes.get(times).numero).append("</td>");
                builder.append("<td> ").append(faltantes.get(times).nombre).append("</td>");
                builder.append("<td> ").append(faltantes.get(times).serial).append("</td>");
                builder.append("<td> ").append(faltantes.get(times).pUsuario).append(" ").append(SIP501V.getPersonal(faltantes.get(times).pUsuario).nombre).append("</td>");
                builder.append(" </tr>  ");
            }
            builder.append("</table>");

        }
        else{
            builder.append("<p style=\"font-size:10px\"> No se registraron faltantes en esta ubicacion </p>");
        }
        builder.append("</div>");

        builder.append("<div align=\"center\">");
        builder.append("<h5 align=\"center\">SOBRANTES </h5>");
        if(lstFaltantes.size()>0) {
        builder.append("<table style=\"width:100%; font-size:10px\" border=\"1\" cellspacing=\"0\" cellpadding=\"7\">");
        builder.append("<tr>");
        builder.append("<td style=\"width:2%\" bgcolor=\"#CED3D5\"><b> # </b></td>");
        builder.append("<td style=\"width:18%\" bgcolor=\"#CED3D5\"><b> NUMERO BN</b></td>");
        builder.append("<td style=\"width:45%\" bgcolor=\"#CED3D5\"><b> DETALLE </b></td>");
        builder.append("<td style=\"width:10%\" bgcolor=\"#CED3D5\"><b> SERIAL </b></td>");
        builder.append("<td style=\"width:18%\" bgcolor=\"#CED3D5\"><b> RPU </b></td>");
        builder.append("</tr>");

        for (int times = 0; times < lstSobrantes.size(); times++) {
            builder.append("<tr>");
            builder.append("<td> ").append(times +1).append("</td>");
            builder.append("<td> ").append(sobrantes.get(times).numero).append("</td>");
            builder.append("<td> ").append(sobrantes.get(times).nombre).append("</td>");
            builder.append("<td> ").append(sobrantes.get(times).serial).append("</td>");
            builder.append("<td> ").append(sobrantes.get(times).pUsuario).append(" ").append(SIP501V.getPersonal(sobrantes.get(times).pUsuario).nombre).append("</td>");
            builder.append(" </tr>  ");
        }
        builder.append("</table>");
        }
        else{
            builder.append("<p style=\"font-size:10px\"> No se registraron sobrantes en esta ubicacion </p>");
        }
        builder.append("</div>");



        builder.append("<table  style=\"width:100%; margin-top: 20px; font-size:10px\" border=\"0\" cellspacing=\"0\" cellpadding=\"7\">");
        builder.append("<tr>");
        builder.append("<td style=\"width:50%\"> DEPARTAMENTO DE BIENES NACIONALES </td>");
        builder.append("<td align=\"right\" style=\"width:50%\"> UNIDAD RESPONSABLE </td>");
        builder.append("</tr> ");
        builder.append("</table>");

        builder.append("</body>");
        builder.append("</html>");
        return builder.toString();
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
    public String fechaActual(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c.getTime());
    }
}
