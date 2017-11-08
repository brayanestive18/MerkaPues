package com.brayadiaz.merka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

public class MenuDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout fullLayout;
    private SharedPreferences preferences;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    FragmentManager fm;
    FragmentTransaction ft;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);
        TextView username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewUser);
        final ImageView profileImg = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);

        // Load preferences
        preferences = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        /**
         * Set username and email depending on
         * whether the user is a guest or logged in
         *  with Facebook or Google+ or as a Guest
         */
        if (preferences.getInt("optlog", 0) == 1 ){
            email.setText("");
            //username.setText(getString(R.string.guest));
        }else {
            /** Read data from shared preferences **/
            // Set name
            username.setText(preferences.getString("name", "No Data"));
            // Set email
            email.setText(preferences.getString("correo", "No Data"));
            // Create image from fb URL
            FetchImage fetchImage = new FetchImage(this, new FetchImage.AsyncResponse() {
                @Override
                public void processFinish(Bitmap bitmap) {
                    if (bitmap != null) {
                        Resources res = getResources();
                        RoundedBitmapDrawable roundBitmap = RoundedBitmapDrawableFactory
                                .create(res, bitmap);
                        roundBitmap.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
                        profileImg.setImageDrawable(roundBitmap);
                    }
                }
            });
            fetchImage.execute(preferences.getString("url_photo", "No Data"));
        }

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        fragment = new PromoFragment();
        ft.add(R.id.frame,fragment).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        Intent intent = null;
        ft = fm.beginTransaction();

        if (id == R.id.nav_promo) {
            fragment = new PromoFragment();
            ft.replace(R.id.frame,fragment).commit();
            //intent = new Intent(getApplication(), MainActivity.class);
            //startActivity(intent);
        } else if (id == R.id.nav_listas) {
            fragment = new ListFragment();
            ft.replace(R.id.frame,fragment).commit();
            //intent = new Intent(getApplication(), ListaActivity.class);
            //startActivity(intent);

        } else if (id == R.id.nav_carrito) {
            fragment = new CarFragment();
            ft.replace(R.id.frame,fragment).commit();
            //intent = new Intent(getApplication(), ListaActivity.class);
            //startActivity(intent);
        } else if (id == R.id.nav_map) {
            fragment = new MapsFragment();
            ft.replace(R.id.frame,fragment).commit();
            //intent = new Intent(getApplication(), MapsActivity.class);
            //startActivity(intent);
        }else if (id == R.id.nav_perfil) {
            fragment = new PerfilFragment();
            ft.replace(R.id.frame,fragment).commit();
            //intent = new Intent(getApplication(), PerfilActivity.class);
            //startActivity(intent);
        } else if (id == R.id.nav_close) {
            intent = new Intent(getApplication(), LoginActivity.class);
            //prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            editor = preferences.edit();

            editor.putInt("optLog", 0);
            editor.commit();

            LoginManager.getInstance().logOut();
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*protected Intent intent;

    protected Runnable delay = new Runnable() {
        @Override
        public void run() {
            startActivity(intent);
            finish();
        }
    };*/

}

/*switch (id) {

        case R.id.nav_close:
               /* fullLayout.closeDrawer(GravityCompat.START);
                if (!item.isChecked()) {
                                    //intent = new Intent(NavigationActivity.this, PerfilActivity.class);
                                    //handler.postDelayed(delay, 150);
                    //item.setChecked(true);


                    prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    editor = prefs.edit();

                    editor.putInt("optLog", 0);
                    editor.commit();

                    LoginManager.getInstance().logOut();

                    intent = new Intent(getApplication(), LoginActivity.class);
                    //intent.putExtra("contrasena",contrasenaR);
                    //intent.putExtra("correo", correoR);
                    //handler.postDelayed(delay, 150);
                    startActivity(intent);
                    item.setChecked(true);
                    finish();
                }*/

//return true;*/