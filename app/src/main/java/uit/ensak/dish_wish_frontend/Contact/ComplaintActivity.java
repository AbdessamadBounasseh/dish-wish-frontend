package uit.ensak.dish_wish_frontend.Contact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Authentification.VerifyEmail;
import uit.ensak.dish_wish_frontend.Command.MapsHomeActivity;
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

        complaint = findViewById(R.id.complaint);
        String complaintString;
        complaintString = complaint.getText().toString();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complaintS = complaint.getText().toString();

                if (complaintS.isEmpty()) {
                    Toast.makeText(ComplaintActivity.this, "Please mention your problem so we can help you", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
