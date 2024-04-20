package com.example.shopkart;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CustomerHome extends AppCompatActivity {
    CustomerManager userProfile;
    PHPRequest requestClient = new PHPRequest("https://lamp.ms.wits.ac.za/~s2436109/");
    Button addOrder;
    Button message;
    TextView addTxt;
    CardView card;
    TextView pending;
    String volId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("application", Context.MODE_PRIVATE);
        String user = preferences.getString("User", null);
        try {
            userProfile = new CustomerManager(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addOrder = findViewById(R.id.btnAddOrder);
        addTxt = findViewById(R.id.txtAdd);
        card = findViewById(R.id.card);
        message = findViewById(R.id.btnMessage);
        pending = findViewById(R.id.txtNotAccepted);

        addTxt.setVisibility(View.GONE);
        addOrder.setVisibility(View.GONE);
        card.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        pending.setVisibility(View.GONE);

        setGUI();

        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomerHome.this, CusNewOrder.class);
                i.putExtra("CustomerID",userProfile.getCustomerID());
                startForResult.launch(i);
                setGUI();
            }
        });

        ImageButton editProfile = findViewById(R.id.profileEdit);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomerHome.this, Profile.class);
                startActivity(i);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomerHome.this, Chat.class);
                i.putExtra("CustomerID", userProfile.getCustomerID());
                i.putExtra("VolunteerID", volId);
                i.putExtra("UserType","Customer");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerHome.this);
        builder.setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User wants to abort registration
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User has cancelled dialog so do nothing.
                    }
                });
        builder.create().show();
    }

    public void setCard(String response){
        String items = "";
        try {
            JSONArray obj = new JSONArray(response);
            //System.out.println(obj);
            for(int i = 0; i < obj.length()-1; i++){
                items += obj.getJSONObject(i).getString("Content") + ": " + obj.getJSONObject(i).getString("Quantity") + "\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView itemView = findViewById(R.id.txtPreviewItems);
        itemView.setText(items);
    }

    public void setGUI(){
        addTxt.setVisibility(View.GONE);
        addOrder.setVisibility(View.GONE);
        card.setVisibility(View.GONE);
        message.setVisibility(View.GONE);

        ContentValues cv = new ContentValues();
        cv.put("CustomerID", userProfile.getCustomerID());
        requestClient.doRequest(this, "custchkod.php", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) throws JSONException {
                try{
                    JSONArray results = new JSONArray(response);
                    card.setVisibility(View.VISIBLE);
                    setCard(response);
                    if(results.getJSONObject(results.length()-1).has("VolID")){
                        String id = results.getJSONObject(results.length()-1).getString("VolID");
                        if(id.equals("null")){
                            //System.out.println("VolID: " + id);
                            pending.setVisibility(View.VISIBLE);
                        }else{
                            volId = id;
                            message.setVisibility(View.VISIBLE);
                        }
                    }
                }catch(JSONException e){
                    addTxt.setVisibility(View.VISIBLE);
                    addOrder.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result != null && result.getResultCode() == RESULT_OK){
                setGUI();
            }
        }
    });

}