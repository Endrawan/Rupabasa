package com.endrawan.dlingua;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.endrawan.dlingua.Classes.SubCourseItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Endrawan made this on 30/12/2016.
 */
public class SectionsActivity extends AppCompatActivity{

    public static class SubCourseViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mTitle;
        public CircleImageView mImage;

        public SubCourseViewHolder(View v) {
            super(v);
            mView = v;
            mTitle = (TextView) v.findViewById(R.id.mTitle);
            mImage = (CircleImageView) v.findViewById(R.id.mImage);
        }
    }

    String mCourseTitle, mCourseImgUrl, mCourseKey;
    ImageView mBackground;
    Toolbar mToolbar;
    RecyclerView mRecyclerView;

    FirebaseRecyclerAdapter<SubCourseItem, SubCourseViewHolder> mSubCourseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mBackground = (ImageView) findViewById(R.id.main_backdrop);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mCourseImgUrl = extras.getString("imgCourseUrl");
            mCourseTitle = extras.getString("courseTitle");
            mCourseKey = extras.getString("courseKey");
            Glide.with(getApplicationContext())
                    .load(mCourseImgUrl)
                    .into(mBackground);
            mToolbar.setTitle(mCourseTitle);
        }


        // Firebase Variables
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        Query mQuery = mFirebaseDatabase.getReference().child("SubCourse")
                .orderByChild("CourseKey").equalTo(mCourseKey);

        // RecyclerView Adapter
        mSubCourseAdapter = new FirebaseRecyclerAdapter<SubCourseItem, SubCourseViewHolder>(
                SubCourseItem.class,
                R.layout.item_linear_subcourse,
                SubCourseViewHolder.class, mQuery) {
            @Override
            protected void populateViewHolder(SubCourseViewHolder viewHolder,
                                              final SubCourseItem model, final int position) {
                viewHolder.mTitle.setText(model.getName());
                Glide.with(getApplicationContext())
                        .load(model.getImgUrl())
                        .into(viewHolder.mImage);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent questionIntent = new Intent(getApplicationContext(),
                                QuestionActivity.class);
                        questionIntent.putExtra("subcourseKey", mSubCourseAdapter
                                .getRef(position).getKey());
                        startActivity(questionIntent);
                        finish();
                    }
                });
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mSubCourseAdapter);
    }
}
