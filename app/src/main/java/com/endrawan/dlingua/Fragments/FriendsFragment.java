package com.endrawan.dlingua.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.endrawan.dlingua.Classes.User;
import com.endrawan.dlingua.NewFriendActivity;
import com.endrawan.dlingua.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Endrawan made this on 28/01/2017.
 */
public class FriendsFragment extends Fragment {

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public CircleImageView mImage;
        public TextView mName, mExp, mRupiah;
        public User mFriend;

        public LeaderboardViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mName = (TextView) itemView.findViewById(R.id.mName);
            mExp = (TextView) itemView.findViewById(R.id.mExp);
            mRupiah = (TextView) itemView.findViewById(R.id.mRupiah);
        }
    }

    Button BnewFriend;
    RecyclerView mRecyclerView;
    FragmentManager fm;
    LinearLayout warning;

    // Firebase
    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseRecyclerAdapter<String, LeaderboardViewHolder> mLeaderboardAdapter;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mFirebaseAuth.getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_friends, container, false);

        // Set the component(s)
        BnewFriend = (Button) v.findViewById(R.id.BnewFriend);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.mRecyclerView);
        warning = (LinearLayout) v.findViewById(R.id.friendNotFound);

        DatabaseReference mFriends = mFirebaseDatabase.getReference("UserFriends")
                .child(mUser.getUid());

        mLeaderboardAdapter = new FirebaseRecyclerAdapter<String, LeaderboardViewHolder>(
                String.class,
                R.layout.item_linear_friend,
                LeaderboardViewHolder.class, mFriends) {
            @Override
            protected void populateViewHolder(final LeaderboardViewHolder viewHolder, String model,
                                              int position) {
                String FriendKey = mLeaderboardAdapter.getRef(position).getKey();
                mFirebaseDatabase.getReference("Users/").child(FriendKey)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.mFriend = dataSnapshot.getValue(User.class);
                        viewHolder.mName.setText(viewHolder.mFriend.getUsername());
                        viewHolder.mExp.setText(String.valueOf(viewHolder.mFriend.getExp()));
                        viewHolder.mRupiah.setText(String.valueOf(viewHolder.mFriend.getRupiah()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Error : " + databaseError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mLeaderboardAdapter);

        fm = getChildFragmentManager();
        fm.beginTransaction().add(R.id.header, new HeaderFragment()).commit();

        BnewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewFriendActivity.class));
            }
        });

        return v;
    }
}
