package com.app.axzifserviceboy.DashBoad;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.axzifserviceboy.Adapter.Servicess;
import com.app.axzifserviceboy.Classes.FragmentActivityMessage;
import com.app.axzifserviceboy.Classes.GlobalBus;
import com.app.axzifserviceboy.Model.Data;
import com.app.axzifserviceboy.Model.Datum;
import com.app.axzifserviceboy.Model.ResponseList;
import com.app.axzifserviceboy.R;
import com.app.axzifserviceboy.Utils.ApplicationConstant;
import com.app.axzifserviceboy.Utils.UtilsMethod;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DashBoadFragment extends Fragment
{
    RecyclerView recyclerview;
    String  list=" ";
    Location gps_loc = null;
    Location network_loc = null;
    Location final_loc;
    double longitude;
    double latitude;
    String userCountry, userAddress;
    TextView city,address;
    String secureloginResponse;
    SwipeRefreshLayout setOnRefreshListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

       View v=  inflater.inflate(R.layout.fragment_dash_boad, container, false);
       GetId(v);
        return v;
    }

    private void GetId(View v)
    {
        recyclerview=v.findViewById(R.id.recyclerview);
       // setOnRefreshListener = v.findViewById(R.id.setOnRefreshListener);
        LocationManager locationManager = (LocationManager)getActivity(). getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return ;}
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
        }
        else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else {
           /* latitude = 0.0;
            longitude = 0.0;*/
        }
        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0)
            {   userCountry = addresses.get(0).getLocality();
                //String userCountry1 = addresses.get(0).getSubLocality();

                userAddress = addresses.get(0).getAddressLine(0);
                city.setText(userCountry );
                address.setText(addresses.get(0).getSubLocality()+" "+addresses.get(0).getAdminArea());
            }
            else
            {   userCountry = "Unknown";
                //msg.setText(userCountry);
            }}
        catch (Exception e) {
            e.printStackTrace();}



        SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        list = myPreferences.getString(ApplicationConstant.INSTANCE.list, "")+"";

        Log.d("hsagdlist",list);
        if (!list.equalsIgnoreCase(""))
        {
          // dataParse(list);
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        secureloginResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        Data  securelogincheckResponse = new Gson().fromJson(secureloginResponse, Data.class);
        String userId=securelogincheckResponse.getId();
       // UtilsMethod.INSTANCE.SerVicesList(getActivity(),userId,"","");
    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage)
    {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("listData"))
        {
           /* if (list.equalsIgnoreCase(""))
            {
              //  dataParse(activityFragmentMessage.getFrom());

            }*/
            dataParse(activityFragmentMessage.getFrom());
            Log.d("fro544343m",activityFragmentMessage.getFrom());





        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void dataParse(String from)
    {
        Toast.makeText(getActivity(), ""+latitude, Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        ResponseList list = gson.fromJson(from, ResponseList.class);
        ArrayList<Datum> data = list.getData();
        Servicess adapter=new Servicess(getActivity(),data,latitude,longitude);
        adapter.notifyDataSetChanged();
        recyclerview.setHasFixedSize(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(llm);
        recyclerview.setAdapter(adapter);

    }


    @Override
    public void onDestroy() {
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    private void loc(String oiginLat, String orginLong, double destiLat, double destiLongi)
    {

        String uri = "http://maps.google.com/maps?saddr=" + Double.parseDouble(oiginLat) + "," + Double.parseDouble(orginLong) + "&daddr=" +12.120000 + "," + 76.680000;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        getActivity().startActivity(intent);

    }











}