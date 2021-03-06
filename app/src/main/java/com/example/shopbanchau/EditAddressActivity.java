package com.example.shopbanchau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.User;
import com.example.shopbanchau.models.UserEdit;
import com.example.shopbanchau.utils.DataLocalManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAddressActivity extends AppCompatActivity {
    private User _user;
    private EditText txt;
    private Button btn;
    private String typeEdit;
    private ImageButton btn_back;

    public static final Pattern PHONE_VN
            = Pattern.compile(
            "(84|0[3|5|7|8|9])+([0-9]{8})\\b");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        Intent i = getIntent();
        typeEdit = i.getStringExtra("TYPE");
        setTitle();
        getUser();
        setBtnBack();
        txt = findViewById(R.id.txt_text_edit_account);
        btn = findViewById(R.id.btn_save_edit_account);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfomation();
            }
        });
    }

    private void setBtnBack() {
        btn_back = findViewById(R.id.btn_back_address);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setTitle(){
        TextView txt = findViewById(R.id.title_address);
        switch (typeEdit){
            case "name":
                txt.setText("S???a t??n");
                break;
            case "phone":
                txt.setText("S???a s??? ??i???n tho???i");
                break;
            case "address":
                txt.setText("S???a ?????a ch???");
                break;

        }
    }

    private void saveInfomation() {

        switch (typeEdit){
            case "name":
                String newName= txt.getText().toString();
                if(!newName.equals("")){
                    _user.setName(newName);
                    editInfo(_user.getName(), "/name");

                }else {
                    Toast.makeText(getApplicationContext(), "T??n kh??ng ???????c ????? tr???ng", Toast.LENGTH_SHORT).show();
                }
                break;
            case "phone":
                String newPhone = txt.getText().toString();
                if(validationPhone(newPhone)){
                    _user.setPhone(newPhone);
                    editInfo(_user.getPhone(), "/phone");
                }else {
                    Toast.makeText(getApplicationContext(), "S??? ??i???n tho???i m???i kh??ng h???p l???", Toast.LENGTH_SHORT).show();
                }
                break;

            case "address":
                String newAddress = txt.getText().toString();
                if(!newAddress.equals("")){
                    _user.setAddress(newAddress);
                    editInfo(_user.getAddress(), "/address");

                }else {
                    Toast.makeText(getApplicationContext(), "?????a ch??? kh??ng ???????c ????? tr???ng", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    public boolean validationPhone(String phone){
        if(TextUtils.isEmpty(phone)){
            return false;
        }
        if(!PHONE_VN.matcher(phone).matches()){
            return false;
        }
        return true;
    }

    private void editInfo(String value, String path) {
        UserEdit edit = new UserEdit(value, path, "replace");
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

    private void getUser() {
        ApiServices.apiService.getUser(DataLocalManager.getId(), DataLocalManager.getToken()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
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