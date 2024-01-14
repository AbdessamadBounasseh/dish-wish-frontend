package uit.ensak.dish_wish_frontend.Command;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.Models.Proposition;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.databinding.ActivityMapsHomeBinding;
import uit.ensak.dish_wish_frontend.filter_by_name_or_city;
//import uit.ensak.dish_wish_frontend.filter_by_name_or_city;

public class MapsHomeActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsHomeBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private Map<String, Proposition> markerPropositionMap = new HashMap<>();
    final ArrayList<Proposition> propositionList = new ArrayList<>();
    private Map<String, Command> markerCommandMap = new HashMap<>();
    final ArrayList<Command> commandList = new ArrayList<>();
    private LinearLayout mBottomSheetLayout;
    private BottomSheetBehavior sheetBehavior;
    private ImageView header_Arrow_Image;
    private Button chooseLocationButton;
    private Button pickTime;
    private Button pickDate;
    private Marker currentMarker;
    private ImageView arrow;
    private String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWluZWVrOEBnbWFpbC5jb20iLCJpYXQiOjE3MDUyMzUzMDAsImV4cCI6MTcwNTMyMTcwMH0.ZhulnzT4vzPRdyjglYUFaWtr06LW9W6p0edjzgizm_Q";
    private long associatedCommandId;
    private Command commandCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Load the FilterByNameOrCityFragment
        loadFilterByNameOrCityFragment();



    }

    private void loadFilterByNameOrCityFragment() {
        // Create a new instance of the fragment
        filter_by_name_or_city filterFragment = new filter_by_name_or_city();

        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin a new FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the existing content with the new fragment
        fragmentTransaction.replace(R.id.search_bar, filterFragment);

        // Commit the transaction
        fragmentTransaction.commit();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng startPosition = new LatLng(34.26101, -6.5802);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, 8.0f));

        retryRequest();
        retryCommandsRequest();

        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
        header_Arrow_Image = findViewById(R.id.bottom_sheet_arrow);
        chooseLocationButton = findViewById(R.id.ChooseLocation);
        Button sendCommandButton = findViewById(R.id.order);
        pickTime = findViewById(R.id.pickTime);
        pickDate = findViewById(R.id.pickDate);
        EditText DelivaryTime = findViewById(R.id.deliveryTime);
        EditText DelivaryDate = findViewById(R.id.deliveryDate);

        header_Arrow_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
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

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker picker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(9)
                        .setMinute(30)
                        .setTitleText("Select a deadline")
                        .build();

                picker.addOnCancelListener(dialogInterface -> {
                    Log.d("TimePicker", "Time picker cancelled");
                });

                picker.addOnPositiveButtonClickListener(r -> {
                    int hour = picker.getHour();
                    int minute = picker.getMinute();
                    String selectedTime = hour + ":" + minute;
                    DelivaryTime.setText(selectedTime);
                    Log.d("TimePicker", selectedTime);
                });

                picker.addOnDismissListener(dialogInterface -> {
                    Log.d("TimePicker", "Time picker dismissed");
                });

                picker.addOnNegativeButtonClickListener(dialogInterface -> {
                    Log.d("TimePicker", "Time picker failed");
                });

                picker.show(getSupportFragmentManager(), TAG);

            }
        });

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                datePicker.addOnCancelListener(dialog -> {
                    Log.d("DatePicker", "Date picker cancelled");
                });

                datePicker.addOnDismissListener(dialog -> {
                    Log.d("DatePicker", "Date picker Dismiss");

                });

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(selection);
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    DelivaryDate.setText(selectedDate);
                    Log.d("DatePicker", selectedDate);
                });

                datePicker.addOnNegativeButtonClickListener(dialog -> {
                    Log.d("DatePicker", "Date picker failed");
                });

                datePicker.show(getSupportFragmentManager(), TAG);
            }
        });

        sendCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommandToBackend();
            }
        });


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
                if (currentMarker != null) {
                    currentMarker.remove();
                }

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("Command's Location")
                        .icon(BitmapFromVector(
                                getApplicationContext(),
                                R.drawable.dish));
                currentMarker = mMap.addMarker(markerOptions);
                updateBottomSheetWithLocation(tappedLatitude, tappedLongitude);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerTitle = marker.getTitle();

                // Check if the marker title starts with "Command Created"
                if (markerTitle != null && markerTitle.startsWith("Command Created")) {
                    // Extract the commandId from the title
                    String commandIdString = markerTitle.replace("Command Created", "");

                    // Parse the extracted string to a long
                    try {
                        long commandId = Long.parseLong(commandIdString);

                        // Call your API to get the command by id using commandId
                        ApiService apiService = RetrofitClient.getApiService();
                        Call<Command> call = apiService.getCommandById("Bearer " + accessToken, commandId);

                        call.enqueue(new Callback<Command>() {
                            @Override
                            public void onResponse(Call<Command> call, Response<Command> response) {
                                if (response.isSuccessful()) {
                                    Command command = response.body();
                                    showUpdatePopup(command);
                                } else {
                                    showErrorDialog();
                                }
                            }

                            @Override
                            public void onFailure(Call<Command> call, Throwable t) {
                                showErrorDialog();
                            }
                        });
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        showErrorDialog();
                    }
                } else if ("offer".equals(markerTitle)) {
                    // Handle the case for offer marker
                    Proposition associatedProposition = getPropositionFromMarker(marker);
                    showPropositionDetailsPopup(associatedProposition);
                }

                return true;
            }
        });


        boolean showSuccessDialog = getIntent().getBooleanExtra("showSuccessDialog", false);

        if (showSuccessDialog) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_offer_confirmed);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edittext);

            dialog.show();

            Button tryAgainButton = dialog.findViewById(R.id.Ok);
            tryAgainButton.setOnClickListener(v -> {
                dialog.dismiss();
            });
        }

    }


    private void addCommandMarkerToMap(String address, long commandId) {
        String[] latLngArray = address.split(",");
        if (latLngArray.length == 2) {
            double latitude = Double.parseDouble(latLngArray[0]);
            double longitude = Double.parseDouble(latLngArray[1]);

            LatLng location = new LatLng(latitude, longitude);

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title("Command Created" + commandId)
                    .icon(BitmapFromVector(
                            getApplicationContext(),
                            R.drawable.order_marker));
            mMap.addMarker(markerOptions);
        } else {
            Log.e("addCommandMarkerToMap", "Invalid address format: " + address);
        }
    }


    private void showPropositionDetailsPopup(Proposition associatedProposition) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_chef_details);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edittext);


        ApiService apiService = RetrofitClient.getApiService();
        Call<Double> call = apiService.getChefRatings(associatedProposition.getChef().getId(), "Bearer " + accessToken);

        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful()) {
                    RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                    double chefRatings = response.body();

                    float floatChefRatings = (float) chefRatings;
                    ratingBar.setRating(floatChefRatings);
                } else {
                    showCustomPopup();
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                showCustomPopup();
            }
        });

        TextView chefFirstName = dialog.findViewById(R.id.firstname);
        TextView chefLastName = dialog.findViewById(R.id.lastname);
        TextView price = dialog.findViewById(R.id.price);
        TextView delivary = dialog.findViewById(R.id.delivary);

        chefFirstName.setText(associatedProposition.getChef().getFirstName());
        chefLastName.setText(associatedProposition.getChef().getLastName());
        float chefProposition = associatedProposition.getLastChefProposition();
        String chefPropositionString = String.valueOf(chefProposition);
        price.setText(chefPropositionString + " DH");
        delivary.setText(associatedProposition.getCommand().getDeadline());


        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.y = (int) getResources().getDisplayMetrics().density * 20;
            window.setAttributes(layoutParams);
        }
        dialog.show();

        arrow = dialog.findViewById(R.id.animation);
        if (arrow != null) {
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        Button chooseCook = dialog.findViewById(R.id.cook);
        if (chooseCook != null) {
            chooseCook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapsHomeActivity.this, FinalizeOrderActivity.class);
                    // Pass associatedCommand data to the new intent
                    intent.putExtra("CommandId", associatedProposition.getCommand().getId());
                    intent.putExtra("ChefId", associatedProposition.getChef().getId());
                    intent.putExtra("price", associatedProposition.getLastChefProposition());
                    intent.putExtra("PropositionId", associatedProposition.getId());
                    intent.putExtra("delivary", associatedProposition.getCommand().getDeadline());
                    intent.putExtra("description", associatedProposition.getChef().getFirstName());
                    intent.putExtra("serving", associatedProposition.getChef().getLastName());
                    startActivity(intent);
                }
            });
        }
    }

    private void retryRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                ApiService apiService = RetrofitClient.getApiService();
                Call<List<Proposition>> call = apiService.getPropositionsByClientId("Bearer " + accessToken,1L);

                call.enqueue(new Callback<List<Proposition>>() {
                    @Override
                    public void onResponse(Call<List<Proposition>> call, Response<List<Proposition>> response) {
                        if (response.isSuccessful()) {
                            List<Proposition> receivedPropositions = response.body();
                            if (receivedPropositions != null) {
                                for (Proposition proposition : receivedPropositions) {
                                    propositionList.add(proposition);
                                    addMarkersToMap(propositionList);
                                }
                            }
                        } else {
                            showCustomPopup();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Proposition>> call, Throwable t) {
                        showCustomPopup();
                    }
                });
            }
        }).start();
    }

    private void retryCommandsRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiService apiService = RetrofitClient.getApiService();
                Call<List<Command>> call = apiService.getCommandsByClientId("Bearer " + accessToken, 2L);

                call.enqueue(new Callback<List<Command>>() {
                    @Override
                    public void onResponse(Call<List<Command>> call, Response<List<Command>> response) {
                        if (response.isSuccessful()) {
                            List<Command> receivedCommands = response.body();
                            if (receivedCommands != null) {
                                for (Command command : receivedCommands) {
                                    commandList.add(command);
                                    addCommandMarkersToMap(commandList);
                                }
                            }
                        } else {
                            showCustomPopup();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Command>> call, Throwable t) {
                        showCustomPopup();
                    }
                });
            }
        }).start();
    }


    private void showCustomPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_command_failed);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edittext);
        dialog.show();
        Button tryAgainButton = dialog.findViewById(R.id.tryagain);
        tryAgainButton.setOnClickListener(v -> {
            retryRequest();
            dialog.dismiss();
        });
    }

    private void showUpdatePopup(Command associatedCommand) {
        if (associatedCommand != null) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_update_command);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edittext);

            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.y = (int) getResources().getDisplayMetrics().density * 20;
                window.setAttributes(layoutParams);
            }


            String address = associatedCommand.getAddress();
            String[] latLng = address.split(",");

            if (latLng.length == 2) {
                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);

                // Perform reverse geocoding to get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        String locationName = addresses.get(0).getLocality();
                        TextView location = dialog.findViewById(R.id.location);
                        location.setText(locationName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            LottieAnimationView allergieAnimationView = dialog.findViewById(R.id.allergie);

            if (associatedCommand.getAllergie()) {
                allergieAnimationView.setVisibility(View.VISIBLE);
            } else {
                allergieAnimationView.setVisibility(View.GONE);
            }

            TextView title = dialog.findViewById(R.id.title);
            TextView description = dialog.findViewById(R.id.Description);
            TextView serving = dialog.findViewById(R.id.serving);
            TextView delivary = dialog.findViewById(R.id.delivary);
            TextView price = dialog.findViewById(R.id.price);

            title.setText(associatedCommand.getTitle());
            description.setText(associatedCommand.getDescription());
            serving.setText(associatedCommand.getServing());
            delivary.setText(associatedCommand.getDeadline());
            price.setText(associatedCommand.getPrice() + "DH");

            dialog.show();

            arrow = dialog.findViewById(R.id.animation);
            if (arrow != null) {
                arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

            Button updateButton = dialog.findViewById(R.id.update);
            if (updateButton != null) {
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MapsHomeActivity.this, UpdateActivity.class);
                        // Pass associatedCommand data to the new intent
                        intent.putExtra("id", associatedCommand.getId());
                        intent.putExtra("title", associatedCommand.getTitle());
                        intent.putExtra("description", associatedCommand.getDescription());
                        intent.putExtra("serving", associatedCommand.getServing());
                        intent.putExtra("deadline", associatedCommand.getDeadline());
                        intent.putExtra("price", associatedCommand.getPrice());
                        intent.putExtra("address", associatedCommand.getAddress());

                        // Start the new activity
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void addMarkersToMap(List<Proposition> propositionList) {
        if (mMap != null && propositionList != null) {
            for (Proposition proposition : propositionList) {
                Chef chef = proposition.getChef();

                if (chef != null && chef.getAddress() != null) {
                    String[] latLng = chef.getAddress().split(",");
                    if (latLng.length == 2) {
                        double latitude = Double.parseDouble(latLng[0]);
                        double longitude = Double.parseDouble(latLng[1]);

                        LatLng location = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(location)
                                .title("offer")
                                .icon(BitmapFromVector(getApplicationContext(), R.drawable.chef));
                        Marker marker = mMap.addMarker(markerOptions);
                        linkMarkerToProposition(marker, proposition);
                    }
                }
            }
        }
    }


    private void linkMarkerToProposition(Marker marker, Proposition proposition) {
        markerPropositionMap.put(marker.getId(), proposition);
    }

    private Proposition getPropositionFromMarker(Marker marker) {
        return markerPropositionMap.get(marker.getId());
    }


    private void addCommandMarkersToMap(List<Command> commandList) {
        if (mMap != null && commandList != null) {
            for (Command command : commandList) {
                String[] latLng = command.getAddress().split(",");

                if (latLng != null && latLng.length == 2) {
                    double latitude = Double.parseDouble(latLng[0]);
                    double longitude = Double.parseDouble(latLng[1]);

                    LatLng location = new LatLng(latitude, longitude);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(location)
                            .title("Command Created" + command.getId())
                            .icon(BitmapFromVector(getApplicationContext(), R.drawable.order_marker));
                    Marker marker = mMap.addMarker(markerOptions);
                    linkMarkerToCommand(marker, command);
                }
            }
        }
    }

    private void linkMarkerToCommand(Marker marker, Command command) {
        markerCommandMap.put(marker.getId(), command);
    }

    private Command getCommandFromMarker(Marker marker) {
        return markerCommandMap.get(marker.getId());
    }



    //method to change the marker's icon
    private BitmapDescriptor
    BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void updateBottomSheetWithLocation(double latitude, double longitude) {
        String locationString = latitude + "," + longitude;

        if (mBottomSheetLayout != null) {
            TextView locationTextView = mBottomSheetLayout.findViewById(R.id.location);
            if (locationTextView != null) {
                locationTextView.setText(locationString);
            }
        }
    }

    private void sendCommandToBackend() {

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
        EditText DelivaryTime = findViewById(R.id.deliveryTime);
        String delivaryTime = DelivaryTime.getText().toString();
        String deadline = delivaryDate + "/" + delivaryTime;
        Log.d("deadline", deadline);

        EditText Price = findViewById(R.id.price);
        String price = Price.getText().toString();


        if (isValidCommand(title, description, serving, location, delivaryDate, delivaryTime, price)) {

            // Assign data to command
            Command command = new Command();
            command.setTitle(title);
            command.setDescription(description);
            command.setServing(serving);
            command.setAddress(location);
            command.setDeadline(deadline);
            command.setPrice(price);

            Switch allergiesSwitch = findViewById(R.id.allergies);

            boolean isAllergieChecked = allergiesSwitch.isChecked();
            Log.d("Switch State=", "" + isAllergieChecked);
            command.setAllergie(isAllergieChecked);

            String[] latLng = location.split(",");

            if (latLng.length == 2) {
                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);

                // Perform reverse geocoding to get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        String locationName = addresses.get(0).getLocality();
                        String city = locationName;
//                      command.setCity(city);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

           /* // Retrieve client ID from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("your_shared_prefs_name", Context.MODE_PRIVATE);
            Long clientId = sharedPreferences.getLong("client_id_key", 3L);

            Client client = new Client();
            client.setId(clientId);
            command.setClient(client);*/


            // Mocking Client IDs
            Client client = new Client();
            client.setId(2L);
            client.setRole("CLIENT");
            command.setClient(client);
            command.setStatus("IN_PROGRESS");

            ApiService apiService = RetrofitClient.getApiService();
            Call<Command> call = apiService.createCommand("Bearer " + accessToken, command);


            call.enqueue(new Callback<Command>() {
                @Override
                public void onResponse(Call<Command> call, Response<Command> response) {
                    if (response.isSuccessful()) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        associatedCommandId = response.body().getId();
                        addCommandMarkerToMap(response.body().getAddress(),response.body().getId());
                        showSuccessDialog();
                        clearFields();
                    } else {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        showErrorDialog();
                    }
                }

                @Override
                public void onFailure(Call<Command> call, Throwable t) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showErrorDialog();
                }
            });
        }
    }




    private void clearFields() {
        EditText titleEditText = findViewById(R.id.title);
        titleEditText.getText().clear();

        EditText descriptionEditText = findViewById(R.id.Description);
        descriptionEditText.getText().clear();

        EditText servingEditText = findViewById(R.id.serving);
        servingEditText.getText().clear();

        EditText locationEditText = findViewById(R.id.location);
        locationEditText.getText().clear();

        EditText deliveryDateEditText = findViewById(R.id.deliveryDate);
        deliveryDateEditText.getText().clear();

        EditText deliveryTimeEditText = findViewById(R.id.deliveryTime);
        deliveryTimeEditText.getText().clear();

        EditText priceEditText = findViewById(R.id.price);
        priceEditText.getText().clear();
    }


    private void showSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_command_created);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edittext);
        dialog.show();
        Button tryAgainButton = dialog.findViewById(R.id.Ok);
        tryAgainButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    private void showErrorDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_command_failed);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edittext);
        dialog.show();
        Button tryAgainButton = dialog.findViewById(R.id.tryagain);
        tryAgainButton.setOnClickListener(v -> {
            dialog.dismiss();
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
    }

    private boolean isValidCommand(String title, String description, String serving, String address, String delivaryDate, String delivaryTime, String price) {
        boolean allFieldsFilled = !title.isEmpty() && !description.isEmpty() && !serving.isEmpty() && !address.isEmpty() && !delivaryDate.isEmpty() && !price.isEmpty() && !delivaryTime.isEmpty();
        if (!allFieldsFilled) {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
            return false;
        }

        boolean isServingValid = isValidServing(serving);
        boolean isDateValid = isValidDate(delivaryDate, delivaryTime);
        boolean isPriceValid = isValidPrice(price);

        return isServingValid && isPriceValid && isDateValid;
    }

    private boolean isValidServing(String serving) {
        try {
            double value = Double.parseDouble(serving);
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Please enter a valid number as a portion", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean isValidDate(String dateString, String timeString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try {
            dateFormat.parse(dateString);
            timeFormat.parse(timeString);
            return true;
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Please enter a valid date and time", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean isValidPrice(String price) {
        try {
            double value = Double.parseDouble(price);
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Please enter a valid number for the price", Toast.LENGTH_LONG).show();
            return false;
        }
    }




}