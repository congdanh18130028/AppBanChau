package com.example.shopbanchau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.Product;
import com.example.shopbanchau.models.ProductAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView rcvProduct;
    private ProductAdapter productAdapter;
    private String value;
    private ImageButton btn_back_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent i = getIntent();
        value = i.getStringExtra("SEARCH");

        setBtnBack();
        rcvProduct = findViewById(R.id.rcv_product_s);
        productAdapter = new ProductAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rcvProduct.setLayoutManager(manager);
        setListProduct(value);
        rcvProduct.setAdapter(productAdapter);

    }

    private void setBtnBack() {
        btn_back_back = findViewById(R.id.btn_back_search);
        btn_back_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListProduct(String value) {
        ApiServices.apiService.getProductsSearch(value).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    List<Product> list = response.body();
                    productAdapter.setData(list);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}