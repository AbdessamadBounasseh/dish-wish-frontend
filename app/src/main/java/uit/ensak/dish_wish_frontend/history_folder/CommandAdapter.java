package uit.ensak.dish_wish_frontend.history_folder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.R;

public class CommandAdapter extends ArrayAdapter<Command> {

    public CommandAdapter(Context context, List<Command> commands) {
        super(context, 0, commands);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Command command = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dish, parent, false);
        }

        ImageView imageDish = convertView.findViewById(R.id.imageDish);
        TextView textDishName = convertView.findViewById(R.id.textDishName);
        TextView textDishPrice = convertView.findViewById(R.id.textDishPrice);

        // we use this image for simulation.
        imageDish.setImageResource(R.drawable.homemade_beef_burger);

        if(command !=null ) {
            textDishName.setText(command.getTitle());
            textDishPrice.setText(command.getPrice());
        }
        return convertView;
    }
}
