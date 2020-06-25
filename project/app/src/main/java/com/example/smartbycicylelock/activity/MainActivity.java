package com.example.smartbycicylelock.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartbycicylelock.BlueTooth.BluetoothLeService;
import com.example.smartbycicylelock.BlueTooth.ScanActivity;
import com.example.smartbycicylelock.InnerDB.DBOpenHelper;
import com.example.smartbycicylelock.R;
import com.example.smartbycicylelock.map.Map_main;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    //main activity를 자물쇠 화면으로 설정할거임
    private static final String DATABASE_NAME = "Bicycle";
    private static final int DATABASE_VERSION = 1;
    private DBOpenHelper dbhelp = new DBOpenHelper(MainActivity.this, DATABASE_NAME, null, 1);
    private Button Find_Button;

    // 기기 정보
    private static final int SCAN_ACTIVITY_CODE = 100;
    private String mDeviceName;
    private String mDeviceAddress;

    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 2;
    private static int PERMISSION_REQUEST_CODE = 3;

    // 스캔후 필요한 변수
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    TextView textView;
    Button button;

    String[] permission_list = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        // 버전 체크후 권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        // 블루투스 어댑터
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 블루투스를 지원하는 기기인지 확인
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 기기 찾기 버튼
        textView = (TextView) findViewById(R.id.data_view);
        button = (Button) findViewById(R.id.button2);
        Find_Button = (Button)findViewById(R.id.find_button);
        final ActionBar actionBar = getSupportActionBar();
        Find_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent, SCAN_ACTIVITY_CODE);
            }
        });
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        for (String permission : permission_list) {
            int chk = checkCallingOrSelfPermission(permission);
            // 권한 거부 상태라면
            if (chk == PackageManager.PERMISSION_DENIED) {
                requestPermissions(permission_list, 0);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        // 블루투스 사용중인지 확인하고 아니면 요청창 띄우기
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(mServiceConnection);
//        mBluetoothLeService = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ScanActivity 에서 넘어오는 데이터
        switch (requestCode){
            case SCAN_ACTIVITY_CODE:
                if(resultCode == RESULT_OK){
                    mDeviceName = data.getStringExtra("name");
                    mDeviceAddress = data.getStringExtra("address");
                    Log.d("yoojs : ",  mDeviceName + " ||||| "  + mDeviceAddress);
                }
                break;
            default:
                break;
        }
    }
}
