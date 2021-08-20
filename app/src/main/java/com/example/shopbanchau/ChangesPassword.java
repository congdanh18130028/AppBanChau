package com.example.shopbanchau;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.User;
import com.example.shopbanchau.models.UserEdit;
import com.example.shopbanchau.utils.DataLocalManager;
import com.example.shopbanchau.utils.MD5Library;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangesPassword extends AppCompatActivity {
    private TextView txt_mk_cu, txt_mk_moi, txt_nl_mk_moi;
    private User _user;
    private Button btn;
    private ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes_password);
        txt_mk_cu = findViewById(R.id.txt_nhap_mk_cu);
        txt_mk_moi = findViewById(R.id.txt_nhap_mk_moi);
        txt_nl_mk_moi = findViewById(R.id.txt_nhap_lai_mk_moi);
        btn = findViewById(R.id.btn_changes_password);
        getUser();
        setBtnBack();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });


    }
    private void setBtnBack() {
        btn_back = findViewById(R.id.btn_back_change_pass);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changePassword() {
        if(validation()){
            UserEdit edit = new UserEdit(MD5Library.getMd5(txt_mk_moi.getText().toString()), "/password", "replace");
            List<UserEdit> list = new ArrayList<UserEdit>();
            list.add(edit);
            ApiServices.apiService.editUser(DataLocalManager.getId(), DataLocalManager.getToken(), list).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "api fail!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private boolean validation() {
        if (!MD5Library.getMd5(txt_mk_cu.getText().toString()).equals(_user.getPassword())){
            Toast.makeText(getApplicationContext(), "Sai mật khảu", Toast.LENGTH_SHORT).show();
            return  false;
        }
        if(txt_mk_moi.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Mật khẩu mới trống", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(txt_mk_moi.getText().toString().length() <6){
            Toast.makeText(getApplicationContext(), "Mật khẩu lớn hơn 5  kí tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!txt_nl_mk_moi.getText().toString().equals(txt_mk_moi.getText().toString())){
            Toast.makeText(getApplicationContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private void getUser() {
        ApiServices.apiService.getUser(DataLocalManager.getId(), DataLocalManager.getToken()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    _user = user;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "api fail!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}