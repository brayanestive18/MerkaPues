package com.brayadiaz.merka;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CarsShopActivity extends NavigationActivity {

    private TextView mTextMessage;
    FrameLayout frameLayout;
    Fragment fragment;
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_shop);

        //frameLayout = (FrameLayout) findViewById(R.id.frame_car);

        //mTextMessage = (TextView) findViewById(R.id.message);
        /*fragment = new CarFragment();

        getSupportFragmentManager().beginTransaction().
                add(R.id.frame_car, fragment).commit();*/

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        CarFragment frangment = new CarFragment();
        ft.add(R.id.frame_car, frangment).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.but_car);
                    /*fragment = new CarFragment();
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.frame_car, fragment).commit();*/

                    fm = getSupportFragmentManager();
                    ft = fm.beginTransaction();

                    CarFragment frangment1 = new CarFragment();
                    ft.replace(R.id.frame_car, frangment1).commit();

                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.but_compras);
                    /*fragment = new MisComprasFragment();
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.frame_car, fragment).commit();*/

                    fm = getSupportFragmentManager();
                    ft = fm.beginTransaction();

                    MisComprasFragment frangment2 = new MisComprasFragment();
                    ft.replace(R.id.frame_car, frangment2).commit();

                    return true;
                /*case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;*/
            }
            return false;
        }

    };

}
