package com.brayadiaz.merkapues;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 56789;
    private  String correoR, contrasenaR, correoL = "", contrasenaL = "";
    private EditText eCorreo, eContrasena;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private int logId; // 1.Facebook, 2.Google, 3.Manual

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));

        // --------------------------- Login Google ----------------------------------------------

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"Error Login",Toast.LENGTH_SHORT).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        // --------------------------- Login Facebook --------------------------------------------

        //Recibimos informacion de Facebook

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(),"Login Exitoso",Toast.LENGTH_SHORT).show();
                logId = 1;
                goMainActivity();
            }


            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Login Cacelado",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Error en el login",Toast.LENGTH_SHORT).show();
            }
        });



        // ---------------- Obtener Hash -------------------------------------
        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.brayadiaz.merkapues",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                System.out.println(Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        */
        //-----------------------------------------------------------------------------//

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void goMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("correo",correoR);
        intent.putExtra("contrasena", contrasenaR);
        startActivity(intent);
        finish();
    }

    public void iniciar(View view) {
        correoL = eCorreo.getText().toString();
        contrasenaL = eContrasena.getText().toString();

        if (correoL.equals("") || contrasenaL.equals("")){
            Toast.makeText(this,"Por Favor, Ingrese todos los campos",Toast.LENGTH_SHORT).show();
        }
        else if (correoL.equals(correoR) && contrasenaL.equals(contrasenaR)){ //correoL.equals(correoR) && contrasenaL.equals(contrasenaR)
            goMainActivity();
        }
        else {
            Toast.makeText(this,"Correo y/o Contraseña son incorrectos",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1234 && resultCode == RESULT_OK){                        //Registro
            correoR = data.getExtras().getString("correo");
            contrasenaR = data.getExtras().getString("contrasena");
            //Toast.makeText(this,correoR,Toast.LENGTH_SHORT).show();
            Log.d("correo", correoR);
            Log.d("contrasena", contrasenaR);
        } else if (requestCode == RC_SIGN_IN) {                                      // Login Google
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {                                                                    // Login Facebook
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("google", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("Nombre", acct.getDisplayName());
            logId = 2;
            goMainActivity();
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(),"Error en el login",Toast.LENGTH_SHORT).show();
        }
    }

    public void registro(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivityForResult(intent, 1234);
    }
}
