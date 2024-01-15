package uit.ensak.dish_wish_frontend.notification_folder;
// Add necessary imports
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.Models.Proposition;
import uit.ensak.dish_wish_frontend.R;


public class NotificationClientAdapter extends RecyclerView.Adapter<NotificationClientAdapter.ViewHolder> {

    private ArrayList<Proposition> propositionList;

    public NotificationClientAdapter(ArrayList<Proposition> notificationList, Context context) {
        this.propositionList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_client, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Proposition proposition = propositionList.get(position);

        // Bind data to views
        holder.textTitle.setText(proposition.getCommand().getTitle());
        holder.textDescription.setText(proposition.getCommand().getDescription());
        holder.textPrice.setText(proposition.getCommand().getPrice());
        holder.textChefName.setText("Chef " + proposition.getCommand().getChef().getFirstName() + " " + proposition.getCommand().getChef().getLastName());
    }

    @Override
    public int getItemCount() {
        return propositionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageChef;
        TextView textChefName;
        TextView textTitle;
        TextView textDescription;
        TextView textPrice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageChef = itemView.findViewById(R.id.imageChef);
            textChefName = itemView.findViewById(R.id.textChefName);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            textPrice = itemView.findViewById(R.id.textPrice);
        }
    }
}