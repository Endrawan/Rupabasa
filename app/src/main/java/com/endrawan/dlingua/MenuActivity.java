package com.endrawan.dlingua;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.endrawan.dlingua.Classes.User;
import com.endrawan.dlingua.Fragments.FriendsFragment;
import com.endrawan.dlingua.Fragments.LearningFragment;
import com.endrawan.dlingua.Fragments.ProfileFragment;
import com.endrawan.dlingua.Fragments.ShopFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Endrawan made this on 27/11/2016.
 */
public class MenuActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    User mUser;

    // Firebase
    FirebaseAnalytics mFirebaseAnalytics;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    FrameLayout FragmentContainer;
    FragmentManager fm;
    Fragment learningFragment = new LearningFragment(), leaderboardFragment = new FriendsFragment(),
            shopFragment = new ShopFragment(), profileFragment = new ProfileFragment();

    BottomBar mBottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_menu);

        // User Session
        if (mFirebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(MenuActivity.this, SignInActivity.class));
            finish();
        }

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SourceSansPro-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        // Get the Current User
        mFirebaseDatabase.getReference("Users/").child(mFirebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MenuActivity.this, "Error : " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });

        // Init component(s)
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        FragmentContainer = (FrameLayout) findViewById(R.id.FragmentContainer);

        fm = getSupportFragmentManager();
        setFirstTimeOpen();

        // Get the API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* Fragment Activity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_practice:
                        fm.beginTransaction()
                                .replace(R.id.FragmentContainer, learningFragment).commit();
                        break;
                    case R.id.tab_social:
                        fm.beginTransaction()
                                .replace(R.id.FragmentContainer, leaderboardFragment).commit();
                        break;
                    case R.id.tab_shop:
                        fm.beginTransaction()
                                .replace(R.id.FragmentContainer, shopFragment).commit();
                        break;
                    case R.id.tab_profile:
                        fm.beginTransaction()
                                .replace(R.id.FragmentContainer, profileFragment).commit();
                        break;
                }
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    private void setFirstTimeOpen() {
        fm.beginTransaction().add(R.id.FragmentContainer, learningFragment).commit();
    }

    public void signOut() {
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public User getCurrentUser() {
        return mUser;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}