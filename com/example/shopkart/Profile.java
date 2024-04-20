package com.example.shopkart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextInputLayout firstName = findViewById(R.id.txtName);
        TextInputLayout surname = findViewById(R.id.txtSurname);
        TextInputLayout emailAddress = findViewById(R.id.txtEmailAddress);
        TextInputLayout passwordLayout = findViewById(R.id.txtPassword);

        TextInputEditText fname = (TextInputEditText) firstName.getEditText();
        TextInputEditText sname = (TextInputEditText) surname.getEditText();
        TextInputEditText email = (TextInputEditText) emailAddress.getEditText();
        TextInputEditText password = (TextInputEditText) passwordLayout.getEditText();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("application", Context.MODE_PRIVATE);
        String strUser = preferences.getString("User", null);
        JSONArray usersArr = null;
        try {
            usersArr = new JSONArray(strUser);
            JSONObject user = usersArr.getJSONObject(0);
            //System.out.println("User Profile: " + user);
            fname.setText(user.getString("Fname"));
            sname.setText(user.getString("Sname"));
            email.setText(user.getString("Email"));
            password.setText(user.getString("Password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button submit = findViewById(R.id.btnUpdate);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PHPRequest phpClient = new PHPRequest("https://lamp.ms.wits.ac.za/~s2436109/");
                ContentValues cv = new ContentValues();
                String fnames = fname.getText().toString();
                String snames = sname.getText().toString();
                String emailNew = email.getText().toString();
                String passwords = password.getText().toString();

                if(!fnames.equals("") && !snames.equals("") && !emailNew.equals("") && !passwords.equals("")){
                    cv.put("EmailOld", getVolunteerEmail());
                    cv.put("Fname", fnames);
                    cv.put("Sname", snames);
                    cv.put("EmailNew", emailNew);
                    cv.put("Password", passwords);
                }else{
                    Toast.makeText(getApplicationContext(),"You can't leave a field blank!", Toast.LENGTH_SHORT).show();
                }


                phpClient.doRequest(Profile.this, "profile.php", cv, new RequestHandler() {
                    @Override
                    public void processResponse(String response) {
                        try {
                            JSONArray result = new JSONArray(response);
                            if(!result.getJSONObject(0).has("result")){
                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("application", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("User",response);
                                editor.apply();
                                finish();
                            }
                        } catch (JSONException e) {
                            try {
                                JSONObject result = new JSONObject(response);
                                if(result.has("result")){
                                    Toast err = Toast.makeText(Profile.this, result.getString("status"), Toast.LENGTH_SHORT);
                                    err.show();
                                }
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }

                        }
                    }
                });
            }
        });
    }

    private String getVolunteerEmail(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("application", Context.MODE_PRIVATE);
        String strUser = preferences.getString("User", null);
        JSONArray usersArr;
        String email = "";
        try {
            usersArr = new JSONArray(strUser);
            JSONObject user = usersArr.getJSONObject(0);
            email = user.getString("Email");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return email;
    }
}