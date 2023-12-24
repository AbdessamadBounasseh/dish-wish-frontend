package uit.ensak.dish_wish_frontend.Profil_Folder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import uit.ensak.dish_wish_frontend.R;

public class become_cook extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_cook);

        Button btnScancard = findViewById(R.id.btnScancard);
        Button btnScancertif = findViewById(R.id.btnScancertif);
        ImageButton btnBack = findViewById(R.id.btnBack);

        btnScancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGalleryPicker();
            }
        });

        btnScancertif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGalleryPicker();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode pour revenir en arrière
                onBackPressed();
            }
        });
    }

    private void launchGalleryPicker() {
        // Intent pour choisir une image depuis la galerie
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Récupérer l'URI de l'image sélectionnée depuis la galerie
            Uri selectedImageUri = data.getData();
            // Faites quelque chose avec l'URI, comme l'afficher dans une ImageView
            // imageView.setImageURI(selectedImageUri);
        }
    }
}
