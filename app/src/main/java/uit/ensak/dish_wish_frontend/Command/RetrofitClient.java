package uit.ensak.dish_wish_frontend.Command;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uit.ensak.dish_wish_frontend.service.AuthenticationService;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.10.1:8082/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    private static ApiService apiService = retrofit.create(ApiService.class);
    private static AuthenticationService authenticationService = retrofit.create(AuthenticationService.class);



    public static ApiService getApiService() {
        return apiService;
    }
}