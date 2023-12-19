package uit.ensak.dish_wish_frontend;


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

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        final EditText passwordEditText = findViewById(R.id.editTextPassword);
        final TextInputEditText confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);

        // Set initial visibility toggle icons
        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
        confirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);

        // Set onTouchListener for password visibility toggle
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleVisibilityToggle(passwordEditText, event);
                return false;
            }
        });

        // Set onTouchListener for confirm password visibility toggle
        confirmPasswordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleVisibilityToggle(confirmPasswordEditText, event);
                return false;
            }
        });

        Button btnResetPassword = findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle reset password logic here
            }
        });
    }

    private void handleVisibilityToggle(EditText editText, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int rightDrawable = editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width();
            if (event.getRawX() >= rightDrawable) {
                // Toggle password visibility
                if (editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_on, 0);
                } else {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                }
            }
        }
    }
}
