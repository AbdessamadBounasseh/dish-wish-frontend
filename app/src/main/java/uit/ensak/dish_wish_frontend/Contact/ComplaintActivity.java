package uit.ensak.dish_wish_frontend.Contact;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import uit.ensak.dish_wish_frontend.R;

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
        complaintLayout = findViewById(R.id.complaint); // Change to the actual ID of TextInputLayout
        complaint = complaintLayout.getEditText(); // Get the EditText from the TextInputLayout

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
