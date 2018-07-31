package com.endrawan.dlingua.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.endrawan.dlingua.Classes.User;
import com.endrawan.dlingua.MenuActivity;
import com.endrawan.dlingua.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Endrawan made this on 29/01/2017.
 */
public class HeaderFragment extends Fragment {

    // Class
    User mUser;

    // Component(s)
    CircleImageView profilePic;
    TextView mName, mUsername;

    // Firebase
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_header, container, false);

        profilePic = (CircleImageView) v.findViewById(R.id.profilePic);
        mName = (TextView) v.findViewById(R.id.mName);
        mUsername = (TextView) v.findViewById(R.id.mUsername);

        MenuActivity activity = (MenuActivity) getActivity();
        mUser = activity.getCurrentUser();

        mName.setText(mFirebaseUser.getDisplayName());
        mUsername.setText("@" + mUser.getUsername());

        Glide.with(getActivity())
                .load(mFirebaseUser.getPhotoUrl())
                .into(profilePic);

        return v;
    }
}
