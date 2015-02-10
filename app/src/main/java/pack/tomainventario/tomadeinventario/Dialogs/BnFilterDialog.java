package pack.tomainventario.tomadeinventario.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import pack.tomainventario.tomadeinventario.DataBase.SBN010D;
import pack.tomainventario.tomadeinventario.DataBase.SIP517V;
import pack.tomainventario.tomadeinventario.Interfaces.Filter;
import pack.tomainventario.tomadeinventario.R;

/**
 * Created by tmachado on 09/01/2015.
 */
public class BnFilterDialog extends DialogFragment {
    Activity context;
    Filter listener;
    Spinner sSedes, sUbic;
    ArrayAdapter<SIP517V> adapSede;
    ArrayAdapter<SBN010D> adapUbic;
    SIP517V defec1=new SIP517V();
    SBN010D defec2=new SBN010D();
    Button btn1;

    public BnFilterDialog(Activity context) {
        this.context = context;
        this.listener = (Filter)context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();


            View dialogView = inflater.inflate(R.layout.dialog_filter_bienes, null);

            sUbic = (Spinner) dialogView.findViewById(R.id.status);
            sSedes = (Spinner) dialogView.findViewById(R.id.sedes);
            btn1 = (Button) dialogView.findViewById(R.id.btn1);

            adapSede = new ArrayAdapter<SIP517V>(getActivity(),android.R.layout.simple_spinner_item);
            adapSede.addAll(SIP517V.getAll());
            defec1.setCodUbic("0");
            defec1.setDesUbic("Todos");
            adapSede.insert(defec1, 0);
            sSedes = (Spinner) dialogView.findViewById(R.id.sedes);
            sSedes.setAdapter(adapSede);


            adapUbic = new ArrayAdapter<SBN010D>(getActivity(),android.R.layout.simple_spinner_item);
            adapUbic.addAll(SBN010D.getAll());
            defec2.setCodUbic("0");
            defec2.setNombre("Todas");
            adapUbic.insert(defec2, 0);
            sUbic = (Spinner) dialogView.findViewById(R.id.status);
            sUbic.setAdapter(adapUbic);

            builder
                    .setTitle("Filtrar bienes por:")
                    .setView(dialogView)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            defec1=SIP517V.getSede(adapSede.getItem(safeLongToInt(sSedes.getSelectedItemId())).codUbic);
                            defec2=SBN010D.getUbic(adapUbic.getItem(safeLongToInt(sUbic.getSelectedItemId())).codUbic);
                            listener.filterSelect(defec1,defec2);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();


        }
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

}
