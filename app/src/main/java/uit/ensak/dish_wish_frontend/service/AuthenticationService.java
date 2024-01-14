package uit.ensak.dish_wish_frontend.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import uit.ensak.dish_wish_frontend.Models.Auth.AuthenticationResponse;
import uit.ensak.dish_wish_frontend.Models.Auth.RegisterRequest;

public interface AuthenticationService {
    @Headers({"Accept: application/json"})
    @POST("auth/register")
    Call<AuthenticationResponse> register(@Body RegisterRequest request);
    @Headers({"Accept: application/json"})
    @POST("auth/register/verify-email")
    Call<ResponseBody> verifyEmail(@Query("code") String code);
}
