package com.example.shopbanchau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BillSuccessActivity extends AppCompatActivity {
    Button btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_success);
        btn_continue = findViewById(R.id.btn_continue_shopping);
    }

    public void tiepTucMuaSam(View view) {
        finish();
    }
}