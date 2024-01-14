package uit.ensak.dish_wish_frontend.Profil;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;
import uit.ensak.dish_wish_frontend.service.AuthenticationService;

public class RetrofitClientProfile {

    private static final String BASE_URL = "http://192.168.43.231:8082/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    private static ApiServiceProfile apiService = retrofit.create(ApiServiceProfile.class);
    private static AuthenticationService authenticationService = retrofit.create(AuthenticationService.class);



    public static ApiServiceProfile getApiService() {
        return apiService;
    }
}
