package com.example.smartbycicylelock.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartbycicylelock.BlueTooth.BluetoothLeService;
import com.example.smartbycicylelock.BlueTooth.SampleGattAttributes;
import com.example.smartbycicylelock.BlueTooth.ScanActivity;
import com.example.smartbycicylelock.InnerDB.DBOpenHelper;
import com.example.smartbycicylelock.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

//    public View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if(mGattCharacteristics != null){
//                final BluetoothGattCharacteristic characteristic = mGattCharacteristics.
//            }
//        }
//    };

    @Override
    protected void onResume() {
        super.onResume();

        // 블루투스 사용중인지 확인하고 아니면 요청창 띄우기
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        //
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//        if (mBluetoothLeService != null) {
//            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
//            Log.d(TAG, "Connect request result=" + result);
//        }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.gatt_services, menu);
//        if (mConnected) {
//            menu.findItem(R.id.menu_connect).setVisible(false);
//            menu.findItem(R.id.menu_disconnect).setVisible(true);
//            menu.findItem(R.id.menu_refresh).setVisible(false);
//        } else {
//            menu.findItem(R.id.menu_connect).setVisible(true);
//            menu.findItem(R.id.menu_disconnect).setVisible(false);
//            menu.findItem(R.id.menu_refresh).setVisible(false);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.menu_connect:
//                mBluetoothLeService.connect(mDeviceAddress);
//                return true;
//            case R.id.menu_disconnect:
//                mBluetoothLeService.disconnect();
//                return true;
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//     서비스가 연결 됐을 때, 안됐을 때 관리
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
//            if (!mBluetoothLeService.initialize()) {
//                Log.e(TAG, "Unable to initialize Bluetooth");
//                finish();
//            }
//            // 성공적으로 초기화가 완료되면 기기에 연결
//            mBluetoothLeService.connect(mDeviceAddress);
//        }

//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            // 기기에 연결 해제
//            mBluetoothLeService = null;
//        }
//    };

    // 서비스에서 발생하는 다양한 이벤트 처리
    // ACTION_GATT_CONNECTED: GATT 서버 연결.
    // ACTION_GATT_DISCONNECTED: GATT 서버에 연결 안됨.
    // ACTION_GATT_SERVICES_DISCOVERED: GATT 서비스 찾음.
    // ACTION_DATA_AVAILABLE: 기기로 부터 데이터 받고. 이것은 읽기 결과나 알림작업이 된다.
//    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
//                mConnected = true;
//                updateConnectionState(R.string.connected);
//                invalidateOptionsMenu();
//            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
//                mConnected = false;
//                updateConnectionState(R.string.disconnected);
//                invalidateOptionsMenu();
//                clearUI();
//            }
////            else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
//                // Show all the supported services and characteristics on the user interface.
////                displayGattServices(mBluetoothLeService.getSupportedGattServices());}
//             else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
//            }
//        }
//    };

//    // 텍스트뷰에 연결 확인
//    private void updateConnectionState(final int resourceId) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText(resourceId+"\n");
//            }
//        });
//    }
//
//    private void displayData(String data) {
//        if (data != null) {
//            textView.append(data+"\n");
//        }
//    }
//
//    private void clearUI() {
//        textView.append("no_data\n");
//    }
//
////    private void displayGattServices(List<BluetoothGattService> gattServices) {
////        if (gattServices == null) return; // 서비스가 없으면 리턴
////        String uuid = null; // uuid = null
////        String unknownServiceString = getResources().getString(R.string.unknown_service); // unknownService
////        String unknownCharaString = getResources().getString(R.string.unknown_characteristic); // unknownCharacteristic
////        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>(); // 해쉬 맵 배열 String, String -> gattServiceData
////        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
////                = new ArrayList<ArrayList<HashMap<String, String>>>(); // gattCharacteristic data -> 해쉬맵 배열 String String
////        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
////
////        // Loops through available GATT Services.
////        // 사용가능한 GATT 서비스를 루프
////        for (BluetoothGattService gattService : gattServices) {
////            HashMap<String, String> currentServiceData = new HashMap<String, String>();
////            uuid = gattService.getUuid().toString();
////            currentServiceData.put(
////                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
////            currentServiceData.put(LIST_UUID, uuid);
////            gattServiceData.add(currentServiceData);
////
////            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
////                    new ArrayList<HashMap<String, String>>();
////            List<BluetoothGattCharacteristic> gattCharacteristics =
////                    gattService.getCharacteristics();
////            ArrayList<BluetoothGattCharacteristic> charas =
////                    new ArrayList<BluetoothGattCharacteristic>();
////
////            // Loops through available Characteristics.
////            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
////                charas.add(gattCharacteristic);
////                HashMap<String, String> currentCharaData = new HashMap<String, String>();
////                uuid = gattCharacteristic.getUuid().toString();
////                currentCharaData.put(
////                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
////                currentCharaData.put(LIST_UUID, uuid);
////                gattCharacteristicGroupData.add(currentCharaData);
////            }
////            mGattCharacteristics.add(charas);
////            gattCharacteristicData.add(gattCharacteristicGroupData);
////        }
////
////        // 리스트 어댑터 설정
////        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(this,
////                gattServiceData,
////                android.R.layout.simple_expandable_list_item_2,
////                new String[] {LIST_NAME, LIST_UUID},
////                new int[] { android.R.id.text1, android.R.id.text2 },
////                gattCharacteristicData,
////                android.R.layout.simple_expandable_list_item_2,
////                new String[] {LIST_NAME, LIST_UUID},
////                new int[] { android.R.id.text1, android.R.id.text2 }
//        );
////        mGattServicesList.setAdapter(gattServiceAdapter);
////    }
//
//    private static IntentFilter makeGattUpdateIntentFilter() {
//        final IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
//        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
//        return intentFilter;
//    }
}
