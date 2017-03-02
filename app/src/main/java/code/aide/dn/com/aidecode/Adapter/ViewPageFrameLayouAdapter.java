package code.aide.dn.com.aidecode.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import code.aide.dn.com.aidecode.Fragment.Fragment1;
import code.aide.dn.com.aidecode.Fragment.Fragment2;

/**
 * Created by 健 on 2017/1/12.
 */

public class ViewPageFrameLayouAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private Context context;
    public ViewPageFrameLayouAdapter(FragmentManager fm,Context context,String[] titles) {
        super(fm);
        this.context = context;
        this.titles = titles;
    }
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment1(context);
            case 1:
                return new Fragment2(context);
        }
        return null;
    }
}
