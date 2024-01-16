package uit.ensak.dish_wish_frontend.Authentification;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.service.AuthenticationService;
import uit.ensak.dish_wish_frontend.service.ResetPasswordRequest;
import uit.ensak.dish_wish_frontend.service.RetrofitClient;

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
                String edittextS= passwordEditText.getText().toString();
                String confirmstring=confirmPasswordEditText.getText().toString();
                Intent intent=getIntent();
                String code=intent.getStringExtra("code");
                handlereset(code,edittextS,confirmstring);

            }
        });
    }
    private void handlereset(String code, String password, String confirmpassword) {
        AuthenticationService authenticationService = RetrofitClient.getAuthenticationService();
        ResetPasswordRequest request=new ResetPasswordRequest();
        request.setNewPassword(password);
        request.setConfirmationPassword(confirmpassword);
        Call<Void> sendReponse = authenticationService.resetPassword(request,code);
        sendReponse.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                Log.d("MyTag", "HTTP Status Code: " + statusCode);
                if (response.isSuccessful() ) {


                    Intent intent1 = new Intent(ResetPasswordActivity.this, connect.class);
                    startActivity(intent1);

                } else {
                    Log.d("MyTag", "Response is null or not successful");
                    Toast.makeText(ResetPasswordActivity.this, "sending failed", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ResetPasswordActivity.this, "Make sure you have an account first", Toast.LENGTH_LONG).show();

            }
        });

    }
}