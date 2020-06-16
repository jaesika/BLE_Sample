package com.example.lock5;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    final static int REQUEST_ENABLE_BT = 1;

    private HashMap<String, BTLE_Device> mBTDevicesHashMap;
    private ArrayList<BTLE_Device> mBTDevicesArrayList;
    private ListAdapter_BTLE_Devices adapter;
    private Scanner_BTLE mBTLeScanner;

    private Button btn_Scan;

    private BroadcastReceiver_BTstate mBTstateUpdateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Utils.toast(getApplicationContext(), "BLE 지원 불가");
            finish();
        }

        mBTstateUpdateReceiver = new BroadcastReceiver_BTstate(getApplicationContext());
        mBTLeScanner = new Scanner_BTLE(this, 15000, -75);

        mBTDevicesHashMap = new HashMap<>();
        mBTDevicesArrayList = new ArrayList<>();

        adapter = new ListAdapter_BTLE_Devices(this,  R.layout.btle_device_list_item, mBTDevicesArrayList);

        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        ((ScrollView)findViewById(R.id.scrollView)).addView(listView);

        btn_Scan = findViewById(R.id.btn_scan);
        findViewById(R.id.btn_scan).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(mBTstateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBTstateUpdateReceiver);
        stopScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                //
            } else if (resultCode == RESULT_CANCELED) {
                Utils.toast(getApplicationContext(), "블루투스를 켜주세요");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_scan:
                Utils.toast(getApplicationContext(), "Scan Button Pressed");

                if(!mBTLeScanner.isScanning()){
                    startScan();
                }
                else {
                    stopScan();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }


    public void stopScan(){
        btn_Scan.setText("Scan Agin");

        mBTLeScanner.stop();
    }

    public void startScan(){
        btn_Scan.setText("Scanning....");

        mBTDevicesArrayList.clear();
        mBTDevicesHashMap.clear();

        adapter.notifyDataSetChanged();
        Log.d("test", "startScan 전에");
        mBTLeScanner.start();
    }

    public void addDevice(BluetoothDevice device, int new_rssi) {
        String address = device.getAddress();

        if(!mBTDevicesHashMap.containsKey(address)){
            BTLE_Device btle_device = new BTLE_Device(device);
            btle_device.setRSSI(new_rssi);

            mBTDevicesHashMap.put(address, btle_device);
            mBTDevicesArrayList.add(btle_device);
            // 로그
            Log.d("test", device.getName() + "33");
        }
        else {
            mBTDevicesHashMap.get(address).setRSSI(new_rssi);
        }
        adapter.notifyDataSetChanged();
    }
}
