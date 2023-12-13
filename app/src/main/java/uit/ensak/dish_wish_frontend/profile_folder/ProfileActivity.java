package uit.ensak.dish_wish_frontend;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.TextView;



public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_details); // Setting the XML layout file
        // Retrieving data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String firstName = intent.getStringExtra("firstName");
            String lastName = intent.getStringExtra("lastName");
            String address = intent.getStringExtra("address");

            // Use the data as needed, for example:
            TextView textViewActualFirstName = findViewById(R.id.textViewActualFirstName);
            TextView textViewActualLastName = findViewById(R.id.textViewActualLastName);
            TextView textViewActualAddress = findViewById(R.id.textViewActualAddress);

            textViewActualFirstName.setText(firstName);
            textViewActualLastName.setText(lastName);
            textViewActualAddress.setText(address);
        }
    }
}
