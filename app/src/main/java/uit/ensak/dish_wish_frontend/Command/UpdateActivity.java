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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.TimeZone;

import uit.ensak.dish_wish_frontend.R;

public class UpdateActivity extends AppCompatActivity {

    private Button chooseLocationButton;
    private Button pickTime;
    private Button pickDate;
    private static final int MAPS_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        chooseLocationButton = findViewById(R.id.ChooseLocation);
        Button sendCommandButton = findViewById(R.id.order);
        pickTime = findViewById(R.id.pickTime);
        pickDate = findViewById(R.id.pickDate);
        EditText DelivaryTime = findViewById(R.id.deliveryTime);
        EditText DelivaryDate = findViewById(R.id.deliveryDate);

        // Retrieve data from the intent
        Intent intent = getIntent();
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


    }


}