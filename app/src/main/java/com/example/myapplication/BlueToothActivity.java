package com.example.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class BlueToothActivity extends BaseActivity {

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private ListView lv_rx;// 可以定义一个接收文本框
    public TextView tv_lo;
    private EditText et_send;
    private ArrayAdapter<String> messageAdapter;

    @Override
    public void init() {

        //super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_main);
        lv_rx = findViewById(R.id.lv_rx);
        tv_lo = findViewById(R.id.tv_lo);
        et_send = findViewById(R.id.et_send);
        Button bt_send = findViewById(R.id.bt_send);
        Button bt_on = findViewById(R.id.bt_on);
        Button bt_off = findViewById(R.id.bt_off);

        messageAdapter = new ArrayAdapter<>(this, R.layout.message_show_up);
        lv_rx.setAdapter(messageAdapter);

        // 接收数据进程
        ReceiveData receivethread = new ReceiveData();// 连接成功后开启接收数据服务
        receivethread.start();
        // 判断蓝牙是否可用
        if (mBtAdapter == null) {
            Toast.makeText(this, "蓝牙是不可用的", Toast.LENGTH_LONG).show();
//            finish();
//            return;
        }
        // 发送数据
        bt_send.setOnClickListener(arg0 -> {
            String msg = et_send.getText().toString();
            if (CONNECT_STATUS) {
                write(msg);
            } else {
                Toast.makeText(getApplicationContext(), "请先连接蓝牙", Toast.LENGTH_SHORT).show();
            }
        });
        // 按键1——显示自身位置
        bt_on.setOnClickListener(arg0 -> {
            if (CONNECT_STATUS) {
//                double latdata = LocationActivity.latitude;
//                double longdata = LocationActivity.longitude;
//                float accurdata = LocationActivity.accuracy;
//                float direcdata = LocationActivity.direction;
//                String showloc = latdata + "---" + longdata + "---";
//                showloc = showloc.replace("\\n","\n");
//                tv_lo.setText(showloc);
            } else {
                Toast.makeText(getApplicationContext(), "请先连接蓝牙", Toast.LENGTH_SHORT).show();
            }
        });
        // 按键2
        bt_off.setOnClickListener(arg0 -> {
            if (CONNECT_STATUS) {
                write("BUTTON2");
            } else {
                Toast.makeText(getApplicationContext(), "请先连接蓝牙", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // 连接设备
    public void ConnectThread(BluetoothDevice device) {
            // 接收数据进程
            ReceiveData receivethread = new ReceiveData();// 连接成功后开启接收数据服务
            receivethread.start();
    }

    // 发送数据
    public void write(String str) {
        str = "%" + SelfNumber + "|" + str + "|" +"\n";
        str = str.replace("\\n","\n");
        byte[] buffer = str.getBytes();
        if (CONNECT_STATUS) {
            try {
                mmOutStream = mmSocket.getOutputStream();
                mmOutStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Intent接收器，返回结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONNECT_DEVICE) {
            // 当DeviceListActivity返回与设备连接的消息
            if (resultCode == Activity.RESULT_OK) {
                // 得到链接设备的MAC
                String address = data.getExtras().getString(
                        BlueToothListActivity.EXTRA_DEVICE_ADDRESS, "");
                // 得到BLuetoothDevice对象
                if (!TextUtils.isEmpty(address)) {
                    BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
                    ConnectThread(device);
                    // new BlueToothActivity().ConnectThread(device);
                }
            }
        }
    }
    // 读数据线程
    public class ReceiveData extends Thread {
        public ReceiveData() {
                try {
                    mmInStream = mmSocket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        @Override
        public void run() {
            int messagebytes= 0;
            int ch2;
            byte[] messagebuffer = new byte[4096];
            while (true) {
                try { // 接收数据
                    messagebytes = 0;
                        while ((ch2 = mmInStream.read()) != '\n')
                        {
                            if(ch2 != -1){
                                messagebuffer[messagebytes] = (byte) ch2;
                                messagebytes++;
                            }
                        }
                        messagebuffer[messagebytes] = (byte) '\n';
                        messagebytes++;
                        String readStrings = new String(messagebuffer, 0, messagebytes);
                        if(readStrings.startsWith("%")){
                            readStrings = readStrings.replace("%","");
                            String[] readStrArray = readStrings.split("//");
                            String ReceivedNum = readStrArray[0];
                            String ReceivedMsg = readStrArray[1];
                            String MessageShow = readStrArray[0] + ": " + readStrArray[1];
                            runOnUiThread(() -> messageAdapter.add(MessageShow));
                            System.out.println(MessageShow);
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
