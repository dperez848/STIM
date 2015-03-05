package pack.tomainventario.tomadeinventario.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import pack.tomainventario.tomadeinventario.Adapters.RpuAdapter;
import pack.tomainventario.tomadeinventario.DataBase.SBN001D;
import pack.tomainventario.tomadeinventario.DataBase.SIP501V;
import pack.tomainventario.tomadeinventario.DetalleToma;
import pack.tomainventario.tomadeinventario.Interfaces.Rpu;
import pack.tomainventario.tomadeinventario.NuevaToma;
import pack.tomainventario.tomadeinventario.R;

/**
 * Created by tmachado on 02/12/2014.
 */
public class RpuDialog extends DialogFragment implements Rpu {
    private RpuAdapter adaptador;
    private ImageView bSearch;
    private EditText eSearch;
    private boolean boolSearch=true;
    private Activity context=getActivity();
    private ListView lstOpciones;
    private AdapterView<?> adapterView;
    private List<SIP501V> data;
    private int num, pos;
    AlertDialog.Builder builder;
    private AdapterView.OnItemClickListener adapterView1;
    @Override
    public void openRpuDialog(int bn) {

    }

    @Override
    public void onRpuItemClick(final SIP501V rpu) {
        /*if(context instanceof DetalleToma || context instanceof NuevaToma) {

            DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            NoticeDialogListener activity = (NoticeDialogListener) getActivity();
                            activity.onDialogItemClick(rpu);
                            dismiss();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage("¿Desea realizar este cambio?").setPositiveButton("Si", dialogClickListener1)
                    .setNegativeButton("No", dialogClickListener1).show();
        }
        else{
            Log.e("aaa", "ANTES ");
            NoticeDialogListener activity = (NoticeDialogListener) getActivity();
            activity.onDialogItemClick(rpu);
            Log.e("aaa", "DESPUES ");
        }*/
    }

    public interface NoticeDialogListener {
        public void onDialogItemClick(SIP501V rpu);
    }

    public RpuDialog(Activity context) {

        this.context = context;
    }

    public RpuDialog(Activity context, int num) {
        this.num = num;
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

       builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_rpu, null);
        builder.setView(dialogView);
        bSearch = (ImageView) dialogView.findViewById(R.id.search);
        eSearch = (EditText) dialogView.findViewById(R.id.edit_search);

        data= SIP501V.getAll();
        if(context instanceof DetalleToma || context instanceof NuevaToma) {
            adaptador = new RpuAdapter(getActivity(), data, SBN001D.getBn(num).pUsuario);
        } else adaptador =  new RpuAdapter(getActivity(),data);
        lstOpciones = (ListView)dialogView.findViewById(R.id.listRpu);
        lstOpciones.setAdapter(adaptador);

       adapterView1 = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                adapterView=a;
                pos=position;

                if(context instanceof DetalleToma || context instanceof NuevaToma) {

                    DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    NoticeDialogListener activity = (NoticeDialogListener) getActivity();
                                    activity.onDialogItemClick((SIP501V) adapterView.getAdapter().getItem(pos));
                                    dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("¿Desea realizar este cambio?").setPositiveButton("Si", dialogClickListener1)
                            .setNegativeButton("No", dialogClickListener1).show();
                }
                else{
                    NoticeDialogListener activity = (NoticeDialogListener) getActivity();
                    activity.onDialogItemClick((SIP501V) a.getAdapter().getItem(position));
                    dismiss();
                }
            }
        };

        lstOpciones.setOnItemClickListener(adapterView1);

        bSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (boolSearch && !(eSearch.getText().toString().equals(""))) {
                    bSearch.setImageResource(R.drawable.x);
                    boolSearch = false;
                    data = SIP501V.getPersonalDialog(eSearch.getText().toString());
                    adaptador.updateAdapter(data);
                    lstOpciones.setOnItemClickListener(adapterView1);
                } else {
                    bSearch.setImageResource(R.drawable.search);
                    eSearch.setText("");
                    boolSearch = true;
                    data = SIP501V.getAll();
                    adaptador.updateAdapter(data);
                    lstOpciones.setOnItemClickListener(adapterView1);
                }
            }
        });

        eSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bSearch.setImageResource(R.drawable.search);
                boolSearch=true;
            }
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
        });

        eSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    bSearch.setImageResource(R.drawable.x);
                    boolSearch=false;
                    return true;
                }
                return false;
            }
        });
        return builder.setCancelable(true).create();
    }
}