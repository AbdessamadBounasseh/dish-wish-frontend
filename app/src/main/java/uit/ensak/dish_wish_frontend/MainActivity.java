package uit.ensak.dish_wish_frontend;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example: Display a simple text message
        TextView textView = findViewById(R.id.textView);
        textView.setText("Hello, Android!");
    }
}
