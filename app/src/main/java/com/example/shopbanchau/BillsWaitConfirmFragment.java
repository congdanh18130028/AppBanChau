package com.example.shopbanchau;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.models.Bill;
import com.example.shopbanchau.models.BillAdapter;
import com.example.shopbanchau.utils.DataLocalManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BillsWaitConfirmFragment extends Fragment {
    private RecyclerView rcvBillConfirm;
    private BillAdapter billAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bills_wait_confirm, container, false);


        rcvBillConfirm = view.findViewById(R.id.rcv_bill_wait_confirm);
        billAdapter = new BillAdapter(view.getContext());
        GridLayoutManager manager = new GridLayoutManager(view.getContext(), 1);
        rcvBillConfirm.setLayoutManager(manager);
        setListBillWaitConfirm();
        rcvBillConfirm.setAdapter(billAdapter);

        return  view;

    }

    private void setListBillWaitConfirm() {
        ApiServices.apiService.getListBillWaitConfirm(DataLocalManager.getToken(), DataLocalManager.getId(), 0).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                if(response.isSuccessful()){
                    List<Bill> list = response.body();
                    billAdapter.setData(list);

                }
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                Toast.makeText(getContext(), "fail api!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}