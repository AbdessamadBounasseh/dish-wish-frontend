package uit.ensak.dish_wish_frontend.notification_folder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import uit.ensak.dish_wish_frontend.Command.ApiService;
import uit.ensak.dish_wish_frontend.service.RetrofitClient;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.Models.Notification;
import uit.ensak.dish_wish_frontend.R;

public class NotificationsClient extends AppCompatActivity {

    private RecyclerView notificationRecyclerView;
    private NotificationClientAdapter notificationAdapter;

    private ArrayList<Notification> notificationList= new ArrayList<Notification>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_client);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("userId", 2L);
        editor.putString("accessToken", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCZXJuYXJkQGdtYWlsLmNvbSIsImlhdCI6MTcwNTI0ODI2NSwiZXhwIjoxNzA1MzM0NjY1fQ.p-QEpjva7wJ5Y0Wt8ilM48gvShCYtcNSBufeXFgwaIw");
        editor.putBoolean("isCook", true);
        editor.apply();
        Boolean isCook = preferences.getBoolean("isCook", false);


//        notificationAdapter = new NotificationClientAdapter(commandList);
//        notificationRecyclerView = findViewById(R.id.NotificationResultsRecyclerView);
//        notificationRecyclerView.setAdapter(notificationAdapter);
//        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchClientNotifications(String accessToken, int clientId) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Notification>> call = apiService.getClientNotifications("Bearer " + accessToken, clientId);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                    List<Notification> receivedNotifications = response.body();
                    if (receivedNotifications != null) {

                        for (Notification notification : receivedNotifications) {
                            notificationList.add(notification);
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }



}