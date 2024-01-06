package uit.ensak.dish_wish_frontend.Profil_Folder;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uit.ensak.dish_wish_frontend.Authentification.page_acceuil;
import uit.ensak.dish_wish_frontend.Command.ApiService;
import uit.ensak.dish_wish_frontend.Command.RetrofitClient;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.R;

import android.content.SharedPreferences;
import android.os.Bundle;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class become_cook extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST_CARD = 1;
    private static final int PICK_IMAGE_REQUEST_CERTIF = 2;


    private byte[] byteIdCard = null; // Pour stocker les données de l'image de la carte d'identité
    private byte[] byteCertificate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_become_cook);

        Button btnScancard = findViewById(R.id.btnScancard);
        Button btnScancertif = findViewById(R.id.btnScancertif);
        ImageButton btnBack = findViewById(R.id.btnBack);
        Button  btnSubmit = findViewById(R.id.btnsubmit);


        // Obtenez une référence à SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("userId",1L);
        editor.putString("accessToken","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGF5bWEyMjAxOEBnbWFpbC5jb20iLCJpYXQiOjE3MDMzMzkxMjgsImV4cCI6MTcwMzQyNTUyOH0.PjpPWRtTjpseAR0YrMwfC30RGps4l3H5JRd-uIvX8Bg");
        editor.apply();

        btnScancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  launchGalleryPicker();
                launchGalleryPicker(PICK_IMAGE_REQUEST_CARD);


            }
        });

        btnScancertif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // launchGalleryPicker();
                launchGalleryPicker(PICK_IMAGE_REQUEST_CERTIF);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode pour revenir en arrière
                onBackPressed();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode pour revenir en arrière

                if (byteIdCard != null && byteCertificate != null) {
                    sendImagesToBackend(byteIdCard, byteCertificate);
                    showToast("Importation réussie!");
                }
               // onBackPressed();
                //ajouter un pop msg
            }


        });
    }

    private void launchGalleryPicker( int requestCode) {
        // Intent pour choisir une image depuis la galerie
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
//

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImageUri = data.getData();

        switch (requestCode) {
            case PICK_IMAGE_REQUEST_CARD:
                // Récupérer les données de l'image de la carte d'identité
                try {
                    byteIdCard = getImageBytes(selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Gérer l'erreur de conversion en bytes
                }
                break;

            case PICK_IMAGE_REQUEST_CERTIF:
                // Récupérer les données de l'image du certificat
                try {
                    byteCertificate = getImageBytes(selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Gérer l'erreur de conversion en bytes
                }
                break;
    }

    }

    private byte[] getImageBytes(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void sendImagesToBackend(byte[] idCardData, byte[] certificateData){

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);
        String authToken = preferences.getString("accessToken", "");

        // Convertir les données d'image en RequestBody
        RequestBody idCardRequestBody = RequestBody.create(MediaType.parse("image/*"), idCardData);
        RequestBody certificateRequestBody = RequestBody.create(MediaType.parse("image/*"), certificateData);

        // Créer les parties du corps de la requête
        MultipartBody.Part idCardPart = MultipartBody.Part.createFormData("idCard", "idCard.jpg", idCardRequestBody);
        MultipartBody.Part certificatePart = MultipartBody.Part.createFormData("certificate", "certificate.jpg", certificateRequestBody);
        // Créer une instance de Retrofit

        ApiService apiService = RetrofitClient.getApiService();

        // Appeler la méthode becomeCook
        Call<Chef> call = apiService.becomeCook("Bearer " + authToken, userId, idCardPart, certificatePart);

        // Exécuter l'appel
        call.enqueue(new Callback<Chef>() {
            @Override
            public void onResponse(Call<Chef> call, Response<Chef> response) {
                // Gérer la réponse du serveur
                System.out.println("test");
                showToast("Importation réussie!");
                //se retourner vers la page d'accueil
                Intent intent = new Intent(become_cook.this, view_profile.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Chef> call, Throwable t) {
                showToast("Importation échoué!");
                Intent intent = new Intent(become_cook.this, view_profile.class);

            }
        });
    }
    //si tout est bien , vers page d'accueil
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
