package com.app.axzifserviceboy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.axzifserviceboy.Activity.SignIn;
import com.app.axzifserviceboy.DashBoad.DashBoadActivity;
import com.app.axzifserviceboy.Utils.ApplicationConstant;

public class MainActivity extends AppCompatActivity {
    String type = "";
    String secureloginResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        secureloginResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        type = myPreferences.getString(ApplicationConstant.INSTANCE.one, "");


        // UtilsMethod.INSTANCE.SerVicesList(this,userId,"","");

        if (type.equalsIgnoreCase("1")) {


            Intent intent = new Intent(MainActivity.this, DashBoadActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(MainActivity.this, SignIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}