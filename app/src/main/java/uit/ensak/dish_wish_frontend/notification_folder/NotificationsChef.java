package uit.ensak.dish_wish_frontend.notification_folder;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.R;

public class NotificationsChef extends AppCompatActivity {

    private RecyclerView notificationRecyclerView;
    private NotificationChefAdapter notificationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_chef);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("userId", 2L);
        editor.putString("accessToken", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCZXJuYXJkQGdtYWlsLmNvbSIsImlhdCI6MTcwNTI0ODI2NSwiZXhwIjoxNzA1MzM0NjY1fQ.p-QEpjva7wJ5Y0Wt8ilM48gvShCYtcNSBufeXFgwaIw");
        editor.putBoolean("isCook", true);
        editor.apply();
        Boolean isCook = preferences.getBoolean("isCook", false);

        // Assuming you have a list of commands

//        notificationAdapter = new NotificationChefAdapter(commandList);
//        notificationRecyclerView = findViewById(R.id.NotificationResultsRecyclerView);
//        notificationRecyclerView.setAdapter(notificationAdapter);
//        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}