package uit.ensak.dish_wish_frontend.notification_folder;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWluZWVrOEBnbWFpbC5jb20iLCJpYXQiOjE3MDUzMjIwMzcsImV4cCI6MTcwNTQwODQzN30.f9PsxKwLsCG_rxlqnHvLXLjDfkCWrxPuCBrmf-8w9xU";

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

        fetchClientNotifications(accessToken,2L);

        Button refreshButton = findViewById(R.id.confirmed);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchChefConfirmationNotifications(accessToken, 2L);
            }
        });

        Button refreshClientButton = findViewById(R.id.command);
        refreshClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchClientNotifications(accessToken, 2L);
            }
        });

        ImageView arrow = findViewById(R.id.animation);
        if (arrow != null) {
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        //setupNotificationClickListener();

    }

    private void setupNotificationClickListener() {
        notificationAdapter.setOnItemClickListener(new NotificationChefAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position >= 0 && position < notificationList.size()) {
                    Command clickedCommand = notificationList.get(position);
                    if (clickedCommand != null) {
                        showCommandDetailsPopup(clickedCommand);
                    }
                }
            }
        });
    }

    private void fetchClientNotifications(String accessToken, long chefId) {
        notificationList.clear();
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


    private void fetchChefConfirmationNotifications(String accessToken, long chefId) {
        notificationList.clear();
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Command>> call = apiService.getChefConfirmedNotifications("Bearer " + accessToken, chefId);

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

    private void showCommandDetailsPopup(Command associatedCommand) {
        if (associatedCommand != null) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_command_details);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edittext);

            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.y = (int) getResources().getDisplayMetrics().density * 20;
                window.setAttributes(layoutParams);
            }


            String address = associatedCommand.getAddress();
            String[] latLng = address.split(",");

            if (latLng.length == 2) {
                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);

                // Perform reverse geocoding to get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        String locationName = addresses.get(0).getAdminArea();
                        TextView location = dialog.findViewById(R.id.location);
                        location.setText(locationName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            TextView title = dialog.findViewById(R.id.title);
            TextView description = dialog.findViewById(R.id.Description);
            TextView serving = dialog.findViewById(R.id.serving);
            TextView delivary = dialog.findViewById(R.id.delivary);
            TextView price = dialog.findViewById(R.id.price);

            LottieAnimationView allergieAnimationView = dialog.findViewById(R.id.allergie);

            if (associatedCommand.getAllergie()) {
                allergieAnimationView.setVisibility(View.VISIBLE);
            } else {
                allergieAnimationView.setVisibility(View.GONE);
            }

            title.setText(associatedCommand.getTitle());
            description.setText(associatedCommand.getDescription());
            serving.setText(associatedCommand.getServing());
            delivary.setText(associatedCommand.getDeadline());
            price.setText(associatedCommand.getPrice() + "DH");

            dialog.show();

            ImageView arrow = dialog.findViewById(R.id.animation);
            if (arrow != null) {
                arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

        }
    }


}