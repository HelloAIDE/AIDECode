package code.aide.dn.com.aidecode.Adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import code.aide.dn.com.aidecode.R;
import code.aide.dn.com.aidecode.util.Utils;

/**
 * Created by GIGAMOLE on 7/27/16.
 */
public class HorizontalPagerAdapter extends PagerAdapter {

    private final Utils.LibraryObject[] LIBRARIES = new Utils.LibraryObject[]{
            new Utils.LibraryObject(R.drawable.blank_activity,"EmptyActivity"),
            new Utils.LibraryObject(R.drawable.basic_activity,"BasicActivity"),
            new Utils.LibraryObject(R.drawable.blank_activity_drawer,"DrawerActivity"),
            new Utils.LibraryObject(R.drawable.blank_activity_tabs,"TabActivity"),
            new Utils.LibraryObject(R.drawable.fullscreen_activity,"FullActivity"),
            new Utils.LibraryObject(R.drawable.login_activity,"LoginActivity"),
            new Utils.LibraryObject(R.drawable.scroll_activity,"ScrollActivity"),
            new Utils.LibraryObject(R.drawable.settings_activity,"SettingsActivity")
    };
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private boolean mIsTwoWay;

    public HorizontalPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return  LIBRARIES.length;
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem( ViewGroup container,  int position) {
        View view;
       view = mLayoutInflater.inflate(R.layout.item,container,false);
        Utils.setupImage(view,this.LIBRARIES[position]);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
}
