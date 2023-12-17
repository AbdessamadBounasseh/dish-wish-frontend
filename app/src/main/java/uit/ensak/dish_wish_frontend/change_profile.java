package uit.ensak.dish_wish_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class change_profile extends AppCompatActivity {

    private EditText editTextNewFirstName, editTextNewLastName, editTextNewAddress, editTextNewPhoneNumber,
            editTextNewAllergies, editTextNewDiet,
            editTextNewBio;

    private Button btnSubmit;
    Spinner spinnerAllergies;
    private String currentFirstName, currentLastName, currentAddress,currentPhoneNumber,currentBio,currentDiet;
    private String newAllergy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        spinnerAllergies = findViewById(R.id.spinnerAllergies);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.allergies_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAllergies.setAdapter(adapter);


        spinnerAllergies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Mettez à jour la nouvelle allergie lorsque l'utilisateur sélectionne une option dans le spinner
                newAllergy = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Cette méthode est requise mais nous n'avons rien à faire ici pour le moment
            }

        });



        //#########################################
        editTextNewFirstName = findViewById(R.id.editTextNewFirstName);
        editTextNewLastName = findViewById(R.id.editTextNewLastName);
        editTextNewAddress = findViewById(R.id.editTextNewAddress);
        editTextNewPhoneNumber = findViewById(R.id.editTextNewPhoneNumber);
        editTextNewBio = findViewById(R.id.editTextNewBio);
        editTextNewDiet = findViewById(R.id.editTextNewDiet);


        btnSubmit = findViewById(R.id.btnsubmit);

        // Récupérer le prénom actuel
        currentFirstName = getIntent().getStringExtra("CURRENT_FIRST_NAME");
        currentLastName = getIntent().getStringExtra("CURRENT_LAST_NAME");
        currentAddress = getIntent().getStringExtra("CURRENT_ADDRESS");

        currentBio = getIntent().getStringExtra("CURRENT_BIO");
        currentDiet = getIntent().getStringExtra("CURRENT_DIET");
        currentPhoneNumber = getIntent().getStringExtra("CURRENT_PHONE_NUMBER");
        // Pré-remplir le champ d'édition avec le prénom actuel

        editTextNewFirstName.setText(currentFirstName);
        editTextNewLastName.setText(currentLastName);
        editTextNewAddress.setText(currentAddress);

        editTextNewPhoneNumber.setText(currentPhoneNumber);
        editTextNewBio.setText(currentBio);
        editTextNewDiet.setText(currentDiet);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mettre à jour le prénom avec la nouvelle valeur


                String newFirstName = editTextNewFirstName.getText().toString();

                String newLastName = editTextNewLastName.getText().toString();

                String newAddress = editTextNewAddress.getText().toString();

                String newPhoneNumber = editTextNewPhoneNumber.getText().toString();

                String newBio = editTextNewBio.getText().toString();

                String newDiet = editTextNewDiet.getText().toString();

                // Créer un Intent pour contenir les nouvelles valeurs
                Intent resultIntent = new Intent();
                // Activité change_profil

                //editTextNewAddress  currentAddress
                resultIntent.putExtra("NEW_FIRST_NAME", newFirstName);
                resultIntent.putExtra("NEW_LAST_NAME", newLastName);
                resultIntent.putExtra("NEW_ADDRESS", newAddress);

                resultIntent.putExtra("NEW_ALLERGY", newAllergy);

                resultIntent.putExtra("NEW_PHONE_NUMBER", newPhoneNumber);
                resultIntent.putExtra("NEW_BIO", newBio);
                resultIntent.putExtra("NEW_DIET", newDiet);

                // Afficher un message de succès
                Toast.makeText(change_profile.this, "Changes successful", Toast.LENGTH_SHORT).show();
                // Revenir à l'activité précédente avec les données mises à jour
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }


}