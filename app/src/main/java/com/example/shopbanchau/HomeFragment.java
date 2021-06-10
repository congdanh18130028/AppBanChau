package com.example.shopbanchau;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.FilePath;
import com.example.shopbanchau.models.Product;
import com.example.shopbanchau.models.ProductAdapter;
import com.example.shopbanchau.utils.DataLocalManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rcvProduct;
    private ProductAdapter productAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rcvProduct = view.findViewById(R.id.rcv_product);
        productAdapter = new ProductAdapter(view.getContext());
        GridLayoutManager manager = new GridLayoutManager(view.getContext(), 2);
        rcvProduct.setLayoutManager(manager);
        setListProduct();
        rcvProduct.setAdapter(productAdapter);
        return view;
    }


    private void setListProduct() {

        ApiServices.apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                        List<Product> list = response.body();
                        productAdapter.setData(list);
                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
