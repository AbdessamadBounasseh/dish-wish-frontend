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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.service.AuthenticationService;
import uit.ensak.dish_wish_frontend.shared.RetrofitClient;

public class ResetEmailActivity extends AppCompatActivity {

    ImageView back;
    Button send;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME="mypref";
    private static final String KEY_EMAIL="email";
    TextView etemail;
    EditText verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_email);
        back=findViewById(R.id.icon_24_bac);
        etemail=findViewById(R.id.emailres);
        verify=findViewById(R.id.verify);
        send=findViewById(R.id.sign);
        sharedPreferences=getSharedPreferences(SHARED_PREFS_NAME,MODE_PRIVATE);
        String email=sharedPreferences.getString(KEY_EMAIL,null);
        if(email!=null){
            etemail.setText(email);
        }
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
                String code=verify.getText().toString();
                Intent intent= new Intent(ResetEmailActivity.this,ResetPasswordActivity.class);
                intent.putExtra("code",code);
                startActivity(intent);



            }
        });

    }

}



