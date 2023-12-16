package uit.ensak.dish_wish_frontend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class view_profile extends AppCompatActivity {

    static final int REQUEST_CODE_CHANGE_PROFILE = 1;

    private TextView textViewFirstName,textViewLastName,textViewAddress, textViewBio, textViewDiet, textViewPHONE_NUMBER, textViewAllergies;
    private Spinner spinnerAllergies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        textViewFirstName = findViewById(R.id.textViewActualFirstName);
        textViewLastName = findViewById(R.id.textViewActualLastName);
        textViewAddress = findViewById(R.id.textViewActualAddress);
        textViewDiet= findViewById(R.id.textViewActualDiet);
        textViewBio= findViewById(R.id.textViewActualBio);
        textViewPHONE_NUMBER = findViewById(R.id.textViewActualPhoneNumber);
        textViewAllergies = findViewById(R.id.textViewActualAllergies);


        ImageButton btnBack = findViewById(R.id.btnBack);

        Button btnChange = findViewById(R.id.btnchange);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancez l'activité ChangeProfileActivity avec startActivityForResult
                Intent intent = new Intent(view_profile.this, MainActivity.class);

                //#####################

                // Passer la valeur actuelle du prénom à l'intention
                intent.putExtra("CURRENT_FIRST_NAME", textViewFirstName.getText().toString());

                intent.putExtra("CURRENT_LAST_NAME", textViewLastName.getText().toString());
                intent.putExtra("CURRENT_ADDRESS", textViewAddress.getText().toString());


                intent.putExtra("CURRENT_PHONE_NUMBER", textViewPHONE_NUMBER.getText().toString());

                intent.putExtra("CURRENT_BIO", textViewBio.getText().toString());
                intent.putExtra("CURRENT_DIET", textViewDiet.getText().toString());

                //startActivityForResult(intent, 1);// Utilisez startActivityForResult pour obtenir les résultats
                startActivityForResult(intent, REQUEST_CODE_CHANGE_PROFILE);

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode pour revenir en arrière
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            // Activité MainActivity
            String newFirstName = data.getStringExtra("NEW_FIRST_NAME");
            String newLastName = data.getStringExtra("NEW_LAST_NAME");
            String newAddress = data.getStringExtra("NEW_ADDRESS");

            String newBio = data.getStringExtra("NEW_BIO");
            String newDiet = data.getStringExtra("NEW_DIET");
            String newPhoneNumber = data.getStringExtra("NEW_PHONE_NUMBER");
            String newAllergy = data.getStringExtra("NEW_ALLERGY");

            // Mettre à jour votre interface utilisateur avec les nouvelles valeurs

            textViewFirstName.setText(newFirstName);
            textViewLastName.setText(newLastName);
            textViewAddress.setText(newAddress);

            textViewPHONE_NUMBER.setText(newPhoneNumber);
            textViewBio.setText(newBio);
            textViewDiet.setText(newDiet);
            textViewAllergies.setText(newAllergy);

        }
    }
}