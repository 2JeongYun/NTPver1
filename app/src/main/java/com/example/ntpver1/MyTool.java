package com.example.ntpver1;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.ntpver1.item.Store;

import java.util.ArrayList;

public class MyTool {

    private static MyTool myTool;
    ArrayList<View> views;

    private MyTool() {
        views = new ArrayList<>();
    }

    public ArrayList<View> getAllViews(ViewGroup viewGroup) {
        views.removeAll(views);

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            views.add(viewGroup.getChildAt(i));
        }

        return views;
    }

    public ArrayList<View> getAllTableCheckBox(TableLayout tableLayout) {
        views.removeAll(views);

        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            View tableRow = tableLayout.getChildAt(i);

            if (tableRow instanceof TableRow) {
                for (int j = 0; j < ((TableRow) tableRow).getChildCount(); j++) {
                    View checkBox = ((TableRow) tableRow).getChildAt(j);
                    views.add(checkBox);
                }
            }
        }

        Log.i("myTool.tableCount", Integer.toString(views.size()));

        return views;
    }

    public static MyTool getInstance() {
        if (myTool == null) {
            myTool = new MyTool();
        }

        return myTool;
    }
}
