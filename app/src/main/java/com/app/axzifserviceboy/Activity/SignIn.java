package com.app.axzifserviceboy.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.app.axzifserviceboy.R;
import com.app.axzifserviceboy.Utils.Loader;
import com.app.axzifserviceboy.Utils.UtilsMethod;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
 EditText password,email;
 private AppCompatButton btnsignin;
 Loader loader;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {   super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email=findViewById(R.id.email);
        password=findViewById(R.id.pp);
        btnsignin=findViewById(R.id.btnsignin);
        btnsignin.setOnClickListener(this);
        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public void onClick(View v)
    {
        if (v==btnsignin)
        {

            if (email.getText().toString().isEmpty())
            {
                email.setError("Please enter email Id");
                email.requestFocus();
            }
            else if (password.getText().toString().isEmpty())
            {   password.setError("Please enter password");
                password.requestFocus();
            }
            else if (UtilsMethod.INSTANCE.isValidEmail(email.getText().toString()) == false)
            {

                email.setError("Please Enter valid User Id");
                email.requestFocus();
            }
            else if (UtilsMethod.INSTANCE.isNetworkAvialable(this) == false)
            {
                Toast.makeText(this, "Please Check Your Network Connectivity", Toast.LENGTH_SHORT).show();
            }
            else
            {   loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilsMethod.INSTANCE.signIn(SignIn.this,loader,email.getText().toString(),password.getText().toString());
            }
        }
    }


}