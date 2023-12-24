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

        Client client1 = new Client();
        client1.setFirstName("Mounir");
        client1.setLastName("Mrabti");
        client1.setAddress("kenitra");

        Chef chef1 = new Chef();
        chef1.setFirstName("Faycal");
        chef1.setLastName("Elou");
        chef1.setAddress("kenitra");

        Client client2 = new Client();
        client2.setFirstName("Mohammed");
        client2.setLastName("naimi");
        client2.setAddress("Rabat");

        Chef chef2 = new Chef();
        chef2.setFirstName("Samir");
        chef2.setLastName("rafidi");
        chef2.setAddress("Rabat");

        dummyResults.add(new Command(1L, "TAJINE", "with berqouq", null, null, null,  "50 DH",  null, client1, chef1));
        dummyResults.add(new Command(2L, "Couscous", "with argan", null, null, null,  "80 DH",  null, client2, chef2));
        dummyResults.add(new Command(3L, "Pizza", "with chicken", null, null, null,  "70 DH",  null, client1, chef1));

        return dummyResults;
    }
}
