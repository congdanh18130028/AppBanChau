package com.example.shopbanchau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;


import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.BillDetails;
import com.example.shopbanchau.models.BillDetailsAdapter;
import com.example.shopbanchau.models.CartItem;
import com.example.shopbanchau.models.CartItemAdapter;
import com.example.shopbanchau.utils.DataLocalManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity {
    private RecyclerView rcvBill;
    private BillDetailsAdapter billDetailsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        rcvBill = findViewById(R.id.rcv_bill_details);
        billDetailsAdapter = new BillDetailsAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rcvBill.setLayoutManager(manager);
        setListBillDetails();
        rcvBill.setAdapter(billDetailsAdapter);
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
}