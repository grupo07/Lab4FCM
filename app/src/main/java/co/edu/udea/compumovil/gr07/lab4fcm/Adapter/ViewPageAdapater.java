package co.edu.udea.compumovil.gr07.lab4fcm.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grupo07 on 18/10/2016.
 */

public class ViewPageAdapater extends FragmentPagerAdapter {

    private List<Fragment> miListaFragment;
    private List<String> miListaTitulosFragment;

    public ViewPageAdapater(FragmentManager fm) {
        super(fm);
        miListaFragment = new ArrayList<>();
        miListaTitulosFragment = new ArrayList<>();
    }

    public void agregarFragment(Fragment frag, String titulo) {
        miListaFragment.add(frag);
        miListaTitulosFragment.add(titulo);

    }

    @Override
    public Fragment getItem(int position) {
        return miListaFragment.get(position);
    }

    @Override
    public int getCount() {
        return miListaFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return miListaTitulosFragment.get(position);
    }
}
