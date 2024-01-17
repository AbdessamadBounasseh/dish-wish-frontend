package uit.ensak.dish_wish_frontend.service;

import com.google.gson.GsonBuilder;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uit.ensak.dish_wish_frontend.Command.ApiService;
import uit.ensak.dish_wish_frontend.service.AuthenticationService;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.56.1:8082/";


    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
            .build();
    private static final ApiService apiService = retrofit.create(ApiService.class);
    private static final ApiServiceProfile apiServiceProfile = retrofit.create(ApiServiceProfile.class);

    private static final AuthenticationService authenticationService = retrofit.create(AuthenticationService.class);

    public static ApiService getApiService() {
        return apiService;
    }
    public static AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public static ApiServiceProfile getApiServiceProfile() {
        return apiServiceProfile;
    }

    private static final ContactService contactService = retrofit.create(ContactService.class);
    public static ContactService getContactService() {
        return contactService;
    }
}