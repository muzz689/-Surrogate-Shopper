package com.example.shopkart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.btnSubmit);
        PHPRequest reqClient = new PHPRequest("https://lamp.ms.wits.ac.za/~s2436109/");

        login.setOnClickListener(view -> {
            TextInputLayout emailAddress = findViewById(R.id.txtEmailAddress);
            TextInputEditText email = (TextInputEditText) emailAddress.getEditText();
            TextInputLayout passwordLayout = findViewById(R.id.txtPassword);
            TextInputEditText password = (TextInputEditText) passwordLayout.getEditText();
            String url = "testlogin.php";
            assert email != null;
            if(Objects.requireNonNull(email.getText()).toString().isEmpty() || Objects.requireNonNull(Objects.requireNonNull(password).getText()).toString().isEmpty()){
                Toast errToast = Toast.makeText(getApplicationContext(),"Invalid Credentials!", Toast.LENGTH_SHORT);
                errToast.show();
            }else {
                ContentValues cv = new ContentValues();
                cv.put("Email", email.getText().toString());
                cv.put("Password", Objects.requireNonNull(password.getText()).toString());
                reqClient.doRequest(LoginActivity.this, url, cv, response -> {
                    try {
                        JSONArray arrRes = new JSONArray(response);
                        JSONObject resJson = arrRes.getJSONObject(0);
                        Context context = getApplicationContext();
                        if (resJson.has("CustomerID")) {
                            SharedPreferences preferences = context.getSharedPreferences("application", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("User",response);
                            editor.apply();
                            //System.out.println("Customer Res: " + resJson);
                            Intent i = new Intent(LoginActivity.this, CustomerHome.class);
                            startActivity(i);
                        } else if (resJson.has("VolunteerID")) {
                            SharedPreferences preferences = context.getSharedPreferences("application", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("User",response);
                            editor.apply();
                            //System.out.println("Volunteer Res: " + resJson);
                            Intent i = new Intent(LoginActivity.this, VolunteerHome.class);
                            startActivity(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LoginActivity.this.runOnUiThread(() -> {
                            Toast err = Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT);
                            err.show();
                        });
                    }
                });
            }
        });

        Button register = findViewById(R.id.btnReg);
        register.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, UserType.class);
            startActivity(i);
        });

        Button forgotPass = findViewById(R.id.btnForgotPass);
        forgotPass.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, ForgotPass.class);
            startActivity(i);
        });
    }
}