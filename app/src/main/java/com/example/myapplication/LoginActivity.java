package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 作者:created by 巴塞罗那的余晖 on 2019/3/10 18：35
 * 邮箱:zhubaoluo@outlook.com
 * 不会写BUG的程序猿不是好程序猿，嘤嘤嘤
 */
public class LoginActivity extends AppCompatActivity {
    //声明变量
    private Boolean login_state=false;
    private TextView mregister;
    private EditText muesername, mpassword;
    private Button mconfirm;
    private UserData data;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //找布局控件对应ID
        ImageView mimageView = findViewById(R.id.iv_1);
        ImageButton mimageButton = findViewById(R.id.im_qq);
        muesername = findViewById(R.id.et_username);
        mpassword = findViewById(R.id.et_password);
        mconfirm = findViewById(R.id.btn_confirm);
        mregister = findViewById(R.id.btn_register);
        checkBox=findViewById(R.id.cb_rm);
        data=new UserData(LoginActivity.this);
        initLogin();
        mregister.setOnClickListener(v -> {
            {
                if(data.getRegister(muesername.getText().toString(),mpassword.getText().toString()))
                {
                    Toast.makeText(LoginActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"注册失败！用户名重复或者为空！",Toast.LENGTH_SHORT).show();
                }
            }


        });
        mconfirm.setOnClickListener(v -> {
            if(data.verifyPassword(muesername.getText().toString(),mpassword.getText().toString()))
            {

                saveLogin(login_state);

                //Toast.makeText(LoginActivity.this,muesername.getText().toString()+"欢迎您！",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this,LocationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            else
            {
                Toast.makeText(LoginActivity.this,"登陆失败！请检查用户是否存在或者密码错误！",Toast.LENGTH_SHORT).show();
            }
        });
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> login_state=isChecked);
    }
    private void saveLogin(boolean flag)
    {
        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("ACCOUNT_REMEMBER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String secreat_name = muesername.getText().toString();
        String secreat_password=mpassword.getText().toString();
        if(sharedPreferences.getBoolean("flag",false))
        {
            return;
            //验证通过，不需要再次保存了
        }
        if(flag) {
            secreat_password=Base64.encodeToString(secreat_password.getBytes(),Base64.NO_WRAP);
            editor.putString("name",secreat_name);
            editor.putString("password", secreat_password);
            editor.putBoolean("flag", flag);
        }
        else{
            editor.clear();
            editor.putString("name",secreat_name);
            editor.putBoolean("flag",false);
        }
        editor.apply();

    }

    private void initLogin()
    {
        SharedPreferences sharedPreferences=LoginActivity.this.getSharedPreferences("ACCOUNT_REMEMBER", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("flag",false)){
            String decode_password=sharedPreferences.getString("password","");
            decode_password= new String(Base64.decode(decode_password.getBytes(),Base64.NO_WRAP));
            muesername.setText(sharedPreferences.getString("name",""));
            mpassword.setText(decode_password);
            checkBox.setChecked(true);
        }
        else {
            muesername.setText(sharedPreferences.getString("name",""));
            checkBox.setChecked(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
