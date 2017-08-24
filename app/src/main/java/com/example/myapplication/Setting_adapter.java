package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class Setting_adapter extends BaseAdapter {


    private Context mContext;
    private int mResource;
    private ArrayList<Setting_item> mItems = new ArrayList<Setting_item>();

    public Setting_adapter(Context context, int resource, ArrayList<Setting_item> items) {
        mContext = context;
        mItems = items;
        mResource = resource;
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        TextView t1;
        TextView t2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent,false);

            holder.t1 = (TextView)convertView.findViewById(R.id.setting_item_topTextView);
            holder.t2 = (TextView)convertView.findViewById(R.id.setting_item_bottomTextView);

            holder.t1.setText(mItems.get(position).mString);
            holder.t2.setText(mItems.get(position).mString2);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        return convertView;
    }
}

class Setting_item {

    String mString;
    String mString2;

    public Setting_item(String a, String b) {
        mString = a;
        mString2 = b;
    }
}