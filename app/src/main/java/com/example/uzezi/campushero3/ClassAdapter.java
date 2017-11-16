package com.example.uzezi.campushero3;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.uzezi.campushero3.Classes;
import com.example.uzezi.campushero3.R;

import java.util.ArrayList;

/**
 * Created by Christopher on 10/1/2017.
 */

public class ClassAdapter extends BaseAdapter {
    /**
     * Adapter context
     */
    Context mContext;
    LayoutInflater mInflater;
    private static ArrayList<Classes> listContact;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public ClassAdapter(Context context, ArrayList<Classes> results) {
        //super(context, layoutResourceId);

        //mContext = context;
        listContact = results;
        //mLayoutResourceId = layoutResourceId;
        mInflater = LayoutInflater.from(context);
        //mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listContact.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listContact.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /**
     * Returns the view for a specific item on the list
     */
    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.class_list_layout, null);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.titleTextView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtname.setText(listContact.get(position).getMsimpleName());

        return convertView;

    }

    static class ViewHolder{
        TextView txtname;
    }
}
/*
@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View row = convertView;
        View v = mInflater.inflate(R.layout.class_list_layout, null);
        /*
        final Classes currentClass = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentClass);

TextView title = (TextView) v.findViewById(R.id.titleTextView);
        title.setText("erwatasdg");
                ///title.setText(currentClass.getMsimpleName());

                return v;
                }
 */