package com.example.shopbanchau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.Bill;
import com.example.shopbanchau.models.BillDetails;
import com.example.shopbanchau.models.BillDetailsAdapter;
import com.example.shopbanchau.models.CartItem;
import com.example.shopbanchau.models.User;
import com.example.shopbanchau.utils.DataLocalManager;
import com.example.shopbanchau.utils.FormatCurrence;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewBillActivity extends AppCompatActivity {
    private int ID;
    private RecyclerView rcvBill;
    private BillDetailsAdapter billDetailsAdapter;
    private int totalPrice;
    private ImageButton btn_back;
    private TextView txt_user_name, txt_user_phone, txt_user_location, txt_total_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_bill);

        Intent i = getIntent();
        ID = i.getIntExtra("ID", 0);

        setUserBill();
        setBill();
        btn_back = findViewById(R.id.btn_back_review_bill);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rcvBill = findViewById(R.id.rcv_review_bill_details);
        billDetailsAdapter = new BillDetailsAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rcvBill.setLayoutManager(manager);
        setListBillDetails();
        rcvBill.setAdapter(billDetailsAdapter);

    }

    private void setListBillDetails() {
        ApiServices.apiService.getListBillDetails(DataLocalManager.getToken(), ID).enqueue(new Callback<List<BillDetails>>() {
            @Override
            public void onResponse(Call<List<BillDetails>> call, Response<List<BillDetails>> response) {
                if(response.isSuccessful()){
                    List<BillDetails> list = response.body();
                    billDetailsAdapter.setData(list);


                }
            }

            @Override
            public void onFailure(Call<List<BillDetails>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUserBill() {
        txt_user_name = findViewById(R.id.txt_user_name_review_bill);
        txt_user_phone = findViewById(R.id.txt_user_phone_review_bill);
        txt_user_location = findViewById(R.id.txt_user_location_review_bill);
        ApiServices.apiService.getUser(DataLocalManager.getId(), DataLocalManager.getToken()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User u = response.body();
                    txt_user_location.setText(u.getAddress());
                    txt_user_name.setText(u.getName());
                    txt_user_phone.setText(u.getPhone());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBill() {

        txt_total_price = findViewById(R.id.txt_tong_thanh_toan_review_bill);
        ApiServices.apiService.getBill(DataLocalManager.getToken(), ID).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                if(response.isSuccessful()){
                    Bill b = response.body();
                    int price = b.getTotalPrice();
                    String currencyFormat = FormatCurrence.formatVnCurrence(getApplicationContext(),String.valueOf(price));
                    txt_total_price.setText(currencyFormat);


                }
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}