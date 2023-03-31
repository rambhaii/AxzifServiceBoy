package com.app.axzifserviceboy.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.axzifserviceboy.Adapter.AcceptOrderAdptar;
import com.app.axzifserviceboy.Model.Datum;
import com.app.axzifserviceboy.Model.ResponseList;
import com.app.axzifserviceboy.R;
import com.app.axzifserviceboy.Utils.Loader;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AcceptActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backprese;
    RecyclerView recyclerview;
    String list = " ";
    Location gps_loc = null;
    Location network_loc = null;
    Location final_loc;
    double longitude;
    double latitude;
    String userCountry, userAddress;
    TextView city, address, texttoolbar;
    String secureloginResponse;
    SwipeRefreshLayout setOnRefreshListener;
    String data;
    Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        backprese = findViewById(R.id.backprese);
        backprese.setOnClickListener(this);
        recyclerview = findViewById(R.id.recyclerview);
        texttoolbar = findViewById(R.id.texttoolbar);
        texttoolbar.setText("Accepted Orders List");
        data = getIntent().getStringExtra("data");


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else {
           /* latitude = 0.0;
            longitude = 0.0;*/
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                userCountry = addresses.get(0).getLocality();
                //String userCountry1 = addresses.get(0).getSubLocality();

                userAddress = addresses.get(0).getAddressLine(0);
                city.setText(userCountry);
                address.setText(addresses.get(0).getSubLocality() + " " + addresses.get(0).getAdminArea());
            } else {
                userCountry = "Unknown";
                //msg.setText(userCountry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        dataParse(data, latitude, longitude);


    }


    @SuppressLint("NotifyDataSetChanged")
    private void dataParse(String from, Double lat, double longi) {
        Gson gson = new Gson();
        ResponseList list = gson.fromJson(from, ResponseList.class);
        ArrayList<Datum> data = list.getData();
        AcceptOrderAdptar adapter = new AcceptOrderAdptar(this, data, lat, longi, loader);
        adapter.notifyDataSetChanged();
        recyclerview.setHasFixedSize(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(llm);
        recyclerview.setAdapter(adapter);

    }


    private void loc(String oiginLat, String orginLong, double destiLat, double destiLongi) {

        String uri = "http://maps.google.com/maps?saddr=" + Double.parseDouble(oiginLat) + "," + Double.parseDouble(orginLong) + "&daddr=" + 12.120000 + "," + 76.680000;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        if (v == backprese) {
            finish();
        }

    }
}