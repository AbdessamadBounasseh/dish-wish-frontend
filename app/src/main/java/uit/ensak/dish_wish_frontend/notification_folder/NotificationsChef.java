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
import uit.ensak.dish_wish_frontend.Command.RetrofitClient;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.R;

public class NotificationsChef extends AppCompatActivity {

    private RecyclerView notificationRecyclerView;
    private NotificationChefAdapter notificationAdapter;

    private static ArrayList<Command> notificationList= new ArrayList<Command>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_chef);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("userId", 2L);
        editor.putString("accessToken", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWluZWVrOEBnbWFpbC5jb20iLCJpYXQiOjE3MDUyNzU2MjQsImV4cCI6MTcwNTM2MjAyNH0.rBGT44eRhuNJFBwUZsLqTCvbLTdc2CYQlQ5C5Lf9jEU");
        editor.putBoolean("isCook", true);
        editor.apply();
        Boolean isCook = preferences.getBoolean("isCook", false);

        fetchClientNotifications("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWluZWVrOEBnbWFpbC5jb20iLCJpYXQiOjE3MDUyNzU2MjQsImV4cCI6MTcwNTM2MjAyNH0.rBGT44eRhuNJFBwUZsLqTCvbLTdc2CYQlQ5C5Lf9jEU",2L);


    }

    private void fetchClientNotifications(String accessToken, long chefId) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Command>> call = apiService.getChefNotifications("Bearer " + accessToken, chefId);

        call.enqueue(new Callback<List<Command>>() {
            @Override
            public void onResponse(Call<List<Command>> call, Response<List<Command>> response) {
                if (response.isSuccessful()) {
                    List<Command> receivedNotifications = response.body();
                    if (receivedNotifications != null) {
                        for (Command command : receivedNotifications) {
                            notificationList.add(command);

                        }
                        // Outside of the loop, after adding all items to notificationList
                        notificationAdapter = new NotificationChefAdapter(notificationList, NotificationsChef.this);
                        notificationRecyclerView = findViewById(R.id.NotificationResultsRecyclerView);
                        notificationRecyclerView.setAdapter(notificationAdapter);
                        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationsChef.this));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Command>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }



}