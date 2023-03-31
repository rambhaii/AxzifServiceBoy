package com.app.axzifserviceboy.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.app.axzifserviceboy.Model.Datum;
import com.app.axzifserviceboy.R;
import com.google.gson.Gson;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {
    ImageView backprese;
    AppCompatButton calling_btn;
    TextView order_id, cust_name, p_name, customer_phone, pay_status, pay_type, texttoolbar, totalamt, pamount, totalpcost;
    TextView date;
    String data;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        data = getIntent().getStringExtra("data");
        Datum datum = new Gson().fromJson(data, Datum.class);
        order_id = findViewById(R.id.order_id);
        order_id.setText(datum.getOrder_id());

        date = findViewById(R.id.date);
        date.setText(datum.getDelivery_date());

        cust_name = findViewById(R.id.cust_name);
        cust_name.setText(datum.getName());

        customer_phone = findViewById(R.id.customer_phone);
        customer_phone.setText(datum.getMobile());

        totalamt = findViewById(R.id.totalamt);
        totalamt.setText(datum.getTotal_amount() + ".00");

        pay_type = findViewById(R.id.pay_type);
        if (datum.getPayment_type().equalsIgnoreCase("0")) {
            pay_type.setText("COD");
        } else if (datum.getPayment_type().equalsIgnoreCase("1")) {
            pay_type.setText("Online");
        }

        pay_status = findViewById(R.id.pay_status);
        if (datum.getPayment_status().equalsIgnoreCase("0")) {
            pay_status.setText("Failed");
        } else if (datum.getPayment_status().equalsIgnoreCase("1")) {
            pay_status.setText("Success");
        } else if (datum.getPayment_status().equalsIgnoreCase("3")) {
            pay_status.setText("Pending");
        } else if (datum.getPayment_status().equalsIgnoreCase("4")) {
            pay_status.setText("Refund");
        } else if (datum.getPayment_status().equalsIgnoreCase("2")) {
            pay_status.setText("COD");
        }

        p_name = findViewById(R.id.p_name);
        p_name.setText(datum.getProduct_name() + " " + " = ");

        pamount = findViewById(R.id.pamount);
        pamount.setText(datum.getTotal_amount() + ".00");

        totalpcost = findViewById(R.id.totalpcost);
        totalpcost.setText(datum.getTotal_amount() + ".00");

        backprese = findViewById(R.id.backprese);
        backprese.setOnClickListener(this);


        texttoolbar = findViewById(R.id.texttoolbar);
        texttoolbar.setText("Order Details");


        calling_btn = findViewById(R.id.calling_btn);
        calling_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+datum.getMobile()));
                startActivity(intent);
            }

        });
    }

    @Override
    public void onClick(View view) {
        if (view == backprese) {
            finish();
        }
    }
}