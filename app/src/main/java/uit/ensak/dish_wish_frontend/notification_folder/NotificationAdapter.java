package uit.ensak.dish_wish_frontend.notification_folder;
// Add necessary imports
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.R;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Command> commandList;

    public NotificationAdapter(List<Command> commandList) {
        this.commandList = commandList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_client, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Command command = commandList.get(position);

        // Bind data to views
        holder.textTitle.setText(command.getTitle());
        holder.textDescription.setText(command.getDescription());
        holder.textPrice.setText(command.getPrice());
//        holder.textChefName.setText(command.getChef().getFirstName() + " " + command.getChef().getLastName());
        holder.textChefName.setText("Chef " + "Ali" + " BOUNINI");
    }

    @Override
    public int getItemCount() {
        return commandList.size();
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
