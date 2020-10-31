package com.example.applogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.applogin.bean.UserInfo;
import com.example.applogin.database.UserDBHelper;
import com.example.applogin.Util.DateUtil;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private UserDBHelper mHelper; // 声明一个用户数据库帮助器的对象
    private EditText R_et_name;
    private EditText R_et_pwd;
    private EditText R_et_pnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        R_et_name = findViewById(R.id.R_et_name);
        R_et_pwd=findViewById(R.id.R_et_pwd);
        R_et_pnumber=findViewById(R.id.R_et_pnumber);
        findViewById(R.id.btn_savedata).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 获得数据库帮助器的实例
        mHelper = UserDBHelper.getInstance(this, 2);
        // 打开数据库帮助器的写连接
        mHelper.openWriteLink();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 关闭数据库连接
        mHelper.closeLink();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_savedata) {
            String name = R_et_name.getText().toString();
            String pwd=R_et_pwd.getText().toString();
            String phone=R_et_pnumber.getText().toString();
            if (TextUtils.isEmpty(name)) {
                showToast("请先填写姓名");
                return;
            } else if (TextUtils.isEmpty(phone)) {
                showToast("请先填写手机号");
                return;
            }

            // 以下声明一个用户信息对象，并填写它的各字段值
            UserInfo info = new UserInfo();
            info.name = name;
            info.update_time = DateUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss");
            info.pwd=pwd;
            info.phone=phone;

            // 执行数据库帮助器的插入操作
            mHelper.insert(info);
            showToast("数据已写入SQLite数据库");

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }
}
