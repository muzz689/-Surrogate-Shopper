package com.example.shopkart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class VolPreviewOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_preview_order);
        Intent i = getIntent();
        String jsonString = i.getStringExtra("json");
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String name = i.getStringExtra("name");
        TextView title = findViewById(R.id.txtTitle);
        title.setText(name);

        PHPRequest requestClient = new PHPRequest("https://lamp.ms.wits.ac.za/~s2436109/");
        ContentValues cv = new ContentValues();
        try {
            cv.put("CustomerID", json.getString("CustomerID"));
            cv.put("RequestID", json.getString("RequestID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        requestClient.doRequest(this, "seemore.php", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) throws JSONException {
                JSONObject obj = new JSONObject(response);
                JSONArray itemArray = obj.getJSONArray("ItemArray");
                String items = "";
                for(int i = 0; i < itemArray.length(); i++){
                    items += itemArray.getJSONObject(i).getString("Content") +": " + itemArray.getJSONObject(i).getString("Quantity") + "\n";
                }
                TextView itemsView = findViewById(R.id.txtPreviewItems);
                itemsView.setText(items);

                JSONObject customerInfo = obj.getJSONArray("Customer").getJSONObject(0);
                TextView address = findViewById(R.id.txtAddress);
                address.setText(customerInfo.getString("Address"));
            }
        });
    }
}