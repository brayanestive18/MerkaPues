package com.brayadiaz.merkapues;

import java.util.ArrayList;

/**
 * Created by HOME-I on 9/04/2017.
 */

public class CustomList {

    private int imageId;
    private int actionItem;
    private String title;
    private String detail;

    public CustomList(int imageId, int actionItem, String title, String detail) {
        this.imageId = imageId;
        this.actionItem = actionItem;
        this.title = title;
        this.detail = detail;
    }

    public int getImageId() {
        return imageId;
    }

    public int getActionItem() {
        return actionItem;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public static ArrayList<CustomList> addItem(int _imageId, int _actionItem, String _title, String _detail) {
        ArrayList<CustomList> users = new ArrayList<>();
        users.add(new CustomList(_imageId, _actionItem, _title, _detail));
        return users;
    }
}
