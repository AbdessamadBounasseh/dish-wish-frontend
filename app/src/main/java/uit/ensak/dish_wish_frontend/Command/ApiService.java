package uit.ensak.dish_wish_frontend.Command;

import java.util.List;

import okhttp3.ResponseBody;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.Models.Proposition;

public interface ApiService {
    @GET("clients/{id}")
    Call<Client> getUser(@Header("Authorization") String authToken, @Path("id") Long id);

    @GET("clients/email")
    Call<Client> getUserByEmail(@Header("Authorization") String authToken,@Body String email);
    @Multipart
    @POST("clients/becomeCook/{clientId}")
    Call<Chef> becomeCook(
            @Header("Authorization") String authToken,
            @Path("clientId") Long clientId,
            @Part MultipartBody.Part idCard,
            @Part MultipartBody.Part certificate
    );

    @DELETE("clients/delete")
    Call<Void> deleteUserAccount(@Header("Authorization") String authToken,@Body String email);

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