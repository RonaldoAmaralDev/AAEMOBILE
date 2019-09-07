package br.com.araujoabreu.timg.helper;


import br.com.araujoabreu.timg.notificacao.MyResponse;
import br.com.araujoabreu.timg.notificacao.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AIzaSyBiJd34eYzcwq1OoZE0iO_9UL1utqD2hq8"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
