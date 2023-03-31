package com.app.axzifserviceboy.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.axzifserviceboy.R;

import java.util.ArrayList;

public class ShowImage  extends RecyclerView.Adapter<ShowImage.ViewHolder>
{ ArrayList<Uri> mArrayUri;
    Context context;
    public ShowImage(Context context, ArrayList<Uri> mArrayUri)
      {
          this.context=context;
          this.mArrayUri=mArrayUri;
      }


    @NonNull
    @Override
    public ShowImage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.img, parent, false);
        return new ShowImage.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowImage.ViewHolder holder, int position)
    {
        holder.imageView.setImageURI(mArrayUri.get(position));

    }

    @Override
    public int getItemCount() {
        return mArrayUri.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView=itemView.findViewById(R.id.img);

        }
    }
}
