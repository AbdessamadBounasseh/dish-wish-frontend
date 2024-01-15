package uit.ensak.dish_wish_frontend;

// SearchResultsAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import uit.ensak.dish_wish_frontend.SearchResult;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private List<SearchResult> searchResults = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(SearchResult searchResult);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setData(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchResult result = searchResults.get(position);
        holder.bind(result);
        // Setting the  click listener for the item
        holder.itemView.setOnClickListener(view ->{
            if (itemClickListener != null) {
                itemClickListener.onItemClick(result);
            }
        });
    }


    @Override
    public int getItemCount() {
        return searchResults.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView firstNameTextView;
        private TextView lastNameTextView;
        private TextView addressTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            firstNameTextView = itemView.findViewById(R.id.firstNameTextView);
            lastNameTextView = itemView.findViewById(R.id.lastNameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
        }

        public void bind(SearchResult result) {
            firstNameTextView.setText(result.getFirstName());
            lastNameTextView.setText(result.getLastName());
            if (result.getAddress() != null ){
                addressTextView.setText(result.getAddress().getCity().getName());
            }
        }
    }

}