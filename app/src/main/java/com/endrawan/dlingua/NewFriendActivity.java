package com.endrawan.dlingua;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.endrawan.dlingua.Classes.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class NewFriendActivity extends AppCompatActivity {

    public static class  SearchViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView mName;
        public ImageView mImage;

        public SearchViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mName = (TextView) itemView.findViewById(R.id.mTitle);
            mImage = (ImageView) itemView.findViewById(R.id.mImage);
        }
    }

    Button Bsearch;
    RecyclerView mRecyclerView;
    EditText ETusername;

    // Firebase
    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseRecyclerAdapter<User, SearchViewHolder> mSearchAdapter;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mFirebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);

        // Set the component
        Bsearch = (Button) findViewById(R.id.Bsearch);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        ETusername = (EditText) findViewById(R.id.searchView);

        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Bsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String username = ETusername.getText().toString();
                Query mQuery = mFirebaseDatabase.getReference("Users/")
                        .orderByChild("username").equalTo(username);

                mSearchAdapter = new FirebaseRecyclerAdapter<User, SearchViewHolder>(
                        User.class,
                        R.layout.item_linear_subcourse,
                        SearchViewHolder.class, mQuery) {
                    @Override
                    protected void populateViewHolder(SearchViewHolder viewHolder,
                                                      final User model, final int position) {
                        viewHolder.mName.setText("@" + model.getUsername());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addFriend(mSearchAdapter.getRef(position).getKey(),
                                        model.getUsername());
                            }
                        });
                    }
                };
                mRecyclerView.setAdapter(mSearchAdapter);
            }
        });
    }

    private void addFriend(String key, String name) {
        mFirebaseDatabase.getReference("UserFriends").child(mUser.getUid()).child(key)
                .setValue(name)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(NewFriendActivity.this, "Teman ditambah",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(NewFriendActivity.this, "Gagal Menambah Teman",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }
}
