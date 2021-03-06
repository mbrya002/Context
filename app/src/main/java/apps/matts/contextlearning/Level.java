package apps.matts.contextlearning;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Stack;

import apps.matts.contextlearning.model.User;
import apps.matts.contextlearning.utils.SharedPrefManager;

public class Level extends AppCompatActivity implements MyAlertDialogFragment2.QuestionInterface {

    gameLevel gm;
    int rq;
    static String hint;
    static String msgString;
    private DatabaseReference database;
    MediaPlayer mpC;
    MediaPlayer mpNC;
    KeepVals mApp;
    Boolean soundEnabled;
    private FirebaseStorage storage;
    Stack<Button> disabledButtons;
    DatabaseReference qnumref;
    DatabaseReference qinforef;
    SharedPrefManager sharedPrefManager;
    private String mEmail;
    Context mContext = this;
    boolean isCorrect;
    ImageView img;
    int userLevel;

    ArrayList<QuestionInfo> questions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        mpC = MediaPlayer.create(this, R.raw.correct);
        mpNC = MediaPlayer.create(this, R.raw.incorrect);
        mApp = ((KeepVals)getApplicationContext());
        soundEnabled = mApp.getGlobalSoundValue();
        rq = -1;
        database = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        sharedPrefManager = new SharedPrefManager(mContext);
        mEmail = sharedPrefManager.getUserEmail().replace(".", ",");
        qnumref = database.child("users").child(mEmail);
        isCorrect = false;
        qinforef = database.child("Questions");
        rq = getIntent().getIntExtra("stage", 1) - 1;
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
                    questions.add(q);
                }
                showQuestion();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        qnumref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    User q = dataSnapshot.getValue(User.class);
                    userLevel = q.getLevel();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showQuestion() {
        isCorrect = false;
        StorageReference storageRef = storage.getReferenceFromUrl("gs://context-bcf1e.appspot.com/").child("Pics/" + questions.get(rq).getWord() + ".png");

        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(img);

        gm.setLevel(questions.get(rq).getWord().toUpperCase());
        hint = questions.get(rq).getHint();
        enableAllButton();
        updateScreen();
    }

    public void nextquestion ()
    {
        if ( rq < 8 ) {
            rq++;
        }
        showQuestion();
    }

    public void checkClick(View view)
    {

        if(gm.makeGuess())
        {
            isCorrect = true;
            if((userLevel <= (rq + 1)) && (rq < 8))
            {
                qnumref.child("level").setValue(rq + 2);
            }
            if(soundEnabled)
            {
                mpC.start();
            }
            msgString = "Congratulations, You did it!";
            MyAlertDialogFragment2 dialog = new MyAlertDialogFragment2();
            dialog.show(getSupportFragmentManager(), "stuff");




        }
        else
        {
            isCorrect = false;
            if(soundEnabled)
            {
                mpNC.start();
            }
            msgString = "Sorry, try again";
            IncorrectAlertDialog dialog = new IncorrectAlertDialog();
            dialog.show(getSupportFragmentManager(), "stuff");


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

    public void enableAllButton(){
        while(!disabledButtons.empty())
        {
            Button b = disabledButtons.pop();
            b.setEnabled(true);
        }
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
        int[] butpos= {1,3,5,7};
        ViewGroup layout = (ViewGroup)findViewById(R.id.layout_top_container_buttons);
        for (int i = 0; i < 4; i++) {

            View child = layout.getChildAt(butpos[i]);
            if(child instanceof Button)
            {
                Button button = (Button) child;
                button.setText(let[i] + "");
            }

        }

        layout = (ViewGroup)findViewById(R.id.layout_bottom_container_buttons);
        for (int x = 0; x < 4; x++) {

            View child = layout.getChildAt(butpos[x]);
            if(child instanceof Button)
            {
                Button button = (Button) child;
                button.setText(let[x+4] + "");
            }

        }

    }

    @Override public void loadNextQuestion() {
        nextquestion();
    }

}