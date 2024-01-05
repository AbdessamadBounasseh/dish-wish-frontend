package uit.ensak.dish_wish_frontend.Profil;
import com.airbnb.lottie.LottieAnimationView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Authentification.page_acceuil;
import uit.ensak.dish_wish_frontend.Command.ApiService;
import uit.ensak.dish_wish_frontend.Command.RetrofitClient;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class become_cook extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST_CARD = 1;
    private static final int PICK_IMAGE_REQUEST_CERTIF = 2;


    private byte[] byteIdCard = null;
    private byte[] byteCertificate = null;
    private LottieAnimationView animationCook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_cook);
        animationCook = findViewById(R.id.animation_cook_space);
        Button btnScancard = findViewById(R.id.btnScancard);
        Button btnScancertif = findViewById(R.id.btnScancertif);
        ImageButton btnBack = findViewById(R.id.btnBack);
        Button  btnSubmit = findViewById(R.id.btnsubmit);
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("userId",1L);
        editor.putString("accessToken","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGF5bWEyMjAxOEBnbWFpbC5jb20iLCJpYXQiOjE3MDMzMzkxMjgsImV4cCI6MTcwMzQyNTUyOH0.PjpPWRtTjpseAR0YrMwfC30RGps4l3H5JRd-uIvX8Bg");
        editor.apply();
        btnScancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGalleryPicker(PICK_IMAGE_REQUEST_CARD);
            }
        });

        btnScancertif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGalleryPicker(PICK_IMAGE_REQUEST_CERTIF);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (byteIdCard != null && byteCertificate != null) {
                    sendImagesToBackend(byteIdCard, byteCertificate);
                    showToast("Import successful!");
                    Intent intent = new Intent(become_cook.this, page_acceuil.class);
                    startActivity(intent);
                }
                onBackPressed();
                showToast("Import failed!");
                Intent intent = new Intent(become_cook.this, become_cook.class);
                startActivity(intent);
            }


        });
    }

    private void launchGalleryPicker( int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImageUri = data.getData();

        switch (requestCode) {
            case PICK_IMAGE_REQUEST_CARD:
                try {
                    byteIdCard = getImageBytes(selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case PICK_IMAGE_REQUEST_CERTIF:
                try {
                    byteCertificate = getImageBytes(selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
    }}

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
        RequestBody idCardRequestBody = RequestBody.create(MediaType.parse("image/*"), idCardData);
        RequestBody certificateRequestBody = RequestBody.create(MediaType.parse("image/*"), certificateData);

        MultipartBody.Part idCardPart = MultipartBody.Part.createFormData("idCard", "idCard.jpg", idCardRequestBody);
        MultipartBody.Part certificatePart = MultipartBody.Part.createFormData("certificate", "certificate.jpg", certificateRequestBody);
        ApiService apiService = RetrofitClient.getApiService();
        Call<Chef> call = apiService.becomeCook("Bearer " + authToken, userId, idCardPart, certificatePart);
        call.enqueue(new Callback<Chef>() {
            @Override
            public void onResponse(Call<Chef> call, Response<Chef> response) {
                showToast("Import successful!");
                Intent intent = new Intent(become_cook.this, page_acceuil.class);
                startActivity(intent);
               // finish();
            }
            @Override
            public void onFailure(Call<Chef> call, Throwable t) {
                showToast("Import failed!");
            }});
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
