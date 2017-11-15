package com.brayadiaz.merka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private static final String TAG = "Registre";
    private String correo = "", contrasena = "", repContrasena = "", user = "", suid = null;
    EditText eCorreo, eContrasena, eRepContrasena, eUser;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    User user_class;

    private FirebaseAuth mAuth;

    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        eRepContrasena = (EditText) findViewById(R.id.eReContrasena);
        eUser = (EditText) findViewById(R.id.eUser);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ultID");//.child("ultID");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    suid = dataSnapshot.getValue(String.class);
                    Toast.makeText(RegistroActivity.this, String.valueOf(suid), Toast.LENGTH_LONG).show();
                }
                //uid = dataSnapshot.getKey();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Toast.makeText(RegistroActivity.this, String.valueOf("OnCreate"), Toast.LENGTH_LONG).show();

    }

    public void registrar(View view) {
        //Toast.makeText(RegistroActivity.this, String.valueOf("Get Ref"), Toast.LENGTH_LONG).show();


        //Toast.makeText(RegistroActivity.this, String.valueOf(suid), Toast.LENGTH_LONG).show();


        int uid = Integer.parseInt(suid) + 1;
        //myRef = database.getReference("ID").child("ultID");
        //myRef.setValue(String.valueOf(uid));

        //database = FirebaseDatabase.getInstance();
        //myRef = database.getReference("Users").child("user"+uid);

        correo = eCorreo.getText().toString();
        contrasena = eContrasena.getText().toString();
        repContrasena = eRepContrasena.getText().toString();
        user = eUser.getText().toString();

        if(correo.equals("") || contrasena.equals("") || user.equals("")){
            Toast.makeText(this,"Por Favor, Ingrese todos los campos",Toast.LENGTH_SHORT).show();
        }

        /*else if (!correo.contains("@")){
            Toast.makeText(this,"Ingrese un correo válido",Toast.LENGTH_SHORT).show();
        }*/

        else if (!validarEmail(correo)){
            //eCorreo.setError("Email no válido");
            Toast.makeText(this,"Ingrese un correo válido",Toast.LENGTH_SHORT).show();
        }

        else if (!contrasena.equals(repContrasena)){
            Toast.makeText(this,"Las Contraseñas no coinciden",Toast.LENGTH_SHORT).show();
        }

        else {

            //Toast.makeText(RegistroActivity.this, String.valueOf(uid), Toast.LENGTH_LONG).show();
            myRef = database.getReference("ultID");//.child("ultID");
            myRef.setValue(String.valueOf(uid));

            prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            editor = prefs.edit();



            //ALmacenar el valor de optLog
            int logId = 0;
            editor.putInt("optLog", logId);
            editor.putString("correo", correo);
            editor.putString("contrasena", contrasena);
            editor.putString("name", user);
            //editor.commit();
            editor.apply();
            
            Toast.makeText(this,"Registro Exitoso",Toast.LENGTH_SHORT).show();

            final Intent intent = new Intent();
            intent.putExtra("correo", correo);
            intent.putExtra("contrasena", contrasena);




            mAuth.createUserWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user, intent);
                                crear_tabla(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                //Toast.makeText(RegistroActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                updateUI(null, intent);
                            }

                            // ...
                        }
                    });




        }
    }

    private void updateUI(FirebaseUser user, Intent intent) {
        if (user != null){
            setResult(RESULT_OK, intent);

            finish();
        }
        else {
            Toast.makeText(RegistroActivity.this, "Authentication failed. Update", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void crear_tabla(FirebaseUser fuser){
        String uid = fuser.getUid();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(uid).child("Info");
        user_class = new User(user, correo, "No Data", contrasena, uid,0);
        myRef.setValue(user_class);
        myRef = database.getReference("Users").child(uid).child("Listas");
    }

}
/*
    if (!validarEmail("miEmail@gmail.com")){
    miEditText.setError("Email no válido")
}

*/