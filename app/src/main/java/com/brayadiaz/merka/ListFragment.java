package com.brayadiaz.merka;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    RecyclerView recyclerView;
    //RecyclerView.Adapter adapter;
    ListaAdapter adapter;
    View v;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    FragmentManager fm;
    FragmentTransaction ft;
    Fragment fragment;

    private Context context;
    private String uid, nameList;
    private int cant;

    private ArrayList<String> listaArrayList = new ArrayList<>();

    public ListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        context = getActivity();
        prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        uid = prefs.getString("uid", "No Data");
        Log.d("UID: ", uid);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mAdd:
                Intent intent = new Intent(getActivity(), NewListActivity.class);
                startActivity(intent);
                break;
        }
        return true;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;
        SearchList();
        loadData();
        init();
    }

    private void loadData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(uid).child("Listas");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaArrayList.clear();
                for(DataSnapshot single:dataSnapshot.getChildren()){
                    if (single.child("Info").child("name").exists()){
                        nameList = single.child("Info").child("name").getValue().toString();
                        Log.d("Name: ", nameList);
                        listaArrayList.add(nameList);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume","YES");
        loadData();
    }

    private void SearchList() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(uid);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cant = dataSnapshot.child("Info").child("cantLis").getValue(Integer.class);

                if (cant == 0){
                    Toast.makeText(getActivity(),"No hay listas para mostrar",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void init() {
        recyclerView = v.findViewById(R.id.my_recycler_view_Lista);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ListaAdapter(getActivity(), listaArrayList);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPos = recyclerView.getChildLayoutPosition(view);
                String name = listaArrayList.get(itemPos);
                //Toast.makeText(getActivity(),"Clic: " + name ,Toast.LENGTH_SHORT).show();

                editor.putString("ListName", name);
                editor.commit();

                fm = getActivity().getSupportFragmentManager();
                ft = fm.beginTransaction();
                fragment = new ProductoFragment();
                ft.replace(R.id.frame,fragment).commit();
            }
        });

        //adapter = new ProductosAdapter(getActivity(), listaArrayList);
        recyclerView.setAdapter(adapter);

    }



}
