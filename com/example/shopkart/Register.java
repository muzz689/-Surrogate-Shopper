package com.example.shopkart;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Register extends AppCompatActivity {
    TextInputLayout passwordLayout;
    TextInputLayout confirmPassLayout;
    TextInputEditText password;
    TextInputEditText confirmPassword;
    private FusedLocationProviderClient fusedLocationClient;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button reg = findViewById(R.id.btnReg);
        passwordLayout = findViewById(R.id.txtPassword);
        confirmPassLayout = findViewById(R.id.txtConfirmPassword);

        ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                if (fineLocationGranted != null && fineLocationGranted) {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        checkGps();
                        fusedLocationClient.getCurrentLocation(100,null).addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location != null){
                                    Geocoder geocoder = new Geocoder(getApplicationContext());
                                    try {
                                        List<Address> locations = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                        address = locations.get(0).getAddressLine(0);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "Please enable location!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    Toast.makeText(getApplicationContext(), "Fine location granted!", Toast.LENGTH_SHORT).show();
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    fusedLocationClient.getCurrentLocation(100,null).addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                Geocoder geocoder = new Geocoder(getApplicationContext());
                                try {
                                    List<Address> locations = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    address = locations.get(0).getAddressLine(0);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Please enable location!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Approximate location granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No location granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });

        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canSubmit()) {
                    String User;
                    Intent i = getIntent();
                    User = i.getStringExtra("Usertype");

                    PHPRequest reqClient = new PHPRequest("https://lamp.ms.wits.ac.za/~s2436109/");
                    ContentValues cv = new ContentValues();

                    TextInputLayout firstName = findViewById(R.id.txtName);
                    TextInputLayout surname = findViewById(R.id.txtSurname);
                    TextInputLayout emailAddress = findViewById(R.id.txtEmailAddress);
                    TextInputEditText fname = (TextInputEditText) firstName.getEditText();
                    TextInputEditText sname = (TextInputEditText) surname.getEditText();
                    TextInputEditText email = (TextInputEditText) emailAddress.getEditText();
                    password = (TextInputEditText) passwordLayout.getEditText();

                    cv.put("Fname", fname.getText().toString());
                    cv.put("Sname", sname.getText().toString());
                    cv.put("Email", email.getText().toString());
                    cv.put("Password", password.getText().toString());
                    cv.put("Address", address);
                    cv.put("User", User);

                    String url = "test2.php";

                    reqClient.doRequest(Register.this, url, cv, new RequestHandler() {
                        @Override
                        public void processResponse(String response) {
                            try {
                                JSONObject resJson = new JSONObject(response);
                                if (resJson.getString("result").equals("0")) {
                                    Register.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast err = Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT);
                                            err.show();
                                        }
                                    });
                                } else {
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
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

    public boolean canSubmit() {
        password = (TextInputEditText) passwordLayout.getEditText();
        confirmPassword = (TextInputEditText) confirmPassLayout.getEditText();
        Toast errorToast;
        String pass = Objects.requireNonNull(password.getText()).toString();
        String confPass = Objects.requireNonNull(confirmPassword.getText()).toString();
        TextInputLayout firstName = findViewById(R.id.txtName);
        TextInputLayout surname = findViewById(R.id.txtSurname);
        TextInputLayout emailAddress = findViewById(R.id.txtEmailAddress);
        TextInputEditText fname = (TextInputEditText) firstName.getEditText();
        TextInputEditText sname = (TextInputEditText) surname.getEditText();
        TextInputEditText email = (TextInputEditText) emailAddress.getEditText();

        if(fname.getText().toString().isEmpty()){
            errorToast = Toast.makeText(getApplicationContext(),"Please enter First Name!", Toast.LENGTH_SHORT);
            errorToast.show();
            return false;
        }
        if(sname.getText().toString().isEmpty()){
            errorToast = Toast.makeText(getApplicationContext(),"Please enter Surname!", Toast.LENGTH_SHORT);
            errorToast.show();
            return false;
        }
        if(email.getText().toString().isEmpty()){
            errorToast = Toast.makeText(getApplicationContext(),"Please enter Email Address!", Toast.LENGTH_SHORT);
            errorToast.show();
            return false;
        }
        if(!pass.equals(confPass) || pass.isEmpty()){
            errorToast = Toast.makeText(getApplicationContext(),"Passwords don't match!", Toast.LENGTH_SHORT);
            errorToast.show();
            return false;
        }
        if(address.isEmpty()){
            errorToast = Toast.makeText(getApplicationContext(),"A location error has occured!", Toast.LENGTH_SHORT);
            errorToast.show();
        }

        return true;
    }

    public void checkGps(){
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Intent enableLocation = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(enableLocation);
        }
    }
}