package uit.ensak.dish_wish_frontend.Profil;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;

public class change_profile extends AppCompatActivity {

    private EditText editTextNewFirstName, editTextNewLastName, editTextNewAddress, editTextNewPhoneNumber, editTextNewAllergie,editTextNewBio;
    private TextView textViewBioTitle;
    private Button btnSubmit;
    Spinner spinnerDiet;
    private String currentFirstName, currentLastName, currentAddress,currentPhoneNumber,currentBio,currentAllergie,currentDiet;

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

        getClientProfile();

        editTextNewBio = findViewById(R.id.editTextNewBio);
       textViewBioTitle = findViewById(R.id.textViewBio);

        if (isCook) {
            textViewBioTitle.setVisibility(View.VISIBLE);
            editTextNewBio.setVisibility(View.VISIBLE);
        }
        currentDiet = getIntent().getStringExtra("CURRENT_DIET");
        spinnerDiet = findViewById(R.id.spinnerDiet);
        ArrayAdapter<CharSequence> adapterDiet = ArrayAdapter.createFromResource(this, R.array.diet_array, android.R.layout.simple_spinner_item);
        adapterDiet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiet.setAdapter(adapterDiet);

        int index = adapterDiet.getPosition(currentDiet);
        if (index != -1) {
            spinnerDiet.setSelection(index);
        }


        spinnerDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                newDiet = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        profileImageView = findViewById(R.id.portrait_of);
        if (getIntent().hasExtra("IMAGE_BITMAP")) {
            imageBitmap = getIntent().getParcelableExtra("IMAGE_BITMAP");
            profileImageView.setImageBitmap(imageBitmap);
        }
        ImageView changePhotoImageView = findViewById(R.id.imageViewChangePhoto);
        changePhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourceDialog();
            }
        });
        editTextNewFirstName = findViewById(R.id.editTextNewFirstName);
        editTextNewLastName = findViewById(R.id.editTextNewLastName);
        editTextNewAddress = findViewById(R.id.editTextNewAddress);
        editTextNewPhoneNumber = findViewById(R.id.editTextNewPhoneNumber);
        editTextNewAllergie = findViewById(R.id.editTextNewAllergie);
        btnSubmit = findViewById(R.id.btnsubmit);
        currentFirstName = getIntent().getStringExtra("CURRENT_FIRST_NAME");
        currentLastName = getIntent().getStringExtra("CURRENT_LAST_NAME");
        currentAddress = getIntent().getStringExtra("CURRENT_ADDRESS");
        currentAllergie = getIntent().getStringExtra("CURRENT_ALLERGY");
        currentPhoneNumber = getIntent().getStringExtra("CURRENT_PHONE_NUMBER");

        if (isCook) {
            currentBio = getIntent().getStringExtra("CURRENT_BIO");
        }
        editTextNewFirstName.setText(currentFirstName);
        editTextNewLastName.setText(currentLastName);
        editTextNewAddress.setText(currentAddress);
        editTextNewPhoneNumber.setText(currentPhoneNumber);

        editTextNewAllergie.setText(currentAllergie);
        if (isCook) {
            editTextNewBio.setText(currentBio);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFirstName = editTextNewFirstName.getText().toString();
                String newLastName = editTextNewLastName.getText().toString();

                String newAddress = editTextNewAddress.getText().toString();
                String newPhoneNumber = editTextNewPhoneNumber.getText().toString();
                String newBio = "";
                if (isCook) {
                   newBio = editTextNewBio.getText().toString();
                }
                String newAllergie = editTextNewAllergie.getText().toString();
                if (newFirstName.trim().isEmpty()) {
                    Toast.makeText(change_profile.this, "First name is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newLastName.trim().isEmpty()) {
                    Toast.makeText(change_profile.this, "Last name is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidFirstName(newFirstName)) {
                    Toast.makeText(change_profile.this, "Invalid first name format", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidLastName(newLastName)) {
                    Toast.makeText(change_profile.this, "Invalid last name format", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidNumber(newPhoneNumber)) {
                    Toast.makeText(change_profile.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidAllergie(newAllergie)) {
                    Toast.makeText(change_profile.this, "Invalid allergy format", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidAdresse(newAddress)) {
                    Toast.makeText(change_profile.this, "Invalid address format", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isCook && !isValidBio(newBio)) {
                    Toast.makeText(change_profile.this, "Invalid bio format", Toast.LENGTH_SHORT).show();
                    return;
                }

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


    }
    private void getClientProfile() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);
        String authToken = preferences.getString("accessToken", "");

        ApiServiceProfile apiService = RetrofitClientProfile.getApiService();

        Call<ResponseBody> call = apiService.getClientProfile("Bearer " + authToken, userId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Utiliser BitmapFactory.decodeStream pour créer un Bitmap directement à partir du flux
                        Bitmap newProfileImageBitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        imageBitmap = newProfileImageBitmap;
                        profileImageView.setImageBitmap(getRoundedBitmap(newProfileImageBitmap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Gérer le cas où la réponse est vide ou le code de statut indique une erreur
                    Toast.makeText(change_profile.this, "Error during user profile fetching " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(change_profile.this, "Unavailable Sever " ,Toast.LENGTH_SHORT).show();
            }
        });

    }
    private boolean isValidNumber(String number) {
        String numberPattern = "\\d{10}";
        Pattern pattern = Pattern.compile(numberPattern);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
    private boolean isValidAllergie(String Allergy) {
        String AllergyPattern = "[\\dA-Za-z ]{0,40}";
        Pattern pattern = Pattern.compile(AllergyPattern);
        Matcher matcher = pattern.matcher(Allergy);
        return matcher.matches();
    }


    private static boolean isValidAdresse(String adresse) {
        String adressePattern = "[\\dA-Za-z ,.-:]+";
        Pattern pattern = Pattern.compile(adressePattern);
        Matcher matcher = pattern.matcher(adresse);
        return matcher.matches();
    }

    private static boolean isValidBio(String bio) {
        String bioPattern = "[A-Za-z ]+";
        Pattern pattern = Pattern.compile(bioPattern);
        Matcher matcher = pattern.matcher(bio);
        return matcher.matches();
    }
    private boolean isValidFirstName(String firstName) {
        String firstNamePattern = "[A-Za-z ]{4,30}";
        Pattern pattern = Pattern.compile(firstNamePattern);
        Matcher matcher = pattern.matcher(firstName);
        return matcher.matches();
    }

    private boolean isValidLastName(String lastName) {
        String lastNamePattern = "[A-Za-z ]{4,30}";
        Pattern pattern = Pattern.compile(lastNamePattern);
        Matcher matcher = pattern.matcher(lastName);
        return matcher.matches();
    }
}

