package sxh.com.fingerapplication;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private FingerprintManagerCompat mFingerManger;
    private KeyguardManager mKeyManger;
    private Button mStartBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFingerManger=FingerprintManagerCompat.from(this);
        mKeyManger= (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        mStartBtn= (Button) findViewById(R.id.start_button);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断Android版本 23
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    //判断权限
                    if (judgePermission()){
                        //各方面都具备可以进行
                        startActivity(new Intent(MainActivity.this,FingerActivity.class));
                    }
                }else{
                    Toast.makeText(MainActivity.this,"版本不够高",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 返回权限、硬件的判断
     * @return
     */
    public boolean judgePermission(){
        //权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请开启指纹识别权限", Toast.LENGTH_LONG).show();
            //可以进行动态申请
            return false;
        }
        //硬件是否支持指纹识别
        if (!mFingerManger.isHardwareDetected()){
            Toast.makeText(this, "您手机不支持指纹识别功能", Toast.LENGTH_LONG).show();
            return false;
        }
        //是否已经录入指纹
        if (!mFingerManger.hasEnrolledFingerprints()){
            Toast.makeText(this, "您还未录入指纹", Toast.LENGTH_LONG).show();
            return false;
        }
        //手机是否开启锁屏密码
        if (!mKeyManger.isKeyguardSecure()){
            Toast.makeText(this, "请开启开启锁屏密码，并录入指纹后再尝试", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
