package com.example.shopbanchau;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.shopbanchau.R;
import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.BillDetails;
import com.example.shopbanchau.models.CartItem;
import com.example.shopbanchau.models.CartItemAdapter;
import com.example.shopbanchau.utils.DataLocalManager;
import com.example.shopbanchau.utils.FormatCurrence;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartLoginFragment extends Fragment {
    private RecyclerView rcvCart;
    private TextView txt_total_price;
    private Button btn_mua_hang;
    private CartItemAdapter cartItemAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_login, container, false);
        txt_total_price = view.findViewById(R.id.txt_total_price_cart);
        btn_mua_hang = view.findViewById(R.id.btn_mua_hang);

        onClickMuaHang();

        rcvCart = view.findViewById(R.id.rcv_cart);
        cartItemAdapter = new CartItemAdapter(view.getContext(), this);
        GridLayoutManager manager = new GridLayoutManager(view.getContext(), 1);
        rcvCart.setLayoutManager(manager);
        setListCartItem();
        rcvCart.setAdapter(cartItemAdapter);

        setTotalPrice();

        return view;
    }

    private void onClickMuaHang() {
        btn_mua_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BillActivity.class);
                startActivity(i);

            }
        });
    }


    private void setTotalPrice() {
        ApiServices.apiService.getTotalPrice(DataLocalManager.getCartId(), 0 ,0).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    int totalPrice = response.body();
                    String currencyFormat = FormatCurrence.formatVnCurrence(getContext(),String.valueOf(totalPrice));
                    txt_total_price.setText(currencyFormat);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListCartItem() {
        ApiServices.apiService.getAllCartItem(DataLocalManager.getCartId()).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if(response.isSuccessful()){
                    List<CartItem> list = response.body();
                    cartItemAdapter.setData(list);
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Toast.makeText(getContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
