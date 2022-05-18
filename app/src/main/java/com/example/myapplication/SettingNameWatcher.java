package com.example.myapplication;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

public class SettingNameWatcher implements TextWatcher {
    private int editStart ;
    private int editCount ;
    private final EditTextPreference mEditTextPreference;
    private final Context mContext;

    public SettingNameWatcher(Context context, EditTextPreference e) {
        mContext = context;
        mEditTextPreference = e;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count,int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//		Log.e("demo", "onTextChanged start:"+start+" count:"+count+" before:"+before);
        editStart = start;
        editCount = count;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (TextUtils.isEmpty(editable)) {
            return;
        }
        String content = editable.toString();

    }


}
