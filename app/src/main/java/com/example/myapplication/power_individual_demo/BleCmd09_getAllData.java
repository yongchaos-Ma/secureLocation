package com.example.myapplication.power_individual_demo;

import android.util.Log;

public class BleCmd09_getAllData extends BaseBleMessage {
    //	byte cmd_close_ring = 0x01;
    public static byte mTheCmd = 0x06;

    public byte[] getAll() {
        byte[] data = new byte[1];

        data[0] = 0x00;

        return setMessageByteData(mTheCmd, data, data.length);
    }

    public byte[] dealBleResponse(byte[] notifyData, int dataLen) {
        int power;

        power = (int)notifyData[0];
        for (int i = 0; i < dataLen; i++) {// 输出结果
            Log.e("", "back_PhoneRemind() data["+i+"] = "+notifyData[i]);
        }
        return null;
    }

}
