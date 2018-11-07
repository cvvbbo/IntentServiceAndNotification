package demo.intentdemo.com.myapplication;

/**
 * Created by xiongzheng on 2018/11/7.
 */

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 更新包下载安装服务
 */
public class UpdateService extends IntentService {
    private static final int NOTIFY_DOWNLOAD= 0;
    private static final int NOTIFY_FINISH = 1;
    private static final String PENDING_INSTALL_ACTION = "demo.intentdemo.com.myapplication";
    private Context mContext;
    private String apkUrl;
    public UpdateService() {
        super("UpdateService");
    }
    private NotificationUtils notificationUtils;
    private File downapkfile;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //正在下载中
                    RemoteViews contentView = notificationUtils.getNotification().contentView;
                    contentView.setTextViewText(R.id.notify_tv, "更新包下载中...");
                    contentView.setProgressBar(R.id.notify_progress_pb, 100, msg.arg1, false);
                    contentView.setTextViewText(R.id.notify_progress_tv,msg.arg1+"%");
                    // 更新UI
                    notificationUtils.getManager().notify(NOTIFY_DOWNLOAD,notificationUtils.getNotification());
                    break;
                case 1:
                    notificationUtils.cancelNotification(NOTIFY_DOWNLOAD);
                    createNotification(NOTIFY_FINISH);
                    break;
            }
            return true;
        }
    });
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            apkUrl = intent.getStringExtra("apkurl");
            handleActionFoo(apkUrl);
        }
    }

    private void handleActionFoo(String param1) {
      //  if(NetworkUtils.isConnected()){
            createNotification(NOTIFY_DOWNLOAD);
            try {
                DownApk(param1);
            } catch (Exception e) {
                e.printStackTrace();
            }
      //  }
    }
    //发送消息进行更新进度条
    public void sendMessage(int what,int mprogress) {
        Message msg0 = mHandler.obtainMessage();
        msg0.what = what;
        msg0.arg1 = mprogress;
        mHandler.sendMessage(msg0);
    }


    private void DownApk(String param1) {
        int oldProcess = 0;
       // if (SDCardUtils.isSDCardEnable()){
            downapkfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/industry.apk");
            try {
                URL url = new URL(param1.trim());
                HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == 200){
                    InputStream inputStream = connection.getInputStream();
                    FileOutputStream fos = new FileOutputStream(downapkfile);
                    //总长度
                    int totalLength = connection.getContentLength();

                    //已下载的长度
                    int currentLength  = 0;
                    byte[] bytes = new byte[512];
                    connection.connect();
                    int flag = 0;
                    while (flag < 100){
                        if (inputStream != null){
                            int read = inputStream.read(bytes);
                            if (read <= 0){
                                sendMessage(1,0);
                                break;
                            }else {
                                fos.write(bytes,0,read);
                                currentLength += read;
                                int mprogress = (int) ((currentLength*100)/totalLength);
                                if(oldProcess <= mprogress-5 ){
                                    // 避免notifymanager ANR，每下载百分之5才进行通知一次
                                    oldProcess =mprogress;
                                    sendMessage(0, mprogress);
                                }
                            }
                        }
                    }
                    fos.close();
                    inputStream.close();
                }
                connection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

       // }

    }

    private void createNotification(int notifyId) {
        switch (notifyId){
            case NOTIFY_DOWNLOAD:
                RemoteViews remoteViews = new RemoteViews("demo.intentdemo.com.myapplication",R.layout.notify_custom_view_layout);
                remoteViews.setTextViewText(R.id.notify_tv,"正在下载...");
                remoteViews.setProgressBar(R.id.notify_progress_pb,100,0,false);
                remoteViews.setTextViewText(R.id.notify_progress_tv,"0%");
                notificationUtils.sendNotification(notifyId,"","",remoteViews,null);
                break;
            case NOTIFY_FINISH:
                Intent intent = new Intent(getApplicationContext(),NotificationBroadCast.class);
                intent.setAction(PENDING_INSTALL_ACTION);
                intent.putExtra("notifyId",NOTIFY_FINISH);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,0,intent, PendingIntent.FLAG_ONE_SHOT);
                notificationUtils.sendNotification(notifyId,"点击安装","更新包已下载完成",null,pendingIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        //初始化通知窗口管理
        notificationUtils = new NotificationUtils(getApplicationContext());
    }
}

