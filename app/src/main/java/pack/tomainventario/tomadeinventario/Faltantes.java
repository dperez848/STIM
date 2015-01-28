package pack.tomainventario.tomadeinventario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import pack.tomainventario.tomadeinventario.Adapters.ReportesAdapter;
import pack.tomainventario.tomadeinventario.Objects.ReportesContent;


public class Faltantes extends android.support.v4.app.Fragment {
    private String opcionSeleccionada;
    private ListView lView;
    private ReportesAdapter adaptador;
    private View view;
    private ReportesContent[] data3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_sobrantes, container, false);

        data3 =
                new ReportesContent[]{
                        new ReportesContent(111, "Subtítulo largo 1", "Subtítulo largo 1"),
                        new ReportesContent(555, "Subtítulo largo 2", "Subtítulo largo 1"),
                        new ReportesContent(7777, "Subtítulo largo 3", "Subtítulo largo 1"),
                        new ReportesContent(786786, "Subtítulo largo 4", "Subtítulo largo 1"),
                        new ReportesContent(786876, "Subtítulo largo 5", "Subtítulo largo 1"),
                        new ReportesContent(786876, "Subtítulo largo 1", "Subtítulo largo 1"),
                        new ReportesContent(786876, "Subtítulo largo 2", "Subtítulo largo 1"),
                        new ReportesContent(786876, "Subtítulo largo 3", "Subtítulo largo 1"),
                        new ReportesContent(786876, "Subtítulo largo 4", "Subtítulo largo 1"),
                        new ReportesContent(78687687, "Subtítulo largo 5", "Subtítulo largo 1")};

        adaptador =  new ReportesAdapter(getActivity(),data3);
        lView= ((ListView)view.findViewById(R.id.LstOpciones));
        lView.setAdapter(adaptador);
        ((ListView)view.findViewById(R.id.LstOpciones)).
                setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                        opcionSeleccionada = ((ReportesContent) a.getAdapter().getItem(position)).getFecha();
                        Toast.makeText(getActivity(), "Opción seleccionada:" + opcionSeleccionada, Toast.LENGTH_LONG).show();
                    }
                });
        return view;
    }
}