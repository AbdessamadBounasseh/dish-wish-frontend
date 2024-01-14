package uit.ensak.dish_wish_frontend.Authentification;


import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import android.os.Bundle;

import uit.ensak.dish_wish_frontend.R;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        final EditText passwordEditText = findViewById(R.id.verify);
        final TextInputEditText confirmPasswordEditText = findViewById(R.id.changed);


        Button btnResetPassword = findViewById(R.id.sign);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle reset password logic here
            }
        });
    }

}