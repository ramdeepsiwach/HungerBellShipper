package com.se_p2.hungerbellshipper.remote;

import com.se_p2.hungerbellshipper.model.FCMResponse;
import com.se_p2.hungerbellshipper.model.FCMSendData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFMCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA-rwI5Sc:APA91bGR6T2axDfwy0TmbmwiTll7XBM5b9_N7LO9eCr4VZT22vqP4u7ImU6DDbCYVmrZBvFeUWkWVhGUwLgv48U1tm5XXJTMIMrwtJiV82NjlgndOfAy8SFLQsFof_RfOcrnffvGYukR"
    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);
}