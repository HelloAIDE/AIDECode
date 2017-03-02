package code.aide.dn.com.aidecode.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import code.aide.dn.com.aidecode.Core.CrashHandler;
import code.aide.dn.com.aidecode.Model.CrashData;
import code.aide.dn.com.aidecode.R;

public class CrashActivity extends AppCompatActivity {
    private TextView msg;
    private Button send;
    private boolean isSend;
    private CrashData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        //绑定控件
        msg = (TextView) findViewById(R.id.crash_msg);
        send = (Button) findViewById(R.id.crash_send);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String err = bundle.getString("err");
        data = (CrashData) bundle.getSerializable("data");

        if (err == "" || null == err) {
            err = "系统错误，请联系开发者：QQ201309512";
        }
        msg.setMovementMethod(ScrollingMovementMethod.getInstance());
        msg.setText("异常信息：\n"+err);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isSend) {
                    CrashHandler handler = CrashHandler.getInstance();
                    handler.sendPreviousReportsToServer(data);
                    Snackbar.make(v, "感谢您的反馈！", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    isSend = true;
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            finish();
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(timerTask,2000);

                } else {
                    Snackbar.make(v, "您已经提交过这条异常信息咯~", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(this,"即将退出",Toast.LENGTH_SHORT).show();
            android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        }
        return false;
    }

}
