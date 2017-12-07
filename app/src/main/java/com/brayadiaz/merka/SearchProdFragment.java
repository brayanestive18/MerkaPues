package com.brayadiaz.merka;


import android.content.Context;
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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchProdFragment extends Fragment implements SearchView.OnQueryTextListener{

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private String uid, listName, cant;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    View v;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private Context context;

    FragmentManager fm;
    FragmentTransaction ft;
    Fragment fragment;

    private ArrayList<Productos> productosArrayList = new ArrayList<>();
    private SearchView searchView;

    ProductosAdapterSearch productosAdapter = new ProductosAdapterSearch(getActivity(), productosArrayList, getActivity());

    public SearchProdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Productos");

        //productosAdapter = new ProductosAdapterSearch(getActivity(), productosArrayList);

        //Query query = myRef.orderByChild(String.valueOf(0));
        //query.addListenerForSingleValueEvent(queryListener);
        context = getActivity();
        prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();

        uid = prefs.getString("uid", "No Data");
        listName = prefs.getString("ListName", "No Data");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("Users").child(uid).child("Listas").child(listName).child("Info");

        //myRef.setValue(0);

        Log.d("Adapter", listName);
        Log.d("Adapter", myRef1.toString());

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int canti;
                Log.d("Adapter", dataSnapshot.toString());
                if (dataSnapshot.child("cantidad").exists()) {
                    canti = dataSnapshot.child("cantidad").getValue(Integer.class);
                    cant = String.valueOf(canti);

                    Log.d("Adapter", cant + " -- Oncreate");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Productos");

        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.buscarProd);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                productosAdapter.setFiltre(productosArrayList);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                productosAdapter.setFiltre(productosArrayList);
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

    private ValueEventListener queryListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            productosArrayList.clear();
            for (DataSnapshot single: dataSnapshot.getChildren()) {
                Productos productos;
                productos = new Productos(single.child("name").getValue().toString(),
                        single.child("code").getValue().toString(),
                        single.child("precio").getValue().toString(),
                        single.child("id").getValue().toString(),0);
                Log.d("IF: ", productos.getName());
                productosArrayList.add(productos);
            }

            /*productosAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPos = recyclerView.getChildLayoutPosition(view);
                    Productos name = productosArrayList.get(itemPos);
                    Toast.makeText(getActivity(),"Clic: " + name.getUid() ,Toast.LENGTH_SHORT).show();

                    editor.putString("ProdID", name.getUid());
                    editor.commit();




                fm = getActivity().getSupportFragmentManager();
                ft = fm.beginTransaction();
                fragment = new ProductoFragment();
                ft.replace(R.id.frame,fragment).commit();
                }
            });*/

            productosAdapter.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void loadProducto(){

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Productos");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot single: dataSnapshot.getChildren()) {
                    Productos productos;
                    productos = new Productos(single.child("name").getValue().toString(),
                            single.child("code").getValue().toString(),
                            single.child("precio").getValue().toString(),
                            single.child("id").getValue().toString(),0);
                    Log.d("IF: ", productos.getName());
                    productosArrayList.add(productos);
                }

                productosAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_prod, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;

        loadProducto();
        Log.d("ViewCreated", "Aqui Estoy");
        init();
    }

    private void init() {
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        productosAdapter.setOnProductosListener(new OnProductosListener() {
            @Override
            public void onProductoCLick(int position) {
                Log.d("Adapter", "CLick Frag: "+ position);
                Productos name = productosArrayList.get(position);
                Log.d("Adapter", "Onclick" + name.getUid());

                Log.d("Adapter", "Onclick" + name.getName());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users").child(uid).child("Listas").child(listName).
                        child("Productos").child("prod"+cant).child("cantidad");
                myRef.setValue(1);

                myRef = database.getReference("Users").child(uid).child("Listas").child(listName).
                        child("Productos").child("prod"+cant).child("id");
                myRef.setValue(Integer.valueOf(name.getUid()));
                int can;
                can = Integer.valueOf(cant);
                can = can + 1;

                myRef = database.getReference("Users").child(uid).child("Listas").child(listName).child("Info").child("cantidad");
                myRef.setValue(can);

                fm = getActivity().getSupportFragmentManager();
                ft = fm.beginTransaction();
                fragment = new ProductoFragment();
                ft.replace(R.id.frame,fragment).commit();


            }
        });

        //productosAdapter = new ProductosAdapterSearch(getActivity(), productosArrayList);

        Log.d("ViewCreated", "init");
        /*productosAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPos = recyclerView.getChildLayoutPosition(view);
                Productos name = productosArrayList.get(itemPos);
                Log.d("ViewCreated", "Onclick");
                Toast.makeText(getActivity(),"Clic: " + name.getUid() ,Toast.LENGTH_SHORT).show();

                editor.putString("ProdID", name.getUid());
                editor.commit();

                fm = getActivity().getSupportFragmentManager();
                ft = fm.beginTransaction();
                fragment = new ProductoFragment();
                ft.replace(R.id.frame,fragment).commit();
            }
        });*/

        recyclerView.setAdapter(productosAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        final ArrayList<Productos> productosFilra = filter(productosArrayList,newText);

        try{


            productosAdapter.setFiltre(productosFilra);
            productosAdapter.notifyDataSetChanged();
            //adapter.setFiltre(productosFilra);
            //recyclerView.setAdapter(productosAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    private ArrayList<Productos> filter(ArrayList<Productos> productos, String texto) {

        ArrayList<Productos>listaFiltrada = new ArrayList<>();
        try {

            texto = texto.toLowerCase();
            for (Productos product: productos){
                String prod = product.getName().toLowerCase();

                if (prod.contains(texto)){
                    listaFiltrada.add(product);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        return listaFiltrada;
    }
}
