package pack.tomainventario.tomadeinventario.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pack.tomainventario.tomadeinventario.Faltantes;
import pack.tomainventario.tomadeinventario.Sobrantes;


public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = { "Faltantes", "Sobrantes" };
    private String ubic;
    private Faltantes fFaltante ;
    private Sobrantes fSobrantes ;

    public TabPagerAdapter(FragmentManager fm, String ubic) {
        super(fm);
        this.ubic=ubic;
        fFaltante = new Faltantes(ubic);
        fSobrantes = new Sobrantes(ubic);
    }
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return  fFaltante;
            case 1:
                return  fSobrantes;
        }
        return null;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2; //No of Tabs
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public void updateAdapter(String ubic){
        this.ubic=ubic;
        fFaltante.updateData(ubic);
        fSobrantes.updateData(ubic);
        notifyDataSetChanged();
    }
}
