package com.endrawan.dlingua.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.endrawan.dlingua.Classes.ShopItem;
import com.endrawan.dlingua.DialogFragments.ItemDetailDialogF;
import com.endrawan.dlingua.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;

/**
 * Endrawan made this on 28/01/2017.
 */
public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    List<ShopItem> itemsList = new ArrayList<>();
    Context mContext;
    FragmentManager fm;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mMyItemReference = mFirebaseDatabase.getReference("UserItems/");

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView mItemName;
        public FButton mBuy;
        public ImageView mItemImage;

        public ShopViewHolder(View v) {
            super(v);
            mView = v;
            mItemName = (TextView) v.findViewById(R.id.mItemName);
            mBuy = (FButton) v.findViewById(R.id.mBuy);
            mItemImage = (ImageView) v.findViewById(R.id.mItemImage);
        }
    }

    public ShopAdapter(Context mContext, FragmentManager fm) {
        this.mContext = mContext;
        this.fm = fm;
        setShopItems();
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_shop, parent, false);

        return new ShopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ShopViewHolder holder, int position) {
        final ShopItem item = itemsList.get(position);
        mMyItemReference.child(mFirebaseAuth.getCurrentUser().getUid()).child(item.getName())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean status = dataSnapshot.getValue(boolean.class);
                    if (status) {
                        buttonAlreadyBought(holder);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.mItemName.setText(item.getName());
        holder.mItemImage.setImageResource(item.getDrawable());
        holder.mBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name", item.getName());
                bundle.putInt("price", item.getPrice());
                bundle.putString("desc", item.getInformation());
                bundle.putInt("image", item.getDrawable());

                ItemDetailDialogF mDialog = new ItemDetailDialogF();
                mDialog.setArguments(bundle);
                mDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                mDialog.show(fm, null);
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    private void setShopItems() {
        itemsList.add(new ShopItem("Double Exp", "beuki gaje", R.drawable.img_exp_double, 1000));
        itemsList.add(new ShopItem("Double Rupiah", "beuki gaje",
                R.drawable.img_rupiah_double, 2000));
    }

    private void buttonAlreadyBought(ShopViewHolder holder) {
        holder.mBuy.setButtonColor(R.color.fbutton_color_emerald);
        holder.mBuy.setText("Dibeli");
        holder.mBuy.setEnabled(false);
    }

}
