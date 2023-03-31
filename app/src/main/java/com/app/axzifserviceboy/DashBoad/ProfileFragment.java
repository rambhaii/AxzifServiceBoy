package com.app.axzifserviceboy.DashBoad;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.axzifserviceboy.Model.Data;
import com.app.axzifserviceboy.R;
import com.app.axzifserviceboy.Utils.ApplicationConstant;
import com.app.axzifserviceboy.Utils.Loader;
import com.app.axzifserviceboy.Utils.UtilsMethod;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    String secureloginResponse;
    Data securelogincheckResponse;
    TextView name, contact,email,address;
    EditText boy_name,boy_number,boy_email,city,state,boy_address,boy_address2,zip;
    CircleImageView profilerimg,edit_profileimg;
    LinearLayout view_details,edit_details;
    Button btn_edit;
    AppCompatButton updatebtn;
    Loader loader;
    ImageView edit_imgupdate;
    String imgProfile="";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        GETid(v);
        return v;
    }

    public void GETid(View v)
    {
        loader = new Loader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        name = v.findViewById(R.id.name);
        profilerimg = v.findViewById(R.id.view_profileimg);
        view_details = v.findViewById(R.id.view_details);
        btn_edit = v.findViewById(R.id.btn_edit);
        email = v.findViewById(R.id.email);
        contact = v.findViewById(R.id.contact);
        address = v.findViewById(R.id.address);
         updatebtn = v.findViewById(R.id.updatebtn);

          updatebtn.setOnClickListener(this);
            btn_edit.setOnClickListener(this);
            SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            secureloginResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
            securelogincheckResponse = new Gson().fromJson(secureloginResponse, Data.class);
            Log.d("sjdkfmxvjhxvxcv"," helloc  "+securelogincheckResponse.getId());
            name.setText(securelogincheckResponse.getName());
            contact.setText(securelogincheckResponse.getMobile());
            email.setText(securelogincheckResponse.getEmail());
            address.setText(securelogincheckResponse.getAddress1());
            Log.d("hjfsderryureu",ApplicationConstant.INSTANCE.baseUrl+securelogincheckResponse.getIcon_img());

            Glide.with(getActivity())
                .load(ApplicationConstant.INSTANCE.baseUrl+securelogincheckResponse.getProfile_img())
                .placeholder(R.drawable.delivery)
                .fitCenter()
                .into(profilerimg);

     //////////////////////// update////////////////////////////////////

        boy_name = v.findViewById(R.id.boy_name);
        boy_number = v.findViewById(R.id.boy_number);
        boy_email = v.findViewById(R.id.boy_email);
        city = v.findViewById(R.id.city);
        state = v.findViewById(R.id.state);
        boy_address = v.findViewById(R.id.boy_address);
        edit_details = v.findViewById(R.id.edit_details);
        edit_imgupdate = v.findViewById(R.id.edit_imgupdate);
        edit_profileimg = v.findViewById(R.id.edit_profileimg);
        boy_address2 = v.findViewById(R.id.boy_address2);
        zip = v.findViewById(R.id.zip);


        edit_imgupdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v==btn_edit)
        {
            edit_details.setVisibility(View.VISIBLE);
            view_details.setVisibility(View.GONE);
            boy_name.setText(securelogincheckResponse.getName());
            boy_number.setText(securelogincheckResponse.getMobile());
            boy_email.setText(securelogincheckResponse.getEmail());
            boy_address.setText(securelogincheckResponse.getAddress1());
            city.setText(securelogincheckResponse.getCity());
            state.setText(securelogincheckResponse.getState());
            boy_address2.setText(securelogincheckResponse.getAddress2());
            zip.setText(securelogincheckResponse.getZip());

        }

        if (v==edit_imgupdate)
        {
            verifyStoragePermissions();
            chooseImage();
        }

        if (v==updatebtn)
        {

            Log.d("vnmnv","dfskjkjdf");
            profileUpdate();
        }




    }
    public void profileUpdate()
    {


        if (boy_name.getText().toString().isEmpty()) {
            boy_name.setError("Please enter first Name");
            boy_name.requestFocus();
        }

        else if (boy_address.getText().toString().isEmpty())
        {   boy_address.setError("Please enter Address");
            boy_address.requestFocus();
        }
        else if (boy_address2.getText().toString().isEmpty())
        {   boy_address2.setError("Please enter Address");
            boy_address2.requestFocus();
        }
        else if (zip.getText().toString().isEmpty())
        {   zip.setError("Please enter Zipcode");
            zip.requestFocus();
        }
        else if (imgProfile.isEmpty())
        {
            Toast.makeText(getActivity(), "Please select Image ", Toast.LENGTH_SHORT).show();
        }

        else if (city.getText().toString().isEmpty())
        {   city.setError("Please enter City Name");
            city.requestFocus();
        }
        else if (state.getText().toString().isEmpty())
        {   state.setError("Please enter state");
            state.requestFocus();
        }else if (boy_number.getText().toString().isEmpty())
        {   boy_number.setError("Please Enter Phone Number");
            boy_number.requestFocus();
        }
        else if(boy_number.getText().toString().length()!=10)
        {
            boy_number.setError("Please Enter Minimum  10 Digits ");
            boy_number.requestFocus();
        }

        else if (UtilsMethod.INSTANCE.isNetworkAvialable(getActivity()) == false)
        {
            Toast.makeText(getActivity(), "Please Check Your Network Connectivity", Toast.LENGTH_SHORT).show();
        }
        else
        {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);
           // Log.d("fkdjvnbvhkjvjc",securelogincheckResponse.getId());
                 UtilsMethod.INSTANCE.updateProfile(getActivity(),loader,
                 securelogincheckResponse.getId(),
                 boy_name.getText().toString()
                ,securelogincheckResponse.getDob().toString(),
                 city.getText().toString()
                ,state.getText().toString()
                 ,securelogincheckResponse.getCountry().toString(),
                  zip.getText().toString(),
                 boy_address.getText().toString()
                  ,boy_address2.getText().toString()
                 ,securelogincheckResponse.getLandmark().toString(),
                 securelogincheckResponse.getAdditional_info().toString()
                 ,imgProfile
                 );
        }}


    public void verifyStoragePermissions() {

        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    private void chooseImage()
    {
        Intent pickimage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimage, 2);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2)
            {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri)
                {
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getActivity().getContentResolver().query(selectedImageUri, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    imgProfile = picturePath;

                    Log.d("djfhjyurtuyrt",""+imgProfile);
                    edit_profileimg.setImageURI(selectedImageUri);
                }


            }
        }
    }

}