package com.example.lock5;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

public class Scanner_BTLE {
    private MainActivity ma;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private android.os.Handler mHandler;

    private long scanPeriod;
    private int signalStrength;

    public Scanner_BTLE(MainActivity mainActivity, long scanPeriod, int signalStrength){
        ma = mainActivity;
        mHandler = new Handler();

        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;

        final BluetoothManager bluetoothManager = (BluetoothManager)ma.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    public boolean isScanning(){
        return mScanning;
    }

    public void start(){
        // 블루투스 상태 체크
        if(!Utils.checkBluetooth(mBluetoothAdapter)){
            Utils.requestUserBluetooth(ma);
            ma.stopScan();
        }
        else{
            scanLeDevice(true);
        }
    }

    public void stop(){
        scanLeDevice(false);
    }

    private void scanLeDevice(final boolean enable){
        if(enable && !mScanning){
            Utils.toast(ma.getApplication(), "Starting BLE Scan...");

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.toast(ma.getApplication(), "Stopping BLE Scan...");

                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    ma.stopScan();
                }
            }, scanPeriod);

            mScanning = true;

            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            final int new_rssi = rssi;
            if(rssi > signalStrength){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ma.addDevice(device, new_rssi);
                    }
                });
            }
        }
    };
}
