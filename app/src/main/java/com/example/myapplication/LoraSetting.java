package com.example.myapplication;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class LoraSetting extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    private final String TAG = "LoRaSetting";
    private EditTextPreference mChannelPreference;
    private EditTextPreference mCodePreference;
    private EditTextPreference mNamePreference;
    private ListPreference mSpeedPreference;
    private ListPreference mPowerPreference;
    private boolean settingChanged = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.lora_setting);  // 如果刚刚的xml用了自己的文件名，此处记得修改

        // 由于PreferenceScreen基于SharedPreferences，所以他的调用方法与是相似的
//        String Default = PreferenceManager.getDefaultSharedPreferences(this)
//                .getString("rename_preference", "DEVICE "+SelfNumber); // 此处的getString可视需要改变为getAll, getBoolean等（具体请查找SharedPreferences相关资料）
//        Log.d(TAG, "onCreate: " + Default);
        mChannelPreference = (EditTextPreference)findPreference("channel_preference");
        mChannelPreference.getEditText().addTextChangedListener(new SettingTextWatcher(LoraSetting.this,mChannelPreference,0,83));

        mCodePreference = (EditTextPreference)findPreference("code_preference");
        mCodePreference.getEditText().addTextChangedListener(new SettingTextWatcher(LoraSetting.this,mCodePreference,0,65535));

        mNamePreference = (EditTextPreference)findPreference("rename_preference");
        mNamePreference.getEditText().addTextChangedListener(new SettingNameWatcher(LoraSetting.this,mNamePreference));

        mSpeedPreference = (ListPreference)findPreference("speed_preference");
        mPowerPreference = (ListPreference)findPreference("power_preference");

        mSpeedPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                settingChanged = true;
                return true;
            }
        });
        mPowerPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                settingChanged = true;
                return true;
            }
        });
        mChannelPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                settingChanged = true;
                return true;
            }
        });
        mCodePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                settingChanged = true;
                return true;
            }
        });

        mNamePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                EventBus.getDefault().post(new LoraSetting.NameChangedEvent(true));
                return true;
            }
        });

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
//        String Default = PreferenceManager.getDefaultSharedPreferences(this)
//                .getString("rename_preference", "DEVICE "+SelfNumber); // 此处的getString可视需要改变为getAll, getBoolean等（具体请查找SharedPreferences相关资料）
        Log.d(TAG, "onChange: " + preference);
        return true;
    }

    public class SettingChangedEvent {

        public final Boolean isChanged;
        //= "Eventbus test";

        public SettingChangedEvent(Boolean isChanged) {
            this.isChanged = isChanged;
        }

    }
    public class NameChangedEvent {

        public final Boolean nameIsChanged;
        //= "Eventbus test";

        public NameChangedEvent(Boolean nameIsChanged) {
            this.nameIsChanged = nameIsChanged;
        }

    }

    @Override
    public void onDetachedFromWindow() {
        //String Default = PreferenceManager.getDefaultSharedPreferences(this)
        //        .getString("rename_preference", "DEVICE "+SelfNumber); // 此处的getString可视需要改变为getAll, getBoolean等（具体请查找SharedPreferences相关资料）
        if(settingChanged){
            Log.d(TAG, "setting is changed");
            EventBus.getDefault().post(new LoraSetting.SettingChangedEvent(true));
        }

        super.onDetachedFromWindow();
    }
}
