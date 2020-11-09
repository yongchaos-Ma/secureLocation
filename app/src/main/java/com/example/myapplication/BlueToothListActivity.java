package com.example.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import static android.content.ContentValues.TAG;

public class BlueToothListActivity extends Activity {

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private ArrayAdapter<String> listAdapter;
    private BluetoothAdapter mBtAdapter;
    int touched = 1;
    //广播接收者
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();//获取蓝牙设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    listAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (listAdapter.getCount() == 0) {
                    Toast.makeText(BlueToothListActivity.this, "没有设备", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_list);
        //注册广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        listAdapter = new ArrayAdapter<>(this, R.layout.bluetooth_device);
        ListView lv_device = findViewById(R.id.listView1);
        Button bt_find = findViewById(R.id.closebutton);
        lv_device.setAdapter(listAdapter);


        bt_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mBtAdapter.startDiscovery();
            }
        });

        //选择连接设备
        lv_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                    long arg3) {
                String info = ((TextView) v).getText().toString();
                if (info.equals("没有已配对设备")) {
                    Toast.makeText(getApplicationContext(), "没有已配对设备", Toast.LENGTH_LONG).show();
                } else {  //防止误点
                    String address = info.substring(info.length() - 17);
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
                        System.out.println(address);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                }
            }
        });
        PrintDevice();
    }

    //打印已配对设备
    public void PrintDevice() {
        //打印出已配对的设备
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                listAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            listAdapter.add("没有已配对设备");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }
}
