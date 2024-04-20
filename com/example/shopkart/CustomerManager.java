package com.example.shopkart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomerManager {
    private final String CustomerID ,Fname, Sname, Email, Address, Password, Instruction;

    public CustomerManager(String customer) throws JSONException {
        JSONArray arrRes = new JSONArray(customer);
        JSONObject resJson = arrRes.getJSONObject(0);
        this.CustomerID = resJson.getString("CustomerID");
        this.Fname = resJson.getString("Fname");
        this.Sname = resJson.getString("Sname");
        this.Email = resJson.getString("Email");
        this.Address = resJson.getString("Address");
        this.Password = resJson.getString("Password");
        this.Instruction = resJson.getString("Instruction");
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public String getFname() {
        return Fname;
    }

    public String getSname() {
        return Sname;
    }

    public String getEmail() {
        return Email;
    }

    public String getAddress() {
        return Address;
    }

    public String getPassword() {
        return Password;
    }

    public String getInstruction() {
        return Instruction;
    }
}
