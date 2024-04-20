package com.example.shopkart;

import org.json.JSONException;

public interface RequestHandler {
    public abstract void processResponse(String response) throws JSONException;
}
