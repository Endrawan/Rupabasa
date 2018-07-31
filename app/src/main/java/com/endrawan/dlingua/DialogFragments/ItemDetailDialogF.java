package com.endrawan.dlingua.DialogFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.endrawan.dlingua.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Endrawan made this on 08/02/2017.
 */
public class ItemDetailDialogF extends DialogFragment {
    TextView mName, mPrice, mDesc;
    ImageView mImage;
    Button mSubmit, mCancel;
    String name;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("UserItems/")
            .child(mFirebaseUser.getUid());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dfrag_shop_detail, container, false);

        // Get the view(s)
        mName = (TextView) v.findViewById(R.id.item_name);
        mPrice = (TextView) v.findViewById(R.id.item_price);
        mDesc = (TextView) v.findViewById(R.id.item_desc);
        mImage = (ImageView) v.findViewById(R.id.item_image);
        mSubmit = (Button) v.findViewById(R.id.button_submit);
        mCancel = (Button) v.findViewById(R.id.button_cancel);

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            mName.setText(name);
            mPrice.setText(String.valueOf(bundle.getInt("price")));
            mDesc.setText(bundle.getString("desc"));
            mImage.setImageResource(bundle.getInt("image"));

            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRef.child(name).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Berhasil membeli barang!",
                                        Toast.LENGTH_SHORT).show();
                                dismiss();
                            } else {
                                Toast.makeText(getActivity(), "Gagal membeli, coba lagi",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }
}
