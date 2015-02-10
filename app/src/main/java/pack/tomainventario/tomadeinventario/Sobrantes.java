package pack.tomainventario.tomadeinventario;

import android.os.Bundle;
import android.util.Log;
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

public class Sobrantes extends android.support.v4.app.Fragment {
    private ListView lView;
    private BnAdapter adaptador;
    private View view;
    private List<SBN051D> inventariados;
    private List<SBN001D> lstSobrantes;
    private String ubic;

    public Sobrantes(String ubic) {
        this.ubic = ubic;
    }
    public Sobrantes() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_sobrantes, container, false);

        lstSobrantes = new ArrayList<SBN001D>();
        inventariados=SBN051D.getAll();
        for (SBN051D aData : inventariados)
            if (!SBN001D.getBn(aData.numeroBn).codUbic.equals(SBN050D.getInv(aData.idInventario).codUbic)
                    && SBN050D.getInv(aData.idInventario).codUbic.equals(ubic))
                lstSobrantes.add(SBN001D.getBn(aData.numeroBn));

        Log.e("TAAAG", "la cantidad de lstSobrantes " + lstSobrantes.size());
        adaptador =  new BnAdapter(getActivity(),lstSobrantes,0);
        lView= ((ListView)view.findViewById(R.id.LstOpciones));
        lView.setAdapter(adaptador);
        return view;
    }

    public void updateData(String ubicacion) {
        ubic=ubicacion;
        lstSobrantes = new ArrayList<SBN001D>();
        inventariados=SBN051D.getAll();
        for (SBN051D aData : inventariados)
            if (!SBN001D.getBn(aData.numeroBn).codUbic.equals(SBN050D.getInv(aData.idInventario).codUbic)
                    && SBN050D.getInv(aData.idInventario).codUbic.equals(ubic))
                lstSobrantes.add(SBN001D.getBn(aData.numeroBn));
        adaptador.updateAdapter(lstSobrantes);
    }


}