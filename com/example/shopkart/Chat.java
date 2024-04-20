package com.example.shopkart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Chat extends AppCompatActivity {
    Handler handler = new Handler();
    Timer timer = new Timer(false);
    String CusID,volID, UserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PHPRequest reqClient = new PHPRequest("https://lamp.ms.wits.ac.za/~s2436109/");
        setContentView(R.layout.activity_chat);

        Intent i = getIntent();
        CusID = i.getStringExtra("CustomerID");
        volID = i.getStringExtra("VolunteerID");
        UserType = i.getStringExtra("UserType");
        List<Message> messages = new LinkedList<>();

        RecyclerView messageRecyler = (RecyclerView) findViewById(R.id.chatView);
        MessageListAdapter adapter = new MessageListAdapter(this,messages,UserType);
        messageRecyler.setLayoutManager(new LinearLayoutManager(this));
        messageRecyler.setAdapter(adapter);

        ContentValues cv = new ContentValues();
        cv.put("CustomerID", CusID);
        cv.put("VolunteerID", volID);

        SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqClient.doRequest(Chat.this, "chatInit.php", cv, new RequestHandler() {
                    @Override
                    public void processResponse(String response) throws JSONException {
                        JSONArray result = new JSONArray(response);
                        if(result.getJSONObject(0).has("MessageID")){
                            int r = messages.size();
                            messages.clear();
                            adapter.notifyDataSetChanged();
                            for(int j = 0; j < result.length(); j++){
                                String msg = result.getJSONObject(j).getString("Content");
                                String sender = result.getJSONObject(j).getString("Owner");
                                int id = Integer.parseInt(result.getJSONObject(j).getString("MessageID"));
                                Message ms = new Message(msg,sender,id);
                                messages.add(ms);
                                adapter.notifyItemInserted(messages.size()-1);
                            }
                        }
                    }
                });
                swipeRefresh.setRefreshing(false);
            }
        });

        TimerTask update = new TimerTask() {
            @Override
            public void run() {
                reqClient.doRequest(Chat.this, "chatInit.php", cv, new RequestHandler() {
                    @Override
                    public void processResponse(String response) throws JSONException {
                        JSONArray result = new JSONArray(response);
                        if(result.getJSONObject(0).has("MessageID")){
                            int r = messages.size();
                            messages.clear();
                            adapter.notifyDataSetChanged();
                            for(int j = 0; j < result.length(); j++){
                                String msg = result.getJSONObject(j).getString("Content");
                                String sender = result.getJSONObject(j).getString("Owner");
                                int id = Integer.parseInt(result.getJSONObject(j).getString("MessageID"));
                                Message ms = new Message(msg,sender,id);
                                messages.add(ms);
                                adapter.notifyItemInserted(messages.size()-1);
                            }
                        }
                    }
                });
                swipeRefresh.setRefreshing(false);
            }
        };

        reqClient.doRequest(Chat.this, "chatInit.php", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) throws JSONException {
                JSONArray result = new JSONArray(response);
                if(result.getJSONObject(0).has("MessageID")){
                    for(int j = 0; j < result.length(); j++){
                        String msg = result.getJSONObject(j).getString("Content");
                        String sender = result.getJSONObject(j).getString("Owner");
                        int id = Integer.parseInt(result.getJSONObject(j).getString("MessageID"));
                        Message ms = new Message(msg,sender,id);
                        messages.add(ms);
                        adapter.notifyItemInserted(messages.size()-1);
                    }
                }
                timer.schedule(update,5000,5000);
            }
        });

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText msg = findViewById(R.id.txtInput);
                if(!msg.getText().toString().isEmpty() || !msg.getText().toString().equals("")){
                    ContentValues cv = new ContentValues();
                    cv.put("CustomerID",CusID);
                    cv.put("VolunteerID",volID);
                    cv.put("Owner",UserType);
                    JSONObject mObj = new JSONObject();
                    try {
                        mObj.put("Content",msg.getText().toString().replaceAll("'", ""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String tst = mObj.toString();
                    reqClient.doRequest(Chat.this, "insertMessage.php", cv, tst,"", new RequestHandler() {
                        @Override
                        public void processResponse(String response) throws JSONException {
                            JSONObject res = new JSONObject(response);
                            int id = Integer.parseInt(res.getString("MessageID"));
                            Message m = new Message(msg.getText().toString().replaceAll("'", ""),UserType,id);
                            messages.add(m);
                            adapter.notifyItemInserted(messages.size()-1);
                            msg.setText("");
                            messageRecyler.scrollToPosition(messages.size()-1);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        finish();
    }
}