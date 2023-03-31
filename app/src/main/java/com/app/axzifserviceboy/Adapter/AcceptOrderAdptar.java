package com.app.axzifserviceboy.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.axzifserviceboy.Activity.OpenBoxDeliveryActivity;
import com.app.axzifserviceboy.Activity.OrderDetails;
import com.app.axzifserviceboy.GetLocation.Root;
import com.app.axzifserviceboy.Model.Data;
import com.app.axzifserviceboy.Model.Datum;
import com.app.axzifserviceboy.R;
import com.app.axzifserviceboy.Utils.AllAPIs;
import com.app.axzifserviceboy.Utils.ApiClient1;
import com.app.axzifserviceboy.Utils.ApplicationConstant;
import com.app.axzifserviceboy.Utils.Loader;
import com.app.axzifserviceboy.Utils.UtilsMethod;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AcceptOrderAdptar extends RecyclerView.Adapter<AcceptOrderAdptar.ViewHolder> {

    private Context context;
    private List<Datum> list = new ArrayList<>();
    double lat, longi;
    Loader loader;

    public AcceptOrderAdptar(Context context, List<Datum> list, double lat, double longi, Loader loader) {
        this.context = context;
        this.list = list;
        this.lat = lat;
        this.longi = longi;
        this.loader = loader;
    }

    @NonNull
    @Override

    public AcceptOrderAdptar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.orderaccepted, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptOrderAdptar.ViewHolder holder, int position) {
        Datum data = list.get(position);
        Log.d("url", data.getProduct_img());
        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String secureloginResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        Data userId = new Gson().fromJson(secureloginResponse, Data.class);
        String date = data.getDelivery_date();
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date time1 = null;
        try {
            time1 = fmt.parse(date);

            String sd = newFormat.format(time1);

            // viewHolder.date.setText(""+sd+"");
            holder.deliverydate.setText(sd);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("fveuwirerterut",data.getReturn_policy());
        if (data.return_policy.equalsIgnoreCase("Open box Delivery"))
        {
            holder.open_delivery.setVisibility(View.VISIBLE);
        }


        holder.prodect.setText(data.getProduct_name());
        holder.user_name_tv.setText(data.getDelivery_name());
        holder.usernumber.setText(data.getDelivery_mobile());
        holder.useraddress.setText(data.getDelivery_address());
        holder.userlandmark.setText(data.getDelivery_landmark());
        Glide.with(holder.itemView)
                .load(data.getProduct_img())
                .fitCenter()
                .into(holder.imge);
        String state = data.getAssign_status();


        holder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+data.getMobile()));
                context.startActivity(intent);
            }
        });
        if (state.equalsIgnoreCase("accept"))
        {
            holder.delivered.setVisibility(View.VISIBLE);
        /*    holder.delivered.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   UtilsMethod.INSTANCE.UpdateStatus(context,userId.getId(),"deliverd",data.getOrder_id(),"Successfully Delivered ");


                    holder.success.setVisibility(View.VISIBLE);
                    holder.delivered.setVisibility(View.GONE);
                }
            });*/

        }
        holder.delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);


                UtilsMethod.INSTANCE.sendOtp(context, loader, data.getOrder_id(), data.getPayment_status(), holder.success, holder.delivered, userId.getId());
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            context.startActivity(new Intent(context, OrderDetails.class).putExtra("data",new Gson().toJson(data)));
            }
        });
/*
        holder.delivered.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UtilsMethod.INSTANCE.UpdateStatus(context,userId.getId(),"deliverd",data.getOrder_id(),"Successfully Delivered ");
                holder.success.setVisibility(View.VISIBLE);
                holder.delivered.setVisibility(View.GONE);
            }
        });
*/
        holder.open_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, OpenBoxDeliveryActivity.class).putExtra("tbl_orderdetailsID",data.getTbl_orderdetailsID()).putExtra("product_id",data.getId()));
            }
        });


        holder.tracker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {

                    String APIkey= ApplicationConstant.INSTANCE.mapkey;
                    AllAPIs git = ApiClient1.getClient().create(AllAPIs.class);

                    Call<Root> call = git.getLatLong( data.getDelivery_address()+""+data.getDelivery_city(),APIkey);
                    call.enqueue(new Callback<Root>() {
                        @Override
                        public void onResponse(Call<Root> call, Response<Root> response)
                        {
                            Log.d("eryertujhghgfher",new Gson().toJson(response.body()));
                            if (response.body().getStatus().equalsIgnoreCase("ok"))
                            {
                               Log.d("eryertkjhkuer34",new Gson().toJson(response.body().getResults().get(0).geometry.location.lat));
                               double latitude=response.body().getResults().get(0).geometry.location.lat;
                               double longitude=response.body().getResults().get(0).geometry.location.lng;
                               Log.d("zdkxcvkxnvbjhxcv",latitude+"     "+longitude);
                                String uri = "http://maps.google.com/maps?saddr=" + lat + "," + longi + "&daddr=" + latitude+ "," + longitude;
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                context.startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(context, "Con not access location !", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Root> call, Throwable t) {

                        }
                    });

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View callbtn;
        TextView paymenttype, prodect, user_name_tv, usernumber, useraddress, userlandmark, accept, reject, concle, delivered, deliverydate, success;
        ImageView imge, tracker;
        LinearLayout status;
        LinearLayout cardView,open_delivery;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            callbtn=itemView.findViewById(R.id.callbutton);
            prodect = itemView.findViewById(R.id.prodect);
            imge = itemView.findViewById(R.id.image);
            user_name_tv = itemView.findViewById(R.id.user_name_tv);
            usernumber = itemView.findViewById(R.id.usernumber);
            useraddress = itemView.findViewById(R.id.useraddress);
            userlandmark = itemView.findViewById(R.id.userlandmark);
            status = itemView.findViewById(R.id.status);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);
            concle = itemView.findViewById(R.id.concle);
            delivered = itemView.findViewById(R.id.delivered);
            cardView = itemView.findViewById(R.id.btncardview);

            deliverydate = itemView.findViewById(R.id.deliverydate);
            success = itemView.findViewById(R.id.success);
            tracker = itemView.findViewById(R.id.tracker);
            open_delivery = itemView.findViewById(R.id.open_delivery);

        }
    }


}
