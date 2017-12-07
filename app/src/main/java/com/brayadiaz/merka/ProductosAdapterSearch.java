package com.brayadiaz.merka;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by braya on 5/12/2017.
 */

public class ProductosAdapterSearch extends RecyclerView.Adapter<ProductosAdapterSearch.ProducttosViewHolder>{// implements View.OnClickListener{

    Context context;
    FragmentActivity fragmentActivity;
    private ArrayList<Productos> productosArrayList;
    private View.OnClickListener listener;
    private OnProductosListener onProductosListener;

    public void setOnProductosListener(OnProductosListener onProductosListener) {
        this.onProductosListener = onProductosListener;
    }

    public ProductosAdapterSearch(Context context, ArrayList<Productos> productosArrayList, FragmentActivity fragmentActivity) {
        this.context = context;
        this.productosArrayList = productosArrayList;
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public ProducttosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View iterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prod_item_search, parent, false);
        ProducttosViewHolder producttosViewHolder = new ProducttosViewHolder(iterView, onProductosListener);
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

    /*public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }*/

    public static class ProducttosViewHolder extends RecyclerView.ViewHolder {

        private TextView tID, tName, tPrecio, tCode;
        ArrayList<Productos> productosArrayList = new ArrayList<Productos>();
        Context context;
        FragmentActivity fragmentActivity;
        FragmentManager fm;
        FragmentTransaction ft;
        Fragment fragment;

        public ProducttosViewHolder(View itemView, final OnProductosListener onProductosListener) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION){
                        onProductosListener.onProductoCLick(position);
                    }
                    Log.d("Adapter", "CLick: " + getAdapterPosition());

                }
            });
            //tID = itemView.findViewById(R.id.tID);
            tName = itemView.findViewById(R.id.tName);
            tPrecio = itemView.findViewById(R.id.tPrecio);
            //tCode = itemView.findViewById(R.id.tCode);

        }

        public void bindProductos(Productos productos, Context context){
            //tID.setText(productos.getUid());
            tName.setText(productos.getName());
            tPrecio.setText("$ " + productos.getPrecio());
            //tCode.setText(productos.getCode());
        }

    }

    public void setFiltre(ArrayList<Productos> listProductos){
        this.productosArrayList = new ArrayList<>();
        this.productosArrayList.addAll(listProductos);
        //notifyDataSetChanged();

    }
}
