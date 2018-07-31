package com.endrawan.dlingua.Fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.endrawan.dlingua.AddLanguageActivity;
import com.endrawan.dlingua.Classes.GridSpacing;
import com.endrawan.dlingua.Classes.LocationItem;
import com.endrawan.dlingua.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Endrawan made this on 24/11/2016.
 */
public class ChooseLocationFragment extends Fragment {

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mTotalLanguage;
        public TextView mLocationName;
        public RelativeLayout mBackgroundImage;

        public LocationViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTotalLanguage = (TextView) itemView.findViewById(R.id.totalCourse);
            mLocationName = (TextView) itemView.findViewById(R.id.locationName);
            mBackgroundImage = (RelativeLayout) itemView.findViewById(R.id.CardBackground);
        }
    }

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private GridLayoutManager mGridLayoutManager;

    private DatabaseReference mLocationReference;
    private FirebaseRecyclerAdapter<LocationItem, LocationViewHolder> mFirebaseAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_location, container, false);

        // Firebase UI
        mProgressBar = (ProgressBar) v.findViewById(R.id.ChooseLanguageProgress);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.RecyclerChooseLocation);
        mGridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        // New child entries
        mLocationReference = FirebaseDatabase.getInstance().getReference().child("Lokasi");
        mFirebaseAdapter = new FirebaseRecyclerAdapter<LocationItem,
                LocationViewHolder>(
                LocationItem.class,
                R.layout.item_grid_location,
                LocationViewHolder.class,
                mLocationReference) {
            @Override
            protected void populateViewHolder(final LocationViewHolder viewHolder,
                                              final LocationItem model, final int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.mLocationName.setText(model.getName());
                viewHolder.mTotalLanguage.setText("Uncountable");

                // Set the background list using glide
                Glide.with(getActivity().getApplicationContext()).load(model.getImgUrl())
                        .asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewHolder.mBackgroundImage.setBackground(drawable);
                        }
                    }
                });

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddLanguageActivity.LocationSelected = mFirebaseAdapter
                                .getRef(position).getKey();
                        AddLanguageActivity mActivity = (AddLanguageActivity) getActivity();
                        mActivity.changeToLanguage();
                    }
                });
            }
        };

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacing(2, dpToPx(4), true));
        mRecyclerView.setAdapter(mFirebaseAdapter);
        return v;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics()));
    }
}
