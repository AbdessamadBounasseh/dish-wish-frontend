package uit.ensak.dish_wish_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class createAcciunt extends AppCompatActivity {

    EditText etemail, etpassword,etpasswordconfirm;
    Button signup;
    TextView already;
    boolean passwordvisible;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acciunt);
        etemail = findViewById(R.id.email);
        etpassword = findViewById(R.id.forg);
        etpasswordconfirm = findViewById(R.id.pass2);
        signup = findViewById(R.id.sign);
        db = new DBHelper(this);
        already = findViewById(R.id.create_acco);
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(createAcciunt.this, connect.class);
                startActivity(intent);
            }
        });
        etpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=etpassword.getRight()-etpassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=etpassword.getSelectionEnd();
                        if(passwordvisible){
                            etpassword.setCompoundDrawablesRelativeWithIntrinsicBounds( 0,0,R.drawable.baseline_visibility_off_24,0 );
                            etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordvisible=false;
                        }else{
                            etpassword.setCompoundDrawablesRelativeWithIntrinsicBounds( 0, 0, R.drawable.baseline_visibility_24,  0);
                            etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordvisible=true;
                        }
                        etpassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        etpasswordconfirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=etpasswordconfirm.getRight()-etpasswordconfirm.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=etpassword.getSelectionEnd();
                        if(passwordvisible){
                            etpasswordconfirm.setCompoundDrawablesRelativeWithIntrinsicBounds( 0,0,R.drawable.baseline_visibility_off_24,0 );
                            etpasswordconfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordvisible=false;
                        }else{
                            etpasswordconfirm.setCompoundDrawablesRelativeWithIntrinsicBounds( 0, 0, R.drawable.baseline_visibility_24,  0);
                            etpasswordconfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordvisible=true;
                        }
                        etpassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password,confirmpassword;
                email=etemail.getText().toString();
                password=etpassword.getText().toString();
                confirmpassword=etpasswordconfirm.getText().toString();
                if(email.equals("")||password.equals("")||confirmpassword.equals("")){
                    Toast.makeText(createAcciunt.this,"all fields should be filled to sign up ",Toast.LENGTH_LONG).show();

                }else{
                    if (isValidEmail(email)) {
                        if(password.equals(confirmpassword)){
                            if(db.checkemail(email))
                            {
                                Toast.makeText(createAcciunt.this,"user already exists",Toast.LENGTH_LONG).show();
                                return;

                            }
                            boolean signupsuccess =db.insertData(email,password);
                            if(signupsuccess) {
                                Intent intent1= new Intent(createAcciunt.this,terms.class);
                                startActivity(intent1);

                            } else
                                Toast.makeText(createAcciunt.this,"failed to sign up",Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(createAcciunt.this,"the two passwords should be the same",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(createAcciunt.this, "Invalid email format", Toast.LENGTH_LONG).show();
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