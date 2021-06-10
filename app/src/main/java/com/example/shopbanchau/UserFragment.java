package com.example.shopbanchau;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {
    private Button startLogin, startRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        clickStartLogin(view);
        clickStartRegister(view);
        return view;
    }

    private void clickStartRegister(View view) {
        startRegister = view.findViewById(R.id.btn_start_act_register);
        startRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
    }

    private void clickStartLogin(View view) {
        startLogin = view.findViewById(R.id.btn_start_act_login);
        startLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });
    }


}
