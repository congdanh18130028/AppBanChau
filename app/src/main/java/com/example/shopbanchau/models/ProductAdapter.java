package com.example.shopbanchau.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanchau.ProductDetailsActivity;
import com.example.shopbanchau.R;
import com.example.shopbanchau.utils.FormatCurrence;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context mContext;
    private List<Product> mListProduct;

    public ProductAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Product> list) {
        this.mListProduct = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_product, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                TextView textView = view.findViewById(R.id.txt_id_list);
                String id = textView.getText().toString();
                intent.putExtra("id",id);
                mContext.startActivity(intent);
            }
        });
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = mListProduct.get(position);
        if (product == null) {
            return;
        }
        holder.id.setText(String.valueOf(product.getId()));
        Picasso.get().load(product.getImgPath().get(0).getPath()).into(holder.img);
        holder.title.setText(product.getName());

        int price = product.getPrice();
        String formattedNumber = FormatCurrence.formatVnCurrence(mContext, String.valueOf(price));

        holder.price.setText(formattedNumber);
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null) {
            return mListProduct.size();
        }
        return 0;
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView id, title, price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.txt_id_list);
            img = itemView.findViewById(R.id.img_product_list);
            title = itemView.findViewById(R.id.txt_product_title_list);
            price = itemView.findViewById(R.id.txt_product_price_list);
        }
    }
}
