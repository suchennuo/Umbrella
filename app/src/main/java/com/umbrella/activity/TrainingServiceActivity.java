package com.umbrella.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.umbrella.service.TrainingService;
import com.umbrella.sharedemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrainingServiceActivity extends AppCompatActivity {

    private static final String TAG = "TrainingServiceActivity";

    @BindView(R.id.bt_service_start)
    Button btStart;

    @BindView(R.id.bt_service_stop)
    Button btStop;

    Intent intent;

    private TrainingService.MyBinder mybinder;

    /** Command to the service to display a message */
    private Messenger sender, receiver;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            mybinder = (TrainingService.MyBinder) service;
//            mybinder.startDownload();

            sender = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_service);
        ButterKnife.bind(this);

        intent = new Intent(this, TrainingService.class);
        Log.d(TAG, "Thread id " + Thread.currentThread().getId());

        receiver = new Messenger(receiverHandler);
    }

    //双向绑定
    public static final int MSG_MUTUAL = 1;

    Handler receiverHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MSG_MUTUAL:
                    Toast.makeText(getApplicationContext(), "hello! " + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };


    @OnClick(R.id.bt_service_stop)
    public void stopService(){
        stopService(intent);
    }

    @OnClick(R.id.bt_service_start)
    public void startService(){
        startService(intent);
    }

    /**
     * BIND_AUTO_CREATE 自动创建 service
     * 调用流程 onCreate()->customFunction();
     * 销毁调用 unbindService()
     * 调用流程 onDestroy()
     */
    @OnClick(R.id.bt_service_bind)
    public void bindService(){
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    /**
     * 解除 service 和 activity 的关联
     */
    @OnClick(R.id.bt_service_unbind)
    public void unbindService(){
        unbindService(connection);
    }

    //一个 service 必须在既没有和任何 activity 关联又处于停止状态，才会被销毁
    @OnClick(R.id.bt_service_messenger)
    public void sayHello(View v){
        Message msg = Message.obtain(); //null, TrainingService.MSG_SAY_HELLO, 0, 0);
        msg.what = TrainingService.MSG_SAY_HELLO;
        msg.obj = "From activity.";
        msg.replyTo = receiver; //提供给 Service 使用，来给 activity 响应的目标
        try {
            sender.send(msg);
        } catch (RemoteException e){
            Log.e(TAG, e+"");
        }
    }

}
