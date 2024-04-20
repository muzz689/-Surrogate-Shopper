package com.example.shopkart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CusNewOrder extends AppCompatActivity {
    List<newItem> items = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_new_order);

        RecyclerView recyclerView = findViewById(R.id.orderView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NewCusOrderAdapter adapter = new NewCusOrderAdapter(items);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CusNewOrder.this);
                builder.setTitle("New Item");

                LinearLayout l1 = new LinearLayout(CusNewOrder.this);
                l1.setOrientation(LinearLayout.VERTICAL);
                final EditText itemTxt = new EditText(CusNewOrder.this);
                final EditText quantity = new EditText(CusNewOrder.this);
                itemTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                itemTxt.setHint("Item");
                quantity.setHint("Quantity");
                l1.addView(itemTxt);
                l1.addView(quantity);
                builder.setView(l1);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String content = itemTxt.getText().toString();
                        String amount = quantity.getText().toString();
                        if(amount.isEmpty()){
                            amount = "1";
                        }
                        newItem item = new newItem(content, amount);
                        items.add(item);
                        adapter.notifyItemInserted(items.size() - 1);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });

        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray arr = new JSONArray();
                if(items.size() == 0){
                    Toast.makeText(getApplicationContext(),"You have not added any items!",Toast.LENGTH_SHORT).show();
                }else{
                    for(int i = 0; i < items.size(); i++){
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("Content",items.get(i).getContent());
                            obj.put("Quantity",items.get(i).getQuantity());
                            arr.put(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    addItems(arr);
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CusNewOrder.this);
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

    public void addItems(JSONArray arr){
        Intent i = getIntent();
        ContentValues cv = new ContentValues();
        cv.put("CustomerID", i.getStringExtra("CustomerID"));
        PHPRequest php = new PHPRequest("https://lamp.ms.wits.ac.za/~s2436109/");

        AlertDialog.Builder builder = new AlertDialog.Builder(CusNewOrder.this);
        builder.setTitle("Add Instructions");

        LinearLayout l1 = new LinearLayout(CusNewOrder.this);
        l1.setOrientation(LinearLayout.VERTICAL);
        final EditText instructions = new EditText(CusNewOrder.this);
        instructions.setInputType(InputType.TYPE_CLASS_TEXT);
        instructions.setHint("Delivery Instructions");
        l1.addView(instructions);
        builder.setView(l1);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String instruct = instructions.getText().toString();
                JSONObject instructs = new JSONObject();
                try {
                    instructs.put("Message",instruct);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String tst = arr.toString();
                //System.out.println(instructs);
                php.doRequest(CusNewOrder.this, "addItemsRequest.php", cv, tst,instructs.toString(), new RequestHandler() {
                    @Override
                    public void processResponse(String response) throws JSONException {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.show();
            }
        });

    }
}