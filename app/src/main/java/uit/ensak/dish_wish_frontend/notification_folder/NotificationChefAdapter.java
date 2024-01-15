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
        import uit.ensak.dish_wish_frontend.R;


public class NotificationChefAdapter extends RecyclerView.Adapter<NotificationChefAdapter.ViewHolder> {

    private ArrayList<Command> commandList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public NotificationChefAdapter(ArrayList<Command> notificationList, Context context) {
        this.commandList = notificationList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_chef, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Command command = commandList.get(position);

        // Bind data to views
        holder.textTitle.setText(command.getTitle());
        holder.textDescription.setText(command.getDescription());
        holder.textPrice.setText(command.getPrice());
        holder.textClientName.setText("Client " + command.getClient().getFirstName() + " " + command.getClient().getLastName());

        // Set click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commandList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageClient;
        TextView textClientName;
        TextView textTitle;
        TextView textDescription;
        TextView textPrice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageClient = itemView.findViewById(R.id.imageClient);
            textClientName = itemView.findViewById(R.id.textClientName);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            textPrice = itemView.findViewById(R.id.textPrice);
        }
    }
}