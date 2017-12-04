package com.brayadiaz.merka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends MenuDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String correoR, contrasenaR;
    private GoogleApiClient mGoogleApiClient;
    private int logId;
    FragmentManager fm;
    FragmentTransaction ft;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frame);
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            correoR = extras.getString("correo");
            contrasenaR = extras.getString("contrasena");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;

        switch (id){
            case R.id.mPerfil:
                intent = new Intent(MainActivity.this, PerfilActivity.class);
                //intent.putExtra("contrasena",contrasenaR);
                //intent.putExtra("correo", correoR);
                startActivity(intent);
                break;
            case R.id.mCerrar:
                prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                editor = prefs.edit();

                editor.putInt("optLog", 0);
                editor.commit();

                //if (logId == 1) {
                    LoginManager.getInstance().logOut(); // Facebook
                /*} else if (logId == 2) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // ...
                                }
                            });
                } else if (logId == 3) {
                    //Putextras correo nombre contraseña a login
                }
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
