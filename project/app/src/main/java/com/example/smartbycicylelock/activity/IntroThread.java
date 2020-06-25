package com.example.smartbycicylelock.activity;


import android.os.Handler;
import android.os.Message;

public class IntroThread extends Thread{

    private Handler handler;

    public IntroThread(Handler handler) // 메세지를 전송하기위해 handler 객체 생성
    {
        this.handler = handler;
    }

    @Override
    public void run() {
        Message msg = new Message();

        try{
            Thread.sleep(2000);
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        super.run();

    }
}
