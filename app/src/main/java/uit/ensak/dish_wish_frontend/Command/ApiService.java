package uit.ensak.dish_wish_frontend.Command;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.Models.Proposition;

public interface ApiService {
    @GET("clients/{id}")
    Call<Client> getUser(@Header("Authorization") String authToken, @Path("id") Long id);

    @POST("clients/add")
    Call<Void> sendData(@Body Client client);

    @POST("commands/create")
    Call<Command> createCommand(@Header("Authorization") String authToken, @Body Command command);

    @GET("commands")
    Call<List<Command>> getCommands(@Header("Authorization") String authToken);

    @GET("commands/{id}")
    Call<Command> getCommandById(@Header("Authorization") String authToken, @Path("id") Long id);

    @POST("/propositions/offer")
    Call<Void> sendProposition(@Header("Authorization") String authToken,@Body Proposition proposition);

    @GET("propositions")
    Call<List<Proposition>> getPropositions(@Header("Authorization") String authToken);

    @PUT("commands/update/{id}")
    Call<Command> updateCommand(@Header("Authorization") String authToken,
            @Path("id") Long commandId,
            @Body Command command
    );
}