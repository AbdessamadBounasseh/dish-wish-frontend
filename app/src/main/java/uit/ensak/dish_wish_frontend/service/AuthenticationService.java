package uit.ensak.dish_wish_frontend.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import uit.ensak.dish_wish_frontend.Models.Auth.AuthenticationResponse;
import uit.ensak.dish_wish_frontend.Models.Auth.RegisterRequest;

public interface AuthenticationService {

    @POST("auth/register")
    Call<AuthenticationResponse> register(@Body RegisterRequest request);
}
