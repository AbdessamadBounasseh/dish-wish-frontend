package uit.ensak.dish_wish_frontend.history_folder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.dto.ChefCommandHistoryDTO;
import uit.ensak.dish_wish_frontend.dto.ClientCommandHistoryDTO;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;
import uit.ensak.dish_wish_frontend.service.RetrofitClient;

public class HistoryActivity extends AppCompatActivity {

    private GridView gridViewDishes;
    private Button btnInProgress, btnPrepared, btnInProgressByMe, btnPreparedByMe;
    private String accessToken;
    private long userId;
    private Boolean isCook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        accessToken = preferences.getString("accessToken", "");
        userId = preferences.getLong("userId", 0);
        isCook = preferences.getBoolean("isCook", false);

        gridViewDishes = findViewById(R.id.gridViewDishes);
        btnInProgress = findViewById(R.id.btnInProgress);
        btnPrepared = findViewById(R.id.btnPrepared);

        if(isCook){
            btnInProgressByMe = findViewById(R.id.btnInProgressByMe);
            btnPreparedByMe = findViewById(R.id.btnPreparedByMe);
            btnPreparedByMe.setVisibility(View.VISIBLE);
            btnInProgressByMe.setVisibility(View.VISIBLE);
            getChefCommandsHistory();
        }
        else {
            getClientCommandsHistory();
        }

        ImageView arrow = findViewById(R.id.animation);
        if (arrow != null) {
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    // updateGridView(filteredCommands);

    private void updateGridView(List<Command> commands) {
        CommandAdapter adapter = new CommandAdapter(this, commands);
        gridViewDishes.setAdapter(adapter);
    }

    private void getClientCommandsHistory (){
        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<ClientCommandHistoryDTO> call = apiService.getClientCommandsHistory("Bearer " + accessToken, userId);

        //receive :
        call.enqueue(new Callback<ClientCommandHistoryDTO>() {
            @Override
            public void onResponse(Call<ClientCommandHistoryDTO> call, Response<ClientCommandHistoryDTO> response) {
                if (response.isSuccessful()) {
                    ClientCommandHistoryDTO cl1 = response.body();
                    btnInProgress.setOnClickListener(v -> updateGridView(cl1.getCommandsInProgress()));
                    btnPrepared.setOnClickListener(v -> updateGridView(cl1.getCommandsFinished()));
                }
                else {
                    Log.d("error","not successful");
                }
            }
            @Override
            public void onFailure(Call<ClientCommandHistoryDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private void getChefCommandsHistory(){

        ApiServiceProfile apiService = RetrofitClient.getApiServiceProfile();
        Call<ChefCommandHistoryDTO> call = apiService.getChefCommandsHistory("Bearer " + accessToken, userId);

        //receive :
        call.enqueue(new Callback<ChefCommandHistoryDTO>() {
            @Override
            public void onResponse(Call<ChefCommandHistoryDTO> call, Response<ChefCommandHistoryDTO> response) {
                if (response.isSuccessful()) {
                    ChefCommandHistoryDTO ch1 = response.body();
                    btnInProgress.setOnClickListener(v -> updateGridView(ch1.getCommandsInProgressForMe()));
                    btnPrepared.setOnClickListener(v -> updateGridView(ch1.getCommandsFinishedForMe()));
                    btnInProgressByMe.setOnClickListener(v -> updateGridView(ch1.getCommandsInProgressByMe()));
                    btnPreparedByMe.setOnClickListener(v -> updateGridView(ch1.getCommandsFinishedByMe()));
                }
                else {
                    Log.d("error","not successful");
                }
            }
            @Override
            public void onFailure(Call<ChefCommandHistoryDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}