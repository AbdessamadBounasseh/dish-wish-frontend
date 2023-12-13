package uit.ensak.dish_wish_frontend.Command;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.databinding.ActivityMapsHomeBinding;

public class MapsHomeActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsHomeBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001; // You can use any number here


    private LinearLayout mBottomSheetLayout;
    private BottomSheetBehavior sheetBehavior;
    private ImageView header_Arrow_Image;
    private Button chooseLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
        header_Arrow_Image = findViewById(R.id.bottom_sheet_arrow);
        chooseLocationButton = findViewById(R.id.ChooseLocation);

        header_Arrow_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                header_Arrow_Image.setRotation(slideOffset * 180);
            }
        });

        chooseLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        Button sendCommandButton = findViewById(R.id.order);
        sendCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommandToBackend();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permission
       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            mMap.setMyLocationEnabled(true);

            // Get the last known location of the user
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations, this can be null.
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 1.0f));
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }*/


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double tappedLatitude = latLng.latitude;
                double tappedLongitude = latLng.longitude;
                updateBottomSheetWithLocation(tappedLatitude, tappedLongitude);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void updateBottomSheetWithLocation(double latitude, double longitude) {
        // Concatenate latitude and longitude into a single string with a delimiter
        String locationString = latitude + "," + longitude;

        if (mBottomSheetLayout != null) {
            TextView locationTextView = mBottomSheetLayout.findViewById(R.id.location);
            if (locationTextView != null) {
                locationTextView.setText(locationString);
            }
        }
    }


    // Assuming this method is called when the button is clicked
    private void sendCommandToBackend() {

        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWluZWVrOEBnbWFpbC5jb20iLCJpYXQiOjE3MDI0NzczNDIsImV4cCI6NjE3MDI0NzczNDJ9.1gJfdcZqaMbs05zNFzUEXqwsfI0biR-3hRNFGVsShFw";
        //form fields
        EditText Title = findViewById(R.id.title);
        String title = Title.getText().toString();
        EditText Description = findViewById(R.id.Description);
        String description = Description.getText().toString();
        EditText Serving = findViewById(R.id.serving);
        String serving = Serving.getText().toString();
        EditText Location = findViewById(R.id.location);
        String location = Location.getText().toString();
        EditText DelivaryDate = findViewById(R.id.deliveryDate);
        String delivaryDate = DelivaryDate.getText().toString();
        EditText Price = findViewById(R.id.price);
        String price = Price.getText().toString();

        // Create a Command object
        Command command = new Command();
        command.setTitle(title);
        command.setDescription(description);
        command.setServing(serving);
        command.setAddress(location);
        command.setDeadline(delivaryDate);
        command.setPrice(price);

        // Mocking Chef and Client IDs
        Chef chef = new Chef();
        chef.setId(1L);
        command.setChef(chef);

        Client client = new Client();
        client.setId(11L);
        command.setClient(client);


        /*Gson gson = new Gson();
        String jsonRequest = gson.toJson(command);

        Log.d("Request Body", jsonRequest);*/


        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.createCommand("Bearer " + accessToken, command);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful response (command stored in the backend)
                    // Show success message or perform any necessary action
                } else {
                    // Handle unsuccessful response
                    // Extract error information from response if needed
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network errors or API call failure
            }
        });
    }



}