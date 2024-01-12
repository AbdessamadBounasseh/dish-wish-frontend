package uit.ensak.dish_wish_frontend.Authentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uit.ensak.dish_wish_frontend.R;

public class ResetEmailActivity extends AppCompatActivity {

    ImageView back;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME="mypref";
    private static final String KEY_EMAIL="email";
    TextView etemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_email);
        back=findViewById(R.id.icon_24_bac);
        etemail=findViewById(R.id.emailres);

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

    }
}



