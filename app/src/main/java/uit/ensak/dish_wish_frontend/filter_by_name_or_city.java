package uit.ensak.dish_wish_frontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;


// FilterByNameOrCityFragment.java
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class filter_by_name_or_city extends Fragment {

    private SearchResultsAdapter searchResultsAdapter;  // Declare searchResultsAdapter as a class variable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter_by_name_or_city, container, false);

        EditText searchEditText = rootView.findViewById(R.id.searchEditText);
        RecyclerView searchResultsRecyclerView = rootView.findViewById(R.id.searchResultsRecyclerView);

        searchResultsAdapter = new SearchResultsAdapter();
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

    private void performSearch(String query) {
        // TODO: Implement logic to query the backend for search results
        // Update the data set of the adapter with the search results
        // For example: searchResultsAdapter.setData(searchResults);

        // Simulating search results with dummy data
        List<SearchResult> dummyResults = new ArrayList<>();

        if (!TextUtils.isEmpty(query)) {
            // Iterate through the dummy data and filter based on the query
            for (SearchResult result : getDummyData()) {
                if (result.getName().toLowerCase().contains(query.toLowerCase()) ||
                        result.getCity().toLowerCase().contains(query.toLowerCase())) {
                    dummyResults.add(result);
                }
            }
        }

        // Update the data set of the adapter with the filtered search results
        searchResultsAdapter.setData(dummyResults);
    }

    private List<SearchResult> getDummyData() {
        // Simulating the complete dummy data
        List<SearchResult> dummyResults = new ArrayList<>();
        dummyResults.add(new SearchResult("John Doe", "New York"));
        dummyResults.add(new SearchResult("Jane Smith", "Los Angeles"));
        dummyResults.add(new SearchResult("Alex Johnson", "Chicago"));
        dummyResults.add(new SearchResult("Faycal Elou", "Kenitra"));
        dummyResults.add(new SearchResult("Brown Jalou", "Kenitra"));
        dummyResults.add(new SearchResult("Ali Kamar", "Rabat"));
        dummyResults.add(new SearchResult("Fahd Alibi", "Kenitra"));
        return dummyResults;
    }
}