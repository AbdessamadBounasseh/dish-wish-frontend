package uit.ensak.dish_wish_frontend.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import uit.ensak.dish_wish_frontend.model.Client;

public interface ApiService {
    @GET("clients/{id}")
    Call<Client> getUser(@Path("id") Long id);
}
