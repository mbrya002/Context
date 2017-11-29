package apps.matts.contextlearning;

/**
 * Created by Matthew on 9/12/2017.
 */

import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import android.support.design.widget.NavigationView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import apps.matts.contextlearning.utils.SharedPrefManager;


// This class is a simple activity with NavigationDrawer
// we get data stored in sharedPrefference and display on the header view of the NavigationDrawer
public class NavDrawerActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    Context mContext = this;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView mFullNameTextView, mEmailTextView;
    private CircleImageView mProfileImageView;
    private String mUsername, mEmail;

    private Button playButton;
    private Button settingButton;

    SharedPrefManager sharedPrefManager;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initNavigationDrawer();

        View header = navigationView.getHeaderView(0);

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);

        settingButton = (Button) findViewById(R.id.settingsButton);
        settingButton.setOnClickListener(this);

        mFullNameTextView = (TextView) header.findViewById(R.id.fullName);
        mEmailTextView = (TextView) header.findViewById(R.id.email);
        mProfileImageView = (CircleImageView) header.findViewById(R.id.profileImage);

        // create an object of sharedPreferenceManager and get stored user data
        sharedPrefManager = new SharedPrefManager(mContext);
        mUsername = sharedPrefManager.getName();
        mEmail = sharedPrefManager.getUserEmail();
        String uri = sharedPrefManager.getPhoto();
        Uri mPhotoUri = Uri.parse(uri);

        //Set data gotten from SharedPreference to the Navigation Header view
        mFullNameTextView.setText(mUsername);
        mEmailTextView.setText(mEmail);

        Picasso.with(mContext)
                .load(mPhotoUri)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(mProfileImageView);

        configureSignIn();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // Initialize and add Listener to NavigationDrawer
    public void initNavigationDrawer(){

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();

                switch (id){

                    case R.id.payment:
                        Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.logout:
                        signOut();
                        drawerLayout.closeDrawers();
                        break;

                }
                return false;
            }
        });

        //set up navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    // This method configures Google SignIn
    public void configureSignIn(){
// Configure sign-in to request the user's basic profile like name and email
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();
        mGoogleApiClient.connect();
    }

    //method to logout
    private void signOut(){
        new SharedPrefManager(mContext).clear();
        mAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent intent = new Intent(NavDrawerActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.playButton){
            //Intent intent = new Intent(NavDrawerActivity.this, Level.class);
            //startActivity(intent);
            new MyAlertDialogFragment().show(getSupportFragmentManager(), "stuff");
        }
        if (id == R.id.settingsButton){
            Intent intent = new Intent(this, SettingScreen.class);
            startActivity(intent);
        }
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        TextView title;
        Button beginnerBtn;
        Button advancedBtn;
        Button expertBtn;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.dialog_level_select, container, false);

            title = (TextView) v.findViewById(R.id.cool_levelSelect);
            beginnerBtn = (Button) v.findViewById(R.id.beginner_button);
            advancedBtn = (Button) v.findViewById(R.id.advanced_button);
            expertBtn = (Button) v.findViewById(R.id.expert_button);
            return v;
        }

        @Override
        public void onStart() {
            super.onStart();
            beginnerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), StageSelect.class);
                    intent.putExtra("Difficulty", 0);
                    startActivity(intent);
                    dismiss();
                }
            });
            advancedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), StageSelect.class);
                    intent.putExtra("Difficulty", 1);
                    startActivity(intent);
                    dismiss();
                }
            });
            expertBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), StageSelect.class);
                    intent.putExtra("Difficulty", 2);
                    startActivity(intent);
                    dismiss();
                }
            });

        }




    }
}
