package com.example.shopbanchau.api;

import com.example.shopbanchau.models.Bill;
import com.example.shopbanchau.models.CartItem;
import com.example.shopbanchau.models.Product;
import com.example.shopbanchau.models.Token;
import com.example.shopbanchau.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {

    Gson gson = new GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    
    ApiServices apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.31.237:45455")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices.class);
    @POST("api/login")
    Call<Token> authenticateUser(@Query("email") String email,
                                 @Query("password") String password);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/login")
    Call<String> getId(@Header("Authorization") String Aut);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/users/{id}")
    Call<User> getUser(@Path("id") int id, @Header("Authorization") String Aut);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/carts/id/{userId}")
    Call<Integer> getCartId(@Path("userId") int id, @Header("Authorization") String Aut);

    @POST("api/users")
    Call<User> addUser(@Body User user);

    @GET("api/users/email/{email}")
    Call<User> checkEmail(@Path("email") String email);

    @GET("api/products")
    Call<List<Product>> getProducts();

    @GET("api/products/{productId}")
    Call<Product> getProduct(@Path("productId") int id);

    @POST("api/carts")
    Call<CartItem> addCart(@Body CartItem cartItem);
    
    @GET("api/carts/{cartId}")
    Call<List<CartItem>> getAllCartItem(@Path("cartId") int id);

    @PATCH("api/carts/increase/{cartItemId}")
    Call<Void> increaseQuantity(@Path("cartItemId") int id);

    @PATCH("api/carts/decrease/{cartItemId}")
    Call<Void> decreaseQuantity(@Path("cartItemId") int id);

    @DELETE("api/carts/{cartItemId}")
    Call<Void> removeCartItem(@Path("cartItemId") int id);

    @GET("api/carts/totalPrice")
    Call<Integer> getTotalPrice(@Query("cartId") int cartId,
                                @Query("ship") int ship,
                                @Query("discount") int discount);

    @POST("api/bills")
    Call<Bill> addBill(@Body Bill bill);

}
