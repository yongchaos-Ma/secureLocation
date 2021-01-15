package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StorageChooseActivity extends AppCompatActivity {
    private Button storageItem1;
    private Button storageItem2;
    private Button storageItem3;
    private Button storageItem4;
    private Button storageItem5;
    private Button admit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_choose);
        storageItem1 = findViewById(R.id.bt_storage_1);
        storageItem2 = findViewById(R.id.bt_storage_2);
        storageItem3 = findViewById(R.id.bt_storage_3);
        storageItem4 = findViewById(R.id.bt_storage_4);
        storageItem5 = findViewById(R.id.bt_storage_5);
    }
}