package com.endrawan.dlingua;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.endrawan.dlingua.Fragments.ChooseLanguageFragment;
import com.endrawan.dlingua.Fragments.ChooseLocationFragment;

public class AddLanguageActivity extends AppCompatActivity {

    public static String LocationSelected;
    private Fragment mFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_language);

        // The Toolbar
        mToolbar = (Toolbar) findViewById(R.id.ToolbarAddLanguage);
        //setSupportActionBar(mToolbar);
        mToolbar.setTitle("Tambah Bahasa Baru");

        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragment = new ChooseLocationFragment();
        mFragmentTransaction.add(R.id.FragmentContainer, mFragment);
        mFragmentTransaction.commit();
    }

    public void changeToLanguage() {
        mFragment = new ChooseLanguageFragment();
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.FragmentContainer, mFragment);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }
}
