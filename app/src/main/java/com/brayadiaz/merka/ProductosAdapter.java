package com.brayadiaz.merka;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by braya on 8/11/2017.
 */

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProducttosViewHolder> {

    Context context;
    private ArrayList<Productos> productosArrayList;

    public ProductosAdapter(Context context, ArrayList<Productos> productosArrayList) {
        this.context = context;
        this.productosArrayList = productosArrayList;
    }

    @Override
    public ProducttosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View iterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prod_item, parent, false);
        ProducttosViewHolder producttosViewHolder = new ProducttosViewHolder(iterView);
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

    public static class ProducttosViewHolder extends RecyclerView.ViewHolder{

        private TextView tID, tName, tPrecio, tCode;

        public ProducttosViewHolder(View itemView) {
            super(itemView);

            //tID = itemView.findViewById(R.id.tID);
            tName = itemView.findViewById(R.id.tName);
            tPrecio = itemView.findViewById(R.id.tPrecio);
            tCode = itemView.findViewById(R.id.tCode);

        }

        public void bindProductos(Productos productos, Context context){
            //tID.setText(productos.getUid());
            tName.setText(productos.getName());
            tPrecio.setText(productos.getPrecio());
            tCode.setText(productos.getCode());
        }

    }

}
