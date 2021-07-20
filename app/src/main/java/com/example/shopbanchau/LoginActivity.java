package com.example.shopbanchau;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txt;
    private Token token;
    private static final String TAG_TOKEN = "Bearer ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt = findViewById(R.id.txt_dang_ky);
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword = findViewById(R.id.edt_password_login);
        btnLogin = findViewById(R.id.btn_login);

    }


    public void chuyenDangKy(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();

    }

    public void login(View view) {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if(validationEmail(email)&&validationPassword(password)){
            password = MD5Library.getMd5(password);
            ApiServices.apiService.authenticateUser(email, password).enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if(response.isSuccessful()){
                        token = response.body();
                        String result = token.getToken();
                        DataLocalManager.setToken(TAG_TOKEN+result);
                        setIdToDataLocal(DataLocalManager.getToken());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("POS", 3);
                        finish();
                        startActivity(intent);
                    }else {
                        Toast.makeText(view.getContext(), "ten tai khoan hoac mat khau khong dung!", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    Toast.makeText(view.getContext(), "api fail!", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    public boolean validationEmail(String email){
        boolean isValidate = true;
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Email empty!", Toast.LENGTH_SHORT).show();
            isValidate = false;
            return isValidate;
    }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(), "Sai dinh dang!", Toast.LENGTH_SHORT).show();
            isValidate = false;
            return isValidate;
    }
        return isValidate;
    }

    public boolean validationPassword(String password){
        boolean isValidate = true;
        if(TextUtils.isEmpty(password)){
            isValidate = false;
            Toast.makeText(getApplicationContext(), "Password empty!", Toast.LENGTH_SHORT).show();
            return  isValidate;
        }
        if(password.length()<6){
            isValidate = false;
            Toast.makeText(getApplicationContext(), "Password >6!", Toast.LENGTH_SHORT).show();
            return isValidate;
        }
        return isValidate;
    }


    private void setIdToDataLocal(String token) {
        ApiServices.apiService.getId(token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int id = Integer.parseInt(response.body());
                DataLocalManager.setId(id);
                setCartIdToDataLocal(DataLocalManager.getId());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "api fail!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCartIdToDataLocal(int id) {
        ApiServices.apiService.getCartId(id, DataLocalManager.getToken()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    int cartId = response.body();
                    DataLocalManager.setCartId(cartId);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "api fail!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
