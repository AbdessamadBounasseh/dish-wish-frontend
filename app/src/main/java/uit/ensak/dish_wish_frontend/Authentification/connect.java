package uit.ensak.dish_wish_frontend.Authentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;


import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Command.ApiService;
import uit.ensak.dish_wish_frontend.Command.MapsChefActivity;
import uit.ensak.dish_wish_frontend.Command.MapsHomeActivity;
import uit.ensak.dish_wish_frontend.Models.Auth.AuthenticationResponse;
import uit.ensak.dish_wish_frontend.Models.Auth.LoginPayload;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.Profil.ApiClientCallback;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;
import uit.ensak.dish_wish_frontend.service.RetrofitClient;
//import uit.ensak.dish_wish_frontend.Profil_Folder.become_cook;

public class connect extends AppCompatActivity {
    EditText email,password;
    boolean passwordvisible;
    CheckBox remember;
    Button signin;
    TextView newacc,forgot;
    DBHelper db;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        email=findViewById(R.id.email);
        password=findViewById(R.id.forg);
        remember=findViewById(R.id.checkbox);
        signin=findViewById(R.id.sign);
        newacc=findViewById(R.id.donthave);
        db= new DBHelper(this);
        back=findViewById(R.id.icon_24_bac);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        newacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(connect.this, CreateAccount.class);

                startActivity(intent1);
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=password.getRight()-password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=password.getSelectionEnd();
                        if(passwordvisible){
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds( 0,0,R.drawable.baseline_visibility_off_24,0 );
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordvisible=false;
                        }else{
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds( 0, 0, R.drawable.baseline_visibility_24,  0);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordvisible=true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        forgot=findViewById(R.id.forgpass);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4= new Intent(connect.this, ForgotPasswordActivity.class);
                startActivity(intent4);
            }
        });





        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailEditText= email.getText().toString();
                String passwordEditText = password.getText().toString();

                if (emailEditText.equals("") || passwordEditText.equals("")) {
                    Toast.makeText(connect.this, "All fields should be filled to sign in", Toast.LENGTH_LONG).show();
                } else {
                    if (isValidEmail(emailEditText)) {
                        LoginPayload loginPayload = new LoginPayload();
                        loginPayload.setEmail(emailEditText);
                        loginPayload.setPassword(passwordEditText);
                        connect(loginPayload, emailEditText);


                    } else {
                        Toast.makeText(connect.this, "Please enter a valid email", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void connect(LoginPayload loginPayload,String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                ApiService apiService = RetrofitClient.getApiService();
                Call<AuthenticationResponse> call = apiService.authentification(loginPayload);

                call.enqueue(new Callback<AuthenticationResponse>() {
                    @Override
                    public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                        if (response.isSuccessful()) {
                            AuthenticationResponse authenticationResponse = response.body();

                            if (authenticationResponse != null) {
                                // Store tokens in SharedPreferences
                                SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("accessToken", authenticationResponse.getAccessToken());
                                editor.putString("refreshToken", authenticationResponse.getRefreshToken());
                                editor.apply();

                                getClient(email);
                            }
                            SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                            Boolean isCook = preferences.getBoolean("isCook", false);
                            Intent intent;
                            if (isCook) {
                                intent = new Intent(connect.this, MapsChefActivity.class);
                            }
                            else {
                                intent = new Intent(connect.this, MapsHomeActivity.class);
                            }
                            startActivity(intent);

                        } else {
                            // Handle failure
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                        // Handle failure
                    }
                });
            }
        }).start();
    }


    private void getClient(String email) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String authToken = preferences.getString("accessToken", "");
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<Client> call = apiService.getUserByEmail("Bearer " + authToken, email);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    Client client = response.body();

                    SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("userId", client.getId());

                    if (client.getRole() == "CHEF"){
                        editor.putBoolean("isCook", true);
                    }else {
                        editor.putBoolean("isCook", false);
                    }
                    editor.apply();
                    Boolean isCook = preferences.getBoolean("isCook", false);
                } else {
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}