package com.example.shopbanchau.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanchau.CartLoginFragment;
import com.example.shopbanchau.R;
import com.example.shopbanchau.api.ApiServices;
import com.example.shopbanchau.utils.DataLocalManager;
import com.example.shopbanchau.utils.FormatCurrence;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private Context mContext;
    private List<CartItem> mCartItemList;
    private CartLoginFragment cartLoginFragment;
    private LinearLayout linear;
    private TextView empty;
    private int cartItemId;


    public CartItemAdapter(Context mContext, CartLoginFragment fragment) {
        this.mContext = mContext;
        this.cartLoginFragment = fragment;

    }
    public void setData(List<CartItem> list){
        this.mCartItemList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);

        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartItemAdapter.CartItemViewHolder holder, int position) {
        CartItem cartItem = mCartItemList.get(position);

        if(cartItem == null){
            return;
        }

        holder.id.setText(String.valueOf(cartItem.getId()));

        ApiServices.apiService.getProduct(cartItem.getProductId()).enqueue(new Callback<Product>() {
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
        holder.quantity.setText(String.valueOf(cartItem.getQuantity()));



    }
    public void setViewOfProduct(Product product, CartItemAdapter.CartItemViewHolder holder){
        Picasso.get().load(product.getImgPath().get(0).getPath()).into(holder.img);
        holder.title.setText(product.getName());
        String currencyFormat = FormatCurrence.formatVnCurrence(mContext,String.valueOf(product.getPrice()));
        holder.price.setText(currencyFormat);

    }

    @Override
    public int getItemCount() {
        if(mCartItemList !=null){
            return mCartItemList.size();
        }
        return 0;
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView id, title, price, quantity;
        private ImageButton btn_delete;
        private ImageView btn_increase, btn_decrease;
        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.cart_item_img);
            id = itemView.findViewById(R.id.cart_item_id);
            title = itemView.findViewById(R.id.cart_item_name);
            price = itemView.findViewById(R.id.cart_item_price);
            quantity = itemView.findViewById(R.id.cart_item_quantity);
            btn_increase = itemView.findViewById(R.id.btn_increase_quantity);
            btn_decrease = itemView.findViewById(R.id.btn_decrease_quantity);
            btn_delete = itemView.findViewById(R.id.btn_delete_cart_item);

            btn_increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItemId = Integer.parseInt(id.getText().toString());
                    ApiServices.apiService.increaseQuantity(cartItemId, DataLocalManager.getToken()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                int n =Integer.parseInt(quantity.getText().toString()) +1;
                                quantity.setText(String.valueOf(n));
                                setTotalPrice();

                            }

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(mContext.getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

            btn_decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItemId = Integer.parseInt(id.getText().toString());
                    ApiServices.apiService.decreaseQuantity(cartItemId, DataLocalManager.getToken()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                int n =Integer.parseInt(quantity.getText().toString()) -1;
                                if(n==0){
                                    mCartItemList.remove(getBindingAdapterPosition());
                                    notifyItemRemoved(getBindingAdapterPosition());
                                }else {
                                    quantity.setText(String.valueOf(n));
                                }
                                setTotalPrice();

                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(mContext.getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItemId = Integer.parseInt(id.getText().toString());
                    ApiServices.apiService.removeCartItem(cartItemId, DataLocalManager.getToken()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                               mCartItemList.remove(getBindingAdapterPosition());
                               notifyItemRemoved(getBindingAdapterPosition());
                               setTotalPrice();
                                if(getItemCount() ==0){
                                    linear = cartLoginFragment.getView().findViewById(R.id.linearLayout_by_cart);
                                    empty = cartLoginFragment.getView().findViewById(R.id.txt_empty_cart);
                                    linear.setVisibility(View.GONE);
                                    empty.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(mContext.getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });



        }

        private void setTotalPrice() {
            TextView txt_total_price = cartLoginFragment.getView().findViewById(R.id.txt_total_price_cart);
            ApiServices.apiService.getTotalPrice(DataLocalManager.getCartId(), 0 ,0, DataLocalManager.getToken()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.isSuccessful()){
                        int totalPrice = response.body();
                        String currencyFormat = FormatCurrence.formatVnCurrence(mContext,String.valueOf(totalPrice));
                        txt_total_price.setText(currencyFormat);
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Toast.makeText(mContext, "fail api!", Toast.LENGTH_SHORT).show();
                }
            });
        }



    }
}
