package uit.ensak.dish_wish_frontend.Command;

import com.google.gson.GsonBuilder;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uit.ensak.dish_wish_frontend.service.AuthenticationService;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.56.1:8082/";


    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
            .build();

    public static AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    private static ApiService apiService = retrofit.create(ApiService.class);
    private static AuthenticationService authenticationService = retrofit.create(AuthenticationService.class);


    public static ApiService getApiService() {
        return apiService;
    }

    public static String getBaseURL() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);

            return "http://"+socket.getLocalAddress().getHostAddress()+ ":8082/";
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}