package pack.tomainventario.tomadeinventario;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import pack.tomainventario.tomadeinventario.Adapters.VisorAdapter;
import pack.tomainventario.tomadeinventario.DataBase.SBN054D;


public class VisorFotos extends Activity {

    private VisorAdapter adapter;
    private ViewPager viewPager;
    private Button btnClose;
    private int pos;
    private ArrayList<SBN054D> fotos;

    public VisorFotos(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_fotos);
        Bundle i = getIntent().getExtras();

        viewPager = (ViewPager) findViewById(R.id.pager2);
        btnClose = (Button) findViewById(R.id.btnClose);

        fotos = SBN054D.getBn(i.getInt("numeroBn"));
        pos= i.getInt("pos");
        adapter = new VisorAdapter(VisorFotos.this, fotos);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}






