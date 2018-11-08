package demo.intentdemo.com.myapplication;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by xiongzheng on 2018/11/7.
 */



public class NotificationBroadCast extends BroadcastReceiver {

    /**
     *
     *  这个是手动点击通知栏弹出更新的逻辑
     *
     *
     * */

    private File downFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/industry.apk");
    private static final String INSTALL_ACTION = "demo.intentdemo.com.myapplication";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(INSTALL_ACTION)){
            int notifyId = intent.getIntExtra("notifyId", 0);
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(notifyId);
            if (downFile.exists() && downFile.length() > 0) {
                //安装
               // AppUtils.installApp(downFile, "gaoxin.com.inforindustry.fileprovider");
                Toast.makeText(context,"开始安装",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

