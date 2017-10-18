package com.brayadiaz.merkapues;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HOME-I on 9/04/2017.
 */

public class CustomListAdapter extends ArrayAdapter<CustomList> {

    public CustomListAdapter(@NonNull Context context, ArrayList<CustomList> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CustomList itemCustomList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list, parent, false);
        }

        // Lookup view for data population
        //TextView tvTitle = (TextView) convertView.findViewById(R.id.list_title);
       // TextView tvDetail = (TextView) convertView.findViewById(R.id.list_detail);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.list_time);
        //ImageView iVAction = (ImageView) convertView.findViewById(R.id.list_action);

        // Populate the data into the template view using the data object
       /* tvTitle.setText(itemCustomList.getTitle());
        tvDetail.setText(itemCustomList.getDetail());*/
        ivIcon.setImageResource(itemCustomList.getImageId());

        if (itemCustomList.getActionItem() != 0){
            //iVAction.setImageResource(itemCustomList.getActionItem());
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
