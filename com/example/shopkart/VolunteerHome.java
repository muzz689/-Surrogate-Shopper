package com.example.shopkart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.VoiceInteractor;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class VolunteerHome extends AppCompatActivity implements OnItemClickListener,OnItemAcceptListener {
    List<JSONObject> items = new LinkedList<>();
    RecyclerView recycleView;
    Button msg;
    VolOrderListAdapter adapter = new VolOrderListAdapter(items);
    VolAcceptedOrder adapterPreview = new VolAcceptedOrder(items);
    PHPRequest phpClient = new PHPRequest("https://lamp.ms.wits.ac.za/~s2436109/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_home);
        ContentValues cv = new ContentValues();
        cv.put("VolunteerID",getVolunteerID());
        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        msg = findViewById(R.id.btnMessage);
        msg.setVisibility(View.GONE);

        adapter.setClickListener(this);
        adapterPreview.setClickListener(this);

        recycleView.setAdapter(adapter);
        updateItemsList();

        ImageButton editProfile = findViewById(R.id.profileEdit);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VolunteerHome.this, Profile.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btnMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VolunteerHome.this, Chat.class);
                try {
                    i.putExtra("CustomerID", items.get(items.size()-1).getString("CustomerID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                i.putExtra("VolunteerID", getVolunteerID());
                i.putExtra("UserType","Volunteer");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(VolunteerHome.this);
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

    private String getVolunteerID(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("application", Context.MODE_PRIVATE);
        String strUser = preferences.getString("User", null);
        JSONArray usersArr = null;
        String id = "";
        try {
            usersArr = new JSONArray(strUser);
            JSONObject user = usersArr.getJSONObject(0);
            id = user.getString("VolunteerID");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void onClick(View view, int position) {
        JSONObject temp = items.get(position);
        ContentValues cv = new ContentValues();
        try {
            cv.put("RequestID", temp.getString("RequestID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        phpClient.doRequest(this, "orderfinish.php", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) throws JSONException {
                //System.out.println(response);
                items.clear();
                updateItemsList();
                recycleView.setAdapter(adapter);
                msg.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAccept(View view, int position){
        JSONObject obj = items.get(position);
        ContentValues cv = new ContentValues();
        cv.put("VolunteerID",getVolunteerID());
        try {
            cv.put("RequestID",obj.getString("RequestID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        phpClient.doRequest(this, "vaccept.php", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) throws JSONException {
                //System.out.println(response);
                items.clear();
                updateItemsList();
                msg.setVisibility(View.VISIBLE);
            }
        });
    }

    public void updateItemsList(){
        ContentValues cv = new ContentValues();
        cv.put("VolunteerID",getVolunteerID());
        phpClient.doRequest(this, "vhome2.php", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                JSONArray orders = null;
                try {
                    orders = new JSONArray(response);
                    JSONObject obj = orders.getJSONObject(0);
                    if(!obj.has("Content")){
                        for(int i = 0; i < orders.length(); i++){
                            JSONObject temp = orders.getJSONObject(i);
                            items.add(temp);
                            adapter.notifyItemInserted(items.size() - 1);
                        }
                    }else{
                        recycleView.setAdapter(adapterPreview);
                        //System.out.println(orders);
                        for(int i = 0; i < orders.length(); i++){
                            JSONObject temp = orders.getJSONObject(i);
                            items.add(temp);
                            adapterPreview.notifyItemInserted(items.size() - 1);
                            msg.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}