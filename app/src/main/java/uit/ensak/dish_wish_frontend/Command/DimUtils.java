package uit.ensak.dish_wish_frontend.Command;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.Toast;

public class DimUtils {

    public static void applyDim(ViewGroup parent, float dimAmount) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Drawable dim = new ColorDrawable(Color.BLACK);
            dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
            dim.setAlpha((int) (255 * dimAmount));

            ViewGroupOverlay overlay = parent.getOverlay();
            overlay.add(dim);
        } else {
            // Handle devices with API < 18 if needed
            Toast.makeText(parent.getContext(), "Dimming not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    public static void clearDim(ViewGroup parent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ViewGroupOverlay overlay = parent.getOverlay();
            overlay.clear();
        } else {
            // Handle devices with API < 18 if needed
            Toast.makeText(parent.getContext(), "Clearing dim not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }
}
