package com.endrawan.dlingua.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endrawan.dlingua.Adapters.ShopAdapter;
import com.endrawan.dlingua.Classes.GridSpacing;
import com.endrawan.dlingua.R;

/**
 * Endrawan made this on 28/01/2017.
 */
public class ShopFragment extends Fragment {

    FragmentManager fm;
    private static final int ITEM_COLUMN = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shop, container, false);
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.mRecyclerView);

        ShopAdapter mShopAdapter = new ShopAdapter(getActivity(), getChildFragmentManager());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), ITEM_COLUMN));
        mRecyclerView.addItemDecoration(new GridSpacing(ITEM_COLUMN, dpToPx(10), true));
        mRecyclerView.setAdapter(mShopAdapter);
        // Setting the header
        fm = getChildFragmentManager();
        fm.beginTransaction().add(R.id.header, new HeaderFragment()).commit();

        return v;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics()));
    }
}
