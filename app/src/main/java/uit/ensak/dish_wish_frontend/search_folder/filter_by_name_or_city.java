package uit.ensak.dish_wish_frontend;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

//menu
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

// FilterByNameOrCityFragment.java
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.content.Intent;
import android.content.SharedPreferences;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uit.ensak.dish_wish_frontend.Profil.RetrofitClientProfile;
import uit.ensak.dish_wish_frontend.Profil.become_cook;
import uit.ensak.dish_wish_frontend.Profil.change_profile;
import uit.ensak.dish_wish_frontend.Profil.become_cook;
import uit.ensak.dish_wish_frontend.Profil.change_profile;
import uit.ensak.dish_wish_frontend.Profil.view_profile;
import uit.ensak.dish_wish_frontend.SearchResultsAdapter;
import uit.ensak.dish_wish_frontend.notification_folder.NotificationsChef;
import uit.ensak.dish_wish_frontend.notification_folder.NotificationsClient;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;

import android.content.SharedPreferences;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uit.ensak.dish_wish_frontend.SearchResult;


public class filter_by_name_or_city extends Fragment implements SearchResultsAdapter.OnItemClickListener {

    private SearchResultsAdapter searchResultsAdapter;  // Declare searchResultsAdapter as a class variable

    private boolean isCook;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter_by_name_or_city, container, false);

        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        isCook = preferences.getBoolean("isCook", false);

        EditText searchEditText = rootView.findViewById(R.id.searchEditText);
        RecyclerView searchResultsRecyclerView = rootView.findViewById(R.id.searchResultsRecyclerView);

        ImageView menuIcon = rootView.findViewById(R.id.menuIcon);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        ImageView notificationIcon = rootView.findViewById(R.id.notificationIcon);

        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event
                Intent intent;

                if (isCook) {
                    intent = new Intent(requireContext() , NotificationsChef.class);
                } else {
                    intent = new Intent(requireContext() , NotificationsClient.class);
                }
                startActivity(intent);
            }
        });

        searchResultsAdapter = new SearchResultsAdapter();
        searchResultsAdapter.setOnItemClickListener(this);
        searchResultsRecyclerView.setAdapter(searchResultsAdapter);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().trim();
                performSearch(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        return rootView;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.inflate(R.menu.menu_main);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handling menu item clicks here
                if (item.getItemId() == R.id.action_profile) {
                    // Start the Profile activity
                    startActivity(new Intent(requireContext(), view_profile.class));
                    return true;
                } else if (item.getItemId() == R.id.action_become_chef) {
                    // Start the BecomeChef activity
                    startActivity(new Intent(requireContext(), become_cook.class));
                    return true;
                }
                // Handle other menu items here

                return false;
            }
        });

        popupMenu.show();
    }

    private void performSearch(String query) {

//        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
//        String authToken = preferences.getString("accessToken", "");
        String authToken= "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmYXljYWxlbG91cnJhdEBnbWFpbC5jb20iLCJpYXQiOjE3MDUxNzM5MjgsImV4cCI6MTcwNTI2MDMyOH0.x3d--BcYC5CsIo1KnUjMnMCar8hPzE1rL1Hk4I2Bfo8";

//        ApiServiceProfile apiService = RetrofitClientProfile.getApiService();
//        Call<List<ChefDTO>> call = apiService.filterByNameAndCity("Bearer " + authToken, query);
        ApiServiceProfile apiService = RetrofitClientProfile.getApiService();
        Call<List<SearchResult>> call = apiService.filterByNameAndCity("Bearer " + authToken, query);

        //receive :
        call.enqueue(new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {
                if (response.isSuccessful()) {
                    List<SearchResult> dummyResults = response.body();
                    // Update the data set of the adapter with the filtered search results
                    searchResultsAdapter.setData(dummyResults);
                }
                else {
                    Log.d("error","not successful");
                }
            }
            @Override
            public void onFailure(Call<List<SearchResult>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onItemClick(SearchResult searchResult) {
        navigateToProfilePage(searchResult);
    }

    private void navigateToProfilePage(SearchResult searchResult) {
        Intent intent = new Intent(requireContext(), change_profile.class);

        // Pass necessary data to the profile page
        intent.putExtra("firstName", searchResult.getFirstName());
        intent.putExtra("lastName", searchResult.getLastName());
        intent.putExtra("address", searchResult.getAddress().getCity().getName());


        startActivity(intent);
    }
}