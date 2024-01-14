package uit.ensak.dish_wish_frontend.Authentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.slf4j.LoggerFactory;

import java.io.IOException;

import ch.qos.logback.classic.Logger;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Command.MapsHomeActivity;
import uit.ensak.dish_wish_frontend.Models.Auth.AuthenticationResponse;
import uit.ensak.dish_wish_frontend.Models.Auth.RegisterRequest;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.service.AuthenticationService;
import uit.ensak.dish_wish_frontend.shared.RetrofitClient;

public class VerifyEmail extends AppCompatActivity {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(page_acceuil.class);

    LottieAnimationView lottie;
    ImageView back;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "mypref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CODE = "code";
    TextView etemail;
    EditText etcode;
    Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        lottie = findViewById(R.id.lottie);
        back = findViewById(R.id.icon_24_bac);
        etemail = findViewById(R.id.email);
        etcode = findViewById(R.id.verify);
        verify = findViewById(R.id.sign);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String code = sharedPreferences.getString(KEY_CODE, null);

            etemail.setText(email);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etcode.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_CODE, code);
                editor.apply();
                handleVerification(code);

            }
        });
    }

    private void handleVerification(String code) {
        AuthenticationService authenticationService = RetrofitClient.getAuthenticationService();
        Call<ResponseBody> verificationReponse = authenticationService.verifyEmail(code);
        verificationReponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();
                Log.d("MyTag", "HTTP Status Code: " + statusCode);
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String verificationResponse = response.body().string();
                        // Traitement du JSON ici
                        logger.info("success");
                        Intent intent1 = new Intent(VerifyEmail.this, MapsHomeActivity.class);
                        startActivity(intent1);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(VerifyEmail.this, "Failed to process response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("MyTag", "Response is null or not successful");
                    Toast.makeText(VerifyEmail.this, "Verification failed", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(VerifyEmail.this, "Verification failed", Toast.LENGTH_LONG).show();

            }
        });

    }

}
