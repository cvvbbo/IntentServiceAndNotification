package demo.intentdemo.com.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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


        //最详细的那篇博客有讲这些路径的区别在哪里
        //内部存储空间应用私有目录下的 files/ 目录
        File filesDir = getFilesDir();
        //内部存储空间应用私有目录下的 cache/ 目录
        File cacheDir = getCacheDir();
        //外部存储空间根目录
        File file = Environment.getExternalStorageDirectory();
        // 外部存储空间应用私有目录下的 files/
        File file1 = getExternalFilesDir(null);
        //外部存储空间应用私有目录下的 cache/
        File file2 = getExternalCacheDir();
        Log.e("---- :",file2+"");

    }

    public void haha(View view){
        Intent intent = new Intent(MainActivity.this,UpdateService.class);
        intent.putExtra("apkurl", "https://7b167e115c7ee4e8a3196b952823615c.dd.cdntips.com/imtt.dd.qq.com/16891/008C065B97FEF232D2FC7DC48627F1AC.apk?mkey=5be3cf907ccf5cef&f=0c99&fsname=com.glove.compass_1.0_25.apk&csr=1bbd");
        startService(intent);
    }




}
