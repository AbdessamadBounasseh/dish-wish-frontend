package uit.ensak.dish_wish_frontend.Profil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import java.io.IOException;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Authentification.CreateAccount;
import uit.ensak.dish_wish_frontend.Command.MapsChefActivity;
import uit.ensak.dish_wish_frontend.Command.MapsHomeActivity;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;
import uit.ensak.dish_wish_frontend.service.RetrofitClient;

public class view_profile extends AppCompatActivity {

    static final int REQUEST_CODE_CHANGE_PROFILE = 1;
    static final int REQUEST_PICK_IMAGE = 3;

    private  String position;
    private TextView textViewFirstName, textViewLastName, textViewAddress,textViewBio, textViewDiet,textViewCity, textViewPHONE_NUMBER, textViewAllergies, textViewBioContent;
    private ImageView profileImageView;

    private String accessToken;
    private long userId;
    private Boolean isCook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        accessToken = preferences.getString("accessToken", "");
        userId = preferences.getLong("userId", 0);
        isCook = preferences.getBoolean("isCook", false);

        setContentView(R.layout.activity_view_profile);

        textViewFirstName = findViewById(R.id.textViewActualFirstName);
        textViewLastName = findViewById(R.id.textViewActualLastName);
        textViewAddress = findViewById(R.id.textViewActualAddress);
        textViewCity= findViewById(R.id.textViewActualCity);
        textViewDiet = findViewById(R.id.textViewActualDiet);
        textViewPHONE_NUMBER = findViewById(R.id.textViewActualPhoneNumber);
        textViewAllergies = findViewById(R.id.textViewActualAllergies);
        profileImageView = findViewById(R.id.portrait_of);
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
                    if(chef.getAddress()!=null){
                    textViewAddress.setText(chef.getAddress().getAddress());
                        textViewCity.setText(chef.getAddress().getCity().getName());
                        position= chef.getAddress().getPosition();
                    }
                    textViewPHONE_NUMBER.setText(chef.getPhoneNumber());
                    if (chef.getDiet() != null) {
                        textViewDiet.setText(chef.getDiet().getTitle());
                    }
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
                    if(client.getAddress()!=null) {
                        textViewAddress.setText(client.getAddress().getAddress());
                        textViewCity.setText(client.getAddress().getCity().getName());
                        position= client.getAddress().getPosition();
                    }
                    textViewPHONE_NUMBER.setText(client.getPhoneNumber());
                    if (client.getDiet() != null) {
                        textViewDiet.setText(client.getDiet().getTitle());
                    }
                    textViewAllergies.setText(client.getAllergies());
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        }

        getClientProfile();


        ImageButton btnBack = findViewById(R.id.btnBack);

        Button btnChange = findViewById(R.id.btnchange);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view_profile.this, change_profile.class);
                intent.putExtra("CURRENT_FIRST_NAME", textViewFirstName.getText().toString());
                intent.putExtra("CURRENT_LAST_NAME", textViewLastName.getText().toString());
                intent.putExtra("CURRENT_ADDRESS", textViewAddress.getText().toString());
                intent.putExtra("CURRENT_PHONE_NUMBER", textViewPHONE_NUMBER.getText().toString());
                intent.putExtra("CURRENT_ALLERGY", textViewAllergies.getText().toString());
                intent.putExtra("CURRENT_POSITION", position);
                if (isCook) {
                    intent.putExtra("CURRENT_BIO", textViewBio.getText().toString());
                }
                intent.putExtra("CURRENT_CITY", textViewCity.getText().toString());
                intent.putExtra("CURRENT_DIET", textViewDiet.getText().toString());
                startActivityForResult(intent, REQUEST_CODE_CHANGE_PROFILE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(isCook){
                    intent = new Intent(view_profile.this, MapsChefActivity.class);
                }else{
                    intent = new Intent(view_profile.this, MapsHomeActivity.class);
                }
                startActivity(intent);
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete your account?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAccount();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    profileImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    private void getClient(ApiClientCallback apiClientCallback) {
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<Client> call = apiService.getClientById("Bearer " + accessToken, userId);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    Client client = response.body();
                    apiClientCallback.onClientReceived(client);
                } else {
                    apiClientCallback.onFailure("Error during user fetching " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                t.printStackTrace();
                apiClientCallback.onFailure("Unavailable Server " + t.getMessage());
            }
        });
    }

    private void getChef(ApiChefCallback apiChefCallback) {
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();

        Call<Chef> call = apiService.getChefById("Bearer " + accessToken, userId);

        call.enqueue(new Callback<Chef>() {
            @Override
            public void onResponse(Call<Chef> call, Response<Chef> response) {
                if (response.isSuccessful()) {
                    Chef chef = response.body();
                    apiChefCallback.onChefReceived(chef);
                } else {
                    apiChefCallback.onFailure("Error during user fetching " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Chef> call, Throwable t) {

                t.printStackTrace();
                apiChefCallback.onFailure("Unavailable Server " + t.getMessage());
            }
        });
    }

    private void deleteAccount() {
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<Void> call = apiService.deleteUserAccount("Bearer " + accessToken, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent loginIntent = new Intent(view_profile.this, CreateAccount.class);
                    Toast.makeText(view_profile.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                    startActivity(loginIntent);
                } else {
                    Intent loginIntent = new Intent(view_profile.this, view_profile.class);
                    Toast.makeText(view_profile.this, "Error deleting the account. Please try again", Toast.LENGTH_SHORT).show();
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(view_profile.this, "Error deleting the account. Please try again", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(view_profile.this, view_profile.class);
                startActivity(loginIntent);
            }
        });
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


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void getClientProfile() {
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();

        Call<ResponseBody> call = apiService.getClientProfile("Bearer " + accessToken, userId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Bitmap newProfileImageBitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        profileImageView.setImageBitmap(getRoundedBitmap(newProfileImageBitmap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                     Toast.makeText(view_profile.this, "Error during user profile fetching " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(view_profile.this, "Unavailable Sever " ,Toast.LENGTH_SHORT).show();
            }
        });
    }
}