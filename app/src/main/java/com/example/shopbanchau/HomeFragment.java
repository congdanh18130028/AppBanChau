package com.example.shopbanchau;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
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
    private EditText edt;
    private Spinner spinner;
    private ImageButton price_up, price_down;
    private String[] prices = {"Tất cả", "0-100k", "100k-200k", ">200k"};
    private ImageButton sidebar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setSpinner(view);
        setBtnPriceUp(view);
        setBtnPriceDown(view);
        setBtnSidebar(view);
        edt = view.findViewById(R.id.txt_value_search);
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                   search();
                }
                return false;
            }
        });

        rcvProduct = view.findViewById(R.id.rcv_product);
        productAdapter = new ProductAdapter(view.getContext());
        GridLayoutManager manager = new GridLayoutManager(view.getContext(), 2);
        rcvProduct.setLayoutManager(manager);
        setListProduct();
        rcvProduct.setAdapter(productAdapter);
        return view;
    }

    private void search() {
        Intent i = new Intent(getContext(), SearchActivity.class);
        i.putExtra("SEARCH", edt.getText().toString());
        startActivity(i);
    }

    private void setBtnSidebar(View view) {
        sidebar = view.findViewById(R.id.btn_sidebar);
        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

    }


    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_sidebar, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.chau_mo_hinh:
                        Intent i = new Intent(getContext(), ProductActivity.class);
                        i.putExtra("CATEGORY", "Cây cảnh");
                        startActivity(i);
                        break;
                        
                }
                return true;
            }
        });
        popup.show();


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.chau_mo_hinh:
                Toast.makeText(getContext(), "nu", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }



    private void setBtnPriceDown(View view) {
        price_down = view.findViewById(R.id.btn_price_down);
        price_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiServices.apiService.getProductsDesc().enqueue(new Callback<List<Product>>() {
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
                        Toast.makeText(getContext(), "fail api!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setBtnPriceUp(View view) {
        price_up = view.findViewById(R.id.btn_price_up);
        price_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiServices.apiService.getProductsAsc().enqueue(new Callback<List<Product>>() {
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
                        Toast.makeText(getContext(), "fail api!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setSpinner(View view) {
        spinner = view.findViewById(R.id.spinner_price);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, prices);
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

    private void getProductsByPrice(int price1, int price2){
        ApiServices.apiService.getProductsPrice(price1, price2).enqueue(new Callback<List<Product>>() {
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
                Toast.makeText(getContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
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
