package com.brayadiaz.merka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 56789;
    private static final String TAG = "Login";
    private String correoR, contrasenaR, correoL = "", contrasenaL = "", user, toke, IDuser;
    private Uri url_photo;
    private EditText eCorreo, eContrasena;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private int logId; // 0.NO LOGIN 1.Facebook, 2.Google, 3.Manual

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    Intent intent = null;

    User user_class;

    DatabaseReference myRef;
    FirebaseDatabase database;

    GoogleApiClient mGoogleApiClient;
    private boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        /*firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Toast.makeText(LoginActivity.this, "loggeo",Toast.LENGTH_LONG).show();
                    crear_tabla(user);
                    goMainActivity();
                }

            }
        };*/

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
                prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                editor = prefs.edit();

                Toast.makeText(getApplicationContext(),"Login Exitoso",Toast.LENGTH_SHORT).show();
                logId = 1;

                Profile perfil = com.facebook.Profile.getCurrentProfile();

                user = perfil.getName();
                url_photo = perfil.getProfilePictureUri(300,300);

                GraphRequest.newMeRequest(
                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            if (response.getError() != null) {
                                Toast.makeText(getApplicationContext(),"No se obtuvo Email",Toast.LENGTH_SHORT).show();
                            } else {
                                // get email and id of the user
                                correoL = object.optString("email");
                            }
                        }
                }).executeAsync();

                //database = FirebaseDatabase.getInstance();
                //myRef = database.getReference("Users").child("user"+uid);

                editor.putString("user", IDuser);
                editor.putString("toke", toke);
                editor.putString("correo", correoL);
                editor.putString("name", user);
                editor.putString("url_photo", url_photo.toString());
                editor.commit();

                handleFacebookAccessToken(loginResult.getAccessToken());

                //goMainActivity();
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

    private void handleFacebookAccessToken(AccessToken token) {

        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (!(buscar(user))) {
                                crear_tabla(user);
                            }
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void goMainActivity() {
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();

        //ALmacenar el valor de optLog
        editor.putInt("optLog", logId);
        //editor.putString("correo", correoR);
        //editor.putString("contrasena", contrasenaR);
        editor.commit();

        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent = new Intent(LoginActivity.this, MenuDrawerActivity.class);
        //intent.putExtra("correo",correoR);
        //intent.putExtra("contrasena", contrasenaR);
        startActivity(intent);
        finish();
    }

    public void iniciar(View view) {
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        logId = prefs.getInt("optLog", 0); //editor.putInt("optLog", 0);
        //Toast.makeText(this, logId, Toast.LENGTH_SHORT).show();
        //Log.d("optlog", String.valueOf(logId));

        correoR = prefs.getString("correo", "No Data");
        contrasenaR = prefs.getString("contrasena", "No Data");
        //Log.d("correo", correoR);
        //Log.d("contrasena", contrasenaR);
        //Toast.makeText(this, correoR, Toast.LENGTH_SHORT).show();
        correoL = eCorreo.getText().toString();
        contrasenaL = eContrasena.getText().toString();

        //editor.commit();


        if (correoL.equals("") || contrasenaL.equals("")) {
            Toast.makeText(this, "Por Favor, Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        } /*else if (correoR.equals("No Data") || contrasenaR.equals("No Data") || logId == 0) {
            Toast.makeText(this, "Por Favor, Registrese", Toast.LENGTH_SHORT).show();
        } else if (correoL.equals(correoR) && contrasenaL.equals(contrasenaR)) { //correoL.equals(correoR) && contrasenaL.equals(contrasenaR)
            logId = 3;
            goMainActivity();
        } else {
            Toast.makeText(this, "Correo y/o Contraseña son incorrectos", Toast.LENGTH_SHORT).show();
        }*/
        else{
        // Login con Farebase
            mAuth.signInWithEmailAndPassword(correoL, contrasenaL)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                //Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });


        }

    }

    private void updateUI(FirebaseUser fuser) {
        if (fuser != null){
            goMainActivity();
        }
        else {
            Toast.makeText(LoginActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1234 && resultCode == RESULT_OK){                        //Registro
            correoR = data.getExtras().getString("correo");
            contrasenaR = data.getExtras().getString("contrasena");
            //Toast.makeText(this,correoR,Toast.LENGTH_SHORT).show();
            //Log.d("correo", correoR);
            //Log.d("contrasena", contrasenaR);
        } else if (requestCode == RC_SIGN_IN) {                                      // Login Google
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

            /*if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }*/

        } else {                                                                    // Login Facebook
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (!(buscar(user))) {
                                Log.w(TAG, "No deberia entrar aca");
                                crear_tabla(user);
                            }
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void handleSignInResult(GoogleSignInResult result) {
        //Log.d("google", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(getApplicationContext(),"Login Exitoso Google",Toast.LENGTH_SHORT).show();
            //Log.d("Nombre", acct.getDisplayName());



            logId = 2;

            prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            editor = prefs.edit();

            /*
            * GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount acct = result.getSignInAccount();
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                editor.putString("user", IDuser);
                editor.putString("toke", toke);
                editor.putString("correo", correoL);
                editor.putString("name", user);
                editor.putString("url_photo", url_photo.toString());
                editor.commit();
            * */

            user = acct.getDisplayName();
            correoL = acct.getEmail();
            url_photo = acct.getPhotoUrl();
            IDuser = acct.getId();

            editor.putString("user", IDuser);
            editor.putString("correo", correoL);
            editor.putString("name", user);
            editor.putString("url_photo", url_photo.toString());
            editor.commit();

            firebaseAuthWithGoogle(acct);

            //goMainActivity();
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(),"Error en el login",Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view) {
        intent = new Intent(LoginActivity.this, RegistroActivity.class);
        //startActivity(intent);
        //finish();

        startActivityForResult(intent, 1234);
    }

    //Ultimo

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        /*Intent intent = new Intent(MainActivity.this, UserProfile.class);
                        intent.putExtra("userProfile", json_object.toString());
                        startActivity(intent);*/
                    }

                });
        Bundle permission_param = new Bundle();
        //Movie picture;
        permission_param.putString("fields", "id,name,email, picture.width(300).height(300)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }


    private void crear_tabla(FirebaseUser fuser){
        String uid = fuser.getUid();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(uid).child("Info");
        user_class = new User(user, correoL, url_photo.toString(), "No Data", uid,0);
        myRef.setValue(user_class);
        myRef = database.getReference("Users").child(uid).child("Listas");
    }

    private boolean buscar (FirebaseUser fuser){
        final String uid = fuser.getUid();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(uid).exists()){
                    Log.w(TAG, "Usuario Encontrado: " + uid);
                    success = true;
                }
                else {
                    Log.w(TAG, "Usuario No Encontrado");
                    success = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        return success;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(firebaseAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mAuth.removeAuthStateListener(firebaseAuthListener);
    }

}
