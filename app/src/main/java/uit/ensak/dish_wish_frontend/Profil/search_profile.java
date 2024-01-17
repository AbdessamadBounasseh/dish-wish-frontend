package uit.ensak.dish_wish_frontend.Profil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Models.Address;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Comment;
import uit.ensak.dish_wish_frontend.Models.Rating;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.dto.ChefDTO;
import uit.ensak.dish_wish_frontend.dto.ClientDTO;
import uit.ensak.dish_wish_frontend.dto.CommentRequestDTO;
import uit.ensak.dish_wish_frontend.dto.CommentResponseDTO;
import uit.ensak.dish_wish_frontend.dto.DietDTO;
import uit.ensak.dish_wish_frontend.dto.RatingDTO;
import uit.ensak.dish_wish_frontend.search_folder.CommentAdapter;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;
import uit.ensak.dish_wish_frontend.service.RetrofitClient;

public class search_profile extends AppCompatActivity{

    static final int REQUEST_CODE_CHANGE_PROFILE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int REQUEST_PICK_IMAGE = 3;
    private ChefDTO chefDTO;
    private TextView textViewFirstName, textViewLastName, textViewAddress, textViewBio, textViewDiet, textViewPHONE_NUMBER, textViewAllergies, textViewBioContent;
    private ImageView profileImageView;
    private EditText editTextComment;
    private Button btnAddComment;
    private RecyclerView recyclerViewComments;
    private CommentAdapter commentAdapter;
    private List<CommentResponseDTO> comments;
    private NestedScrollView nestedScrollView;
    private String clientFirstName;
    private String clientLastName;
    private String accessToken;
    private boolean isCook;
    private long userId;
    private Long chefClickedId;
    private RatingBar ratingBar;
    private  Chef chef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        accessToken = preferences.getString("accessToken", "");
        isCook = preferences.getBoolean("isCook", false);
        userId = preferences.getLong("userId", 0);

        Intent intent = getIntent();
        chefClickedId = intent.getLongExtra("id", 0);
        setContentView(R.layout.activity_search_profile);

        Button btnRating = findViewById(R.id.btnAddReview);



        ratingBar = findViewById(R.id.ratingBar);
        // getRating(chefClickedId);
        // getRating(7L);

        // float rating = ratingBar.getRating();


        double ratingValue = (double) ratingBar.getRating();

        textViewFirstName = findViewById(R.id.textViewActualFirstName);
        textViewLastName = findViewById(R.id.textViewActualLastName);
        textViewAddress = findViewById(R.id.textViewActualAddress);
        textViewDiet = findViewById(R.id.textViewActualDiet);
        textViewPHONE_NUMBER = findViewById(R.id.textViewActualPhoneNumber);
        textViewAllergies = findViewById(R.id.textViewActualAllergies);
        profileImageView = findViewById(R.id.portrait_of);
        textViewBioContent = findViewById(R.id.textViewActualBio);
        editTextComment = findViewById(R.id.editTextComment);
        btnAddComment = findViewById(R.id.btnAddComment);
        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        nestedScrollView = findViewById(R.id.nestedScrollView);

        getComments();

        TextView textViewBioTitle = findViewById(R.id.textViewBio);


        textViewBioTitle.setVisibility(View.VISIBLE);
        textViewBioContent.setVisibility(View.VISIBLE);
        getChef(new ApiChefCallback() {
            @Override
            public void onChefReceived(Chef chef) {
                textViewFirstName.setText(chef.getFirstName());
                textViewLastName.setText(chef.getLastName());
                textViewAddress.setText(chef.getAddress().getCity().getName());
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


        getClientProfile();
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnRating.setOnClickListener(new View.OnClickListener() {
            //
            ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();

            Call<ResponseBody> call = apiService.getClientProfile("Bearer " + accessToken, userId);


            @Override
            public void onClick(View v) {

                double ratingVlue = (double) ratingBar.getRating();
                sendRating(ratingVlue);
            }
        });
        ratingBar.setRating((float) ratingValue);

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getClientForComment();
            }
        });





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
            Bitmap newProfileImageBitmap = data.getParcelableExtra("NEW_PROFILE_IMAGE_BITMAP");

            if (newProfileImageBitmap != null) {
                profileImageView.setImageBitmap(getRoundedBitmap(newProfileImageBitmap));
            } else {

            }
            SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
            Boolean isCook = preferences.getBoolean("isCook", false);

            textViewFirstName.setText(newFirstName);
            textViewLastName.setText(newLastName);
            textViewAddress.setText(newAddress);
            textViewPHONE_NUMBER.setText(newPhoneNumber);
            textViewDiet.setText(newDiet);
            textViewAllergies.setText(newAllergy);
            if(isCook) {
                textViewBio.setText(newBio);
            }

        }
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_PICK_IMAGE) && resultCode == RESULT_OK) {
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
    }
    private void scrollToBottom() {
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });}

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void getClientForComment() {
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<Client> call = apiService.getClientById("Bearer " + accessToken, userId);
//        Call<Client> call = apiService.getClientById("Bearer " +"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGF5bWFhZTEyM0BnbWFpbC5tYSIsImlhdCI6MTcwNTM5OTIyOSwiZXhwIjoxNzA1NDg1NjI5fQ.NiJMSteCZesR02fdZ5waSJF6dHVGKfJTO-nbwzSQdyU" , 8L);

        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    Client client = response.body();
                    clientFirstName = client.getFirstName();
                    clientLastName = client.getLastName();
                    addComment();
                } else {

                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private void getChef(ApiChefCallback apiChefCallback) {
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();

        Call<Chef> call = apiService.getChefById("Bearer " + accessToken, chefClickedId);

        call.enqueue(new Callback<Chef>() {
            @Override
            public void onResponse(Call<Chef> call, Response<Chef> response) {
                if (response.isSuccessful()) {
                    chef = response.body();
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
                    Toast.makeText(search_profile.this, "Error during user profile fetching " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(search_profile.this, "Unavailable Sever " ,Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addComment() {

        String commentContent = editTextComment.getText().toString().trim();
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();

        CommentRequestDTO commentRequestDTO =new CommentRequestDTO(commentContent, chefClickedId, userId);
//        CommentRequestDTO commentRequestDTO =new CommentRequestDTO(commentContent, 7L, 13L);
        Call<CommentResponseDTO> call = apiService.sendClientComment("Bearer " + accessToken,commentRequestDTO);
//        Call<CommentResponseDTO> call = apiService.sendClientComment("Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGF5bWFhZTEyM0BnbWFpbC5tYSIsImlhdCI6MTcwNTM5OTIyOSwiZXhwIjoxNzA1NDg1NjI5fQ.NiJMSteCZesR02fdZ5waSJF6dHVGKfJTO-nbwzSQdyU",commentRequestDTO);
        call.enqueue(new Callback<CommentResponseDTO>() {
            @Override
            public void onResponse(Call<CommentResponseDTO> call, Response<CommentResponseDTO> response) {
                if (response.isSuccessful()) {
                    getComments();
                    editTextComment.setText("");
                    scrollToBottom();
                } else {
                    showToast("Failed to send comment");
                }
            }

            @Override
            public void onFailure(Call<CommentResponseDTO> call, Throwable t) {
                showToast("Unavailable Server ");
            }
        });
    }

    private void getComments() {
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<List<CommentResponseDTO>> call = apiService.getChefComments("Bearer "+ accessToken, chefClickedId);
//        Call<List<CommentResponseDTO>> call = apiService.getChefComments("Bearer "+ "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGF5bWFhZTEyM0BnbWFpbC5tYSIsImlhdCI6MTcwNTM5OTIyOSwiZXhwIjoxNzA1NDg1NjI5fQ.NiJMSteCZesR02fdZ5waSJF6dHVGKfJTO-nbwzSQdyU", 7L);
        call.enqueue(new Callback<List<CommentResponseDTO>>() {
            @Override
            public void onResponse(Call<List<CommentResponseDTO>> call, Response<List<CommentResponseDTO>> response) {
                if (response.isSuccessful()) {
                    List<CommentResponseDTO> comments = response.body();
                    commentAdapter = new CommentAdapter(search_profile.this, comments);
                    recyclerViewComments.setLayoutManager(new LinearLayoutManager(search_profile.this));
                    recyclerViewComments.setAdapter(commentAdapter);
                } else {
                    showToast("Failed to get comments" );
                }
            }

            @Override
            public void onFailure(Call<List<CommentResponseDTO>> call, Throwable t) {
                showToast("Unavailable Server ");
            }

        });
    }

    private void getRating(Long chefid) {
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<Double> call = apiService.getclientrating("Bearer "+ accessToken, chefClickedId);
//        Call<Double> call = apiService.getclientrating("Bearer "+ "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGF5bWFhZTEyM0BnbWFpbC5tYSIsImlhdCI6MTcwNTM5OTIyOSwiZXhwIjoxNzA1NDg1NjI5fQ.NiJMSteCZesR02fdZ5waSJF6dHVGKfJTO-nbwzSQdyU", 7L);
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful()) {
                    Double rating = response.body();
                    if (rating != null) {
                        Float ratingValue = rating.floatValue();
                        ratingBar.setRating(ratingValue);
                    }
                } else {
                    showToast("Failed to get rating" );
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                showToast("Unavailable Server ");
            }
        });
    }
    private void sendRating(Double rating) {
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();

        RatingDTO ratingDTO =new RatingDTO();
        ratingDTO.setChefId(chefClickedId);
        ratingDTO.setClientId(userId);
//        ratingDTO.setChefId(7L);
//        ratingDTO.setClientId(8L);
        ratingDTO.setRating(rating);
        Call<ResponseBody> call = apiService.sendClientRating("Bearer " + accessToken,ratingDTO);
//        Call<ResponseBody> call = apiService.sendClientRating("Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGF5bWFhZTEyM0BnbWFpbC5tYSIsImlhdCI6MTcwNTM5OTIyOSwiZXhwIjoxNzA1NDg1NjI5fQ.NiJMSteCZesR02fdZ5waSJF6dHVGKfJTO-nbwzSQdyU",ratingDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    getRating(chefClickedId);
//                    getRating(7L);
                    showToast("Rating sent successfully");
                } else {
                    showToast("Failed to send rating" );
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToast("Unavailable Server ");
            }
        });
    }
}