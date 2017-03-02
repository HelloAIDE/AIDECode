package code.aide.dn.com.aidecode.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import code.aide.dn.com.aidecode.R;

public class UserInfoActivity extends AppCompatActivity {
    private Toolbar infoTool;
    private ImageView infoImageAvater;
    private TextView infoTVName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        init();
    }

    private void init() {

    }
}
