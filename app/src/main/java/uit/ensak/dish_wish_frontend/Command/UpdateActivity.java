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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.security.PrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.R;

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




    }


}