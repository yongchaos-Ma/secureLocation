package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ResourceUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

public class RecognizeModule {
    private SpeechRecognizer recognizer=null;
    private final String TAG = "Recognize Module";
    public static String recordMessage = null;
    private final String recoverMessage = null;
    private boolean recordIsFinish=false;
    private ProgressDialog pDialog=null;

    public String startNoDialogOffline(Context context){
        if (recognizer!=null){
            recognizer.cancel();
            recognizer.destroy();
        }
        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        Log.d(TAG,"初始化语音对象(函数内)");
        recognizer = SpeechRecognizer.createRecognizer(context,mInitListener);
        Log.d(TAG, "startNoDialogOffline: Init complete");
        if(recognizer==null){
            Log.e(TAG,"recognizer is null");
        }
        //2.设置听写参数
        Log.d(TAG, "startNoDialogOffline: begin set params");
        //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
        recognizer.setParameter( SpeechConstant.PARAMS, null );
        recognizer.setParameter( SpeechConstant.SUBJECT, null );
        //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
        recognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //此处engineType为“cloud”
        recognizer.setParameter( SpeechConstant.ENGINE_TYPE, "local" );
        //设置语音输入语言，zh_cn为简体中文
        recognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言
        recognizer.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        //取值范围{1000～10000}
        recognizer.setParameter(SpeechConstant.VAD_BOS, "4000");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        //自动停止录音，范围{0~10000}
        recognizer.setParameter(SpeechConstant.VAD_EOS, "1000");
        //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        recognizer.setParameter(SpeechConstant.ASR_PTT,"1");

        // 设置本地识别资源
        recognizer.setParameter(ResourceUtil.ASR_RES_PATH, getRecognizeResourcePath(context));
        // 设置语法构建路径
        //recognizer.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
        // 设置本地识别使用语法id
        //recognizer.setParameter(SpeechConstant.LOCAL_GRAMMAR, "call");
        // 设置识别的门限值
        recognizer.setParameter(SpeechConstant.MIXED_THRESHOLD, "30");
        // 使用8k音频的时候请解开注释
//			mAsr.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        //recognizer.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        //recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/asr.wav");
        Log.d(TAG, "startNoDialogOffline: Parameter settled");

        //启动对话框
        showProgressDialog(context);

        //3.设置回调接口
        recognizer.startListening(new RecognizerListener() {
            //public IBinder asBinder() {return null;}

            @Override
            public void onVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onBeginOfSpeech() {
                Log.d(TAG, "onResult: Listen start");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "onEndOfSpeech: " + recordMessage);
//                if(pDialog.isShowing() && recordMessage == null){
//                    pDialog.dismiss();
//                    Toast.makeText(context, "您好像没有说话哦.(错误码:10118))", Toast.LENGTH_SHORT).show();
//                }


            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                Log.d(TAG, "onResult: Listen is over");
                if (isLast) {
                    String json = recognizerResult.getResultString();
                    recordMessage = JsonParser.parseIatResult(json);
                    //Log.d("tag",recordMessage);

                    String GBKMessage = null;
                    try {
                        GBKMessage = new String(recordMessage.getBytes("GBK"), "GBK");
                        Log.d(TAG, "GBKcode: " + GBKMessage);
                        String GBKonHEX = StringTrans.str2HexStr(GBKMessage);
                        Log.d(TAG, "GBKonHEX: " + GBKonHEX);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }



                    //传递基本数据类型
                    String extraMessage = StringTrans.str2HexStr(recordMessage);
                    Log.d(TAG, "HexedRun: recordMessage to Hex is:" + extraMessage);


                    Log.d(TAG, "onResult: recordMessage is: " + recordMessage);
                    //Toast.makeText(MainActivity.this, recordMessage, Toast.LENGTH_SHORT).show();
                    //语音听写完成标志---------------------------
                    recordIsFinish=true;
                    //------------------------------------------
                    //recoverMessage = StringTrans.hexStr2Str(extraMessage);
                    EventBus.getDefault().post(new MessageEvent(recordMessage));


                    if(pDialog.isShowing() && recordMessage == null){
                        pDialog.dismiss();
                        Toast.makeText(context, "您好像没有说话哦.(错误码:10118))", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            public void onError(SpeechError speechError) {
                Log.d("error", speechError.toString());
            }


            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });

        return recordMessage;

    }

    /**
     * 初始化监听器。
     */
    private final InitListener mInitListener = code -> {
        Log.d("tag", "SpeechRecognizer init() code = " + code);
        if (code != ErrorCode.SUCCESS) {
            Log.d("tag", "初始化失败，错误码：" + code);
        }
    };

    //语音识别资源
    private String getRecognizeResourcePath(Context context){
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
        tempBuffer.append(";");
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
        //识别8k资源-使用8k的时候请解开注释
        return tempBuffer.toString();
    }

    private void showProgressDialog(Context context) {
        pDialog = new ProgressDialog(context);

        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setProgress(100);
        pDialog.setMessage("请稍等...");
        pDialog.setIndeterminate(false);
        pDialog.show();

        WindowManager.LayoutParams lp = pDialog.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        Window win = pDialog.getWindow();
        win.setAttributes(lp);

        new Thread(new Runnable() {

            @Override
            public void run() {
                //long startTime = System.currentTimeMillis();
                int progress = 0;

                //while (System.currentTimeMillis() - startTime < 1000) {
                //语音听写完成后，释放对话框
                while (!recordIsFinish) {
                    try {
                        progress += 10;
                        pDialog.setProgress(progress);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        pDialog.dismiss();
                    }
                }

                pDialog.dismiss();
                recordIsFinish=false;
            }
        }).start();
    }

    public class MessageEvent {

        public final String message;
                //= "Eventbus test";

        public MessageEvent(String message) {
            this.message = message;
        }
    }
}
