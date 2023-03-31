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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.axzifserviceboy.Model.Data;
import com.app.axzifserviceboy.Model.Datum;
import com.app.axzifserviceboy.R;
import com.app.axzifserviceboy.Utils.ApplicationConstant;
import com.app.axzifserviceboy.Utils.UtilsMethod;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Servicess  extends  RecyclerView.Adapter<Servicess.ViewHolder>
{
    private Context context;
    private List<Datum> list = new ArrayList<>();
    double lat, longi;
    public Servicess(Context context, List<Datum> list,double lat,double longi)
    {
        this.context = context;
        this.list = list;
        this.lat = lat;
        this.longi = longi;
    }

    @NonNull
    @Override
    public Servicess.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.services, parent, false);
        return  new  ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull Servicess.ViewHolder holder, int position)
    {
         Datum data = list.get(position);
         Log.d("url",data.getProduct_img());
         SharedPreferences myPreferences =context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
         String  secureloginResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
         Data  userId = new Gson().fromJson(secureloginResponse, Data.class);
         String date=data.getDelivery_date();
         SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
         SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
         Date time1= null;
        try {
            time1 = fmt.parse(date);

            String sd = newFormat.format(time1);

           // viewHolder.date.setText(""+sd+"");
            holder.deliverydate.setText(sd);

        } catch (ParseException e) {
            e.printStackTrace();
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
        String state=data.getAssign_status();





        if (state.equalsIgnoreCase("assigned"))
        {
            holder.accept.setVisibility(View.VISIBLE);
            holder.reject.setVisibility(View.VISIBLE);
            holder.accept.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    UtilsMethod.INSTANCE.UpdateStatus(context,userId.getId(),"accept",data.getOrder_id(),"Order Accepted");
                    holder.delivered.setVisibility(View.VISIBLE);
                    holder.reject.setVisibility(View.GONE);
                    holder.accept.setVisibility(View.GONE);


                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    UtilsMethod.INSTANCE.UpdateStatus(context,userId.getId(),"reject",data.getOrder_id(),"Order canceled ");
                    holder.concle.setVisibility(View.VISIBLE);
                    holder.reject.setVisibility(View.GONE);
                    holder.accept.setVisibility(View.GONE);
                }
            });

        }
         else if(state.equalsIgnoreCase("accept"))
        {
            holder.delivered.setVisibility(View.VISIBLE);
           /* holder.delivered.setOnClickListener(new View.OnClickListener()
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
        }
        else if(state.equalsIgnoreCase("reject"))
        {
            holder.concle.setVisibility(View.VISIBLE);

        }
        else if(state.equalsIgnoreCase("deliverd"))
        {
            holder.success.setVisibility(View.VISIBLE);

        }

     /*   holder.delivered.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UtilsMethod.INSTANCE.UpdateStatus(context,userId.getId(),"deliverd",data.getOrder_id(),"Successfully Delivered ");
                holder.success.setVisibility(View.VISIBLE);
                holder.delivered.setVisibility(View.GONE);
            }
        });*/


        holder.tracker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {   String uri = "http://maps.google.com/maps?saddr=" + lat + "," + longi + "&daddr=" + Double.parseDouble(data.getLatitude()) + "," +  Double.parseDouble(data.getLongitude());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
         TextView prodect,user_name_tv,usernumber,useraddress,userlandmark,accept,reject,concle,delivered,deliverydate,success;
         ImageView imge,tracker;
         LinearLayout status;
        public ViewHolder(@NonNull View itemView)
        {  super(itemView);
            prodect=itemView.findViewById(R.id.prodect);
            imge=itemView.findViewById(R.id.image);
            user_name_tv=itemView.findViewById(R.id.user_name_tv);
            usernumber=itemView.findViewById(R.id.usernumber);
            useraddress=itemView.findViewById(R.id.useraddress);
            userlandmark=itemView.findViewById(R.id.userlandmark);
            status=itemView.findViewById(R.id.status);
            accept=itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);
            concle=itemView.findViewById(R.id.concle);
            delivered=itemView.findViewById(R.id.delivered);

            deliverydate=itemView.findViewById(R.id.deliverydate);
            success=itemView.findViewById(R.id.success);
            tracker=itemView.findViewById(R.id.tracker);

        }
    }



}
