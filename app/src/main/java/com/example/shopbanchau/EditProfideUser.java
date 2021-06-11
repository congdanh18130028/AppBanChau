package com.example.shopbanchau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.User;
import com.example.shopbanchau.utils.DataLocalManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfideUser extends AppCompatActivity {
    private TextView txt_name, txt_phone, txt_address;
    private User _user;
    private LinearLayout btn_name, btn_phone, btn_address, btn_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profide_user);
        setView();
        setButtonEdit();
    }

    private void setButtonEdit() {
        btn_name = findViewById(R.id.btn_edit_name);
        btn_phone = findViewById(R.id.btn_edit_phone);
        btn_address = findViewById(R.id.btn_edit_address);
        btn_password = findViewById(R.id.btn_edit_password);

        btn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfideUser.this, EditAddressActivity.class);
                i.putExtra("TYPE","name");
                startActivity(i);
            }
        });

        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfideUser.this, EditAddressActivity.class);
                i.putExtra("TYPE","phone");
                startActivity(i);
            }
        });

        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfideUser.this, EditAddressActivity.class);
                i.putExtra("TYPE","address");
                startActivity(i);
            }
        });

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfideUser.this, ChangesPassword.class);
                startActivity(i);

            }
        });

    }

    private void setView() {
        txt_name = findViewById(R.id.txt_name_edit);
        txt_phone = findViewById(R.id.txt_phone_edit);
        txt_address = findViewById(R.id.txt_address_edit);

        ApiServices.apiService.getUser(DataLocalManager.getId(), DataLocalManager.getToken()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    _user = user;
                    txt_name.setText(_user.getName());
                    txt_phone.setText(_user.getPhone());
                    txt_address.setText(_user.getAddress());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "api fail!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}