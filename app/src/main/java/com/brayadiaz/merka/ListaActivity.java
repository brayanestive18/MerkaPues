package com.brayadiaz.merka;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaActivity extends MenuDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ListView listView;
    private ArrayList<Productos> productos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarList);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);



 /*       ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1,nombres);*/

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Productos");

        listView = (ListView) findViewById(R.id.list);
        productos = new ArrayList<Productos>();

        final Adapter adapter = new Adapter(this, productos);

        listView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    productos.add(userSnapshot.getValue(Productos.class));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListaActivity.this, String.valueOf(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    static class Adapter extends ArrayAdapter<Productos>{

        public Adapter(ListaActivity context, ArrayList<Productos> productos) {
            super(context, R.layout.list_item, productos);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.list_item, null);

            Productos productos = getItem(position);

            TextView tUid = item.findViewById(R.id.tID);
            tUid.setText(productos.getUid());

            TextView tName = item.findViewById(R.id.tName);
            tName.setText(productos.getName());

            TextView tCode = item.findViewById(R.id.tCode);
            tCode.setText(productos.getCode());

            TextView tPrecio = item.findViewById(R.id.tPrecio);
            tPrecio.setText(productos.getPrecio());

            return item;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){

            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }
}
