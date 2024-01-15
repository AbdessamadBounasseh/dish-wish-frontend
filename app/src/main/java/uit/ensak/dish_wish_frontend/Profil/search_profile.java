package uit.ensak.dish_wish_frontend.Profil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.dto.ChefDTO;
import uit.ensak.dish_wish_frontend.dto.DietDTO;
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
    private List<Comment> comments;
    private NestedScrollView nestedScrollView;
    private String clientFirstName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("userId", 6L);
        editor.putString("accessToken", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGF5bWEyMDEwMkBnbWFpbC5tYSIsImlhdCI6MTcwNTE4MDE5NiwiZXhwIjoxNzA1MjY2NTk2fQ.6F-wVZL4avcj4lRGYGDBZ91jfaC2JT_QsPTsSiNoAwY");
        editor.putBoolean("isCook", false);
        editor.apply();

        final RatingBar ratingBar =(RatingBar) findViewById(R.id.ratingBar);
        Button btnRating = findViewById(R.id.btnAddReview);
        //final  TextView RatingResult = findViewById(R.id.textViewActualRating);
        // double ratingValue = (double) ratingBar.getRating();

        Boolean isCook = preferences.getBoolean("isCook", false);

        setContentView(R.layout.activity_search_profile);

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

        comments = loadCommentsFromPrefs();
        commentAdapter = new CommentAdapter(this, comments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComments.setAdapter(commentAdapter);
        TextView textViewBioTitle = findViewById(R.id.textViewBio);

        if (isCook) {
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
        } else {
            textViewBioTitle.setVisibility(View.INVISIBLE);
            textViewBioContent.setVisibility(View.INVISIBLE);
            getClient(new ApiClientCallback() {
                @Override
                public void onClientReceived(Client client) {
                    clientFirstName = client.getFirstName();
                    textViewFirstName.setText(client.getFirstName());
                    textViewLastName.setText(client.getLastName());
                    textViewAddress.setText(client.getAddress().getAddress());
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        btnRating.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // ratingValue.setText("yourRating is "+ ratingBar.getRating());
//
//                //RatingResult.setText("yourRating is " + ratingInt);
//            }
//        });
//        ratingBar.setRating((float) ratingValue);

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addComment();
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
            DietDTO dietDTO = new DietDTO();
            ChefDTO chefDTO = new ChefDTO();
            Address address = new Address();

            chefDTO.setFirstName(newFirstName);
            chefDTO.setLastName(newLastName);
            chefDTO.setAddress(address);
            chefDTO.setPhoneNumber(newPhoneNumber);
            chefDTO.setAllergies(newAllergy);
            dietDTO.setTitle(newDiet);
            chefDTO.setDietDTO(dietDTO);
            if (isCook) {
                chefDTO.setBio(newBio);
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

    private void getClient(ApiClientCallback apiClientCallback) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);
        String authToken = preferences.getString("accessToken", "");
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<Client> call = apiService.getClientById("Bearer " + authToken, userId);
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
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);
        String authToken = preferences.getString("accessToken", "");

        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();

        Call<Chef> call = apiService.getChefById("Bearer " + authToken, userId);

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
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);
        String authToken = preferences.getString("accessToken", "");

        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();

        Call<ResponseBody> call = apiService.getClientProfile("Bearer " + authToken, userId);

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
        String username = clientFirstName;
        String commentContent = editTextComment.getText().toString().trim();
        if (!commentContent.isEmpty()) {
            comments.add(new Comment(username, commentContent));
            commentAdapter.notifyDataSetChanged();
            editTextComment.setText("");
            scrollToBottom();
        }
    }
    private List<Comment> loadCommentsFromPrefs() {
        CommentAdapter commentAdapter = new CommentAdapter(this, new ArrayList<>());
        return commentAdapter.getCommentsFromPrefs();
    }
    private void scrollToBottom() {
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });}
}