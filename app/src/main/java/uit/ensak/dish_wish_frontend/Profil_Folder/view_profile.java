package uit.ensak.dish_wish_frontend.Profil_Folder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.common.api.Api;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Authentification.page_acceuil;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.Profil_Folder.change_profile;
import uit.ensak.dish_wish_frontend.dto.ChefDTO;
import uit.ensak.dish_wish_frontend.dto.DietDTO;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;

public class view_profile extends AppCompatActivity {

    static final int REQUEST_CODE_CHANGE_PROFILE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int REQUEST_PICK_IMAGE = 3;


    private TextView textViewFirstName,textViewLastName,textViewAddress,textViewDiet,textViewBioContent, textViewPHONE_NUMBER, textViewAllergies;

    private ImageView profileImageView;
     private  ChefDTO chefDTO;
     private Bitmap newProfileImageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("userId",2L);
        editor.putString("accessToken","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZXJuYXJkYWxwaGFvbWVnYWRla3BlQGdtYWlsLmNvbSIsImlhdCI6MTcwMzg4ODI0MywiZXhwIjoxNzAzOTc0NjQzfQ.sZaz6HH0qcz6PcwHghzDI4w-Fl438RlU9n-efvj9hQE");
       //si c'est un chef
       editor.putBoolean("isCook",false);
       editor.apply();


        Boolean isCook= preferences.getBoolean("isCook", false);


        // Supposons que vous avez déjà une référence à votre TextView


        setContentView(R.layout.activity_view_profile);

        textViewFirstName = findViewById(R.id.textViewActualFirstName);
        textViewLastName = findViewById(R.id.textViewActualLastName);
        textViewAddress = findViewById(R.id.textViewActualAddress);
        textViewDiet = findViewById(R.id.textViewActualDiet);
        textViewPHONE_NUMBER = findViewById(R.id.textViewActualPhoneNumber);
        textViewAllergies = findViewById(R.id.textViewActualAllergies);
        textViewBioContent = findViewById(R.id.textViewActualBio);

        TextView textViewBioTitle = findViewById(R.id.textViewBio);



        if (isCook) {
            textViewBioTitle.setVisibility(View.VISIBLE);
            textViewBioContent.setVisibility(View.VISIBLE);
            getChef(new ApiChefCallback() {
                @Override
                public void onChefReceived(Chef chef) {
                    textViewFirstName.setText(chef.getFirstName());
                    textViewLastName.setText(chef.getLastName());
                    textViewAddress.setText(chef.getAddress());
                    textViewPHONE_NUMBER.setText(chef.getPhoneNumber());
                    if(chef.getDiet()!= null){
                    textViewDiet.setText(chef.getDiet().getTitle());}
                    textViewAllergies.setText(chef.getAllergies());
                    textViewBioContent.setText(chef.getBio());
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        } else {
            textViewBioTitle.setVisibility(View.INVISIBLE);
            textViewBioContent.setVisibility(View.INVISIBLE);
            getClient(new ApiClientCallback() {
                @Override
                public void onClientReceived(Client client) {
                    textViewFirstName.setText(client.getFirstName());
                    textViewLastName.setText(client.getLastName());
                    textViewAddress.setText(client.getAddress());
                    textViewPHONE_NUMBER.setText(client.getPhoneNumber());
                    if(client.getDiet()!=null){
                    textViewDiet.setText(client.getDiet().getTitle());}
                    textViewAllergies.setText(client.getAllergies());
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        }


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
                intent.putExtra("CURRENT_Allergy", textViewAllergies.getText().toString());
                if (isCook) {
                    intent.putExtra("CURRENT_BIO", textViewBioContent.getText().toString());
                }
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

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);
        String authToken = preferences.getString("accessToken", "");

        ApiServiceProfile apiService = RetrofitClientProfile.getApiService();

        // Appeler la méthode becomeCook
        Call<Void> call = apiService.deleteUserAccount("Bearer " + authToken, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(view_profile.this, "Account has been successfully deleted", Toast.LENGTH_SHORT).show();
                    //il faut retourner a login activity
                    Intent loginIntent = new Intent(view_profile.this, page_acceuil.class);
                    startActivity(loginIntent);
                    finish(); // Pour fermer l'activité actuelle après la suppression du compte
                } else {
                    Toast.makeText(view_profile.this, "Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(view_profile.this, "Error2", Toast.LENGTH_SHORT).show();

            }
        });

        // Par exemple, vous pourriez appeler une API de suppression de compte, ou supprimer les données localement, etc.


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Boolean isCook= preferences.getBoolean("isCook", false);
        String newBio="";

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            DietDTO dietDTO= new DietDTO();
            chefDTO.setFirstName(data.getStringExtra("NEW_FIRST_NAME"));
            chefDTO.setLastName(data.getStringExtra("NEW_LAST_NAME"));
            chefDTO.setAddress(data.getStringExtra("NEW_ADDRESS"));
            chefDTO.setPhoneNumber(data.getStringExtra("NEW_PHONE_NUMBER"));
            chefDTO.setAllergies(data.getStringExtra("NEW_ALLERGY"));
            dietDTO.setTitle(data.getStringExtra("NEW_DIET"));
            chefDTO.setDietDTO(dietDTO);
            if (isCook) {
                chefDTO.setBio(data.getStringExtra("NEW_BIO"));
            }
            // Ajoutez ces lignes pour récupérer l'image redimensionnée dans MainActivity
            newProfileImageBitmap = data.getParcelableExtra("NEW_PROFILE_IMAGE_BITMAP");

            if (newProfileImageBitmap != null) {
                profileImageView.setImageBitmap(getRoundedBitmap(newProfileImageBitmap));
            } else {
                // Faites quelque chose ici pour gérer le cas où newProfileImageBitmap est null
            }
            textViewFirstName.setText(chefDTO.getFirstName());
            textViewLastName.setText(chefDTO.getLastName());
            textViewAddress.setText(chefDTO.getAddress());
            textViewPHONE_NUMBER.setText(chefDTO.getPhoneNumber());
            textViewAllergies.setText(chefDTO.getAllergies());
            textViewDiet.setText(dietDTO.getTitle());
            if (isCook) {
                textViewBioContent.setText(chefDTO.getBio());
            }

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
        updateUser(chefDTO,newProfileImageBitmap);

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
    }

    private void getClient(ApiClientCallback apiClientCallback) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);
        String authToken = preferences.getString("accessToken", "");
        ApiServiceProfile apiService = RetrofitClientProfile.getApiService();
        Call<Client> call = apiService.getClientById("Bearer " + authToken, userId);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    Client client = response.body();
                    apiClientCallback.onClientReceived(client);
                } else {
                    apiClientCallback.onFailure("Error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Client> call, Throwable t) {

                t.printStackTrace();
                apiClientCallback.onFailure("Error: " + t.getMessage());
            }
        });
    }

    private void getChef(ApiChefCallback apiChefCallback) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);
        String authToken = preferences.getString("accessToken", "");

        ApiServiceProfile apiService = RetrofitClientProfile.getApiService();

        Call<Chef> call = apiService.getChefById("Bearer " + authToken, userId);

        call.enqueue(new Callback<Chef>() {
            @Override
            public void onResponse(Call<Chef> call, Response<Chef> response) {
                if (response.isSuccessful()) {
                    Chef chef = response.body();
                    apiChefCallback.onChefReceived(chef);
                } else {
                    apiChefCallback.onFailure("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Chef> call, Throwable t) {

                t.printStackTrace();
                apiChefCallback.onFailure("Error: " + t.getMessage());
            }
        });
    }

    private void updateUser(ChefDTO chefDTO, Bitmap imageBitmap) {
        // Convertir le Bitmap en tableau de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        // Créer une RequestBody à partir du tableau de bytes
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);

        // Créer un MultipartBody.Part à partir de la RequestBody
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo", "photo.jpg", requestFile);

        // Le reste de votre code
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);
        String authToken = preferences.getString("accessToken", "");
        ApiServiceProfile apiService = RetrofitClientProfile.getApiService();
        Call<Client> call = apiService.updateClient("Bearer " + authToken, userId, chefDTO, photoPart);

        // Continuez avec le reste de votre logique...
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    Client client = response.body();
                    Toast.makeText(view_profile.this, "User updat succesful", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(view_profile.this, "Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

                t.printStackTrace();


            }
        });
    }


}