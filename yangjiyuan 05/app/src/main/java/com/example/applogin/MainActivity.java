package com.example.applogin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.applogin.Util.ViewUtil;
import com.example.applogin.database.UserDBHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //声明对象
    private RadioGroup rg;
    private RadioButton rbtn_1;
    private RadioButton rbtn_2;
    private EditText et_pnumber;
    private TextView tv_pwd;
    private EditText et_pwd;
    private Button btn_forget;
    private Switch sw_remember;
    private Button btn_login;
    private Button btn_register;
    private TextView tv_rember_result;

    private SharedPreferences mShared;
    private UserDBHelper mHelper;

    private int mRequestCode = 0;        // 跳转页面时的请求代码
    private int mType = 2;               // 用户类型
    private boolean bRemember = false;   // 是否记住密码
    private String mPassword = "666666"; // 默认密码
    private String mVerifyCode;          // 验证码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 从布局文件中获取控件
        rg = findViewById(R.id.rg);
        rbtn_1 = findViewById(R.id.rbtn_1);
        rbtn_2 = findViewById(R.id.rbtn_2);
        et_pnumber = findViewById(R.id.et_pnumber);
        tv_pwd = findViewById(R.id.tv_pwd);
        et_pwd = findViewById(R.id.et_pwd);
        btn_forget = findViewById(R.id.btn_forget);
        btn_login = findViewById(R.id.btn_login);
        sw_remember = findViewById(R.id.sw_remember);
        tv_rember_result = findViewById(R.id.tv_rember_result);
        btn_register=findViewById(R.id.btn_register);

        // 给rg设置单选监听器
        rg.setOnCheckedChangeListener(new RadioListener());
        // 给et_pnumber添加文本变更监听器
        et_pnumber.addTextChangedListener(new HideTextWatcher(et_pnumber));
        // 给R_et_pwd添加文本变更监听器
        et_pwd.addTextChangedListener(new HideTextWatcher(et_pwd));

        btn_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        initTypeSpinner();
        // 给ck_remember设置勾选监听器

        //从share_login.xml中获取共享参数对象
        mShared = getSharedPreferences("share_login", MODE_PRIVATE);
        // 获取共享参数中保存的手机号码
        String phone = mShared.getString("phone", "");
        // 获取共享参数中保存的密码
        String password = mShared.getString("password", "");
        et_pnumber.setText(phone); // 给手机号码编辑框填写上次保存的手机号
        et_pwd.setText(password); // 给密码编辑框填写上次保存的密码
    }

    // 初始化用户类型的下拉框
    private String[] typeArray = {"学生", "老师","杨济源-18990192"};

    private void initTypeSpinner() {
        // 声明一个下拉列表的数组适配器
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, typeArray);
        // 设置数组适配器的布局样式
        typeAdapter.setDropDownViewResource(R.layout.item_dropdown);
        Spinner sp_type = findViewById(R.id.sp_type);
        // 设置下拉框的标题
        sp_type.setPrompt("请选择用户类型");
        // 设置下拉框的数组适配器
        sp_type.setAdapter(typeAdapter);
        // 设置下拉框默认显示第几项
        sp_type.setSelection(mType);
        // 给下拉框设置选择监听器
        sp_type.setOnItemSelectedListener(new TypeSelectedListener());
    }

    // 定义用户类型的选择监听器
    class TypeSelectedListener implements AdapterView.OnItemSelectedListener {
        // 选择事件的处理方法
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            mType = arg2;
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    // 定义登录方式的单选监听器
    private class RadioListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.rbtn_1) { // 选择密码登录
                tv_pwd.setText("登录密码：");
                et_pwd.setHint("请输入密码");
                btn_forget.setText("忘记密码");
                sw_remember.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.rbtn_2) { // 选择验证码登录
                tv_pwd.setText("　验证码：");
                et_pwd.setHint("请输入验证码");
                btn_forget.setText("获取验证码");
                sw_remember.setVisibility(View.INVISIBLE);
            }
        }
    }

    // 定义编辑框的文本变化监听器
    private class HideTextWatcher implements TextWatcher {

        private EditText mView;
        private int mMaxLength;
        private CharSequence mStr;

        HideTextWatcher(EditText v) {
            super();
            mView = v;
            mMaxLength = ViewUtil.getMaxLength(v);
        }

        // 在编辑框的输入文本变化前触发
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        // 在编辑框的输入文本变化时触发
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mStr = s;
        }

        // 在编辑框的输入文本变化后触发
        public void afterTextChanged(Editable s) {
            if (mStr == null || mStr.length() == 0)
                return;
            // 关闭软键盘
            if ((mStr.length() == 11 && mMaxLength == 11) ||
                    (mStr.length() == 8 && mMaxLength == 8)) {
                ViewUtil.hideOneInputMethod(MainActivity.this, mView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        String phone = et_pnumber.getText().toString();
        if (v.getId() == R.id.btn_forget) { // 点击了“忘记密码”按钮
            if (phone.length() < 11) { // 手机号码不足11位
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (rbtn_1.isChecked()) { // 选择密码登录，跳到找回密码页面
                Intent intent = new Intent(this, ForgetPassword.class);
                // 转到找回密码页面
                intent.putExtra("phone", phone);
                startActivityForResult(intent, mRequestCode);
            } else if (rbtn_2.isChecked()) { // 选择验证码方式，要生成数字验证码
                mVerifyCode = String.format("%06d", (int) ((Math.random() * 9 + 1) * 100000));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请记住验证码");
                builder.setMessage("本次验证码是" + mVerifyCode );
                builder.setPositiveButton("好的", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else if (v.getId() == R.id.btn_login) { // 点击登录按钮
            if (phone.length() < 11) { // 手机号码不足11位
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (rbtn_1.isChecked()) { // 密码校验
                if (!et_pwd.getText().toString().equals(mPassword)) {
                    Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
                } else {
                    loginSuccess();
                }
            } else if (rbtn_2.isChecked()) { // 验证码校验
                if (!et_pwd.getText().toString().equals(mVerifyCode)) {
                    Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else {
                    loginSuccess();
                }
            }
        }else if (v.getId() == R.id.btn_register){
            if (rbtn_1.isChecked()) {
                Intent intent = new Intent(this, Register.class);
                startActivityForResult(intent, mRequestCode);
            }
        }
    }

    // 忘记密码操作完成
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode && data != null) {
            //更新密码
            mPassword = data.getStringExtra("new_password");
        }
    }

    // 返回登录页面，清空密码输入框
    @Override
    protected void onRestart() {
        et_pwd.setText("");
        super.onRestart();
    }

    // 校验通过，登录成功
    private void loginSuccess() {
        String desc = String.format("登录成功", et_pnumber.getText().toString(), typeArray[mType]);
        // 弹出提醒对话框，提示用户登录成功
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录成功");
        builder.setMessage(desc);
        builder.setNegativeButton("确定", null);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
