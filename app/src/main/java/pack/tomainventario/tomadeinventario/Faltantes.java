package pack.tomainventario.tomadeinventario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.BnAdapter;
import pack.tomainventario.tomadeinventario.Models.SBN001D;
import pack.tomainventario.tomadeinventario.Models.SBN050D;
import pack.tomainventario.tomadeinventario.Models.SBN051D;


public class Faltantes extends android.support.v4.app.Fragment {
    private ListView lView;
    private BnAdapter adaptador;
    private View view;
    private List<SBN050D> tomas;
    private List<SBN001D> lstFaltantes, bienes;
    private String ubic;

    public Faltantes() {
    }

    public Faltantes(String ubic) {
        this.ubic = ubic;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_sobrantes, container, false);

        lstFaltantes = new ArrayList<SBN001D>();
        bienes= new ArrayList<SBN001D>();
        tomas=SBN050D.getAllUbic(ubic);
        for (SBN050D aData : tomas){
            bienes= SBN001D.getUbic(aData.codUbic);
            for (SBN001D aBien : bienes){
                if(SBN051D.getBn(aBien.numero)==null){
                    lstFaltantes.add(aBien);
                }
            }
        }

        adaptador =  new BnAdapter(getActivity(),lstFaltantes,0);
        lView= ((ListView)view.findViewById(R.id.LstOpciones));
        lView.setAdapter(adaptador);

        return view;
    }

    public void updateData(String ubicacion) {
        ubic=ubicacion;

        lstFaltantes = new ArrayList<SBN001D>();
        bienes= new ArrayList<SBN001D>();
        tomas=SBN050D.getAllUbic(ubic);
        for (SBN050D aData : tomas){
            bienes= SBN001D.getUbic(aData.codUbic);
            for (SBN001D aBien : bienes){
                if(SBN051D.getBn(aBien.numero)==null){
                    lstFaltantes.add(aBien);
                }
            }
        }
        adaptador.updateAdapter(lstFaltantes);
    }
}