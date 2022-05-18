package com.example.myapplication.power_individual_demo;

import static com.example.myapplication.GetTime.dateToStamp;
import static com.example.myapplication.GetTime.getCurrentTime;

import android.util.Log;

public class BleCmd20_setTime extends BaseBleMessage {
    public static byte mTheCmd = 0x20;

    public byte[] getAllData() throws Exception {
        byte[] data=new byte[4];
        int a=10;

        String dateString=dateToStamp(getCurrentTime());//截至目前为止毫秒数。十进制
        Log.d("MainActivity","毫秒数为:"+dateString);
        Log.d("MainActivity","日期:"+getCurrentTime());

        Integer ten=Integer.parseInt(dateString) + 28800;
        Log.d("MainActivity","十进制为:"+ten);


        String sixteen=String.format("%08X",ten);
        Log.d("MainActivity","十六进制为:"+sixteen);


        //int int1=Integer.parseInt(sixteen.substring(0,2), 10);
        int int1=OxStringtoInt(sixteen.substring(0,2));
//        int int2=Integer.parseInt(sixteen.substring(2,4), 10);
//        int int3=Integer.parseInt(sixteen.substring(4,6), 10);
//        int int4=Integer.parseInt(sixteen.substring(6,8), 10);
        int int2=OxStringtoInt(sixteen.substring(2,4));
        int int3=OxStringtoInt(sixteen.substring(4,6));
        int int4=OxStringtoInt(sixteen.substring(6,8));
        Log.d("MainActivity","int1为:"+int1);


        data[0]=(byte)(int4 & 0xff);//存十进制数是十进制，存十六进制还是10进制
        data[1]=(byte)(int3 & 0xff);
        data[2]=(byte)(int2 & 0xff);
        data[3]=(byte)(int1 & 0xff) ;

        Log.d("MainActivity","data为:"+ data[3]);
//        Log.d("MainActivity","data为:"+ data[1]);
//        Log.d("MainActivity","data为:"+ data);

        //data是长为四个字节的时间
        return setMessageByteData(mTheCmd, data, data.length);

    }

    public static int OxStringtoInt(String ox) throws Exception {
        ox=ox.toLowerCase();
        if(ox.startsWith("0x")){
            ox=ox.substring(2);
        }
        int ri = 0;
        int oxlen = ox.length();
        if (oxlen > 8)
            throw (new Exception("too lang"));
        for (int i = 0; i < oxlen; i++) {
            char c = ox.charAt(i);
            int h;
            if (('0' <= c && c <= '9')) {
                h = c - 48;
            } else if (('a' <= c && c <= 'f'))
            {
                h = c - 87;

            }
            else if ('A' <= c && c <= 'F') {
                h = c - 55;
            } else {
                throw (new Exception("not a integer "));
            }
            byte left = (byte) ((oxlen - i - 1) * 4);
            ri |= (h << left);
        }
        return ri;

    }

}
