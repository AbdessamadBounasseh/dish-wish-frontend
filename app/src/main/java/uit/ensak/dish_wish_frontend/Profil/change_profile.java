package uit.ensak.dish_wish_frontend.Profil;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import uit.ensak.dish_wish_frontend.R;

public class change_profile extends AppCompatActivity {

    private EditText editTextNewFirstName, editTextNewLastName, editTextNewAddress, editTextNewPhoneNumber, editTextNewAllergie,editTextNewBio;
    private TextView textViewBioTitle;
    private Button btnSubmit;
    Spinner spinnerDiet;
    private String currentFirstName, currentLastName, currentAddress,currentPhoneNumber,currentBio,currentAllergie;

    private String newDiet;

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_PICK_IMAGE = 102;
    private ImageView profileImageView;
    private Bitmap imageBitmap;
    private Bitmap resizeBitmap(Bitmap originalBitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isCook",false);
        editor.apply();
        Boolean isCook= preferences.getBoolean("isCook", false);

        setContentView(R.layout.activity_change_profile);

        editTextNewBio = findViewById(R.id.editTextNewBio);
       textViewBioTitle = findViewById(R.id.textViewBio);

        if (isCook) {
            textViewBioTitle.setVisibility(View.VISIBLE);
            editTextNewBio.setVisibility(View.VISIBLE);
        }

        spinnerDiet = findViewById(R.id.spinnerDiet);
        ArrayAdapter<CharSequence> adapterDiet = ArrayAdapter.createFromResource(this, R.array.diet_array, android.R.layout.simple_spinner_item);
        adapterDiet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiet.setAdapter(adapterDiet);


        spinnerDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Mettez à jour la nouvelle allergie lorsque l'utilisateur sélectionne une option dans le spinner
                newDiet = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Cette méthode est requise mais nous n'avons rien à faire ici pour le moment
            }

        });
        //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
        profileImageView = findViewById(R.id.portrait_of);
        if (getIntent().hasExtra("IMAGE_BITMAP")) {
            imageBitmap = getIntent().getParcelableExtra("IMAGE_BITMAP");
            profileImageView.setImageBitmap(imageBitmap);
        }

        ImageView changePhotoImageView = findViewById(R.id.imageViewChangePhoto);
        // Ajoutez un écouteur de clic pour le changement d'image
        changePhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourceDialog();
            }
        });


        //#########################################
        editTextNewFirstName = findViewById(R.id.editTextNewFirstName);
        editTextNewLastName = findViewById(R.id.editTextNewLastName);
        editTextNewAddress = findViewById(R.id.editTextNewAddress);
        editTextNewPhoneNumber = findViewById(R.id.editTextNewPhoneNumber);

        editTextNewAllergie = findViewById(R.id.editTextNewAllergie);
        btnSubmit = findViewById(R.id.btnsubmit);
        // Récupérer le prénom actuel
        currentFirstName = getIntent().getStringExtra("CURRENT_FIRST_NAME");
        currentLastName = getIntent().getStringExtra("CURRENT_LAST_NAME");
        currentAddress = getIntent().getStringExtra("CURRENT_ADDRESS");
        if (isCook) {
            currentBio = getIntent().getStringExtra("CURRENT_BIO");
        }
        currentAllergie = getIntent().getStringExtra("CURRENT_ALLERGY");
        currentPhoneNumber = getIntent().getStringExtra("CURRENT_PHONE_NUMBER");
        editTextNewFirstName.setText(currentFirstName);
        editTextNewLastName.setText(currentLastName);
        editTextNewAddress.setText(currentAddress);
        editTextNewPhoneNumber.setText(currentPhoneNumber);
        if (isCook) {
            editTextNewBio.setText(currentBio);
        }
        editTextNewAllergie.setText(currentAllergie);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mettre à jour le prénom avec la nouvelle valeur


                String newFirstName = editTextNewFirstName.getText().toString();

                String newLastName = editTextNewLastName.getText().toString();

                String newAddress = editTextNewAddress.getText().toString();

                String newPhoneNumber = editTextNewPhoneNumber.getText().toString();
                String newBio = "";
                if (isCook) {

                   newBio = editTextNewBio.getText().toString();
                }

                String newAllergie = editTextNewAllergie.getText().toString();

                // String imagePath = saveImageToInternalStorage(imageBitmap);

                // Créer un Intent pour contenir les nouvelles valeurs
                Intent resultIntent = new Intent();

                resultIntent.putExtra("NEW_FIRST_NAME", newFirstName);
                resultIntent.putExtra("NEW_LAST_NAME", newLastName);
                resultIntent.putExtra("NEW_ADDRESS", newAddress);
                resultIntent.putExtra("NEW_ALLERGY", newAllergie);
                resultIntent.putExtra("NEW_PHONE_NUMBER", newPhoneNumber);
                if (isCook) {
                    resultIntent.putExtra("NEW_BIO", newBio);
                }
                resultIntent.putExtra("NEW_DIET", newDiet);
                resultIntent.putExtra("NEW_PROFILE_IMAGE_BITMAP", imageBitmap);
                // Afficher un message de succès
                Toast.makeText(change_profile.this, "Changes successful", Toast.LENGTH_SHORT).show();
                // Revenir à l'activité précédente avec les données mises à jour
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }


    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        pickImageFromGallery();
                        break;
                }
            }
        });
        builder.show();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        // Vous devez définir la logique pour sauvegarder l'image dans le stockage interne ici
        // Retournez le chemin du fichier après l'enregistrement

        String imagePath = getFilesDir() + "/profile_image.jpg";

        return imagePath;
    }


    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    imageBitmap = resizeBitmap(imageBitmap, 92, 92);
                    imageBitmap=getRoundedBitmap(imageBitmap);
                    profileImageView.setImageBitmap(imageBitmap);
                    imageBitmap = getIntent().getParcelableExtra("IMAGE_BITMAP");
                    break;
                case REQUEST_PICK_IMAGE:
                    Uri selectedImage = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        imageBitmap = resizeBitmap(imageBitmap, 92, 92);
                        profileImageView.setImageBitmap(getRoundedBitmap(imageBitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }


    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int diameter = Math.min(width, height);

        Bitmap output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, diameter, diameter);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(diameter / 2f, diameter / 2f, diameter / 2f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);

        return output;


    }}

