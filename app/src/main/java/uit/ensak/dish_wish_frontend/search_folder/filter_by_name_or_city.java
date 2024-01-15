package uit.ensak.dish_wish_frontend.search_folder;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import java.util.List;

//menu
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.widget.PopupMenu;

// FilterByNameOrCityFragment.java
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.content.Intent;
import android.content.SharedPreferences;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uit.ensak.dish_wish_frontend.Authentification.ChangePasswordActivity;
import uit.ensak.dish_wish_frontend.Contact.ComplaintActivity;
import uit.ensak.dish_wish_frontend.Contact.QuestionsActivity;
import uit.ensak.dish_wish_frontend.Profil.become_cook;
import uit.ensak.dish_wish_frontend.Profil.change_profile;
import uit.ensak.dish_wish_frontend.Profil.search_profile;
import uit.ensak.dish_wish_frontend.Profil.view_profile;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.SearchResultsAdapter;
import uit.ensak.dish_wish_frontend.history_folder.HistoryActivity;
import uit.ensak.dish_wish_frontend.notification_folder.NotificationsChef;
import uit.ensak.dish_wish_frontend.notification_folder.NotificationsClient;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.SearchResult;
import uit.ensak.dish_wish_frontend.service.RetrofitClient;


public class filter_by_name_or_city extends Fragment implements SearchResultsAdapter.OnItemClickListener {

    private SearchResultsAdapter searchResultsAdapter;  // Declare searchResultsAdapter as a class variable
    private String accessToken;
    private boolean isCook;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter_by_name_or_city, container, false);

        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        accessToken = preferences.getString("accessToken", "");
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
        MenuItem becomeChefItem = popupMenu.getMenu().findItem(R.id.action_become_chef);

        becomeChefItem.setVisible(!isCook);

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
                }else if (item.getItemId() == R.id.action_orders) {
                    // Start the BecomeChef activity
                    startActivity(new Intent(requireContext(), HistoryActivity.class));
                    return true;
                }else if (item.getItemId() == R.id.action_notifications) {
                    Intent intent;

                    if (isCook) {
                        intent = new Intent(requireContext() , NotificationsChef.class);
                    } else {
                        intent = new Intent(requireContext() , NotificationsClient.class);
                    }
                    startActivity(intent);
                }else if (item.getItemId() == R.id.action_support) {
                    // Start the BecomeChef activity
                    startActivity(new Intent(requireContext(), ComplaintActivity.class));
                    return true;
                }else if (item.getItemId() == R.id.action_FAQ) {
                    // Start the BecomeChef activity
                    startActivity(new Intent(requireContext(), QuestionsActivity.class));
                    return true;
                }
                else if (item.getItemId() == R.id.action_change_password) {
                    // Start the BecomeChef activity
                    startActivity(new Intent(requireContext(), ChangePasswordActivity.class));
                    return true;
                }

                return true;
            }
        }
    );

        popupMenu.show();
}

    private void performSearch(String query) {

        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<List<SearchResult>> call = apiService.filterByNameAndCity("Bearer " + accessToken, query);

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
        Intent intent = new Intent(requireContext(), search_profile.class);

        // Pass necessary data to the profile page
        intent.putExtra("id",searchResult.getId());



        startActivity(intent);
    }
}