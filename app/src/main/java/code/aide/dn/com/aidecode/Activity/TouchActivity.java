package code.aide.dn.com.aidecode.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import code.aide.dn.com.aidecode.R;

/**
 * Created by 大牛哥 on 2017/2/17.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class TouchActivity extends AppCompatActivity {
    SlidrConfig mSlidrConfig;
    SlidrConfig.Builder mBuilder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int primary = getResources().getColor(R.color.colorPrimary);
        int secondary = getResources().getColor(R.color.colorAccent);
/*      .primaryColor(primary)//滑动时状态栏的渐变结束的颜色
        .secondaryColor(secondary)//滑动时状态栏的渐变开始的颜色
        .scrimColor(Color.BLACK)//滑动时Activity之间的颜色
        . position(SlidrPosition.LEFT)//从左边滑动
        .scrimStartAlpha(0.8f)//滑动开始时两个Activity之间的透明度
        .scrimEndAlpha(0f)//滑动结束时两个Activity之间的透明度
        .velocityThreshold(5f)//超过这个滑动速度，忽略位移限定值就切换Activity
        .distanceThreshold(.35f);//滑动位移占屏幕的百分比，超过这个间距就切换Activity */
        mBuilder = new SlidrConfig.Builder().primaryColor(primary)
                .secondaryColor(secondary)
                .scrimColor(Color.BLACK)
                .position(SlidrPosition.LEFT)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(5f)
                .distanceThreshold(.35f);
        mSlidrConfig = mBuilder.build();
        Slidr.attach(this, mSlidrConfig);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.translate_right_left, R.anim.translate_left_right);
    }
}
