package com.brayadiaz.merka;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewListActivity extends AppCompatActivity {

    private static final String TAG = "NewList";
    private EditText eName;
    private String name = "", uid, sCant;
    private int cant;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        eName = findViewById(R.id.eName);

        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        editor = prefs.edit();
        uid = prefs.getString("uid", "No Data");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(uid);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                cant = dataSnapshot.child("Info").child("cantLis").getValue(Integer.class);
                Log.d(TAG, "Cantidad:  " + cant);
                //cant = Integer.parseInt(sCant);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void cancelar(View view) {
        finish();
    }

    public void aceptar(View view) {
        name = eName.getText().toString();

        if (name.equals("")){
            Toast.makeText(this,"Por Favor, Ingrese El nombre de la lista",Toast.LENGTH_SHORT).show();
        } else {
            sCant = String.valueOf(cant);
            loadData(uid, name, sCant, cant);
            finish();
        }

    }

    private void loadData(String uid, String name, String sCant, int cant){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(uid).child("Listas").child("list"+sCant).child("Info").child("cantidad");
        myRef.setValue(0);
        myRef = database.getReference("Users").child(uid).child("Listas").child("list"+sCant).child("Info").child("id");
        myRef.setValue(cant);
        myRef = database.getReference("Users").child(uid).child("Listas").child("list"+sCant).child("Info").child("name");
        myRef.setValue(name);

        myRef = database.getReference().child("Users").child(uid).child("Info").child("cantLis");
        myRef.setValue(cant+1);
    }
}
