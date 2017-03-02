package code.aide.dn.com.aidecode.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.model.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.model.MaterialAboutTitleItem;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import code.aide.dn.com.aidecode.R;
import code.aide.dn.com.aidecode.util.Tools;

public class AboutActivity extends MaterialAboutActivity {
    SlidrConfig mSlidrConfig;
    SlidrConfig.Builder mBuilder;
    @Override
    protected MaterialAboutList getMaterialAboutList() {
        //第一个卡片
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text("AIDE CODE")
                .icon(R.mipmap.icon)
                .build());
        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Version")
                .subText(Tools.getVersion(this))
                .icon(R.mipmap.info)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {

                    }
                })
                .build());
        //第二个卡片
        MaterialAboutCard.Builder appCardBuilder1 = new MaterialAboutCard.Builder();
        appCardBuilder1.title("开发者");
        appCardBuilder1.addItem(new MaterialAboutActionItem.Builder()
                .text("大牛哥")
                .subText("QQ201309512")
                .icon(R.mipmap.user)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Uri uri = Uri.parse("http://wpa.qq.com/msgrd?v=1&uin=201309512&site=houdao.com&menu=yes");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);


                    }
                })
                .build());
        appCardBuilder1.addItem(new MaterialAboutActionItem.Builder()
                .text("工具发布")
                .subText("内测密码为大牛哥QQ")
                .icon(R.mipmap.tieba)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Uri uri = Uri.parse("https://www.pgyer.com/aidecode");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                })
                .build());
        appCardBuilder1.addItem(new MaterialAboutActionItem.Builder()
                .text("官方QQ群")
                .subText("技术交流群：186117943")
                .icon(R.mipmap.qqgroup)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        joinQQGroup("pNdOu_42QG7oSMUnGC_NJhY0VqwSDFiB");
                    }
                })
                .build());
        //第三个卡片
        MaterialAboutCard.Builder appCardBuilder2 = new MaterialAboutCard.Builder();
        appCardBuilder2.addItem(new MaterialAboutActionItem.Builder()
                .text("LICENSE")
                .icon(R.mipmap.icon_license)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this, R.style.MyAlertDialogStyle);
                        builder.setTitle("LICENSE");
                        builder.setMessage("Copyright 2017 大牛哥\n" +
                                "\n" +
                                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                                "you may not use this file except in compliance with the License.\n" +
                                "You may obtain a copy of the License at\n" +
                                "\n" +
                                "http://www.apache.org/licenses/LICENSE-2.0\n" +
                                "\n" +
                                "Unless required by applicable law or agreed to in writing, software\n" +
                                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                                "See the License for the specific language governing permissions and\n" +
                                "limitations under the License.");
                        builder.show();
                    }
                })
                .build());
        return new MaterialAboutList.Builder().addCard(appCardBuilder.build()).addCard(appCardBuilder1.build()).addCard(appCardBuilder2.build())
                .build();
    }

    @Override
    protected CharSequence getActivityTitle() {
        return "关于";
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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
    /****************
     *
     * 发起添加群流程。群号：AIDE-技术交流总群(186117943) 的 key 为： pNdOu_42QG7oSMUnGC_NJhY0VqwSDFiB
     * 调用 joinQQGroup(pNdOu_42QG7oSMUnGC_NJhY0VqwSDFiB) 即可发起手Q客户端申请加群 AIDE-技术交流总群(186117943)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}
