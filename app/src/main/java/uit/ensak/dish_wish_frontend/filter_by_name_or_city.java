package uit.ensak.dish_wish_frontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.filter_by_name_or_city, container, false);

        EditText searchEditText = rootView.findViewById(R.id.searchEditText);
        RecyclerView searchResultsRecyclerView = rootView.findViewById(R.id.searchResultsRecyclerView);

        SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter();
        searchResultsRecyclerView.setAdapter(searchResultsAdapter);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().trim();
                performSearch(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return rootView;
    }

    private void performSearch(String query) {
        // TODO: Implement logic to query the backend for search results
        // Update the data set of the adapter with the search results
        // For example: searchResultsAdapter.setData(searchResults);
    }
}
