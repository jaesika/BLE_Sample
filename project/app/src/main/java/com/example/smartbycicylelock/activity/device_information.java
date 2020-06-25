package com.example.smartbycicylelock.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.smartbycicylelock.InnerDB.DBOpenHelper;
import com.example.smartbycicylelock.R;

public class device_information extends AppCompatActivity {

    TextView device_name;
    TextView device_battery;
    TextView device_gps;

    private DBOpenHelper dbHelper;
    SQLiteDatabase database;
    private static String DATABASENAME= "Bicycle";
    private static int VERSION = 1;

    public void openDatabase()
    {
        database = openOrCreateDatabase("Bicycle", MODE_PRIVATE, null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);
        openDatabase();
        dbHelper = new DBOpenHelper(this, DATABASENAME, null, VERSION);
        device_name = (TextView)findViewById(R.id.device_n);
        device_name.setText(dbHelper.get_name());
    }
}
