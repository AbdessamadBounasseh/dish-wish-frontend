package uit.ensak.dish_wish_frontend.Profil;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Command.MapsChefActivity;
import uit.ensak.dish_wish_frontend.Command.UpdateActivity;
import uit.ensak.dish_wish_frontend.Models.Address;
import uit.ensak.dish_wish_frontend.Models.City;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.dto.ChefDTO;
import uit.ensak.dish_wish_frontend.dto.DietDTO;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;
import uit.ensak.dish_wish_frontend.service.RetrofitClient;

public class change_profile extends AppCompatActivity {

    private EditText editTextNewFirstName, editTextNewLastName, editTextNewAddress,editTextNewPosition, editTextNewPhoneNumber, editTextNewAllergie,editTextNewBio;
    private TextView textViewBioTitle;
    private Button btnSubmit,btnPosition;
    Spinner spinnerDiet,spinnerCity;
    private String currentFirstName, currentLastName, currentAddress,currentPosition,currentPhoneNumber,currentBio,currentAllergie,currentDiet,currentCity;

    private String newDiet, newCity;

    private static final int REQUEST_PICK_IMAGE = 102;
    private ImageView profileImageView;
    private Bitmap imageBitmap;

    private Button chooseLocationButton;
    private ActivityResultLauncher<Intent> mapsActivityLauncher;
    private static final int Maps_REQUEST_CODE = 123;

    private String accessToken;
    private long userId;
    private Boolean isCook;


    private Bitmap resizeBitmap(Bitmap originalBitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        accessToken = preferences.getString("accessToken", "");
        userId = preferences.getLong("userId", 0);
        isCook = preferences.getBoolean("isCook", false);

        getClientProfile();

        editTextNewBio = findViewById(R.id.editTextNewBio);
        textViewBioTitle = findViewById(R.id.textViewBio);

        if (isCook) {
            textViewBioTitle.setVisibility(View.VISIBLE);
            editTextNewBio.setVisibility(View.VISIBLE);
        }
        currentDiet = getIntent().getStringExtra("CURRENT_DIET");
        currentCity =getIntent().getStringExtra("CURRENT_CITY");
        spinnerDiet = findViewById(R.id.spinnerDiet);
        spinnerCity = findViewById(R.id.spinnerCity);
        ArrayAdapter<CharSequence> adapterDiet = ArrayAdapter.createFromResource(this, R.array.diet_array, android.R.layout.simple_spinner_item);
        adapterDiet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiet.setAdapter(adapterDiet);

        ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(this, R.array.city, android.R.layout.simple_spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapterCity);

        int indexDiet = adapterDiet.getPosition(currentDiet);
        int indexCity = adapterCity.getPosition(currentCity);
        if (indexDiet != -1) {
            spinnerDiet.setSelection(indexDiet);
        }
        if (indexCity != -1) {
            spinnerCity.setSelection(indexCity);
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

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                newCity = parentView.getItemAtPosition(position).toString();
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
        editTextNewPosition= findViewById(R.id.editTextPosition);
        btnSubmit = findViewById(R.id.btnsubmit);
        btnPosition= findViewById(R.id.btnPosition);
        currentFirstName = getIntent().getStringExtra("CURRENT_FIRST_NAME");
        currentLastName = getIntent().getStringExtra("CURRENT_LAST_NAME");
        currentPosition = getIntent().getStringExtra("CURRENT_POSITION");
        currentAddress = getIntent().getStringExtra("CURRENT_ADDRESS");
        currentAllergie = getIntent().getStringExtra("CURRENT_ALLERGY");
        currentPhoneNumber = getIntent().getStringExtra("CURRENT_PHONE_NUMBER");

        if (isCook) {
            currentBio = getIntent().getStringExtra("CURRENT_BIO");
        }
        editTextNewFirstName.setText(currentFirstName);
        editTextNewLastName.setText(currentLastName);
        editTextNewAddress.setText(currentAddress);
        editTextNewPosition.setText(currentPosition);
        editTextNewPhoneNumber.setText(currentPhoneNumber);

        editTextNewAllergie.setText(currentAllergie);
        if (isCook) {
            editTextNewBio.setText(currentBio);
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
       btnPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                String newFirstName = editTextNewFirstName.getText().toString();
                String newLastName = editTextNewLastName.getText().toString();
                String newAddress = editTextNewAddress.getText().toString();
                String newPosition = editTextNewPosition.getText().toString();
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
                if (newCity.trim().isEmpty()) {
                    Toast.makeText(change_profile.this, "City is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newLastName.trim().isEmpty()) {
                    Toast.makeText(change_profile.this, "Last name is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPhoneNumber.trim().isEmpty()) {
                    Toast.makeText(change_profile.this, "Phone Number is required", Toast.LENGTH_SHORT).show();
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

                DietDTO dietDTO = new DietDTO();
                ChefDTO chefDTO = new ChefDTO();
                Address address = new Address();
                City city = new City();
                chefDTO.setId(userId);

                chefDTO.setFirstName(newFirstName);
                chefDTO.setLastName(newLastName);
                city.setName(newCity);
                address.setAddress(newAddress);
                address.setCity(city);
                address.setPosition(newPosition);

                chefDTO.setAddress(address);
                chefDTO.setPhoneNumber(newPhoneNumber);
                chefDTO.setAllergies(newAllergie);

                dietDTO.setTitle(newDiet);

                chefDTO.setDietDTO(dietDTO);
                if (isCook) {
                    chefDTO.setBio(newBio);
                }
                updateUser(chefDTO, imageBitmap);
                finish();
            }
        });

        mapsActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        LatLng selectedLatLng = data.getParcelableExtra("selected_location");
                        EditText addressEditTe = findViewById(R.id.editTextPosition);
                        addressEditTe.setText(selectedLatLng.latitude + "," + selectedLatLng.longitude);
                    }
                }
        );



        chooseLocationButton = findViewById(R.id.btnPosition);
        chooseLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(change_profile.this, MapsChefActivity.class);
                mapsActivityLauncher.launch(intent);
            }
        });



    }


    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                        pickImageFromGallery();

            }
        });
        builder.show();
    }

    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Maps_REQUEST_CODE) {
                // Handle the result from MapsChefActivity
                if (data != null) {
                    LatLng selectedLatLng = data.getParcelableExtra("selected_location");
                    // Use selectedLatLng as needed
                    EditText addressEditText = findViewById(R.id.location);
                    addressEditText.setText(selectedLatLng.latitude + "," + selectedLatLng.longitude);
                }
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                // Handle the result from image picking
                if (data != null) {
                    Uri selectedImage = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        imageBitmap = resizeBitmap(imageBitmap, 92, 92);
                        profileImageView.setImageBitmap(getRoundedBitmap(imageBitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    private void getClientProfile() {
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<ResponseBody> call = apiService.getClientProfile("Bearer " + accessToken, userId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Utiliser BitmapFactory.decodeStream pour créer un Bitmap directement à partir du flux
                        Bitmap newProfileImageBitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        imageBitmap = newProfileImageBitmap;
                        imageBitmap = resizeBitmap(imageBitmap, 92, 92);
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
    private void updateUser(ChefDTO chefDTO, Bitmap imageBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo", "photo.jpg", requestFile);

        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<Client> call = apiService.updateClient("Bearer " + accessToken, userId, chefDTO, photoPart);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    Client client = response.body();
                    Intent intent = new Intent(change_profile.this, view_profile.class);
                    Toast.makeText(change_profile.this, "User updated succesfully", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(change_profile.this, "Error during updating", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(change_profile.this, "Unavailable Server", Toast.LENGTH_SHORT).show();
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
        String adressePattern = "[\\dA-Za-z ,.-:]{5,50}";
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

