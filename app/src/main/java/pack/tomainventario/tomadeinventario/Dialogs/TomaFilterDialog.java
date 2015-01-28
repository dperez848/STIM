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
import android.widget.EditText;
import android.widget.Spinner;

import pack.tomainventario.tomadeinventario.DataBase.SBN206D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.Interfaces.Observacion;
import pack.tomainventario.tomadeinventario.R;

/**
 * Created by tmachado on 09/01/2015.
 */
public class TomaFilterDialog extends DialogFragment  {

    private int numeroBn,spinnerPosition,edo;
    private SIP501V pUsuario;
    private ArrayAdapter<SBN206D> adapEdo;
    private Spinner sEdo;
    private EditText editComment,editRpu;
    private Activity context;
    private Observacion listener;
    private String observacion;
    private static final String LOGTAG = "INFORMACION";

    public TomaFilterDialog(int numeroBn,Activity context, String observacion, int edo) {
        this.numeroBn = numeroBn;
        this.listener = (Observacion)context;
        this.observacion=observacion;
        this.context=context;
        this.edo=edo;
    }

     @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_toma, null);

        editComment = (EditText) dialogView.findViewById(R.id.comment);
        editComment.setText(observacion);
        sEdo = (Spinner) dialogView.findViewById(R.id.estado);


        adapEdo = new ArrayAdapter<SBN206D>(getActivity(),android.R.layout.simple_spinner_item);
        adapEdo.addAll(SBN206D.getAll());
        sEdo.setAdapter(adapEdo);
        spinnerPosition = adapEdo.getPosition(SBN206D.getEdoDB(edo));
        sEdo.setSelection(spinnerPosition);


        builder
                .setTitle("Editar:")
                .setView(dialogView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

//                        if (context instanceof DetalleToma) {

                            DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            listener.putObservacion(editComment.getText().toString(), numeroBn,
                                                    adapEdo.getItem(safeLongToInt(sEdo.getSelectedItemId())).codEdo);
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setMessage("¿Desea realizar este cambio?").setPositiveButton("Si", dialogClickListener1)
                                    .setNegativeButton("No", dialogClickListener1).show();

//
//                        } else {
//
//                            listener.putObservacion(editComment.getText().toString(), numeroBn,
//                                    adapEdo.getItem(safeLongToInt(sEdo.getSelectedItemId())).codEdo);
//
//                        }

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
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