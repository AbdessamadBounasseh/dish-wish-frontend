package uit.ensak.dish_wish_frontend.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import uit.ensak.dish_wish_frontend.model.Client;

public interface ApiService {
    @GET("clients/{id}")
    Call<Client> getUser(@Path("id") Long id);

    @POST("clients/add")
    Call<Void> sendData(@Body Client client);

}
