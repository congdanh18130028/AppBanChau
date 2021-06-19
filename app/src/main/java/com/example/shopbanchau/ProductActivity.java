package com.example.shopbanchau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.Product;
import com.example.shopbanchau.models.ProductAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView rcvProduct;
    private ProductAdapter productAdapter;
    private Spinner spinner;
    private ImageButton price_up, price_down, btn_back_back;
    private String[] prices = {"Tất cả", "0-100k", "100k-200k", ">200k"};
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Intent i = getIntent();

        setBtnBack();


        category = i.getStringExtra("CATEGORY");
        setSpinner();
        setBtnPriceUp();
        setBtnPriceDown();


        rcvProduct = findViewById(R.id.rcv_product_p);
        productAdapter = new ProductAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rcvProduct.setLayoutManager(manager);
        setListProduct();
        rcvProduct.setAdapter(productAdapter);
    }

    private void setBtnBack() {
        btn_back_back = findViewById(R.id.btn_back_category);
        btn_back_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setSpinner() {
        spinner = findViewById(R.id.spinner_price_p);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, prices);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        getProductsByPrice(0, 10000000);
                        break;
                    case 1:
                        getProductsByPrice(0, 100000);
                        break;
                    case 2:
                        getProductsByPrice(100000,200000);
                        break;
                    case 3:
                        getProductsByPrice(200000, 10000000);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setBtnPriceUp() {
        price_up = findViewById(R.id.btn_price_up_p);
        price_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiServices.apiService.getProductsCategoryAsc(category).enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful()) {
                            List<Product> list = response.body();
                            productAdapter.removeData();
                            productAdapter.setData(list);


                        }

                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "fail api!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setBtnPriceDown() {
        price_down = findViewById(R.id.btn_price_down_p);
        price_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiServices.apiService.getProductsCategoryDesc(category).enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful()) {
                            List<Product> list = response.body();
                            productAdapter.removeData();
                            productAdapter.setData(list);

                        }

                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "fail api!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getProductsByPrice(int price1, int price2){
        ApiServices.apiService.getProductsCategoryPrice(category, price1, price2).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> list = response.body();

                    productAdapter.removeData();
                    productAdapter.setData(list);


                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListProduct() {

        ApiServices.apiService.getProductsCategory(category).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
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