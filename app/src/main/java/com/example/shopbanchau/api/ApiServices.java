package com.example.shopbanchau.api;

import com.example.shopbanchau.models.Bill;
import com.example.shopbanchau.models.BillDetails;
import com.example.shopbanchau.models.CartItem;
import com.example.shopbanchau.models.Product;
import com.example.shopbanchau.models.Token;
import com.example.shopbanchau.models.User;
import com.example.shopbanchau.models.UserEdit;
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
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();
    
    ApiServices apiService = new Retrofit.Builder()
            .baseUrl("https://chaudecor.tk")
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

    @GET("api/products/search")
    Call<List<Product>> getProductsSearch(@Query("value") String value);

    @GET("api/products/category")
    Call<List<Product>> getProductsCategory(@Query("category") String category);

    @GET("api/products/asc")
    Call<List<Product>> getProductsAsc();

    @GET("api/products/category/asc")
    Call<List<Product>> getProductsCategoryAsc(@Query("category") String category);

    @GET("api/products/desc")
    Call<List<Product>> getProductsDesc();

    @GET("api/products/category/desc")
    Call<List<Product>> getProductsCategoryDesc(@Query("category") String category);

    @GET("api/products/price")
    Call<List<Product>> getProductsPrice(@Query("price1") int price1,
                                         @Query("price2") int price2);

    @GET("api/products/category/price")
    Call<List<Product>> getProductsCategoryPrice(@Query("category") String category,
                                                 @Query("price1") int price1,
                                                 @Query("price2") int price2);

    @GET("api/products/{productId}")
    Call<Product> getProduct(@Path("productId") int id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/carts")
    Call<CartItem> addCart(@Body CartItem cartItem,
                           @Header("Authorization") String Aut);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/carts/{cartId}")
    Call<List<CartItem>> getAllCartItem(@Path("cartId") int id,
                                        @Header("Authorization") String Aut);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PATCH("api/carts/increase/{cartItemId}")
    Call<Void> increaseQuantity(@Path("cartItemId") int id,
                                @Header("Authorization") String Aut);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PATCH("api/carts/decrease/{cartItemId}")
    Call<Void> decreaseQuantity(@Path("cartItemId") int id,
                                @Header("Authorization") String Aut);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @DELETE("api/carts/{cartItemId}")
    Call<Void> removeCartItem(@Path("cartItemId") int id,
                              @Header("Authorization") String Aut);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/carts/totalPrice")
    Call<Integer> getTotalPrice(@Query("cartId") int cartId,
                                @Query("ship") int ship,
                                @Query("discount") int discount,
                                @Header("Authorization") String Aut);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/bills")
    Call<Bill> addBill(@Body Bill bill,
                       @Header("Authorization") String Aut);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PATCH("api/users/{userId}")
    Call<Void> editUser(@Path("userId") int id,
                        @Header("Authorization") String Aut,
                        @Body List<UserEdit> list);

    @GET("api/users/forgotPassword/{email}")
    Call<Void> getForgotPassword(@Path("email") String email);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/bills/billDetails/{billId}")
    Call<List<BillDetails>> getListBillDetails(@Header("Authorization") String Aut,
                                               @Path("billId") int billId);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/bills/user")
    Call<List<Bill>> getListBillWaitConfirm(@Header("Authorization") String Aut,
                                            @Query("userId") int userId,
                                            @Query("state") int state);
}
