package uit.ensak.dish_wish_frontend.Command;

import static java.lang.Float.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Models.Chef;
import uit.ensak.dish_wish_frontend.Models.Client;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.Models.Proposition;
import uit.ensak.dish_wish_frontend.R;

public class FinalizeOrderActivity extends AppCompatActivity {
    private ImageView arrow;
    private ImageView negotiate;
    private long propositionId;
    private Button sendOffer;
    String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWluZWVrOEBnbWFpbC5jb20iLCJpYXQiOjE3MDUyMzUzMDAsImV4cCI6MTcwNTMyMTcwMH0.ZhulnzT4vzPRdyjglYUFaWtr06LW9W6p0edjzgizm_Q";

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

        negotiate = findViewById(R.id.adjustprice);
        if (negotiate != null) {
            negotiate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();

                    // Apply dimming effect
                    DimUtils.applyDim(root, 0.5f);
                    Dialog popup = new Dialog(FinalizeOrderActivity.this);
                    popup.setContentView(R.layout.popup_negotiate);

                    popup.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edittext);

                    EditText pricefield = popup.findViewById(R.id.price);
                    int intValue = (int) price;
                    pricefield.setText(intValue + "DH");

                    popup.show();

                    arrow = popup.findViewById(R.id.animation);
                    if (arrow != null) {
                        arrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popup.dismiss();
                                DimUtils.clearDim(root);
                            }
                        });
                    }



                    Button addButton = popup.findViewById(R.id.addButton);
                    Button subtractButton = popup.findViewById(R.id.subtractButton);
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String priceText = pricefield.getText().toString();
                            int currentPrice = Integer.parseInt(priceText.replace("DH", ""));
                            currentPrice += 5;
                            pricefield.setText(currentPrice + "DH");
                        }
                    });

                    subtractButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String priceText = pricefield.getText().toString();
                            int currentPrice = Integer.parseInt(priceText.replace("DH", ""));
                            currentPrice -= 5;
                            if (currentPrice < 0) {
                                currentPrice = 0;
                            }
                            pricefield.setText(currentPrice + "DH");
                        }
                    });


                    sendOffer = popup.findViewById(R.id.sendOffer);
                    sendOffer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Proposition proposition = new Proposition();

                            proposition.setLastChefProposition(valueOf(price));

                            EditText pricefield = popup.findViewById(R.id.price);

                            if (pricefield != null) {
                                String priceText = pricefield.getText().toString();
                                float priceValue = Float.parseFloat(priceText.replace("DH", ""));

                                if (!priceText.isEmpty()) {
                                    try {
                                        proposition.setLastClientProposition(priceValue);
                                    } catch (NumberFormatException e) {
                                        Log.d("NumberFormatException", "Failed to parse priceText to float");
                                    }
                                } else {
                                    Log.d("EmptyFieldException", "Price field is empty");
                                }
                            } else {
                                Log.d("NullPointerException", "Price field is null");
                            }


                            ApiService apiService = RetrofitClient.getApiService();
                            Call<Proposition> call = apiService.updateProposition( propositionId,proposition,"Bearer " + accessToken);
                            call.enqueue(new Callback<Proposition>() {
                                @Override
                                public void onResponse(Call<Proposition> call, Response<Proposition> response) {
                                    if (response.isSuccessful()) {
                                        popup.dismiss();
                                        DimUtils.clearDim(root);
                                        Toast.makeText(getApplicationContext(), "Success! New offer sent", Toast.LENGTH_LONG).show();
                                        Command commandupdate = new Command();
                                        String priceText = pricefield.getText().toString();
                                        float priceValue = Float.parseFloat(priceText.replace("DH", ""));
                                        commandupdate.setPrice(String.valueOf(priceValue));

                                        Call<Command> updateCommandCall = apiService.updateCommand( "Bearer " + accessToken,commandId,commandupdate);
                                        updateCommandCall.enqueue(new Callback<Command>() {
                                            @Override
                                            public void onResponse(Call<Command> call, Response<Command> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Original offer updated", Toast.LENGTH_LONG).show();
                                                } else {
                                                    popup.dismiss();
                                                    showErrorDialog();
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<Command> call, Throwable t) {
                                                // Handle failure for the command update call
                                                popup.dismiss();
                                                showErrorDialog();
                                            }
                                        });
                                    } else {
                                        popup.dismiss();
                                        showErrorDialog();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Proposition> call, Throwable t) {
                                    popup.dismiss();
                                    showErrorDialog();
                                }
                            });
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