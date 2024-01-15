package uit.ensak.dish_wish_frontend.Contact;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import uit.ensak.dish_wish_frontend.R;

public class QuestionsActivity extends AppCompatActivity {
    String questions[]={"question1","question2"};
    String reponses[]={"reponse1","reponse2"};
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        listview=findViewById(R.id.list);
        ListAdapter adapter= new ListAdapter(getApplicationContext(),questions,reponses);
        listview.setAdapter(adapter);
    }
}