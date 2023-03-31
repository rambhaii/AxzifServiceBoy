package com.app.axzifserviceboy.Utils;

import static android.content.Context.MODE_PRIVATE;

import static com.app.axzifserviceboy.Activity.OpenBoxDeliveryActivity.getDataColumn;
import static com.app.axzifserviceboy.Activity.OpenBoxDeliveryActivity.isDownloadsDocument;
import static com.app.axzifserviceboy.Activity.OpenBoxDeliveryActivity.isExternalStorageDocument;
import static com.app.axzifserviceboy.Activity.OpenBoxDeliveryActivity.isGooglePhotosUri;
import static com.app.axzifserviceboy.Activity.OpenBoxDeliveryActivity.isMediaDocument;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.axzifserviceboy.Activity.ALLServices;
import com.app.axzifserviceboy.Activity.AcceptActivity;
import com.app.axzifserviceboy.Activity.CompletedServices;
import com.app.axzifserviceboy.Activity.RejectActivity;
import com.app.axzifserviceboy.Classes.FragmentActivityMessage;
import com.app.axzifserviceboy.Classes.GlobalBus;
import com.app.axzifserviceboy.DashBoad.DashBoadActivity;
import com.app.axzifserviceboy.Model.Data;
import com.app.axzifserviceboy.Model.MyResponse;
import com.app.axzifserviceboy.Model.ResponseData;
import com.app.axzifserviceboy.Model.ResponseList;
import com.app.axzifserviceboy.R;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.aabhasjindal.otptextview.OtpTextView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

public enum UtilsMethod {
    INSTANCE;

    public void setLoginrespose(Context context, String Loginrespose, String one) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.Loginrespose, Loginrespose);
        editor.putString(ApplicationConstant.INSTANCE.one, one);
        editor.commit();

    }

    public void list(Context context, String list) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.list, list);
        editor.commit();

    }
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return uri.toString();
    }

    public void upload(Context context,ArrayList<Uri> userimage,String productId,String orderId)
    {

        Log.d("upload","sjdfhj");
         ArrayList<MultipartBody.Part> fileToUpload1 = new ArrayList<>();
          for (int i = 0; i < userimage.size(); i++)
            {
                File file = new File(getPathFromUri(context, userimage.get(i)));
               // File file = new File(String.valueOf(userimage.get(i)));
                Log.d("dfjgmfmbvj", String.valueOf(file));
                RequestBody requestBody1 = RequestBody.create(MediaType.parse("*image/*"), file);
                fileToUpload1.add(MultipartBody.Part.createFormData("productImgae[]", file.getName(), requestBody1));
            }
        RequestBody product_Id = RequestBody.create(MediaType.parse("text/plain"), productId);
        RequestBody order_Id = RequestBody.create(MediaType.parse("text/plain"), orderId);

        try
        {
            String header = ApplicationConstant.INSTANCE.Headertoken;
            AllAPIs api = APIClient.getClient().create(AllAPIs.class);
          Call<ResponseList> call = api.uploadImage(header,fileToUpload1,order_Id);
           call.enqueue(new Callback<ResponseList>() {
             @Override
             public void onResponse(Call<ResponseList> call, Response<ResponseList> response)
             {
                 Log.d("dssdjhgkjfdmfg",""+response.body().getMessage());
                 if (response.body().getStatus()==1)
                 {
                     Toast.makeText(context, "Product Images uploaded successfully !", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onFailure(Call<ResponseList> call, Throwable t)
             {
                 Log.d("vxcdskjfdmfg",""+t.getMessage());
             }
         });


        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }














    public  void updateProfile(Context context,Loader loader,String delivery_boy_id,String name,String dob,String city,String state,String country,String zip,String address1
            ,String address2,String landmark,String additional_info,String image)
    {
        try
        {
            File file = new File(image);
            Log.e("profileimage", "" + file);
            MultipartBody.Part fileToUpload1;
            if (image.equalsIgnoreCase("1")) {
                fileToUpload1 = MultipartBody.Part.createFormData("image", "");

            } else {
                RequestBody requestBody1 = RequestBody.create(MediaType.parse("*image/*"), file);
                fileToUpload1 = MultipartBody.Part.createFormData("files", file.getName(), requestBody1);
            }

            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), delivery_boy_id);
            RequestBody first_name = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody d_dob = RequestBody.create(MediaType.parse("text/plain"), dob);


            RequestBody city_name = RequestBody.create(MediaType.parse("text/plain"), city);
            RequestBody state_name = RequestBody.create(MediaType.parse("text/plain"), state);
            RequestBody c_country = RequestBody.create(MediaType.parse("text/plain"), country);
            RequestBody address1_name = RequestBody.create(MediaType.parse("text/plain"), address1);
            RequestBody address2_name = RequestBody.create(MediaType.parse("text/plain"), address2);
            RequestBody z_zipcode = RequestBody.create(MediaType.parse("text/plain"), zip);
            RequestBody l_landmark = RequestBody.create(MediaType.parse("text/plain"), landmark);
            RequestBody a_additional_info = RequestBody.create(MediaType.parse("text/plain"), additional_info);
            String header = ApplicationConstant.INSTANCE.Headertoken;
            AllAPIs api = APIClient.getClient().create(AllAPIs.class);
            Call<MyResponse> call = api.updateprofileimg(header, user_id, first_name, d_dob, city_name, state_name, c_country, address1_name, address2_name, z_zipcode, l_landmark, a_additional_info, fileToUpload1);
            call.enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response)
                {

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }

                        if (response.body().getStatus()==1)
                        {
                            Toast.makeText(context, "Profile Update Successfully!", Toast.LENGTH_SHORT).show();
                            UtilsMethod.INSTANCE.setLoginrespose(context, "" + new Gson().toJson(response.body().getData()), "1");
                            context.startActivity(new Intent(context,DashBoadActivity.class));
                        }
                        else
                        {
                            Toast.makeText(context, "Error !", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<MyResponse> call, Throwable t)
                {
                    Log.d("sddfgsdfg",""+t.getMessage());
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.d("sdfgdfgvvf",""+e.getMessage());
        }
    }


    public void signIn(Context context, Loader loader, String email, String password) {
        try {

            Log.d("gkncb","dfjxvc"+email+""+password);
            String header = ApplicationConstant.INSTANCE.Headertoken;
            AllAPIs api = APIClient.getClient().create(AllAPIs.class);
            Call<MyResponse> call = api.signIn(header, email, password);
            call.enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response)
                {

                    if (response != null)
                    {
                        if (loader != null)
                        {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {

                            if (response.body().getStatus() == 1)
                            {
                                UtilsMethod.INSTANCE.setLoginrespose(context, "" + new Gson().toJson(response.body().getData()), "1");

                                Intent i = new Intent(context, DashBoadActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(i);
                                Toast.makeText(context, "Login successfully !", Toast.LENGTH_SHORT).show();
                               // sweetAlertBox(context, "Login successfully!", "DashBoad");

                            } else {
                                sweetAlertBoxFailed(context, "Please Enter valid e-mail or password", "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


                @Override
                public void onFailure(Call<MyResponse> call, Throwable t)
                {
                    if (loader != null)
                    {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Toast.makeText(context, "Please Enter correct E-mail id or Password ", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.d("sdjhrrrf", e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void SerVicesList(Context context, String UserId, String status, String date, String activity) {

        String header = ApplicationConstant.INSTANCE.Headertoken;
        AllAPIs api = APIClient.getClient().create(AllAPIs.class);
        Call<ResponseList> call = api.listServices(header, UserId, status, date);
        call.enqueue(new Callback<ResponseList>() {
            @Override
            public void onResponse(Call<ResponseList> call, Response<ResponseList> response) {
                if (response.body().getStatus() == 1) {
                    if (activity.equalsIgnoreCase("allservices")) {
                        context.startActivity(new Intent(context, ALLServices.class).putExtra("data", new Gson().toJson(response.body()).toString()));
                    } else if (activity.equalsIgnoreCase("AcceptActivity")) {
                        context.startActivity(new Intent(context, AcceptActivity.class).putExtra("data", new Gson().toJson(response.body()).toString()));
                    } else if (activity.equalsIgnoreCase("RejectActivity")) {
                        context.startActivity(new Intent(context, RejectActivity.class).putExtra("data", new Gson().toJson(response.body()).toString()));
                    } else if (activity.equalsIgnoreCase("CompletedServices")) {
                        context.startActivity(new Intent(context, CompletedServices.class).putExtra("data", new Gson().toJson(response.body()).toString()));
                    }
                }
                else
                {
                    Toast.makeText(context, "Con't have a Orders ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseList> call, Throwable t) {
                Log.d("ewurty", t.getMessage());
            }
        });


    }

    //  UpdateStatus
    public void UpdateStatus(Context context, String deliveryBoyId, String status, String order_id, String message) {

        Log.d("lidst", deliveryBoyId + "  " + status + "  " + order_id);
        String header = ApplicationConstant.INSTANCE.Headertoken;
        AllAPIs api = APIClient.getClient().create(AllAPIs.class);
        Call<ResponseList> call = api.UpdateStatus(header, deliveryBoyId, status, order_id);
        call.enqueue(new Callback<ResponseList>() {
            @Override
            public void onResponse(Call<ResponseList> call, Response<ResponseList> response) {
                if (response.body().getStatus() == 1) {
                    Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseList> call, Throwable t) {
                Log.d("messageeeee", "" + t.getMessage());
            }
        });

    }

    public void sendOtp(Context context, Loader loader, String order_id, String payment_type, TextView success, TextView delivered, String userId) {
        String header = ApplicationConstant.INSTANCE.Headertoken;
        AllAPIs api = APIClient.getClient().create(AllAPIs.class);
        Call<MyResponse> call = api.SendOtp(header, order_id);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response != null) {
                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                        Log.d("dfjkdkfjgh",""+response.body().getData().getOtp());
                    if (response.body().getStatus() == 1)
                    {
                        Toast.makeText(context, " Otp send Successfully", Toast.LENGTH_SHORT).show();
                        updateStatus(context, order_id, payment_type, success, delivered, userId);

                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.d("msufvnbjhnvbhj", t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void otpVerify(Context context, String order_id, String payment_type, String otp, TextView success, TextView delivered, String userId,Dialog dialog) {
        String header = ApplicationConstant.INSTANCE.Headertoken;
        AllAPIs api = APIClient.getClient().create(AllAPIs.class);
        Call<ResponseList> call = api.verify(header, order_id, payment_type, otp);
        call.enqueue(new Callback<ResponseList>() {
            @Override
            public void onResponse(Call<ResponseList> call, Response<ResponseList> response) {
                if (response.body().getStatus() == 1) {
                    Toast.makeText(context, "Successfully verify", Toast.LENGTH_SHORT).show();
                    UtilsMethod.INSTANCE.UpdateStatus(context, userId, "deliverd", order_id, "Successfully Delivered ");
                    success.setVisibility(View.VISIBLE);
                    delivered.setVisibility(View.GONE);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "This otp is wrong ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseList> call, Throwable t) {
                Log.d("msufvnbjhnvbhj", t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void updateStatus(Context context, String order_id, String payment_type, TextView success, TextView delivered, String userId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewPup = inflater.inflate(R.layout.verfyotp, null);

        Button okButton = (Button) viewPup.findViewById(R.id.okButton);
        Button cancelButton = (Button) viewPup.findViewById(R.id.cancelButton);

        RadioGroup radioGroup;
        radioGroup = viewPup.findViewById(R.id.radioGroup);
        TextView verify = viewPup.findViewById(R.id.verify);
        OtpTextView otpverify = viewPup.findViewById(R.id.otp_view);

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(viewPup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("7655e", otpverify.getOTP());
                if (otpverify.getOTP().isEmpty())
                {
                    otpverify.showError();
                    Toast.makeText(context, "Please Enter otp", Toast.LENGTH_SHORT).show();
                }
                else {

                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    if (selectedRadioButtonId != -1)
                    {
                        RadioButton payment;
                        payment = viewPup.findViewById(selectedRadioButtonId);
                        String paymentType = payment.getText().toString();

                        if (paymentType.equals("Online"))
                        {
                            otpVerify(context, order_id, "1", otpverify.getOTP(), success, delivered, userId,dialog);
                           // dialog.dismiss();

                        } else if (paymentType.equalsIgnoreCase("offline")) {
                            otpVerify(context, order_id, "0", otpverify.getOTP(), success, delivered, userId,dialog);
                            //dialog.dismiss();
                        }

                    }

                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();


            }
        });

        dialog.show();
    }


    public void sweetAlertBoxFailed(Context context, String message, String openspage) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("")
                .setContentText("" + "" + message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                    }
                }).show();
    }

    public void sweetAlertBox(Context context, String message, String openspage) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("")
                .setContentText("" + "" + message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        if (openspage.equalsIgnoreCase("DashBoad"))
                        {
                            Intent i = new Intent(context, DashBoadActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(i);
                        }


                    }
                }).show();
    }

    public boolean isValidEmail(String email) {

        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public boolean isNetworkAvialable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean isValidPassword(String password)
    {

        Pattern pattern;
        Matcher matcher;

        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public void Updateworkingstatus(Context context, String status) {
        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String secureloginResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        Data userId = new Gson().fromJson(secureloginResponse, Data.class);
        String header = ApplicationConstant.INSTANCE.Headertoken;
        AllAPIs api = APIClient.getClient().create(AllAPIs.class);

        Call<ResponseData> call = api.ServiveboyStatus(header, userId.getId(), status);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.body().getStatus() == 1) {

                    FragmentActivityMessage fragmentActivityMessage = new FragmentActivityMessage("datastatus",
                            "" + response.body().getData());
                    GlobalBus.getBus().post(fragmentActivityMessage);


                    if (response.body().getData().equalsIgnoreCase("0"))
                    {
                        //Toast.makeText(context, "You are Offline", Toast.LENGTH_SHORT).show();
                    } else {
                    //    Toast.makeText(context, "Your are Online", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });


    }


}
