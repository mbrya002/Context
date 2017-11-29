package apps.matts.contextlearning;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import apps.matts.contextlearning.utils.SharedPrefManager;

public class SettingScreen extends AppCompatActivity {

    Boolean soundEnabled;
    Button soundbtn;
    KeepVals mApp;
    private DatabaseReference database;
    SharedPrefManager sharedPrefManager;
    private String mEmail;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);
        mApp = ((KeepVals)getApplicationContext());
        soundEnabled = mApp.getGlobalSoundValue();
        soundbtn = (Button) findViewById(R.id.soundToggle);
        database = FirebaseDatabase.getInstance().getReference();
        sharedPrefManager = new SharedPrefManager(mContext);

        mEmail = sharedPrefManager.getUserEmail().replace(".", ",");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (soundEnabled)
        {
            soundbtn.setText("On");
        }
        else
        {
            soundbtn.setText("Off");
        }

    }

    public void soundClick(View view)
    {
        mApp.toggleGlobalSoundValue();
        soundEnabled = mApp.getGlobalSoundValue();
        if (soundEnabled)
        {
            soundbtn.setText("On");
        }
        else
        {
            soundbtn.setText("Off");
        }
    }

    public void resetClick(View view)
    {
        database.child("users").child(mEmail).child("level").setValue(1);
    }

}
