package com.brayadiaz.merka;


import android.*;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback{

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private MapView mapView;

    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        //mapView = new MapView(getContext());
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);



        /*mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.onCreate(savedInstanceState);
        mapFragment.getMapAsync(this);*/

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*final LatLng udea = new LatLng(6.268163, -75.567634);
        mMap.addMarker(new MarkerOptions().position(udea).title("Marker in Udea"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(udea, 15));*/

        final LatLng Pacho = new LatLng(6.268162, -75.567705);
        mMap.addMarker(new MarkerOptions().position(Pacho).title("Tienda Don Pacho"));//.
                //icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_radius)));

        final LatLng Pedro = new LatLng(6.270296 , -75.560285);
        mMap.addMarker(new MarkerOptions().position(Pedro).title("Supermercado Don Pedro"));

        final LatLng cuatro = new LatLng(6.259567  , -75.561763);
        mMap.addMarker(new MarkerOptions().position(cuatro).title("Supermercado Cuatro Esquinas"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Pacho, 17));

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);

/*
        LatLng myLocation = new LatLng(13.0810, 80.2740);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(myLocation.latitude, myLocation.longitude), 11.0f), 1500, null);*/

    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
