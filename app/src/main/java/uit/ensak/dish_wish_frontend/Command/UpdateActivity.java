package uit.ensak.dish_wish_frontend.Command;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.shared.RetrofitClient;

public class UpdateActivity extends AppCompatActivity {

    private Button chooseLocationButton;
    private Button pickTime;
    private Button pickDate;
    private Long CommandID;
    private ImageView arrow;

    private static final int MAPS_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        chooseLocationButton = findViewById(R.id.ChooseLocation);
        Button updateCommand = findViewById(R.id.update);
        pickTime = findViewById(R.id.pickTime);
        pickDate = findViewById(R.id.pickDate);
        EditText DelivaryTime = findViewById(R.id.deliveryTime);
        EditText DelivaryDate = findViewById(R.id.deliveryDate);

        // Retrieve data from the intent
        Intent intent = getIntent();
        long receivedId = intent.getLongExtra("id", -1);
        CommandID = receivedId;
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String serving = intent.getStringExtra("serving");
        String deadlineString = intent.getStringExtra("deadline");
        String price = intent.getStringExtra("price");
        String address = intent.getStringExtra("address");

        deadlineString = deadlineString.replace("/", " ");

        String[] parts = deadlineString.split(" ");

        EditText titleEditText = findViewById(R.id.title);
        EditText descriptionEditText = findViewById(R.id.Description);
        EditText servingEditText = findViewById(R.id.serving);
        EditText deliveryDateEditText = findViewById(R.id.deliveryDate);
        EditText deliveryTimeEditText = findViewById(R.id.deliveryTime);
        EditText priceEditText = findViewById(R.id.price);
        EditText addressEditText = findViewById(R.id.location);

        titleEditText.setText(title);
        descriptionEditText.setText(description);
        servingEditText.setText(serving);
        deliveryDateEditText.setText(parts[0]);
        deliveryTimeEditText.setText(parts[1]);
        priceEditText.setText(price);
        addressEditText.setText(address);



        ActivityResultLauncher<Intent> mapsActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Get the selected location (LatLng) from the result
                        Intent data = result.getData();
                        LatLng selectedLatLng = data.getParcelableExtra("selected_location");

                        // Now you can use the selectedLatLng as needed
                        // For example, set it to an EditText
                        EditText addressEditTe = findViewById(R.id.location);
                        addressEditTe.setText(selectedLatLng.latitude + "," + selectedLatLng.longitude);
                    }
                }
        );

        chooseLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this, MapsChefActivity.class);
                mapsActivityLauncher.launch(intent);
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

        updateCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateCommand();
            }
        });

        arrow = findViewById(R.id.animation);
        if (arrow != null) {
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


    }


    private void UpdateCommand() {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWluZWVrOEBnbWFpbC5jb20iLCJpYXQiOjE3MDQ5NjgzMzUsImV4cCI6MTcwNTA1NDczNX0.492foEuc2CyhQbIWrMT8xSe--2n1egUoV_mu-aVuuG4";

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
        String deadline =  delivaryDate + "/" + delivaryTime;
        Log.d("deadline", deadline);

        EditText Price = findViewById(R.id.price);
        String price = Price.getText().toString();



        if (isValidCommand(title, description, serving, location, delivaryDate,delivaryTime, price)) {

            // Create a Command object
            Command command = new Command();
            command.setTitle(title);
            command.setDescription(description);
            command.setServing(serving);
            command.setAddress(location);
            command.setDeadline(deadline);
            command.setPrice(price);

           /* // Retrieve client ID from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("your_shared_prefs_name", Context.MODE_PRIVATE);
            Long clientId = sharedPreferences.getLong("client_id_key", 3L);

            Client client = new Client();
            client.setId(clientId);
            command.setClient(client);*/


            // Mocking Client IDs
            Client client = new Client();
            client.setId(2L);
         //  client.setRole("CLIENT");
            command.setClient(client);
            command.setStatus("IN_PROGRESS");

            ApiService apiService = RetrofitClient.getApiService();
           // Call<Command> call = apiService.updateCommand("Bearer " + accessToken,CommandID, command);


//            call.enqueue(new Callback<Command>() {
//                @Override
//                public void onResponse(Call<Command> call, Response<Command> response) {
//                    if (response.isSuccessful()) {
//                        Toast.makeText(getApplicationContext(), "Command Updated", Toast.LENGTH_LONG).show();
//                        clearFields();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Something went wrong, please try again", Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Command> call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), "Something went wrong, please try again", Toast.LENGTH_LONG).show();
//                }
//            });
        }
    }



    private boolean isValidCommand(String title, String description, String serving, String address, String delivaryDate, String delivaryTime, String price) {
        boolean allFieldsFilled = !title.isEmpty() && !description.isEmpty() && !serving.isEmpty() && !address.isEmpty() && !delivaryDate.isEmpty() && !price.isEmpty() && !delivaryTime.isEmpty();
        if (!allFieldsFilled) {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
            return false;
        }

        boolean isServingValid = isValidServing(serving);
        boolean isDateValid = isValidDate(delivaryDate,delivaryTime);
        boolean isPriceValid = isValidPrice(price);

        return isServingValid && isPriceValid && isDateValid;
    }

    private boolean isValidServing(String serving) {
        try {
            double value = Double.parseDouble(serving);
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(),"Please enter a valid number as a portion",Toast.LENGTH_LONG).show();
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


}