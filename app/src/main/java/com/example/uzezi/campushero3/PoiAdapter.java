package com.example.uzezi.campushero3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Christopher on 12/12/2017.
 */

public class PoiAdapter extends BaseAdapter{
    /**
     * Adapter context
     */
    Context mContext;
    LayoutInflater mInflater;
    private static String[] listContact;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public PoiAdapter(Context context, String[] results) {
        listContact = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listContact.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listContact[position];
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
        PoiAdapter.ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.drop_down_layout, null);
            holder = new PoiAdapter.ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.Poi_Item_name);

            convertView.setTag(holder);
        } else {
            holder = (PoiAdapter.ViewHolder) convertView.getTag();
        }

        holder.txtname.setText(listContact[position]);

        return convertView;

    }

    static class ViewHolder{
        TextView txtname;
    }
}
