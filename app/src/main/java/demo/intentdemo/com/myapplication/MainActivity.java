package demo.intentdemo.com.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT>=23){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }
    }

    public void haha(View view){
        Intent intent = new Intent(MainActivity.this,UpdateService.class);
        intent.putExtra("apkurl", "https://15bd1b599f7a3e950097ff61106922db.dd.cdntips.com/imtt.dd.qq.com/16891/D4109853CA50D9FA7C3AAF99DD2C16AB.apk?mkey=5be2987b7ccf5cef&f=24c3&fsname=com.snda.wifilocating_4.3.15_181031.apk&csr=1bbd&cip=124.207.122.26&proto=https");
        startService(intent);
    }




}
