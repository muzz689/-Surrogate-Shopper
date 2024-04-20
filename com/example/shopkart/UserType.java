package com.example.shopkart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        Button customer = findViewById(R.id.btnCustomer);
        Button volunteer = findViewById(R.id.btnVolunteer);

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserType.this, Register.class);
                i.putExtra("Usertype", "customer");
                startActivity(i);
                finish();
            }
        });

        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserType.this, Register.class);
                i.putExtra("Usertype", "volunteer");
                startActivity(i);
                finish();
            }
        });
    }
}