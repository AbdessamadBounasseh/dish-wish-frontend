package uit.ensak.dish_wish_frontend.history_folder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.ensak.dish_wish_frontend.Models.Command;
import uit.ensak.dish_wish_frontend.Profil.RetrofitClientProfile;
import uit.ensak.dish_wish_frontend.R;
import uit.ensak.dish_wish_frontend.SearchResult;
import uit.ensak.dish_wish_frontend.dto.ChefCommandHistoryDTO;
import uit.ensak.dish_wish_frontend.dto.ClientCommandHistoryDTO;
import uit.ensak.dish_wish_frontend.history_folder.CommandAdapter;
import uit.ensak.dish_wish_frontend.service.ApiServiceProfile;

public class HistoryActivity extends AppCompatActivity {

    private GridView gridViewDishes;
    private Button btnOrdered , btnInProgress, btnPrepared, btnInProgressByMe, btnPreparedByMe;

//    private List<Command> allCommands;

    private ClientCommandHistoryDTO clientCommandHistoryDTO;
    private ChefCommandHistoryDTO chefCommandHistoryDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("userId", 3L);
        editor.putString("accessToken", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaW5kYTFAZ21haWwuY29tIiwiaWF0IjoxNzA1MjQ5MzU5LCJleHAiOjE3MDUzMzU3NTl9.4OCq43UiXf76wE2kf74ioCovv6F64gFUkHr9O5-_SdE");
        editor.putBoolean("isCook", false);
        editor.apply();
        Boolean isCook = preferences.getBoolean("isCook", false);


        setContentView(R.layout.activity_history);

        gridViewDishes = findViewById(R.id.gridViewDishes);
        btnInProgress = findViewById(R.id.btnInProgress);
        btnPrepared = findViewById(R.id.btnPrepared);

        if(isCook) {
            btnInProgressByMe = findViewById(R.id.btnInProgressByMe);
            btnPreparedByMe = findViewById(R.id.btnPreparedByMe);
            btnPreparedByMe.setVisibility(View.VISIBLE);
            btnInProgressByMe.setVisibility(View.VISIBLE);
        }

        if(isCook){
            getChefCommandsHistory();
        }
        else {
            getClientCommandsHistory();
        }

        if(!isCook) {
            btnInProgress.setOnClickListener(v -> updateGridView(clientCommandHistoryDTO.getCommandsInProgress()));
            btnPrepared.setOnClickListener(v -> updateGridView(clientCommandHistoryDTO.getCommandsFinished()));
        }
        if(isCook){
            btnInProgress.setOnClickListener(v -> updateGridView(chefCommandHistoryDTO.getCommandsInProgressForMe()));
            btnPrepared.setOnClickListener(v -> updateGridView(chefCommandHistoryDTO.getCommandsFinishedForMe()));
            btnInProgressByMe.setOnClickListener(v -> updateGridView(chefCommandHistoryDTO.getCommandsInProgressByMe()));
            btnPreparedByMe.setOnClickListener(v -> updateGridView(chefCommandHistoryDTO.getCommandsFinishedByMe()));

        }

    }

    // updateGridView(filteredCommands);

    private void updateGridView(List<Command> commands) {
        CommandAdapter adapter = new CommandAdapter(this, commands);
        gridViewDishes.setAdapter(adapter);
    }

    private void getClientCommandsHistory (){

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String authToken = preferences.getString("accessToken", "");
        Long userClientId = preferences.getLong("userId", 0);

        ApiServiceProfile apiService = RetrofitClientProfile.getApiService();
        Call<ClientCommandHistoryDTO> call = apiService.getClientCommandsHistory("Bearer " + authToken, userClientId);

        //receive :
        call.enqueue(new Callback<ClientCommandHistoryDTO>() {
            @Override
            public void onResponse(Call<ClientCommandHistoryDTO> call, Response<ClientCommandHistoryDTO> response) {
                if (response.isSuccessful()) {
                    ClientCommandHistoryDTO cl1 = response.body();
                    clientCommandHistoryDTO.setCommandsFinished(cl1.getCommandsFinished());
                    clientCommandHistoryDTO.setCommandsInProgress(cl1.getCommandsInProgress());
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


        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String authToken = preferences.getString("accessToken", "");
        Long userChefId = preferences.getLong("userId", 0);

        ApiServiceProfile apiService = RetrofitClientProfile.getApiService();
        Call<ChefCommandHistoryDTO> call = apiService.getChefCommandsHistory("Bearer " + authToken, userChefId);

        //receive :
        call.enqueue(new Callback<ChefCommandHistoryDTO>() {
            @Override
            public void onResponse(Call<ChefCommandHistoryDTO> call, Response<ChefCommandHistoryDTO> response) {
                if (response.isSuccessful()) {
                    ChefCommandHistoryDTO ch1 = response.body();
                    chefCommandHistoryDTO.setCommandsFinishedForMe(ch1.getCommandsFinishedForMe());
                    chefCommandHistoryDTO.setCommandsInProgressByMe(ch1.getCommandsInProgressByMe());
                    chefCommandHistoryDTO.setCommandsInProgressForMe(ch1.getCommandsInProgressForMe());
                    chefCommandHistoryDTO.setCommandsFinishedByMe(ch1.getCommandsFinishedByMe());
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