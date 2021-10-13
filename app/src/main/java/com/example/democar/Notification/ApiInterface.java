package com.example.democar.Notification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    String SERVER_KEY  = "AAAADOnMCeU:APA91bE3vKHkov7GkW1V1S8UJt1Q1ko9C6CKWJ6zrUU0vFXLIQsZKz3x1qvVt-6BJVbpnxBrlyjGgQb6jGdsJd7J0Fl7sA-1WK5g6yDIgo9N3IteTeJPi_VjzukRDkO6VkuEgtrjwpen";
            @Headers({
            "Content-Type: application/json",
            "Authorization: key="+SERVER_KEY
    })
    @POST("send")
    Call<ResponseBody> sendNotification(@Body RootModel root);
}
