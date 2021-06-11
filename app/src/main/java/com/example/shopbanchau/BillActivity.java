package com.example.shopbanchau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity {
    private RecyclerView rcvBill;
    private BillDetailsAdapter billDetailsAdapter;
    private int totalPrice;
    private TextView txt_user_name, txt_user_phone, txt_user_location, txt_tong_tien_hang , txt_tong_thanh_toan, txt_total_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        setUserBill();
        setBill();

        rcvBill = findViewById(R.id.rcv_bill_details);
        billDetailsAdapter = new BillDetailsAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rcvBill.setLayoutManager(manager);
        setListBillDetails();
        rcvBill.setAdapter(billDetailsAdapter);
    }

    private void setBill() {
        txt_tong_tien_hang = findViewById(R.id.txt_tong_tien_hang);
        txt_tong_thanh_toan = findViewById(R.id.txt_tong_thanh_toan);
        txt_total_price = findViewById(R.id.txt_tong_thanh_toan_bottom);
        ApiServices.apiService.getTotalPrice(DataLocalManager.getCartId(), 0, 0).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    int price = response.body();
                    String currencyFormat = FormatCurrence.formatVnCurrence(getApplicationContext(),String.valueOf(price));
                    txt_tong_tien_hang.setText(currencyFormat);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        ApiServices.apiService.getTotalPrice(DataLocalManager.getCartId(), 30000, 0).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    int price = response.body();
                    totalPrice = price;
                    String currencyFormat = FormatCurrence.formatVnCurrence(getApplicationContext(),String.valueOf(price));
                    txt_tong_thanh_toan.setText(currencyFormat);
                    txt_total_price.setText(currencyFormat);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUserBill() {
        txt_user_name = findViewById(R.id.txt_user_name_bill);
        txt_user_phone = findViewById(R.id.txt_user_phone_bill);
        txt_user_location = findViewById(R.id.txt_user_location_bill);
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

    private void setListBillDetails() {
        ApiServices.apiService.getAllCartItem(DataLocalManager.getCartId()).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if(response.isSuccessful()){
                    List<CartItem> list = response.body();
                    List<BillDetails> result = new ArrayList<BillDetails>();
                    for(CartItem i : list){
                        BillDetails b = new BillDetails(i.getProductId(), i.getQuantity());
                        result.add(b);
                    }
                    billDetailsAdapter.setData(result);
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void datHang(View view) {

        Bill bill = new Bill(Calendar.getInstance().getTime(), DataLocalManager.getId(), totalPrice, 0, false );
        ApiServices.apiService.addBill(bill).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(BillActivity.this, BillSuccessActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}