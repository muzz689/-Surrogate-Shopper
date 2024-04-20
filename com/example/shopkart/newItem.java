package com.example.shopkart;

public class newItem {
    private String content = null;
    private String quantity = null;

    public newItem(String content, String quantity){
        this.content = content;
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getContent() {
        return content;
    }
}
