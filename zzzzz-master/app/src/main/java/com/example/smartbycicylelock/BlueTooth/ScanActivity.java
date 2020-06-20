package com.example.smartbycicylelock.BlueTooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.smartbycicylelock.R;
import com.example.smartbycicylelock.activity.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ScanActivity extends AppCompatActivity {
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning = true;
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000; // 스캔 시간 10초
//    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2;

    private static int PERMISSION_REQUEST_CODE = 3;
    private final static String TAG = "ScanActivity";



    ListView list1;
    TextView text1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Request Location Permissions:");
//        // 버전 체크후 권한 확인
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mHandler = new Handler();
        list1 = findViewById(R.id.list1);
        text1 = findViewById(R.id.textView);

//        // ble 지원하는 기기인지 확인
//        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
//            finish();
//        }

        // 블루투스 어댑터 가져오기
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

//        // 블루투스를 지원하는 기기인지 확인
//        if (mBluetoothAdapter == null) {
//            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 블루투스가 사용중인지 확인하고 아니면 확인창 띄우기
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // 어댑터 세팅
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        list1.setAdapter(mLeDeviceListAdapter);
        // 리스트뷰 클릭 세팅
        list1.setOnItemClickListener(ListviewListener);

        scanLeDevice(true);
    }

    // 리스트뷰 클릭 리스너
    AdapterView.OnItemClickListener ListviewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final BluetoothDevice device = mLeDeviceListAdapter.getDevcie(position);
            if(device == null)
                return;

//            Intent ScanIntent = new Intent();
//            ScanIntent.putExtra("name", device.getName());
//            ScanIntent.putExtra("address", device.getAddress());

            final Intent intent = new Intent(getApplicationContext(), DeviceControlActivity.class);
            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
            if (mScanning) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                mScanning = false;
            }
            startActivity(intent);
//            setResult(RESULT_OK, ScanIntent);
//            finish();
        }
    };

    // 권한 확인 메서드 오버라이딩
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            //Do something based on grantResults
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "coarse location permission granted");
            } else {
                Log.d(TAG, "coarse location permission denied");
            }
        }
    }

    // 사용자가 권한 요청창에서 블루투스 사용 취소를 눌렀을 때 어플 종료
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    // 옵션 메뉴 구성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_menu, menu);
        if(!mScanning){
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setVisible(false);
        }
        else{
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_indeterminate_progress);
        }

        return true;
    }

    // 옵션 메뉴 선택시
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 스캔 기능
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }


    // 블루투스 콜백 함수
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("ddd", "device name : " + device.getName());
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    // 커스텀 리스트 어댑터
    class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevcies;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevcies = new ArrayList<BluetoothDevice>();
            mInflator = ScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!mLeDevcies.contains(device)) {
                mLeDevcies.add(device);
            }
        }

        public BluetoothDevice getDevcie(int position) {
            return mLeDevcies.get(position);
        }

        //        mLeDevcies.clear();
        public void clear() {
//            if(mLeDevcies != null){
            mLeDevcies.clear();
//            }

        }

        @Override
        public int getCount() {
            return mLeDevcies.size();
        }

        @Override
        public Object getItem(int position) {
            return mLeDevcies.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 재사용 가능한 뷰가 없다면 뷰를 생성
            if (convertView == null) {
                convertView = mInflator.inflate(R.layout.listitem_device, null);
            }
            // 뷰를 구성
            TextView sub_text = (TextView) convertView.findViewById(R.id.device_name);
            TextView sub_text2 = (TextView) convertView.findViewById(R.id.device_address);

            BluetoothDevice device = mLeDevcies.get(position);
            final String devcieName = device.getName();

            Log.d(TAG, "device :" + device.getName());
            if (devcieName != null && devcieName.length() > 0)
                sub_text.setText(devcieName);
            else
                sub_text.setText("Unknow device");
            sub_text2.setText(device.getAddress());

            return convertView;
        }
    }

}
