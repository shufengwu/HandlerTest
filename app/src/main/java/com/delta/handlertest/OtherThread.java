/**
 * 工作线程创建消息队列
 */
package com.delta.handlertest;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by Shufeng.Wu on 2017/9/21.
 */

public class OtherThread extends Thread {
    public static final String TAG = OtherThread.class.getSimpleName();
    Handler otherHandeler;

    @Override
    public void run() {
        Looper.prepare();
        otherHandeler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        Log.i(TAG, "handleMessage: ");
                        break;
                    default:
                        break;
                }
            }
        };
        Looper.loop();

    }
}
