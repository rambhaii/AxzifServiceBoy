package com.app.axzifserviceboy.DashBoad;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.axzifserviceboy.Classes.FragmentActivityMessage;
import com.app.axzifserviceboy.Classes.GlobalBus;
import com.app.axzifserviceboy.Model.Data;
import com.app.axzifserviceboy.R;
import com.app.axzifserviceboy.Utils.ApplicationConstant;
import com.app.axzifserviceboy.Utils.UtilsMethod;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MainFragment extends Fragment implements View.OnClickListener {
    TextView switchstatus, helloname, activemsg;
    View callbutton;
    TextView txtMarquee;
    LinearLayout all_services, accept, reject, completed;
    String secureloginResponse;
    Data securelogincheckResponse;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        GetId(v);
        return v;
    }

    @SuppressLint("SetTextI18n")
    public void GetId(View v) {
        txtMarquee = v.findViewById(R.id.marqueeText);

        // Now we will call setSelected() method
        // and pass boolean value as true
        callbutton = v.findViewById(R.id.callbtn);
        txtMarquee.setSelected(true);
        all_services = v.findViewById(R.id.all_services);
        accept = v.findViewById(R.id.accept);
        reject = v.findViewById(R.id.reject);
        completed = v.findViewById(R.id.completed);
        helloname = v.findViewById(R.id.helloname);
//        activemsg = v.findViewById(R.id.activemsg);
        SharedPreferences prefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String status = prefs.getString("status_data", "");
        Log.d("stajhgfhjtus", status + "hello");

       /* switchstatus = v.findViewById(R.id.switchstatus);
        switchbutton = v.findViewById(R.id.switchbutton);

        if (switchbutton.isChecked()) {
            switchstatus.setText("Active");
            activemsg.setText("Congratulation, Your Partner Profile is Active.");
        } else {
            switchstatus.setText("Inactive");
            activemsg.setText("");
        }

        switchbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchbutton.isChecked()) {
                    UtilsMethod.INSTANCE.Updateworkingstatus(getActivity(),"1");
                    switchstatus.setText("Active");
                    activemsg.setText("Congratulation, Your Partner Profile is Active.");
                } else
                {
                    UtilsMethod.INSTANCE.Updateworkingstatus(getActivity(),"0");
                    switchstatus.setText("Inactive");
                    activemsg.setText("");
                }
            }
        });*/

        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7897982544"));
                startActivity(intent);
            }
        });
        completed.setOnClickListener(this);
        reject.setOnClickListener(this);
        accept.setOnClickListener(this);
        all_services.setOnClickListener(this);
        SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        secureloginResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        securelogincheckResponse = new Gson().fromJson(secureloginResponse, Data.class);
        userId = securelogincheckResponse.getId();
        helloname.setText("Hello"+" "+securelogincheckResponse.getName()+",");

    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("datastatus")) {

            //  dataParse(activityFragmentMessage.getFrom());
            SharedPreferences prefs = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String status = prefs.getString("status_data", "");
            securelogincheckResponse = new Gson().fromJson(secureloginResponse, Data.class);
            userId = securelogincheckResponse.getId();
            Log.d("stajhgfhjtus", status + "hello");
            Log.d("nfkljsblsdv", new Gson().toJson(activityFragmentMessage.getFrom()));
//            if (activityFragmentMessage.getFrom().equalsIgnoreCase("1"))
//            {
//                activemsg.setText("Congratulation, Your Partner Profile is Active.");
//            } else
//            {
//                activemsg.setText("");
//            }
        }
    }

    @Override
    public void onDestroy() {
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == all_services) {

            UtilsMethod.INSTANCE.SerVicesList(getActivity(), userId, "assigned", "", "allservices");

        }
        if (v == accept) {

            UtilsMethod.INSTANCE.SerVicesList(getActivity(), userId, "accept", "", "AcceptActivity");

        }
        if (v == reject) {

            UtilsMethod.INSTANCE.SerVicesList(getActivity(), userId, "reject", "", "RejectActivity");

        }
        if (v == completed) {

            UtilsMethod.INSTANCE.SerVicesList(getActivity(), userId, "deliverd", "", "CompletedServices");

        }
    }
}