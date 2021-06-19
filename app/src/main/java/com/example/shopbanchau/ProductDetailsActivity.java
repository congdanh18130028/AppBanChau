
package com.example.shopbanchau;

import android.content.Intent;
import android.os.Bundle;

import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.CartItem;
import com.example.shopbanchau.models.Product;
import com.example.shopbanchau.utils.DataLocalManager;
import com.example.shopbanchau.utils.FormatCurrence;


import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    private int productId;
    private Product product;
    private TextView txt_id, txt_title, txt_price, txt_description;
    private ImageView img;
    private ImageButton btn_add_cart, btn_back;
    private Button btn_buy_now;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        btn_back = findViewById(R.id.btn_back_address);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setId();
        getProduct();



    }



    private void setView(Product product) {
        txt_id = findViewById(R.id.product_id);
        txt_title = findViewById(R.id.product_title);
        txt_price = findViewById(R.id.product_price);
        txt_description = findViewById(R.id.product_decription);
        img = findViewById(R.id.product_img);

        btn_add_cart = findViewById(R.id.btn_add_cart);

        btn_buy_now = findViewById(R.id.btn_buy_now);

        txt_id.setText(String.valueOf(product.getId()));
        txt_title.setText(product.getName());
        String formattedNumber = FormatCurrence.formatVnCurrence(this,String.valueOf(product.getPrice()));
        txt_price.setText(formattedNumber);
        txt_description.setText(product.getDescripton());
        Picasso.get().load(product.getImgPath().get(0).getPath()).into(img);

    }

    private void getProduct() {
        ApiServices.apiService.getProduct(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()){
                    Product product = response.body();
                    setView(product);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "api fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setId() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if(!id.equals("")){
            productId = Integer.parseInt(id);
        }

    }

    public void addCart(View view) {
        int product_id = Integer.parseInt(txt_id.getText().toString());
        int quantity = 1;
        int cartId = DataLocalManager.getCartId();
        if(cartId!=0){
            CartItem cartItem = new CartItem(product_id, quantity, cartId);
            ApiServices.apiService.addCart(cartItem, DataLocalManager.getToken()).enqueue(new Callback<CartItem>() {
                @Override
                public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(view.getContext(), "success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CartItem> call, Throwable t) {
                    Toast.makeText(view.getContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(view.getContext(), "chua dang nhap", Toast.LENGTH_SHORT).show();
        }


    }

    public void byNow(View view) {
    }


}