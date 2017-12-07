package com.brayadiaz.merka;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by braya on 8/11/2017.
 */

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProducttosViewHolder> {

    Context context;
    private ArrayList<Productos> productosArrayList;
    private View.OnClickListener listener;
    private OnAddListener onAddListener;
    //private OnProductosListener onProductosListener;


    public void setOnAddListener(OnAddListener onAddListener) {
        this.onAddListener = onAddListener;
    }

    public ProductosAdapter(Context context, ArrayList<Productos> productosArrayList) {
        this.context = context;
        this.productosArrayList = productosArrayList;
    }

    @Override
    public ProducttosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View iterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prod_item, parent, false);
        ProducttosViewHolder producttosViewHolder = new ProducttosViewHolder(iterView, onAddListener);
        return producttosViewHolder;
    }

    @Override
    public void onBindViewHolder(ProducttosViewHolder holder, int position) {
        Productos item = productosArrayList.get(position);
        holder.bindProductos(item, context);
    }

    @Override
    public int getItemCount() {
        return productosArrayList.size();
    }

    /*public void setOnClickListener(OnAddListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }*/

    public static class ProducttosViewHolder extends RecyclerView.ViewHolder{

        private TextView tID, tName, tPrecio, tCode;
        private Button bAdd, bDis;

        public ProducttosViewHolder(View itemView, final OnAddListener onAddListener) {
            super(itemView);

            //tID = itemView.findViewById(R.id.tID);
            tName = itemView.findViewById(R.id.tName);
            tPrecio = itemView.findViewById(R.id.tPrecio);
            tCode = itemView.findViewById(R.id.tCode);
            bAdd = itemView.findViewById(R.id.mas);
            bDis = itemView.findViewById(R.id.menos);

            bAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onAddListener.onAddClicker(position);
                        Log.d("ADD", "ADD1: " + position);
                    }
                }
            });

            bDis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        onAddListener.onDisClicker(position);
                        Log.d("ADD", "DIS1: " + position);
                    }
                }
            });

        }

        public void bindProductos(Productos productos, Context context){
            //tID.setText(productos.getUid());
            tName.setText(productos.getName());
            tPrecio.setText("$ "+productos.getPrecio());
            tCode.setText(String.valueOf(productos.getCant()));
        }

    }

    public void setFiltre(ArrayList<Productos> listProductos){
        this.productosArrayList = new ArrayList<>();
        this.productosArrayList.addAll(listProductos);
        notifyDataSetChanged();

    }

}
