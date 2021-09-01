package com.example.shopbanchau;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.User;
import com.example.shopbanchau.utils.DataLocalManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginFragment extends Fragment {
    private Button btnLogout;
    private LinearLayout btnEditAccount, btnViewListBills;
    private TextView txt_email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_login, container, false);
        logout(view);
        setEmail(view);
        setCartId();
        clickButtonEditAccount(view);
        clickButtonViewListBills(view);
        return view;

    }

    private void clickButtonViewListBills(View view) {
        btnViewListBills = view.findViewById(R.id.btn_view_list_bills);
        btnViewListBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListBills.class);
                startActivity(intent);
            }
        });
    }

    private void setCartId() {
        ApiServices.apiService.getCartId(DataLocalManager.getId(), DataLocalManager.getToken()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    int cartId = response.body();
                    DataLocalManager.setCartId(cartId);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "api fail!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickButtonEditAccount(View view) {
        btnEditAccount = view.findViewById(R.id.btn_edit_account);
        btnEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfideUser.class);
                startActivity(intent);
            }
        });
    }



    private void setEmail(View view) {
        try {
            txt_email = view.findViewById(R.id.txt_fm_user_email);
            int id = DataLocalManager.getId();
            String token = DataLocalManager.getToken();
            ApiServices.apiService.getUser(id, token).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    txt_email.setText(user.getEmail());
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                }
            });

        }catch (NumberFormatException e){
            Log.println(Log.ERROR, "NumberFormatException", e.getMessage() );
        }
    }

    private void logout(View view) {
        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Bạn chắc chắn muốn đăng xuất?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataLocalManager.removeToken();
                                DataLocalManager.removeID();
                                DataLocalManager.removeCartID();
                                getActivity().finish();
                                startActivity(getActivity().getIntent());
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();

            }
        });
    }

}
