package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

public class SpeakModule {

    private final String TAG = "Speak Module";
    // 语音合成对象
    private SpeechSynthesizer mTts;
    private SynthesizerListener mTtsListener;

    // 默认云端发音人
    public static String voicerCloud="xiaoyan";
    // 默认本地发音人
    public static String voicerLocal="xiaoyan";
    public static String voicerXtts="xiaoyan";
    //缓冲进度
    private int mPercentForBuffering = 0;
    //播放进度
    private int mPercentForPlaying = 0;
    // 引擎类型
    private final String mEngineType = SpeechConstant.TYPE_XTTS;

    private Toast mToast;
    private SharedPreferences mSharedPreferences;

    public void speechSync(Context context, String TextMessage){
        if (mTts!=null){
            mTts.destroy();
        }

        //1.初始化初始化合成对象
        Log.d(TAG,"初始化语音合成对象");
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        Log.d(TAG, "speechSync: Init complete");
        if( null == mTts ){
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            Log.d(TAG, "创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化");
        }
        //2.设置参数
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.SUBJECT, null);
        //设置合成
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_XTTS);
        //设置发音人资源路径
        mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath(context));
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME,voicerXtts);
        //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY,"1");//支持实时音频流抛出，仅在synthesizeToUri条件下支持
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        //	mTts.setParameter(SpeechConstant.STREAM_TYPE, AudioManager.STREAM_MUSIC+"");

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
        Log.d(TAG, "speechSync: Parameter settled");

        if(TextMessage != null){
            mTts.startSpeaking(TextMessage, mTtsListener);
            int code2 = mTts.startSpeaking(TextMessage, mTtsListener);
            if (code2 != ErrorCode.SUCCESS) {
                Log.d(TAG, "speechSync: "+"语音合成失败,错误码: " + code2+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                //showTip("语音合成失败,错误码: " + code2+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
        //3.设置回调接口

        mTtsListener = new SynthesizerListener() {

            @Override
            public void onSpeakBegin() {
                //showTip("开始播放");
                Log.d("Record test","开始播放："+ System.currentTimeMillis());

            }

            @Override
            public void onSpeakPaused() {
                //showTip("暂停播放");
            }

            @Override
            public void onSpeakResumed() {
                //showTip("继续播放");
            }

            @Override
            public void onBufferProgress(int percent, int beginPos, int endPos,
                                         String info) {
                // 合成进度
                mPercentForBuffering = percent;
                //showTip(String.format(getString(R.string.tts_toast_format),
                //        mPercentForBuffering, mPercentForPlaying));
            }

            @Override
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
                // 播放进度
                mPercentForPlaying = percent;
                //showTip(String.format(getString(R.string.tts_toast_format),
                //        mPercentForBuffering, mPercentForPlaying));
            }

            @Override
            public void onCompleted(SpeechError error) {
                if (error == null) {
                    Log.d(TAG, "onCompleted: " + "播放完成");
                    //showTip("播放完成");
                } else if (error != null) {
                    Log.d(TAG, "onCompleted: " + error.getPlainDescription(true));
                    //showTip(error.getPlainDescription(true));
                }
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
                // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
                // 若使用本地能力，会话id为null
                if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                    String sid = obj.getString(SpeechEvent.KEY_EVENT_AUDIO_URL);
                    Log.d(TAG, "session id =" + sid);
                }

                //实时音频流输出参考
			/*if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
				byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
				Log.e("MscSpeechLog", "buf is =" + buf);
			}*/
            }
        };

    }

    /**
     * 初始化监听。
     */
    private final InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, "onInit: " + "初始化失败,错误码："+code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");


            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里

            }
        }
    };

    //获取发音人资源路径
    private String getResourcePath(Context context){
        StringBuffer tempBuffer = new StringBuffer();
        String type= "xtts";

        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "xtts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "xtts/xiaoyan.jet"));
        return tempBuffer.toString();
    }

}
