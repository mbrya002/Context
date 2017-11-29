package apps.matts.contextlearning;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);
        mApp = ((KeepVals)getApplicationContext());
        soundEnabled = mApp.getGlobalSoundValue();
        soundbtn = (Button) findViewById(R.id.soundToggle);
        database = FirebaseDatabase.getInstance().getReference();
        sharedPrefManager = new SharedPrefManager(mContext);
        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        mEmail = sharedPrefManager.getUserEmail().replace(".", ",");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
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

    public void logoutClick(View view)
    {
        mAuth.signOut();

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(SettingScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

}
