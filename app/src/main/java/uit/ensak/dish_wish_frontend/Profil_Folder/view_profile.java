package uit.ensak.dish_wish_frontend.Profil_Folder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uit.ensak.dish_wish_frontend.Command.ApiService;
import uit.ensak.dish_wish_frontend.Command.RetrofitClient;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.Profil_Folder.change_profile;


public class view_profile extends AppCompatActivity {

    static final int REQUEST_CODE_CHANGE_PROFILE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int REQUEST_PICK_IMAGE = 3;


    private TextView textViewFirstName,textViewLastName,textViewAddress, textViewBio, textViewDiet, textViewPHONE_NUMBER, textViewAllergies;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        textViewFirstName = findViewById(R.id.textViewActualFirstName);
        textViewLastName = findViewById(R.id.textViewActualLastName);
        textViewAddress = findViewById(R.id.textViewActualAddress);
        textViewDiet = findViewById(R.id.textViewActualDiet);
        textViewBio = findViewById(R.id.textViewActualBio);
        textViewPHONE_NUMBER = findViewById(R.id.textViewActualPhoneNumber);
        textViewAllergies = findViewById(R.id.textViewActualAllergies);
        profileImageView = findViewById(R.id.portrait_of);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnChange = findViewById(R.id.btnchange);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancez l'activité ChangeProfileActivity avec startActivityForResult
                Intent intent = new Intent(view_profile.this, change_profile.class);


                // Passer la valeur actuelle du prénom à l'intention
                intent.putExtra("CURRENT_FIRST_NAME", textViewFirstName.getText().toString());

                intent.putExtra("CURRENT_LAST_NAME", textViewLastName.getText().toString());
                intent.putExtra("CURRENT_ADDRESS", textViewAddress.getText().toString());


                intent.putExtra("CURRENT_PHONE_NUMBER", textViewPHONE_NUMBER.getText().toString());

                intent.putExtra("CURRENT_BIO", textViewBio.getText().toString());
               // intent.putExtra("CURRENT_DIET", textViewDiet.getText().toString());
                // intent.putExtra("CURRENT_PROFILE_IMAGE_PATH", imagePath);




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

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Demander confirmation à l'utilisateur avant de supprimer le compte
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        // Utilisez une boîte de dialogue (AlertDialog) pour demander la confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete your account?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ajoutez ici la logique de suppression du compte
                // Vous pouvez appeler une méthode pour effectuer la suppression ou utiliser une API appropriée

                // Exemple fictif : Supprimer le compte et retourner à l'écran de connexion
                deleteAccount();
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // L'utilisateur a annulé la suppression, ne rien faire
            }
        });
        builder.show();
    }
    private void deleteAccount() {
        // Ajoutez ici la logique réelle pour supprimer le compte
        // Par exemple, vous pourriez appeler une API de suppression de compte, ou supprimer les données localement, etc.

        Toast.makeText(view_profile.this, "Account has been successfully deleted", Toast.LENGTH_SHORT).show();
        //il faut retourner a login activity
        Intent loginIntent = new Intent(view_profile.this, change_profile.class);
        startActivity(loginIntent);
        finish(); // Pour fermer l'activité actuelle après la suppression du compte
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            String newFirstName = data.getStringExtra("NEW_FIRST_NAME");
            String newLastName = data.getStringExtra("NEW_LAST_NAME");
            String newAddress = data.getStringExtra("NEW_ADDRESS");
            String newBio = data.getStringExtra("NEW_BIO");
            String newDiet = data.getStringExtra("NEW_DIET");
            String newPhoneNumber = data.getStringExtra("NEW_PHONE_NUMBER");
            String newAllergy = data.getStringExtra("NEW_ALLERGY");

            // Ajoutez ces lignes pour récupérer l'image redimensionnée dans MainActivity
            Bitmap newProfileImageBitmap = data.getParcelableExtra("NEW_PROFILE_IMAGE_BITMAP");

// Utilisez cette image pour mettre à jour votre interface utilisateur
            profileImageView.setImageBitmap(getRoundedBitmap(newProfileImageBitmap));


            // Bitmap newProfileImageBitmap = data.getParcelableExtra("NEW_PROFILE_IMAGE_BITMAP");

//            if (newProfileImageBitmap != null) {
//
//            }

            // Mettre à jour votre interface utilisateur avec les nouvelles valeurs

            textViewFirstName.setText(newFirstName);
            textViewLastName.setText(newLastName);
            textViewAddress.setText(newAddress);

            textViewPHONE_NUMBER.setText(newPhoneNumber);
            textViewBio.setText(newBio);
            textViewDiet.setText(newDiet);
            textViewAllergies.setText(newAllergy);
            //#######################################


        }
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_PICK_IMAGE) && resultCode == RESULT_OK) {
            // Gérer la sélection d'une nouvelle image de profil
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                profileImageView.setImageBitmap(imageBitmap);
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    profileImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
