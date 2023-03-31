package com.app.axzifserviceboy.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.app.axzifserviceboy.Adapter.AcceptOrderAdptar;
import com.app.axzifserviceboy.Adapter.ShowImage;
import com.app.axzifserviceboy.MainActivity;
import com.app.axzifserviceboy.Model.Datum;
import com.app.axzifserviceboy.Model.ResponseList;
import com.app.axzifserviceboy.R;
import com.app.axzifserviceboy.Utils.UtilsMethod;
import com.google.gson.Gson;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class OpenBoxDeliveryActivity extends AppCompatActivity implements View.OnClickListener {
    Button   next;
    ImageView imageView;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    List<String> imagesEncodedList;
    RecyclerView recyclerview;
    ImageView photoButton;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Button open_submit;
    ImageView backprese;
    String order_id,product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_box_delivery);
        imageView = findViewById(R.id.image);
        recyclerview = findViewById(R.id.recyclerview);
        open_submit = findViewById(R.id.open_submit);
        open_submit.setOnClickListener(this);
        photoButton = findViewById(R.id.pic);
        backprese = findViewById(R.id.backprese);
        backprese.setOnClickListener(this);
        product_id=getIntent().getStringExtra("product_id");
        order_id=getIntent().getStringExtra("tbl_orderdetailsID");

        mArrayUri = new ArrayList<Uri>();
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                    else
                    {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            }
        });





      /*  next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                for (position=0;position<mArrayUri.size();position++)
                {
                    Log.d("kdjfgkdhfg", String.valueOf(mArrayUri.get(position)));
                }


                if (position < mArrayUri.size() - 1)
                {
                    // increase the position by 1
                    position++;
                    imageView.setImageURI(mArrayUri.get(position));
                } else {
                    Toast.makeText(OpenBoxDeliveryActivity.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/

        imageView = findViewById(R.id.image);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data)
        {
            if (data.getClipData() != null)
            {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++)
                {  Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);
                }
                addImg(mArrayUri);
            // imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            }

            else
            {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            }
        }
        else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "Title", null);
            Uri img= Uri.parse(path);
            Log.d("djfkkf", String.valueOf(img));
            mArrayUri.add(img);
            addImg(mArrayUri);

          //  imageView.setImageBitmap(photo);
           // mArrayUri.add(photo);
        }
        else
        {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public void addImg(ArrayList<Uri> mArrayUri)
    {

        ShowImage adapter = new ShowImage(this,mArrayUri );
        adapter.notifyDataSetChanged();
        recyclerview.setHasFixedSize(false);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
    }


    @Override
    public void onClick(View view)
    {
        if (view==open_submit)
        {
            if (mArrayUri.size()>=0)
            {
                   if (UtilsMethod.INSTANCE.isNetworkAvialable(this) == false)
                  {
                  Toast.makeText(this, "Please Check Your Network Connectivity", Toast.LENGTH_SHORT).show();
                   }else
                   {
                       UtilsMethod.INSTANCE.upload(OpenBoxDeliveryActivity.this,mArrayUri,product_id,order_id);
                   }
            }
            else
            {
                Toast.makeText(this, "Please select Image from gallery Or Camera !", Toast.LENGTH_SHORT).show();
            }

        }
        mArrayUri.clear();
        addImg(mArrayUri);

        if (view==backprese)
        {
            finish();
        }
    }

    public static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


}