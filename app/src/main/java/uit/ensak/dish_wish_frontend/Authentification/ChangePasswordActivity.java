package uit.ensak.dish_wish_frontend.Authentification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import uit.ensak.dish_wish_frontend.R;

public class ChangePasswordActivity extends AppCompatActivity {
    ImageView back;
    Button change;
  EditText etpasswordchanged, etpassword, etpasswordconfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        etpassword = findViewById(R.id.verify);
        etpasswordchanged = findViewById(R.id.changed);
        etpasswordconfirm = findViewById(R.id.pass2);
        back=findViewById(R.id.icon_24_bac);
        change=findViewById(R.id.sign);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String passwordEditText = etpasswordchanged.getText().toString();
        String confirmpasswordEditText = etpasswordconfirm.getText().toString();
        String passwordEditTextlast = etpassword.getText().toString();
      change.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (!passwordEditText.equals(confirmpasswordEditText)) {
                  Toast.makeText(ChangePasswordActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
              }
              if (confirmpasswordEditText .equals("") || passwordEditText.equals("")||passwordEditTextlast.equals("")) {
                  Toast.makeText(ChangePasswordActivity.this, "All fields should be filled to sign in", Toast.LENGTH_LONG).show();
              }
          }
      });
    }
}