package com.brayadiaz.merka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

/**
 * Created by humor on 05/05/2017.
 */

public class FilterListFragment extends ListFragment implements AdapterView.OnItemClickListener{
    protected int posit;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        //posit = getArguments().getInt("position");
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Filtro");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<CustomList> list = new ArrayList<>();
        switch(posit){
            case 0:
                list = CustomList.addItem(R.drawable.ic_menu_gallery, R.drawable.ic_menu_send, "Primer elemento", "Detalle");
                break;
            case 1:
                //list = CustomList.COOTRANSBLAN(getActivity());
                break;
        }

        CustomListAdapter adapter  = new CustomListAdapter((getActivity()).getApplicationContext(), list);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    protected Intent intent;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        //Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        /*Handler handler = new Handler();
        intent = new Intent(getContext(), SeeRoutes_Show.class);
        TextView routeTitle = (TextView) view.findViewById(R.id.list_title);
        TextView routeNumber = (TextView) view.findViewById(R.id.list_detail);

        intent.putExtra(Tags.TAG_ROUTE_NAME, routeTitle.getText().toString());
        intent.putExtra(Tags.TAG_ROUTE_NUMBER, routeNumber.getText().toString());
        intent.putExtra(FindRoute_TripActivity.TAG_OPTION, "show");
        handler.postDelayed(delay, 150);*/
    }
    protected Runnable delay = new Runnable() {
        @Override
        public void run() {
            startActivity(intent);
        }
    };
}
