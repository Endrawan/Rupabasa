package com.endrawan.dlingua;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.endrawan.dlingua.Classes.ShopItemEffects;
import com.endrawan.dlingua.Classes.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Endrawan made this on 26/01/2017.
 */
public class ResultActivity extends AppCompatActivity {

    int rightAnswer, wrongAnswer, totalQuestion, RupiahGet = 0, ExpGet = 0;
    int newExp, newRupiah;
    Button Bdone;
    TextView TVright, TVwrong;

    // Test Chart
    int[] dataObjects = {1, 2, 3, 4, 5};

    List<Entry> entries = new ArrayList<>();

    // Firebase
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userDatabase = mFirebaseDatabase.getReference("Users")
            .child(mFirebaseUser.getUid());
    DatabaseReference itemReference = mFirebaseDatabase.getReference("UserItems/")
            .child(mFirebaseUser.getUid());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Set local variable(s)
        final ShopItemEffects effect = new ShopItemEffects(getApplicationContext());

        Bdone = (Button) findViewById(R.id.Bdone);
        TVright = (TextView) findViewById(R.id.totalTrue);
        TVwrong = (TextView) findViewById(R.id.totalFalse);
        LineChart chart = (LineChart) findViewById(R.id.chart);

        // Test
        for(int data: dataObjects) {
            entries.add(new Entry(data, data));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

        // Get the value from intent
        Bundle extras = getIntent().getExtras();
        totalQuestion = extras.getInt("totalQuestion");
        rightAnswer = extras.getInt("rightAnswer");
        wrongAnswer = extras.getInt("wrongAnswer");

        // Set total value
        ExpGet += rightAnswer;
        RupiahGet += rightAnswer * 1000;

        if (totalQuestion == rightAnswer) {
            ExpGet += 10;
            RupiahGet += 5000;
        }

        // Set score for database
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User mUser = dataSnapshot.getValue(User.class);
                newExp = mUser.getExp() + ExpGet;
                newRupiah = mUser.getRupiah() + RupiahGet;
                mUser.setExp(newExp);
                mUser.setRupiah(newRupiah);
                userDatabase.setValue(mUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ResultActivity.this, "Error : " + databaseError,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Set first view
        TVright.setText("Total Benar : " + rightAnswer);
        TVwrong.setText("Total Salah : " + wrongAnswer);

        // Activate Item
        itemReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    boolean status = snap.getValue(boolean.class);
                    switch (snap.getKey()) {
                        case "Double Exp":
                            if (status)
                                effect.doubleEXP(ExpGet, newExp, userDatabase.child("exp"));
                            break;
                        case "Double Rupiah":
                            if(status)
                                effect.doubleRupiah(RupiahGet, newRupiah,
                                        userDatabase.child("rupiah"));
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Bdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
