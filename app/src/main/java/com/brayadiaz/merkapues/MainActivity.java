package com.brayadiaz.merkapues;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity {

    private String correoR, contrasenaR;
    private GoogleApiClient mGoogleApiClient;
    private int logId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        correoR = extras.getString("correo");
        contrasenaR = extras.getString("contrasena");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;

        switch (id){
            case R.id.mPerfil:
                intent = new Intent(MainActivity.this, PerfilActivity.class);
                intent.putExtra("contrasena",contrasenaR);
                intent.putExtra("correo", correoR);
                startActivity(intent);
                break;
            case R.id.mCerrar:

                if (logId == 1) {
                    LoginManager.getInstance().logOut(); // Facebook
                } else if(logId == 2) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // ...
                                }
                            });
                } else if(logId == 3) {
                    //Putextras correo nombre contraseña a login
                }
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
