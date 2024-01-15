package uit.ensak.dish_wish_frontend.service;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Rating;
import uit.ensak.dish_wish_frontend.dto.ChefDTO;
import uit.ensak.dish_wish_frontend.SearchResult;


public interface ApiServiceProfile {
    @GET("clients/{clientId}")
    Call<Client> getClientById(
            @Header("Authorization") String authToken,
            @Path("clientId") Long clientId);

    @GET("chefs/{chefId}")
    Call<Chef> getChefById(
            @Header("Authorization") String authToken,
            @Path("chefId") Long chefId);
    @Multipart
    @PUT("clients/update/{id}")
    Call<Client> updateClient(
            @Header("Authorization") String authorization,
            @Path("id") Long id,
            @Part("user") ChefDTO chefDTO,
            @Part MultipartBody.Part photoPart
            );


    @Multipart
    @POST("clients/becomeCook/{clientId}")
    Call<Chef> becomeCook(
            @Header("Authorization") String authToken,
            @Path("clientId") Long clientId,
            @Part MultipartBody.Part idCard,
            @Part MultipartBody.Part certificate
    );
    @GET("clients/profile/{id}")
    Call<ResponseBody> getClientProfile(
            @Header("Authorization") String authToken,
            @Path("id") Long id
    );
    @GET("clients/profile{id}")
    Call<Rating> getclientrating(
            @Header("Authorization") String authToken,
            @Query("userId") Long userId);



    @DELETE("clients/delete/{id}")
    Call<Void> deleteUserAccount(
            @Header("Authorization") String authToken,
            @Path("id") Long id);

    @POST("clients/rate/{id}")
    Call<ResponseBody> sendClientRating(
            @Header("Authorization") String authToken,
            @Path("id") Long userId,
            @Body float rating);


    @GET("chefs/filter/{query}")
    Call<List<SearchResult>> filterByNameAndCity(
            @Header("Authorization") String authToken,
            @Path("query") String query );
}


