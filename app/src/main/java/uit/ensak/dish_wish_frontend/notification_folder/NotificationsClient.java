package uit.ensak.dish_wish_frontend.notification_folder;

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

public class NotificationsClient extends AppCompatActivity {

    private RecyclerView notificationRecyclerView;
    private NotificationClientAdapter notificationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_client);

        // Assuming you have a list of commands
        List<Command> commandList = GetDummyData();

        notificationAdapter = new NotificationClientAdapter(commandList);
        notificationRecyclerView = findViewById(R.id.NotificationResultsRecyclerView);
        notificationRecyclerView.setAdapter(notificationAdapter);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Command> GetDummyData() {
        List<Command> dummyResults = new ArrayList<>();

//        Client client1 = new Client();
//
//
//        Chef chef1 = new Chef();
//
//
//
//        dummyResults.add(new Command(1L, "TAJINE", "with berqouq", null, null, null,  "50 DH",  null, client1, chef1));
//
        return dummyResults;
    }
}