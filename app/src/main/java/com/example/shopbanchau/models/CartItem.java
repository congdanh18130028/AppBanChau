package com.example.shopbanchau.models;

public class CartItem {
    private int id;
    private int productId;
    private int quantity;
    private int cartId;

    public CartItem(int id, int productId, int quantity, int cartId) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.cartId = cartId;
    }
    public CartItem(int productId, int quantity, int cartId) {
        this.productId = productId;
        this.quantity = quantity;
        this.cartId = cartId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
