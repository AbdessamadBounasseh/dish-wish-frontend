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

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_client);

        // Assuming you have a list of commands
        List<Command> commandList = GetDummyData();

        recyclerView = findViewById(R.id.NotificationResultsRecyclerView);
        adapter = new NotificationAdapter(commandList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private List<Command> GetDummyData() {
        List<Command> dummyResults = new ArrayList<>();

        Client client = new Client();
        client.setFirstName("Mounir");
        client.setLastName("Mrabti");
        client.setAddress("kenitra");

        Chef chef = new Chef();
        chef.setFirstName("Faycal");
        chef.setFirstName("Elou");
        chef.setAddress("kenitra");



        dummyResults.add(new Command(1L, "TAJINE", "with berqouq", null, null, null,  "50 DH",  null, client, chef));
        dummyResults.add(new Command(2L, "Couscous", "with argan", null, null, null,  "80 DH",  null, client, chef));

        return dummyResults;
    }
}
