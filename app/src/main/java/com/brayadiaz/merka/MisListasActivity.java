package com.brayadiaz.merka;

import android.support.v4.app.Fragment;
import android.os.Bundle;

public class MisListasActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_listas);

        if (findViewById(R.id.containerView) != null){

            Fragment fragment = new Tab1Fragment();

            getSupportFragmentManager().beginTransaction().
                    add(R.id.containerView, fragment).commit();
        }
    }
}
/*
*
*
public class PromoActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        if (findViewById(R.id.containerView) != null){

            Fragment fragment = new TabFragment();

            getSupportFragmentManager().beginTransaction().
                    add(R.id.containerView, fragment).commit();
        }
    }
}
* */