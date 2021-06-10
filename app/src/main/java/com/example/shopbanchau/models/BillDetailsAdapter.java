package com.example.shopbanchau.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanchau.R;
import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.utils.FormatCurrence;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillDetailsAdapter extends RecyclerView.Adapter<BillDetailsAdapter.BillDetailsViewHolder>{
    private Context mContext;
    private List<BillDetails> mBillDetailsList;
    public BillDetailsAdapter(Context mContext){
        this.mContext = mContext;
    }
    public void setData(List<BillDetails> list){
        this.mBillDetailsList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public BillDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_details, parent, false);

        return new BillDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillDetailsAdapter.BillDetailsViewHolder holder, int position) {
        BillDetails billDetails = mBillDetailsList.get(position);
        if(billDetails == null){
            return;
        }
        ApiServices.apiService.getProduct(billDetails.getProductId()).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()){
                    Product p = response.body();
                    setViewOfProduct(p, holder);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(mContext.getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });
        holder.quantity.setText("x"+String.valueOf(billDetails.getQuantity()));

    }

    public void setViewOfProduct(Product product, BillDetailsAdapter.BillDetailsViewHolder holder){
        Picasso.get().load(product.getImgPath().get(0).getPath()).into(holder.img);
        holder.title.setText(product.getName());
        String currencyFormat = FormatCurrence.formatVnCurrence(mContext,String.valueOf(product.getPrice()));
        holder.price.setText(currencyFormat);

    }

    @Override
    public int getItemCount() {
        if(mBillDetailsList !=null){
            return mBillDetailsList.size();
        }
        return 0;
    }

    public class BillDetailsViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView title, price, quantity;
        public BillDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bill_details_img);
            title = itemView.findViewById(R.id.bill_details_name);
            price = itemView.findViewById(R.id.bill_details_price);
            quantity = itemView.findViewById(R.id.bill_details_quantity);
        }
    }
}
