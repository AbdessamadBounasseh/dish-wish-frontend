package uit.ensak.dish_wish_frontend.Authentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import uit.ensak.dish_wish_frontend.Command.MapsChefActivity;
import uit.ensak.dish_wish_frontend.Command.MapsHomeActivity;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.Profil_Folder.become_cook;

public class terms extends AppCompatActivity {
    Button start;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        setContentView(R.layout.activity_terms);
        start= findViewById(R.id.st);
        back=findViewById(R.id.icon_24_bac);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5= new Intent(terms.this, MapsChefActivity.class);
                startActivity(intent5);
            }
        });
    }
}