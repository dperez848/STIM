package pack.tomainventario.tomadeinventario.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pack.tomainventario.tomadeinventario.Faltantes;
import pack.tomainventario.tomadeinventario.Sobrantes;


public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = { "Faltantes", "Sobrantes" };

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new Faltantes();
            case 1:
                return new Sobrantes();
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
}
