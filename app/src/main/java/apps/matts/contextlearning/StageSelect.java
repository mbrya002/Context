package apps.matts.contextlearning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import apps.matts.contextlearning.model.User;
import apps.matts.contextlearning.utils.SharedPrefManager;


/**
 * Created by mgrah on 11/26/2017.
 */

public class StageSelect extends AppCompatActivity {

    private TextView t;
    Button lvl1btn, lvl2btn, lvl3btn, lvl4btn, lvl5btn, lvl6btn, lvl7btn, lvl8btn, lvl9btn;
    ArrayList<Button> btns = new ArrayList<Button>();
    private DatabaseReference database;
    DatabaseReference qnumref;
    SharedPrefManager sharedPrefManager;
    private String mEmail;
    Context mContext = this;
    int userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stageselect);
        t = (TextView) findViewById(R.id.difficulty_text);
        Intent i = getIntent();
        int diff = i.getIntExtra("difficulty", 0);
        switch (diff){
            case 0:
                t.setText("Beginner");
                break;
            case 1:
                t.setText("Advanced");
                break;
            case 2:
                t.setText("Expert");
                break;
            default:
                t.setText("Beginner");
                break;

        }

        database = FirebaseDatabase.getInstance().getReference();
        sharedPrefManager = new SharedPrefManager(mContext);
        mEmail = sharedPrefManager.getUserEmail().replace(".", ",");
        qnumref = database.child("users").child(mEmail);

        lvl1btn = (Button) findViewById(R.id.lvl1);
        lvl2btn = (Button) findViewById(R.id.lvl2);
        lvl3btn = (Button) findViewById(R.id.lvl3);
        lvl4btn = (Button) findViewById(R.id.lvl4);
        lvl5btn = (Button) findViewById(R.id.lvl5);
        lvl6btn = (Button) findViewById(R.id.lvl6);
        lvl7btn = (Button) findViewById(R.id.lvl7);
        lvl8btn = (Button) findViewById(R.id.lvl8);
        lvl9btn = (Button) findViewById(R.id.lvl9);
        if(btns.isEmpty())
        {
            btns.add(lvl1btn);
            btns.add(lvl2btn);
            btns.add(lvl3btn);
            btns.add(lvl4btn);
            btns.add(lvl5btn);
            btns.add(lvl6btn);
            btns.add(lvl7btn);
            btns.add(lvl8btn);
            btns.add(lvl9btn);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        qnumref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User q = dataSnapshot.getValue(User.class);
                userLevel = q.getLevel();
                updateButtons();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateButtons()
    {
        for(int i = 0; i < 9; i++)
        {
            if(i <= (userLevel - 1 ))
            {
                btns.get(i).setEnabled(true);
            }
            else
            {
                btns.get(i).setEnabled(false);
            }
        }
    }

    public void stageClick(View view)
    {
        Button b = (Button)view;
        int buttonText = Integer.parseInt(b.getText().toString());
        Intent intent = new Intent(this, Level.class);
        intent.putExtra("stage", buttonText);
        startActivity(intent);
    }
}
