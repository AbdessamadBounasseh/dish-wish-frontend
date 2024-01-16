package uit.ensak.dish_wish_frontend.Authentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Command.MapsHomeActivity;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.service.AuthenticationService;
import uit.ensak.dish_wish_frontend.service.RetrofitClient;

public class ForgotPasswordActivity extends AppCompatActivity {
    ImageView back;
    Button send;
    EditText etemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back=findViewById(R.id.icon_24_bac);

        setContentView(R.layout.activity_forgot_password);
        back=findViewById(R.id.icon_24_bac);
        etemail=findViewById(R.id.verify);
        send=findViewById(R.id.sign);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etemail.getText().toString();
                handlesend(email);

            }
        });

    }
    private void handlesend(String email) {
        AuthenticationService authenticationService = RetrofitClient.getAuthenticationService();
        Call<ResponseBody> sendReponse = authenticationService.forgotPassword(email);
        sendReponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();
                Log.d("MyTag", "HTTP Status Code: " + statusCode);
                if (response.isSuccessful() && response.body() != null) {


                        Intent intent1 = new Intent(ForgotPasswordActivity.this, ResetEmailActivity.class);
                        startActivity(intent1);

                } else {
                    Log.d("MyTag", "Response is null or not successful");
                    Toast.makeText(ForgotPasswordActivity.this, "sending failed", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ForgotPasswordActivity.this, "Make sure you have an account first", Toast.LENGTH_LONG).show();

            }
        });

    }
}