package com.endrawan.dlingua.Classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

/**
 * Endrawan made this on 06/02/2017.
 */
public class ShopItemEffects {

    private Context mContext;

    public ShopItemEffects(Context context) {
        this.mContext = context;
    }
    public void doubleEXP(int expGained, int currentExp, DatabaseReference locationRef) {
        locationRef.setValue(expGained + currentExp);
    }

    public void doubleRupiah(int rupiahGained, int currentRupiah, DatabaseReference locationRef) {
        locationRef.setValue(rupiahGained + currentRupiah);
    }
}
