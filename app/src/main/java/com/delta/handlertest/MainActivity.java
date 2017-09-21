package com.delta.handlertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Button btnShowToast;
    private Button btnTestPost;
    private Button btnTestMyLooper;
    private OtherThread otherThread;
    private TextView tvIndex;
    private Handler mHandler = new Handler();
    private HandlerThread mHandlerThread;
    private Handler mThreadHandler;
    private boolean isUpdateInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnShowToast = (Button)findViewById(R.id.btnShowToast);
        btnTestPost = (Button) findViewById(R.id.btnTestPost);
        btnTestMyLooper = (Button) findViewById(R.id.btnTestMyLooper);
        tvIndex = (TextView) findViewById(R.id.tvIndex);
        btnShowToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });
        btnTestPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "post成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        otherThread = new OtherThread();
        otherThread.start();
        btnTestMyLooper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherThread.otherHandeler.sendEmptyMessage(1);
            }
        });

        initBackThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isUpdateInfo = true;
        mThreadHandler.sendEmptyMessage(1);

    }

    @Override
    protected void onPause() {
        super.onPause();
        isUpdateInfo = false;
        mThreadHandler.removeMessages(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit();
    }

    private void initBackThread() {
        mHandlerThread = new HandlerThread("check-state");
        mHandlerThread.start();
        mThreadHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                checkForUpdate();
                if (isUpdateInfo) {
                    mThreadHandler.sendEmptyMessageDelayed(1, 1000);
                }
            }


        };
    }

    private void checkForUpdate() {
        try {
            Thread.sleep(1000);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String result = "指数：%d";
                    result = String.format(result, (int) (Math.random() * 3000 + 1000));
                    tvIndex.setText(result);

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
