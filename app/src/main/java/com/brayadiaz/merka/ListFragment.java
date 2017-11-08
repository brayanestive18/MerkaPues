package com.brayadiaz.merka;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    RecyclerView.Adapter adapter;
    View v;

    private ArrayList<Productos> productosArrayList = new ArrayList<>();

    public ListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;

        loadData();
        init();
    }

    private void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Productos");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productosArrayList.clear();
                String flag;
                for(DataSnapshot single:dataSnapshot.getChildren()){
                    flag = single.getKey().toLowerCase();
                    Log.d("key: ", flag);
                    flag = single.child("id").getValue().toString();
                    Log.d("code: ", flag);
                    if (single.child("name").exists() && single.child("code").exists()
                            && single.child("precio").exists() && single.child("id").exists()){

                        Productos productos;
                        productos = new Productos(single.child("name").getValue().toString(),
                                single.child("code").getValue().toString(),
                                single.child("precio").getValue().toString(),
                                single.child("id").getValue().toString());
                        Log.d("IF: ", productos.getName());
                        productosArrayList.add(productos);
                    }
                    //productosArrayList.add(single.getKey().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductosAdapter(getActivity(), productosArrayList);
        recyclerView.setAdapter(adapter);
    }
}
