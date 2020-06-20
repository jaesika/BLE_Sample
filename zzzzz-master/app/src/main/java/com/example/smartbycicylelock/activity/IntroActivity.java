package com.example.smartbycicylelock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.smartbycicylelock.R;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        IntroThread introThread = new IntroThread(handler);
        introThread.start();


        // intro 부분 gifImage 작동 할 수 있도록 하는 함수.
        ImageView starting = (ImageView)findViewById(R.id.start);
        GlideDrawableImageViewTarget gifImage =  new GlideDrawableImageViewTarget(starting);
        Glide.with(this).load(R.drawable.start).into(gifImage);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1)
            {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    };
}
