package com.app.axzifserviceboy.Utils;

import com.app.axzifserviceboy.GetLocation.Root;
import com.app.axzifserviceboy.Model.MyResponse;
import com.app.axzifserviceboy.Model.ResponseData;
import com.app.axzifserviceboy.Model.ResponseList;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface AllAPIs {

    @FormUrlEncoded
    @POST("/api/Delivery_boy/login_deliver_boy")
    Call<MyResponse> signIn(@Header("X-API-KEY") String authorization,
                            @Field("email") String email,
                            @Field("password") String password


    );

    @FormUrlEncoded
    @POST("/api/Delivery_boy/GetassignorderBydeliveryboyid")
    Call<ResponseList> listServices(@Header("X-API-KEY") String authorization,
                                    @Field("delivery_boy_id") String delivery_boy_id,
                                    @Field("status") String status,
                                    @Field("date") String date


    );

    @FormUrlEncoded
    @POST("/api/Delivery_boy/UpdateAssignstatus")
    Call<ResponseList> UpdateStatus(@Header("X-API-KEY") String authorization,
                                    @Field("delivery_boy_id") String delivery_boy_id,
                                    @Field("status") String status,
                                    @Field("order_id") String order_id
    );


    @FormUrlEncoded
    @POST("/api/Delivery_boy/Sendotp")
    Call<MyResponse> SendOtp(@Header("X-API-KEY") String authorization,
                             @Field("order_id") String order_id

    );


    @FormUrlEncoded
    @POST("/api/Delivery_boy/VerifyOtp")
    Call<ResponseList> verify(@Header("X-API-KEY") String authorization,
                              @Field("order_id") String order_id,
                              @Field("payment_type") String payment_type,
                              @Field("otp") String otp

    );

    @FormUrlEncoded
    @POST("/api/Delivery_boy/Updateworkingstatus")
    Call<ResponseData> ServiveboyStatus(@Header("X-API-KEY") String authorization,
                                        @Field("delivery_boy_id") String order_id,
                                        @Field("status") String status);


    @Multipart
    @POST("/api/Delivery_boy/UpdateProfile")
    Call<MyResponse> updateprofileimg(@Header("X-API-KEY") String authorization,
                                        @Part("delivery_boy_id") RequestBody delivery_boy_id,
                                        @Part("name") RequestBody name,
                                        @Part("dob") RequestBody dob,
                                        @Part("city") RequestBody city,
                                        @Part("state") RequestBody state,
                                        @Part("country") RequestBody country,
                                        @Part("zip") RequestBody zip,
                                        @Part("address1") RequestBody address1,
                                        @Part("address2") RequestBody address2,
                                        @Part("landmark") RequestBody landmark,
                                        @Part("additional_info") RequestBody additional_info,
                                        @Part MultipartBody.Part file5
    );

    @GET("/maps/api/geocode/json?")
    Call<Root> getLatLong(@Query("address")  String address,
                          @Query("key") String APIkey );
    @Multipart
    @POST("/api/Delivery_boy/UploadMultipleProductImages")
    Call<ResponseList> uploadImage(@Header("X-API-KEY") String authorization,
                           @Part ArrayList<MultipartBody.Part> file5,
                            @Part("orderDetails_id") RequestBody orderDetails_id
    );





}
