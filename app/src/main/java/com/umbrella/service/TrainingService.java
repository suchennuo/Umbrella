package com.umbrella.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.umbrella.activity.MainActivity;
import com.umbrella.activity.TrainingServiceActivity;
import com.umbrella.sharedemo.R;

/**
 * Created by Zhang.Y.C on 2017/7/4.
 */

public class TrainingService extends Service {
    public static final String TAG = "TrainingService";
    /**
     * 扩展 Binder 类
     * 1. 创建 Binder 实例
     * 2. onBind() 回调方法返回此 Binder 实例
     * 3. client ，从 onServiceConnected() 回调方法接受 Binder, 并使用提供的方法调用绑定服务
     */
    private MyBinder binder = new MyBinder();

    /**
     * 使用 Message 处理 service 与 远程进程间的通信，有别与 AIDL.
     * Message 方式可让服务器一次处理一个调用，而不用像 AIDL 那样，面临多进程管理问题。
     * 1. Service 实现一个 Handler
     * 2. Messenger 创建一个 IBinder, service 通过 onBind() 使其返回客户端
     * 3. Client 使用 IBinder 将 Message 实例化，然后使用后者将 Message 对象发送给 Service
     * 4. Service 在其 Handler 中接受每个 Message
     */
    public static final int MSG_SAY_HELLO = 1;
    Handler handleMessage = new  Handler() {

        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case  MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(), "hello! " + msg.obj, Toast.LENGTH_SHORT).show();

                    Messenger messenger = msg.replyTo;
                    Message message = Message.obtain();
                    message.what = TrainingServiceActivity.MSG_MUTUAL;
                    message.obj = "From service.";
                    try {
                        messenger.send(message);
                    } catch (RemoteException e){
                        Log.e(TAG, "" + e);
                    }

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    final Messenger messenger = new Messenger(handleMessage);

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        Log.d(TAG, "thread id " + Thread.currentThread().getId());

        //一下代码是创建前台 service
//        Notification notification = new Notification(R.drawable.notice, "You have a message.", System.currentTimeMillis());
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        notification.setLatestEventInfo(this, "Notification Title ", "Content", pendingIntent);
//        startForeground(1, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand() executed");

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 执行后台耗时任务
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() executed.");
        //扩展 Binder 类
//        return binder;

        //When binding to the service, we return an interface to our messenger for sending messages to the service.
        return messenger.getBinder();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    public class MyBinder extends Binder{
        public void startDownload(){
            Log.d(TAG, "startDownload() executed");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 执行具体下载任务
                }
            }).start();
        }
    }
}
