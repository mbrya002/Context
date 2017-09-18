package apps.matts.contextlearning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Level extends AppCompatActivity {

    gameLevel gm;
    private DatabaseReference database;
    Stack<Button> disabledButtons;
    DatabaseReference qnumref;
    DatabaseReference qinforef;
    ImageView img;
    //int numQuestions;
    //String word = "";
    //String hint = "";
    String imageUrl = "";
    QNum qnum = new QNum();
    //QuestionInfo qinfo = new QuestionInfo();
    ArrayList<QuestionInfo> questions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        database = FirebaseDatabase.getInstance().getReference();

        //Random r = new Random();
        //DatabaseReference countRef = database.child("count");
        //int randomQuestion = r.nextInt(numQuestions - 1) + 1;
        qnumref = database.child("Count");

        qinforef = database.child("Questions");

        img = (ImageView)findViewById(R.id.Pic) ;
        disabledButtons = new Stack<Button>();
        gm = new gameLevel();
    }

    @Override
    public void onStart() {
        super.onStart();


        qinforef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    QuestionInfo q = ds.getValue(QuestionInfo.class);
                    Log.d("Level", q.getWord());
                    questions.add(q);
                }

                showQuestion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // [END post_value_event_listener]

    }

    public void showQuestion() {
        Random r = new Random();

        int rq = r.nextInt(questions.size() - 1) + 1;
        imageUrl = "http://www.cs.odu.edu/~mbryant/Context/" + questions.get(rq).getWord() + ".png";
        Picasso.with(this).load(imageUrl).into(img);
        gm.setLevel(questions.get(rq).getWord().toUpperCase());
        updateScreen();
    }

    public void checkClick(View view)
    {
        if(gm.makeGuess())
        {
            Toast.makeText(this, "Congratulations, You did it!",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Sorry, try agian",
                    Toast.LENGTH_LONG).show();
        }
    }



    public void eraseClick(View view)
    {
        if(!disabledButtons.empty())
        {
            gm.removeGuessLetter();
            Button b = disabledButtons.pop();
            b.setEnabled(true);
        }
        updateText();
    }

    public void letterClick(View view)
    {
        Button b = (Button)view;
        Character buttonText = b.getText().charAt(0);
        gm.guessLetter(buttonText);
        b.setEnabled(false);
        disabledButtons.add(b);
        updateScreen();
    }

    public void updateScreen()
    {
        updateButtons();
        updateText();
    }

    public void updateText()
    {
        TextView textView = (TextView) findViewById(R.id.textview);
        textView.setText(gm.getGuessShown());
    }

    public void updateButtons()
    {
        char[] let = gm.getGuessLettersChar();
        ViewGroup layout = (ViewGroup)findViewById(R.id.layout_top_container_buttons);
        for (int i = 0; i < 4; i++) {

            View child = layout.getChildAt(i);
            if(child instanceof Button)
            {
                Button button = (Button) child;
                button.setText(let[i] + "");
            }

        }

        layout = (ViewGroup)findViewById(R.id.layout_bottom_container_buttons);
        for (int x = 0; x < 4; x++) {

            View child = layout.getChildAt(x);
            if(child instanceof Button)
            {
                Button button = (Button) child;
                button.setText(let[x+4] + "");
            }

        }

    }

}
