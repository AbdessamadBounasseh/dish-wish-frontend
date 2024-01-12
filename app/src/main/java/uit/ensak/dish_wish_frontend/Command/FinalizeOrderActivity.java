package uit.ensak.dish_wish_frontend.Command;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.R;

public class FinalizeOrderActivity extends AppCompatActivity {
    private ImageView arrow;
    private long propositionId;
    String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWluZWVrOEBnbWFpbC5jb20iLCJpYXQiOjE3MDUwNjQyMDEsImV4cCI6MTcwNTE1MDYwMX0.Trk2cmuXm9SlyXrjNRuGb2mRwlbbGLqlSPB05YQekeM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_order);

        Intent intent = getIntent();
        long commandId = intent.getLongExtra("CommandId", 0);
        propositionId = intent.getLongExtra("PropositionId", 0);
        long chefId = intent.getLongExtra("ChefId", 0);
        float price = intent.getFloatExtra("price",0.0f);
        String delivery = intent.getStringExtra("delivary");
        String firstname = intent.getStringExtra("description");
        String lastname = intent.getStringExtra("serving");

        TextView priceEditText = findViewById(R.id.price);
        TextView delivaryEditText = findViewById(R.id.delivary);
        TextView firstnameEditText = findViewById(R.id.firstname);
        TextView lastnameEditText = findViewById(R.id.lastname);

        String priceString = String.valueOf(price);
        priceEditText.setText(priceString + " DH");
        delivaryEditText.setText(delivery);
        firstnameEditText.setText(firstname);
        lastnameEditText.setText(lastname);


        arrow = findViewById(R.id.animation);
        if (arrow != null) {
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        Button confirm = findViewById(R.id.confirmOffer);
        if (confirm != null) {
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ApiService apiService = RetrofitClient.getApiService();
                    Call<Void> call = apiService.assignChefToCommand(commandId, chefId, "Bearer " + accessToken);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // Handle success
                                showSuccessDialog();
                            } else {
                                // Handle failure
                                showErrorDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // Handle failure
                            showErrorDialog();
                        }
                    });
                }
            });
        }
    }

    private void showSuccessDialog() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.deleteProposition("Bearer " + accessToken, propositionId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(FinalizeOrderActivity.this, MapsHomeActivity.class);

                    intent.putExtra("showSuccessDialog", true);

                    startActivity(intent);
                } else {
                    showErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showErrorDialog();
            }
        });
    }



    private void showErrorDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_command_failed);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edittext);
        dialog.show();
        Button tryAgainButton = dialog.findViewById(R.id.tryagain);
        tryAgainButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }
}