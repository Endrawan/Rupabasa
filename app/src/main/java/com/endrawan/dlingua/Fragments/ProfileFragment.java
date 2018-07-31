package com.endrawan.dlingua.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.endrawan.dlingua.Classes.User;
import com.endrawan.dlingua.MenuActivity;
import com.endrawan.dlingua.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Endrawan made this on 30/11/2016.
 */
public class ProfileFragment extends Fragment{

    User mUser;

    ImageView mLogout;
    CircleImageView mProfilePic;
    TextView mName, mUsername, mExp, mLevel, mMoney;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    // Facebook API
    CallbackManager callbackManager;
    //LoginButton loginButton;
    AccessTokenTracker accessTokenTracker;
    ShareButton shareButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        // Set user
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Facebook
        callbackManager = CallbackManager.Factory.create();

        shareButton = (ShareButton) v.findViewById(R.id.fb_share_button);
        mLogout = (ImageView) v.findViewById(R.id.mLogout);
        mProfilePic = (CircleImageView) v.findViewById(R.id.profilePic);
        mName = (TextView) v.findViewById(R.id.name);
        mUsername = (TextView) v.findViewById(R.id.username);
        mExp = (TextView) v.findViewById(R.id.expText);
        mLevel = (TextView) v.findViewById(R.id.levelText);
        mMoney = (TextView) v.findViewById(R.id.moneyText);

        Glide.with(getActivity())
                .load(mFirebaseUser.getPhotoUrl())
                .into(mProfilePic);

        MenuActivity activity = (MenuActivity) getActivity();
        mUser = activity.getCurrentUser();

        mName.setText(mFirebaseUser.getDisplayName());
        mUsername.setText("@" + mUser.getUsername());
        mExp.setText(String.valueOf(mUser.getExp()));
        mLevel.setText(String.valueOf(mUser.getLevel()));
        mMoney.setText(String.valueOf(mUser.getRupiah()));

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Keluar")
                        .setMessage("Apakah kamu yakin ingin keluar?")
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                MenuActivity activity = (MenuActivity) getActivity();
                                activity.signOut();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        // FB
        /*loginButton.setReadPermissions(Arrays.asList("user_status"));
        loginButton.setFragment(this);*/

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://1cak.com/"))
                .setContentDescription("Hey aku sudah mencapai level " + mUser.getLevel() +
                        ", dengan point raihan " + mUser.getExp()
                + " dan total uang " + mUser.getRupiah() + " Rupiah!")
                .setContentTitle("Cak test cak!")
                .setImageUrl(Uri.parse("http://1cak.com/images/logo3.png"))
                .build();
        shareButton.setShareContent(content);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Toast.makeText(getActivity(), "Token not found!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Your token is : " +
                            currentAccessToken.getToken(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}
