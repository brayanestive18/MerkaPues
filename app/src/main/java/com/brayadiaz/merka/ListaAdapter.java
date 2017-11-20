package com.brayadiaz.merka;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by braya on 19/11/2017.
 */

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListaViewHolder>{

    Context context;
    private ArrayList<String> listasArrayList;

    public ListaAdapter(Context context, ArrayList<String> listasArrayList){
        this.context = context;
        this.listasArrayList = listasArrayList;
    }

    @Override
    public ListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View iterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ListaViewHolder ListaViewHolder = new ListaViewHolder(iterView);
        return ListaViewHolder;
    }

    @Override
    public void onBindViewHolder(ListaViewHolder holder, int position) {
        String item = listasArrayList.get(position);
        holder.bindLista(item, context);
    }

    @Override
    public int getItemCount() {
        return listasArrayList.size();
    }

    public static class ListaViewHolder extends RecyclerView.ViewHolder{

        private TextView tName;

        public ListaViewHolder(View itemView) {
            super(itemView);

            tName = itemView.findViewById(R.id.tNameList);


        }

        public void bindLista(String lista, Context context){

            tName.setText(lista);
        }

    }


}