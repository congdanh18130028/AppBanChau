package com.example.shopbanchau;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.Token;
import com.example.shopbanchau.models.User;
import com.example.shopbanchau.utils.DataLocalManager;
import com.example.shopbanchau.utils.MD5Library;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText edt_name, edt_phone, edt_address, edt_email, edt_password, edt_repassword;

    public static final Pattern PHONE_VN
            = Pattern.compile(
            "(84|0[3|5|7|8|9])+([0-9]{8})\\b");
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        progressBar = findViewById(R.id.progress_register);
        edt_name = findViewById(R.id.edt_name_register);
        edt_phone = findViewById(R.id.edt_phone_register);
        edt_address = findViewById(R.id.edt_address_register);
        edt_email = findViewById(R.id.edt_email_register);
        edt_password = findViewById(R.id.edt_password_register);
        edt_repassword = findViewById(R.id.edt_repassword_register);
    }

    private boolean validationForm(String name, String phone, String address, String email, String password, String repassword ){
        if(TextUtils.isEmpty(name)){
            Toast.makeText(getApplicationContext(), "Tên người dùng trống", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(getApplicationContext(), "Số điện thoại trống", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if(!PHONE_VN.matcher(phone).matches()){
            Toast.makeText(getApplicationContext(), "Sai định dạng số điện thoại", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if(TextUtils.isEmpty(address)){
            Toast.makeText(getApplicationContext(), "Địa chỉ trống", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Email trống!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(), "Sai định dạng email", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Mật khẩu trống", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if(password.length()<6){
            Toast.makeText(getApplicationContext(), "Đặt mật khẩu phải lớn hơn 5 kí tự", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if(TextUtils.isEmpty(repassword)){
            Toast.makeText(getApplicationContext(), "Nhập lại mật khẩu trống", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if(!password.equals(repassword)){
            Toast.makeText(getApplicationContext(), "Nhập lại mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        return true;
    }


    public void back(View view) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void chuyenDangNhap(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void isEmailExists(String name, String phone, String address, String email, String password)
    {

        ApiServices.apiService.checkEmail(email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

               if(user==null){
                   regis(name, phone, address, email, password);
                   progressBar.setVisibility(View.GONE);
               }else {
                   Toast.makeText(getApplicationContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
               }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void register(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String name = edt_name.getText().toString().trim();
        String phone = edt_phone.getText().toString().trim();
        String address = edt_address.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String repassword = edt_repassword.getText().toString().trim();
        if (validationForm(name, phone, address, email, password, repassword)){
            isEmailExists(name, phone, address, email, password);
        }

    }
    public void regis(String name, String phone, String address, String email, String password){
        User user = new User(name, phone, address, email, MD5Library.getMd5(password));
                ApiServices.apiService.addUser(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User u = response.body();
                        Intent intent = new Intent(RegisterActivity.this, RegisterSuccessActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
