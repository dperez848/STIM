package pack.tomainventario.tomadeinventario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.BnAdapter;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SBN050D;
import pack.tomainventario.tomadeinventario.DataBase.SBN051D;


public class Faltantes extends android.support.v4.app.Fragment {
    private ListView lView;
    private BnAdapter adaptador;
    private View view;
    private List<SBN050D> tomas;
    private List<SBN051D> inventariados;
    private List<SBN001D> lstFaltantes, bienes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_sobrantes, container, false);

        lstFaltantes = new ArrayList<SBN001D>();
        bienes= new ArrayList<SBN001D>();
        tomas=SBN050D.getAll();
        for (SBN050D aData : tomas){
            bienes= SBN001D.getUbic(aData.codUbic);
            inventariados=SBN051D.getInventario(aData.idInventario);
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
}