package com.endrawan.dlingua.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.endrawan.dlingua.Classes.CourseItem;
import com.endrawan.dlingua.Fragments.LearningFragment;
import com.endrawan.dlingua.SectionsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Endrawan made this on 10/01/2017.
 */
public class CourseAdapter extends FirebaseRecyclerAdapter<String,
        LearningFragment.CourseViewHolder> {

    Context myContext;

    public CourseAdapter(Class<String> modelClass, int modelLayout,
                         Class<LearningFragment.CourseViewHolder> viewHolderClass,
                         DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(final LearningFragment.CourseViewHolder viewHolder,
                                      String model, final int position) {
        final String CourseKey = this.getRef(position).getKey();
        FirebaseDatabase.getInstance().getReference().child("Course").child(CourseKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final CourseItem mCourseItem = dataSnapshot.getValue(CourseItem.class);
                viewHolder.mCourseTitle.setText(mCourseItem.getName());
                Glide.with(myContext)
                        .load(mCourseItem.getImgUrl())
                        .into(viewHolder.mCourseImage);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(myContext, SectionsActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("imgCourseUrl", mCourseItem.getImgUrl());
                        i.putExtra("courseKey", CourseKey);
                        i.putExtra("courseTitle", mCourseItem.getName());
                        myContext.startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(mContext, "Error : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setContext(Context context) {
        this.myContext = context;
    }
}
