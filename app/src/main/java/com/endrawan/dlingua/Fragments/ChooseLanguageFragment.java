package com.endrawan.dlingua.Fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.endrawan.dlingua.Classes.LanguageItem;
import com.endrawan.dlingua.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Endrawan made this on 02/12/2016.
 */
public class ChooseLanguageFragment extends Fragment {
    public static class LanguageViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mLanguageName;
        public RelativeLayout mBackgroundImage;

        public LanguageViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mLanguageName = (TextView) itemView.findViewById(R.id.languageName);
            mBackgroundImage = (RelativeLayout) itemView.findViewById(R.id.CardBackground);
        }
    }

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private GridLayoutManager mGridLayoutManager;

    private DatabaseReference mLanguageReference;
    private FirebaseRecyclerAdapter<LanguageItem, LanguageViewHolder> mFirebaseAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_language, container, false);

        // Firebase UI
        mProgressBar = (ProgressBar) v.findViewById(R.id.ChooseLanguageProgress);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.RecyclerChooseLanguage);
        mGridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        // New Child entries
        mLanguageReference = FirebaseDatabase.getInstance().getReference().child("Bahasa");
        Query CallTheLanguage = mLanguageReference.orderByChild("LocationKey")
                .equalTo(AddLanguageActivity.LocationSelected);
        mFirebaseAdapter = new FirebaseRecyclerAdapter<LanguageItem,
                LanguageViewHolder>(
                LanguageItem.class,
                R.layout.item_grid_language,
                LanguageViewHolder.class,
                CallTheLanguage) {
            @Override
            protected void populateViewHolder(final LanguageViewHolder viewHolder,
                                              final LanguageItem model, final int position) {
                mProgressBar.setVisibility(ProgressBar.GONE);
                viewHolder.mLanguageName.setText(model.getName());

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
                        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase.getInstance().getReference().child("UsersBahasa")
                                .child(mFirebaseUser.getUid())
                                .child(mFirebaseAdapter.getRef(position).getKey())
                                .setValue(model.getName())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    getActivity().finish();
                                }
                            }
                        });
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
