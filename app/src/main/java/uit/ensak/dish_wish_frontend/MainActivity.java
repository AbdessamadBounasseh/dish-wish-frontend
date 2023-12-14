package uit.ensak.dish_wish_frontend;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load the FilterByNameOrCityFragment
        loadFilterByNameOrCityFragment();
    }

    private void loadFilterByNameOrCityFragment() {
        // Create a new instance of the fragment
        uit.ensak.dish_wish_frontend.filter_by_name_or_city filterFragment = new uit.ensak.dish_wish_frontend.filter_by_name_or_city();

        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin a new FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the existing content with the new fragment
        fragmentTransaction.replace(R.id.search_bar_container, filterFragment);

        // Commit the transaction
        fragmentTransaction.commit();
    }
}
