package com.example.shopkart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgotPass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        TextInputLayout emailAddress = findViewById(R.id.txtEmailAddress);
        TextInputEditText email = (TextInputEditText) emailAddress.getEditText();
        TextInputLayout passwordLayout = findViewById(R.id.txtPassword);
        TextInputEditText password = (TextInputEditText) passwordLayout.getEditText();
        TextInputLayout confPasswordLayout = findViewById(R.id.txtConfirmPassword);
        TextInputEditText confPassword = (TextInputEditText) confPasswordLayout.getEditText();

        Button update = findViewById(R.id.btnUpdate);
        PHPRequest reqClient = new PHPRequest("https://lamp.ms.wits.ac.za/~s2436109/");
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canSubmit(password,confPassword)){
                    String url = "forgot.php";
                    ContentValues cv = new ContentValues();
                    cv.put("Email", email.getText().toString());
                    cv.put("NewPassword", password.getText().toString());
                    reqClient.doRequest(ForgotPass.this, url, cv, new RequestHandler() {
                        @Override
                        public void processResponse(String response) {
                            try {
                                JSONObject resJson = new JSONObject(response);
                                //System.out.println(resJson);
                                if(!resJson.getString("result").equals("0")){
                                    finish();
                                }else{
                                    ForgotPass.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast err = Toast.makeText(getApplicationContext(),"Email does not exist!", Toast.LENGTH_SHORT);
                                            err.show();
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            finish();
                        }
                    });
                }
            }
        });
    }

    public boolean canSubmit(TextInputEditText password, TextInputEditText confirmPassword){
        Toast errorToast;
        String pass = Objects.requireNonNull(password.getText()).toString();
        String confPass = Objects.requireNonNull(confirmPassword.getText()).toString();
        TextInputLayout emailAddress = findViewById(R.id.txtEmailAddress);
        TextInputEditText email = (TextInputEditText) emailAddress.getEditText();

        if(email.getText().toString().isEmpty()){
            errorToast = Toast.makeText(getApplicationContext(),"Email is missing!", Toast.LENGTH_SHORT);
            errorToast.show();
            return false;

        }
        if(!pass.equals(confPass) || pass.isEmpty()){
            errorToast = Toast.makeText(getApplicationContext(),"Passwords don't match!", Toast.LENGTH_SHORT);
            errorToast.show();
            return false;
        }
        return true;
    }
}