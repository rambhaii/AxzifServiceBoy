package com.app.axzifserviceboy.DashBoad;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.app.axzifserviceboy.Activity.SignIn;
import com.app.axzifserviceboy.R;
import com.app.axzifserviceboy.Utils.ApplicationConstant;
import com.app.axzifserviceboy.Utils.UtilsMethod;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashBoadActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    Switch switchbutton;
    TextView switchstatus, activemsg;

    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_boad);

        loadFragment(new MainFragment());
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);

        switchstatus = findViewById(R.id.switchstatus);
        switchbutton = findViewById(R.id.switchbutton);

        switchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchbutton.isChecked()) {
                    switchstatus.setText("Active");
                    switchstatus.setTextColor(getResources().getColor(R.color.green));
                    //activemsg.setText("Congratulation, Your Partner Profile is Active.");
                } else {
                    switchstatus.setText("Inactive");
                    switchstatus.setTextColor(getResources().getColor(R.color.red));
                    //activemsg.setText("");
                }
            }
        });


        switchbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchbutton.isChecked()) {
                    UtilsMethod.INSTANCE.Updateworkingstatus(DashBoadActivity.this, "1");
                    switchstatus.setText("Active");
                    SharedPreferences prefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("status_data", "1");
                    editor.commit();
//                    activemsg.setText("Congratulation, Your Partner Profile is Active.");
                } else {
                    UtilsMethod.INSTANCE.Updateworkingstatus(DashBoadActivity.this, "0");
                    switchstatus.setText("Inactive");
                    SharedPreferences prefs = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("status_data", "0");
                    editor.commit();
                    //   activemsg.setText("");
                }
            }
        });


    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_containe, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home_nav:
                fragment = new MainFragment();
                break;
            case R.id.profile_nav:
                fragment = new ProfileFragment();
                break;
            case R.id.logOut_nav:
                Logout_popup();
                break;


        }
        return loadFragment(fragment);

    }

    private void Logout_popup() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewpop = inflater.inflate(R.layout.log_out, null);

        Button okButton = (Button) viewpop.findViewById(R.id.okButton);
        Button Cancel = (Button) viewpop.findViewById(R.id.Cancel);
        TextView msg = (TextView) viewpop.findViewById(R.id.msg);

        final Dialog dialog = new Dialog(DashBoadActivity.this);
        Cancel.setText("No");
        dialog.setCancelable(false);
        dialog.setContentView(viewpop);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        msg.setText("Do you want to logout in Application  ?");

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UtilsMethod.INSTANCE.setLoginrespose(DashBoadActivity.this, "", "0");
                startActivity(new Intent(DashBoadActivity.this, SignIn.class));
                finish();
                dialog.dismiss();

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();


    }
}
