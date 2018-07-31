package com.endrawan.dlingua.Fragments;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.endrawan.dlingua.Adapters.CourseAdapter;
import com.endrawan.dlingua.AddLanguageActivity;
import com.endrawan.dlingua.Classes.LanguageItem;
import com.endrawan.dlingua.MenuActivity;
import com.endrawan.dlingua.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Endrawan made this on 24/11/2016.
 */
public class LearningFragment extends Fragment {

    public static class MyLanguagesViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public TextView TVname;
        public RecyclerView RVcourse;
        public CourseAdapter mCourseAdapter;

        public MyLanguagesViewHolder(View itemView) {
            super(itemView);

            Context context = itemView.getContext();
            mView = itemView;
            TVname = (TextView) itemView.findViewById(R.id.languageName);
            RVcourse = (RecyclerView) itemView.findViewById(R.id.RVcourse);
            RVcourse.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false));
        }
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mCourseTitle;
        public ImageView mCourseImage;

        public CourseViewHolder(View v) {
            super(v);
            mView = v;
            mCourseTitle = (TextView) v.findViewById(R.id.CourseTitle);
            mCourseImage = (ImageView) v.findViewById(R.id.CourseImage);
        }
    }

    String LanguageKey;
    LinearLayout warning;
    private Context mContext;
    private Button mAddLanguage;
    private ProgressBar mProgressBar;
    private FragmentManager fm;

    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mLanguageReference;
    private FirebaseRecyclerAdapter<String, MyLanguagesViewHolder> mLanguageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_learning, container, false);
        mContext = getActivity().getApplicationContext();

        // Firebase UI
        mAddLanguage = (Button) v.findViewById(R.id.tambahBahasa);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.learningRecyclerView);
        warning = (LinearLayout) v.findViewById(R.id.friendNotFound);

        LinearLayoutManager mLinearLayout = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayout);

        // New child entries
        mLanguageReference = mFirebaseDatabase.getReference().child("Bahasa");
        DatabaseReference MyLanguageReference = mFirebaseDatabase.getReference()
                .child("UsersBahasa").child(mFirebaseUser.getUid());

        // Check data is exist or not
        MyLanguageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    mProgressBar.setVisibility(ProgressBar.GONE);
                    warning.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mLanguageAdapter = new FirebaseRecyclerAdapter<String, MyLanguagesViewHolder>(
                String.class,
                R.layout.item_learning_bahasa,
                MyLanguagesViewHolder.class,
                MyLanguageReference) {
            @Override
            protected void populateViewHolder(final MyLanguagesViewHolder viewHolder,
                                              final String model, final int position) {
                warning.setVisibility(View.GONE);
                LanguageKey = mLanguageAdapter.getRef(position).getKey();
                mLanguageReference.child(LanguageKey)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        LanguageItem mLanguageItem = dataSnapshot.getValue(LanguageItem.class);
                        viewHolder.TVname.setText(mLanguageItem.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(mContext, "Error : " + databaseError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                DatabaseReference CourseBahasaReference = mFirebaseDatabase.getReference()
                        .child("CourseBahasa").child(LanguageKey);
                viewHolder.mCourseAdapter = new CourseAdapter(String.class,
                        R.layout.item_grid_course, CourseViewHolder.class, CourseBahasaReference);
                viewHolder.mCourseAdapter.setContext(mContext);
                viewHolder.RVcourse.setAdapter(viewHolder.mCourseAdapter);

                mProgressBar.setVisibility(ProgressBar.GONE);
            }
        };

        mRecyclerView.setLayoutManager(mLinearLayout);
        mRecyclerView.setAdapter(mLanguageAdapter);

        // Other component(s)
        mAddLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddLanguageActivity.class));
            }
        });

        return v;
    }
}
