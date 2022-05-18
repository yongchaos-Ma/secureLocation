package com.example.myapplication;

import static com.example.myapplication.BaseActivity.GET_WARNED;
import static com.example.myapplication.BaseActivity.SelfNumber;
import static com.example.myapplication.BaseActivity.getWarnedTypes;
import static com.example.myapplication.GetTime.dateToStamp;
import static com.example.myapplication.GetTime.format;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.everyonelitepal.Node1;
import com.example.myapplication.everyonelitepal.Node10;
import com.example.myapplication.everyonelitepal.Node11;
import com.example.myapplication.everyonelitepal.Node12;
import com.example.myapplication.everyonelitepal.Node13;
import com.example.myapplication.everyonelitepal.Node14;
import com.example.myapplication.everyonelitepal.Node15;
import com.example.myapplication.everyonelitepal.Node16;
import com.example.myapplication.everyonelitepal.Node17;
import com.example.myapplication.everyonelitepal.Node18;
import com.example.myapplication.everyonelitepal.Node19;
import com.example.myapplication.everyonelitepal.Node2;
import com.example.myapplication.everyonelitepal.Node20;
import com.example.myapplication.everyonelitepal.Node3;
import com.example.myapplication.everyonelitepal.Node4;
import com.example.myapplication.everyonelitepal.Node5;
import com.example.myapplication.everyonelitepal.Node6;
import com.example.myapplication.everyonelitepal.Node7;
import com.example.myapplication.everyonelitepal.Node8;
import com.example.myapplication.everyonelitepal.Node9;
import com.example.myapplication.everyonelitepal.NodeMother;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeadAnalysis {
    public double ReceiveLatitude = 0.0;
    public double ReceiveLongitude = 0.0;
    public int ReceiveSerialNumber = 0;

    Node1 mNode1 = new Node1();
    Node2 mNode2 = new Node2();
    Node3 mNode3 = new Node3();
    Node4 mNode4 = new Node4();
    Node5 mNode5 = new Node5();
    Node6 mNode6 = new Node6();
    Node7 mNode7 = new Node7();
    Node8 mNode8 = new Node8();
    Node9 mNode9 = new Node9();
    Node10 mNode10 = new Node10();
    Node11 mNode11 = new Node11();
    Node12 mNode12 = new Node12();
    Node13 mNode13 = new Node13();
    Node14 mNode14 = new Node14();
    Node15 mNode15 = new Node15();
    Node16 mNode16 = new Node16();
    Node17 mNode17 = new Node17();
    Node18 mNode18 = new Node18();
    Node19 mNode19 = new Node19();
    Node20 mNode20 = new Node20();
    Map<String, NodeMother> m1 = new HashMap<>();
    private int ReceivedTime = 0;
    private int alertNum;
    private boolean direct = true;
    private boolean WRONG_RECEIVED = false;

    public void MapConsist(){
        m1.put("Node1", mNode1);
        m1.put("Node2", mNode2);
        m1.put("Node3", mNode3);
        m1.put("Node4", mNode4);
        m1.put("Node5", mNode5);
        m1.put("Node6", mNode6);
        m1.put("Node7", mNode7);
        m1.put("Node8", mNode8);
        m1.put("Node9", mNode9);
        m1.put("Node10", mNode10);
        m1.put("Node11", mNode11);
        m1.put("Node12", mNode12);
        m1.put("Node13", mNode13);
        m1.put("Node14", mNode14);
        m1.put("Node15", mNode15);
        m1.put("Node16", mNode16);
        m1.put("Node17", mNode17);
        m1.put("Node18", mNode18);
        m1.put("Node19", mNode19);
        m1.put("Node20", mNode20);
    }

    public void Head (String readStr, Context context) throws ParseException {
        //配置返回值
        if(readStr.startsWith("OK")){
            Looper.prepare();
            Log.d("Head", "Head: " + readStr);
            Toast.makeText(context,"参数设置完成",Toast.LENGTH_SHORT).show();
            Looper.loop();
        }else if(readStr.startsWith("ERROR")){
            Looper.prepare();
            Log.d("Head", "Head: " + readStr);
            Toast.makeText(context,"参数设置失败！请重新发送",Toast.LENGTH_SHORT).show();
            Looper.loop();
        }

        if ("#".equals(readStr.substring(0, 1))) {
            direct = true;
        } else if ("*".equals(readStr.substring(0, 1))) {
            direct = false;
        }
        if("#".equals(readStr.substring(0, 1)) || "*".equals(readStr.substring(0, 1))){
            readStr = readStr.replace("#", "");
            readStr = readStr.replace("*", "");
            readStr = readStr.replace(" ", "");

            String[] readStrArray = readStr.split(",");
            if(checkStrIsNum02(readStrArray[0])){
                if(Integer.parseInt(readStrArray[0]) <= 20 && Integer.parseInt(readStrArray[0]) != SelfNumber){
                    ReceiveSerialNumber = Integer.parseInt(readStrArray[0]);
                }else
                    WRONG_RECEIVED = true;
                if(checkStrIsNum02(readStrArray[1]) && checkStrIsNum02(readStrArray[2])){
                    ReceiveLatitude = Double.parseDouble(readStrArray[1]);
                    ReceiveLongitude = Double.parseDouble(readStrArray[2]);
                }else
                    WRONG_RECEIVED = true;
                if(!WRONG_RECEIVED){
                    ReceivedTime = Integer.parseInt(dateToStamp(format));

                    if(readStrArray[3].equals("sos0") || readStrArray[3].equals("sos1") ||
                            //readStrArray[3].equals("sos2") ||
                            readStrArray[3].equals("sos3")){
                        alertNum = Integer.parseInt(readStrArray[3].replace("sos", ""));
                        getWarnedTypes = alertNum;
                        if(alertNum != 0) {
                            GET_WARNED = true;
                        }

                        storage(ReceiveSerialNumber);

                    }
                }
            }else
                WRONG_RECEIVED = true;
        }else if("%".equals(readStr.substring(0, 1))){
            readStr = readStr.replace("%", "");
            String[] recordArray = readStr.split(",");
            if(checkStrIsNum02(recordArray[0])){
                if(Integer.parseInt(recordArray[0]) <= 20 && Integer.parseInt(recordArray[0]) != SelfNumber){
                    int recordNumber = Integer.parseInt(recordArray[0]);
                    String recordMessage = recordArray[1];
                    EventBus.getDefault().post(new HeadAnalysis.RecordMessageEvent(recordArray));
                }else
                    WRONG_RECEIVED = true;

            }else
                WRONG_RECEIVED = true;
        }


//        .

    }
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
    /**
     * 利用正则表达式来判断字符串是否为数字
     */
    public static boolean checkStrIsNum02(String str) {
        Matcher isNum = NUMBER_PATTERN.matcher(str);
        return isNum.matches();
    }
    public class RecordMessageEvent {

        public final String[] recordMessage;
        //= "Eventbus test";

        public RecordMessageEvent(String[] recordMessage) {
            this.recordMessage = recordMessage;
        }

    }

    private void storage (int Number){
//        if(Looper.getMainLooper() == Looper.myLooper())
//            Log.d("ReceivedStorage", "Main thread, " + Number +"is storing");
//        else Log.d("ReceivedStorage", "Other thread, " + Number +"is storing");
        MapConsist();
        String Node = "Node" + Number;
        Cursor c = LitePal.findBySQL("select * from " + Node);
        int number = c.getCount();

        List<? extends NodeMother> NodeInformation = LitePal.order("time desc").limit(1).find(m1.get(Node).getClass());
        if(number == 0){
            Objects.requireNonNull(m1.get(Node)).setLatitude(ReceiveLatitude);
            m1.get(Node).setLongitude(ReceiveLongitude);
            m1.get(Node).setWarnType(getWarnedTypes);
            m1.get(Node).setTime(ReceivedTime);
            m1.get(Node).setDirect(direct);
            m1.get(Node).save();
        }else {
            if(NodeInformation.get(0).getLatitude() == ReceiveLatitude &&
                    NodeInformation.get(0).getLongitude() == ReceiveLongitude){

                ContentValues values = new ContentValues();
                values.put("warntype", getWarnedTypes);
                values.put("time", ReceivedTime);
                values.put("direct", direct);
                LitePal.update(m1.get(Node).getClass(), values, NodeInformation.get(0).getId());

            } else {
                m1.get(Node).setLatitude(ReceiveLatitude);
                m1.get(Node).setLongitude(ReceiveLongitude);
                m1.get(Node).setWarnType(getWarnedTypes);
                m1.get(Node).setTime(ReceivedTime);
                m1.get(Node).setDirect(direct);
                m1.get(Node).save();
            }

        }

    }
}
