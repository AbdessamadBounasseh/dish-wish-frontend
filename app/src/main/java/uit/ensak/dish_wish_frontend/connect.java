package uit.ensak.dish_wish_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                Intent intent1= new Intent(connect.this,createAcciunt.class);
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





        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailEditText= email.getText().toString();
                String passwordEditText = password.getText().toString();

                if (emailEditText.equals("") || passwordEditText.equals("")) {
                    Toast.makeText(connect.this, "All fields should be filled to sign in", Toast.LENGTH_LONG).show();
                } else {
                    if (isValidEmail(emailEditText)) {
                        boolean isLogged = db.checkuser(emailEditText, passwordEditText);
                        if (isLogged) {
                            Intent intent = new Intent(connect.this, become_cook.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(connect.this, "Email or password incorrect", Toast.LENGTH_LONG).show();
                        }
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

}