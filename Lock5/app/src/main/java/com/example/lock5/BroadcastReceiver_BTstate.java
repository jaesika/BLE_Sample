package com.example.lock5;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastReceiver_BTstate extends BroadcastReceiver {

    Context activityContext;

    public BroadcastReceiver_BTstate(Context activityContext){
        this.activityContext = activityContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

        switch (state) {
            case BluetoothAdapter.STATE_OFF:
                Utils.toast(activityContext, "Bluetooth is off");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                Utils.toast(activityContext, "Bluetooth is turning off...");
                break;
            case BluetoothAdapter.STATE_ON:
                Utils.toast(activityContext, "Bluetooth is on");
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                Utils.toast(activityContext, "Bluetooth is turning on...");
                break;
        }
    }
}
