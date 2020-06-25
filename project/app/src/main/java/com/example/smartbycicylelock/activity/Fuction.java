package com.example.smartbycicylelock.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.smartbycicylelock.R;
import com.example.smartbycicylelock.map.Map_main;

public class Fuction extends AppCompatActivity {

    Button locking;
    Button notification;
    Button find_location;
    Button user_info;
    Button device_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuction);

        locking = (Button)findViewById(R.id.unlocking_button);
        notification = (Button)findViewById(R.id.notification_button);
        find_location = (Button)findViewById(R.id.find_location_button);
        user_info = (Button)findViewById(R.id.user_info);
        device_info = (Button)findViewById(R.id.device_info);

        //finde_location 버튼 함수 설정
        //-------------------------------------------------
        find_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fuction.this, Map_main.class);
                startActivity(intent);
            }
        });

        device_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fuction.this, device_information.class);
                startActivity(intent);
            }
        });

        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fuction.this, User_information.class);
                startActivity(intent);
            }
        });
    }
}
