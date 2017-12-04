package com.brayadiaz.merka;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductoFragment extends Fragment {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private Context context;

    private String cant;
    String codeContent, codeFormat;

    private String uid, listName;
    private int cantPr;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    View v;

    FragmentManager fm;
    FragmentTransaction ft;
    Fragment fragment;

    SearchView searchView;

    private ArrayList<Productos> productosArrayList = new ArrayList<>();
    private ArrayList<Integer> idList = new ArrayList<>();
    private ArrayList<Integer> cadList = new ArrayList<>();

    public ProductoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_producto, container, false);

        context = getActivity();
        prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        uid = prefs.getString("uid", "No Data");
        listName = prefs.getString("ListName", "No Data");
        Log.d("ListNAme: ", listName);
        Log.d("UID: ", uid);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem menuItem = menu.findItem(R.id.buscarProd);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscar(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.buscarProd:
                //Intent intent = new Intent(getActivity(), NewListActivity.class);
                //startActivity(intent);
                break;
            case R.id.codeqr:

                fm = getActivity().getSupportFragmentManager();
                ft = fm.beginTransaction();
                fragment = new LectorFragment();
                ft.replace(R.id.frame,fragment).commit();

                //barCode();

                break;
        }
        return true;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;
        searchProd();
        loadDataProd();
        //loadData();
        init();
    }

    private void buscar(String textProd){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database.getReference("Productos");
        Query mQuery = myRef2.orderByChild("name").equalTo(textProd);

        Log.d("Sear", mQuery.toString());
        Log.d("Sear", textProd);

    }

    private void loadDataProd() {
        Log.d("key: ", "loadDataProd");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(uid).child("Listas").child(listName).child("Productos");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idList.clear();
                cadList.clear();
                String flag;
                for(DataSnapshot single:dataSnapshot.getChildren()){
                    flag = single.getKey().toLowerCase();
                    Log.d("key: ", flag);
                    //flag = single.child("id").getValue().toString();
                    //Log.d("code: ", flag);

                    if (single.child("id").exists() && single.child("cantidad").exists()){
                        int idLista, cantProd;
                        idLista = single.child("id").getValue(Integer.class);
                        cantProd = single.child("cantidad").getValue(Integer.class);
                        Log.d("key: ", String.valueOf(idLista));
                        idList.add(idLista);
                        cadList.add(cantProd);
                    }

                    //productosArrayList.add(single.getKey().toString());
                }

                //adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        loadData();
    }

    private void loadData() {
        Log.d("key1: ", "loadData");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("Productos");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productosArrayList.clear();
                String flag;
                for(DataSnapshot single:dataSnapshot.getChildren()){
                    flag = single.getKey().toLowerCase();
                    Log.d("key2: ", flag);
                    flag = single.child("id").getValue().toString();
                    Log.d("keyid: ", flag);
                    if (single.child("name").exists() && single.child("code").exists()
                            && single.child("precio").exists() && single.child("id").exists()){

                        for (int item = 0;item < idList.size();item++ ){
                            int idLis = idList.get(item);
                            //int idLis = 0;

                            Log.d("keyItem: ", String.valueOf(item));
                            Log.d("keyIdLis: ", Integer.toString(idLis));
                            Log.d("keyIdFB: ", single.child("id").getValue().toString());

                            if(single.child("id").getValue().toString().equals(String.valueOf(idLis))){
                                Log.d("key5:", String.valueOf(idLis));
                                Productos productos;
                                productos = new Productos(single.child("name").getValue().toString(),
                                        single.child("code").getValue().toString(),
                                        single.child("precio").getValue().toString(),
                                        single.child("id").getValue().toString(),cadList.get(item));
                                Log.d("keyIF: ", productos.getName());
                                productosArrayList.add(productos);
                            }
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

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProductosAdapter(getActivity(), productosArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void searchProd(){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(uid).child("Listas").child(listName);
        //myRef.addListenerForSingleValueEvent(new ValueEventListener() {
        Log.d("Prod", uid + " ::: " + listName);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Prod", dataSnapshot.child("Info").child("cantidad").toString());
                if (dataSnapshot.child("Info").child("cantidad").exists()) {
                    Log.d("Prod", "SearchProd");
                    cantPr = dataSnapshot.child("Info").child("cantidad").getValue(Integer.class);
                    Log.d("Prod", String.valueOf(cantPr));

                    if (cantPr == 0) {
                        Toast.makeText(getActivity(), "No hay productos para mostrar", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
