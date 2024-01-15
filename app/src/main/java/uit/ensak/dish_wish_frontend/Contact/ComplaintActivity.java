package uit.ensak.dish_wish_frontend.Contact;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.service.ComplaintPayload;
import uit.ensak.dish_wish_frontend.service.ContactService;
import uit.ensak.dish_wish_frontend.service.RetrofitClient;

public class ComplaintActivity extends AppCompatActivity {
    ImageView back;
    Button send;
    TextInputLayout complaintLayout; // Change to TextInputLayout
    EditText complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        back = findViewById(R.id.icon_24_bac);
        send = findViewById(R.id.sign);
        complaintLayout = findViewById(R.id.complaint);
        complaint = complaintLayout.getEditText();
        String complaintString;
        complaintString = complaint.getText().toString();// Get the EditText from the TextInputLayout

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complaintContent = complaint.getText().toString();

                ComplaintPayload payload = new ComplaintPayload();
                payload.setContent(complaintContent);

                sendComplaint(payload);

                if (complaintContent.isEmpty()) {
                    Toast.makeText(ComplaintActivity.this, "Please mention your problem so we can help you", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendComplaint(ComplaintPayload payload) {
        ContactService contactservice = RetrofitClient.getContactService();
        Call<Void> sentComplaint = contactservice.sendComplaint(payload);
        sentComplaint.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                Log.d("MyTag", "HTTP Status Code: " + statusCode);
                if (response.isSuccessful()) {

                    Toast.makeText(ComplaintActivity.this, "Complaint sent", Toast.LENGTH_LONG).show();

                } else {
                    Log.d("MyTag", "Response is null or not successful");
                    Toast.makeText(ComplaintActivity.this, " Sending Complaint failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ComplaintActivity.this, "Error on sending complaint", Toast.LENGTH_LONG).show();
            }
        });
    }
}