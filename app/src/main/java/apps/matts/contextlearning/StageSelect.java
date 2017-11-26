package apps.matts.contextlearning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by mgrah on 11/26/2017.
 */

public class StageSelect extends AppCompatActivity {

    private TextView t;

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
