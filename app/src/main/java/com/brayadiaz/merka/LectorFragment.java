package com.brayadiaz.merka;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class LectorFragment extends Fragment {

    String codeContent, codeFormat;
    int flag = 0;

    private static final String TAG = "NewList";

    private String uid, listName;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    FragmentManager fm;
    FragmentTransaction ft;
    Fragment fragment;

    private ArrayList<Productos> productosArrayList = new ArrayList<>();
    private String cant;

    /*
        public LectorFragment() {
            // Required empty public constructor
        }
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        // use forSupportFragment or forFragment method to use fragments instead of activity
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(this.getString(R.string.scan));
        integrator.setResultDisplayDuration(0); // milliseconds to display result on screen after scan
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();

        Context context = getActivity();

        prefs = context.getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        editor = prefs.edit();
        uid = prefs.getString("uid", "No Data");
        listName = prefs.getString("ListName", "No Data");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("Users").child(uid).child("Listas").child(listName).child("Info");

        //myRef.setValue(0);

        Log.d("Fire1", listName);
        Log.d("Fire1", myRef1.toString());

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int canti;
                Log.d("Fire1", dataSnapshot.toString());
                if (dataSnapshot.child("cantidad").exists()) {
                    canti = dataSnapshot.child("cantidad").getValue(Integer.class);
                    cant = String.valueOf(canti);

                    Log.d("Fire1", cant + " -- Oncreate");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        //ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();

        if (scanningResult != null) {
            //we have a result
            codeContent = scanningResult.getContents();
            codeFormat = scanningResult.getFormatName();

            Log.d("Scan", codeContent);

            searchCode();

            // send received data
            //parentActivity.scanResultData(codeFormat,codeContent);

        }else{
            // send exception
            //parentActivity.scanResultData(new NoScanResultException(noResultErrorMsg));
        }
    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_producto, container, false);
    }
*/

    @Override
    public void onResume() {
        if ( flag == 1) {
            Log.d("Scan", "onResume " + String.valueOf(flag));
/*
            fm = getActivity().getSupportFragmentManager();
            ft = fm.beginTransaction();
            fragment = new ProductoFragment();
            ft.replace(R.id.frame,fragment).commit();
            */
        }
        super.onResume();
    }


    @Override
    public void onStop() {
        Log.d("Scan", "onStop");
        flag = 1;
        super.onStop();
    }


    private void addProd(String id){
        Log.d("Fire1", id);
        Log.d("Fire1", listName);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        /*DatabaseReference myRef1 = database.getReference("Users").child(uid).child("Listas").child(listName).child("Info");

        //myRef.setValue(0);

        Log.d("Fire1", listName);
        Log.d("Fire1", myRef1.toString());

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int canti;
                Log.d("Fire1", dataSnapshot.toString());
                if (dataSnapshot.child("cantidad").exists()) {
                    canti = dataSnapshot.child("cantidad").getValue(Integer.class);
                    cant = String.valueOf(canti);

                    Log.d("Fire1", cant);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        //Log.d("Fire1", cant);
        Log.d("Fire1", listName);

        DatabaseReference myRef = database.getReference("Users").child(uid).child("Listas").child(listName).
                child("Productos").child("prod"+cant).child("cantidad");

        Log.d("Fire1", myRef.toString());

        myRef.setValue(1);

        myRef = database.getReference("Users").child(uid).child("Listas").child(listName).
                child("Productos").child("prod"+cant).child("id");
        myRef.setValue(Integer.valueOf(id));

        myRef = database.getReference("Users").child(uid).child("Listas").child(listName).child("Info").child("cantidad");
        int canti = Integer.valueOf(cant);
        canti = canti + 1;
        myRef.setValue(canti);

        //myRef.setValue((String.valueOf(cant)+1));
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();
        fragment = new ProductoFragment();
        ft.replace(R.id.frame,fragment).commit();
    }

    private void searchCode() {
        Log.d("Code: ", "loadData");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("Productos");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productosArrayList.clear();
                String flag;
                for(DataSnapshot single:dataSnapshot.getChildren()){
                    flag = single.getKey().toLowerCase();
                    Log.d("Code: ", flag);
                    flag = single.child("id").getValue().toString();
                    Log.d("Code: ", flag);
                    if (single.child("name").exists() && single.child("code").exists()
                            && single.child("precio").exists() && single.child("id").exists()){

                            if(single.child("code").getValue().toString().equals(codeContent)){

                                Log.d("Code:", single.child("code").getValue().toString());
                                Log.d("Code:", codeContent + ", Code Equals");

                                Log.d("Fire", single.child("id").getValue().toString());

                                addProd(single.child("id").getValue().toString());

                                /*Productos productos;
                                productos = new Productos(single.child("name").getValue().toString(),
                                        single.child("code").getValue().toString(),
                                        single.child("precio").getValue().toString(),
                                        single.child("id").getValue().toString(),cadList.get(item));
                                Log.d("keyIF: ", productos.getName());
                                productosArrayList.add(productos);*/
                            } else {
                                Toast.makeText(getActivity(), "No se encontro el producto", Toast.LENGTH_SHORT).show();
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                fragment = new ProductoFragment();
                                ft.replace(R.id.frame,fragment).commit();
                            }

                        /*
                        Productos productos;
                        productos = new Productos(single.child("name").getValue().toString(),
                                single.child("code").getValue().toString(),
                                single.child("precio").getValue().toString(),
                                single.child("id").getValue().toString());
                        Log.d("IF: ", productos.getName());
                        productosArrayList.add(productos);
                        */
                    }
                    //productosArrayList.add(single.getKey().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
